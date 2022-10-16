package edu.ktu.screenshotanalyser;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import edu.ktu.screenshotanalyser.checks.*;
import edu.ktu.screenshotanalyser.checks.experiments.colors.ColorCompatibilityCheck;
import edu.ktu.screenshotanalyser.exceptions.MissingSettingException;
import edu.ktu.screenshotanalyser.tools.*;
import edu.ktu.screenshotanalyser.utils.helpers.FileExplorerHelper;
import org.opencv.core.Core;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class StartUp {
    static {
        nu.pattern.OpenCV.loadShared();

        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    public static void main(String[] args) throws IOException, InterruptedException, MissingSettingException {
        var output = ConsoleOutput.instance();

        var rules = new BaseRuleCheck[] {new ColorCompatibilityCheck()};
        var renderer = new ProgressBarRenderer(
            20,
            "[",
            "]",
            "=",
            new String[] {"-", "\\", "|", "/"},
            " "
        );
        var progressTracker = new ProgressTracker(
            renderer,
            rules,
            FileExplorerHelper.getAllAppDirectories().length,
            FileExplorerHelper.getAllStateImageFiles().length,
            output
        );
        output.write("Running experiments...");
        progressTracker.writeProgress();
        runExperiments(rules);
        output.write("Finished experiments...");


    }

    private static void runExperiments(BaseRuleCheck[] rules) throws IOException, InterruptedException, MissingSettingException {
        var failures = new DataBaseResultsCollector("defects-db", false);
        var checker = new RulesSetChecker();

        for (var rule : rules) {
            checker.addRule(rule);
        }

        var apps = FileExplorerHelper.getAllAppDirectories();
        var processorCount = Runtime
            .getRuntime()
            .availableProcessors();
        var exec = Executors.newFixedThreadPool(processorCount);

        for (var app : apps) {
            runChecks(app, exec, checker, failures);
        }

        exec.shutdown();
        exec.awaitTermination(Integer.MAX_VALUE, TimeUnit.SECONDS);

        failures.finishRun();
    }
	
	/*
	@SuppressWarnings("unused")
	private static void runChecks(File appName) throws IOException, InterruptedException
	{
		int threads = Runtime.getRuntime().availableProcessors();
		
		ExecutorService exec = Executors.newFixedThreadPool(threads);		
		
		//runChecks(appName, exec);
		
		exec.shutdown();
		exec.awaitTermination(Integer.MAX_VALUE, TimeUnit.SECONDS);		
	}*/

    private static void runChecks(
        File appName, ExecutorService exec, RulesSetChecker rules, ResultsCollector failures) throws IOException, InterruptedException {
        var appChecker = new AppChecker();

        appChecker.runChecks(appName, rules, exec, failures);
    }

    private static void enableLogs() {
        var logContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        var log = logContext.getLogger("com.jayway.jsonpath.internal.path.CompiledPath");

        log.setLevel(Level.ERROR);
    }
}
