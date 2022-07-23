package edu.ktu.screenshotanalyser;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import edu.ktu.screenshotanalyser.checks.experiments.*;
import edu.ktu.screenshotanalyser.tools.ColorSpace;
import edu.ktu.screenshotanalyser.tools.ColorSpaceConverter;
import edu.ktu.screenshotanalyser.tools.ConsoleOutput;
import org.opencv.core.Core;
import org.slf4j.LoggerFactory;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import edu.ktu.screenshotanalyser.checks.AppChecker;
import edu.ktu.screenshotanalyser.checks.DataBaseResultsCollector;
import edu.ktu.screenshotanalyser.checks.ResultsCollector;
import edu.ktu.screenshotanalyser.checks.RulesSetChecker;
import edu.ktu.screenshotanalyser.tools.Settings;

import javax.imageio.ImageIO;

public class StartUp
{
	static
	{
		nu.pattern.OpenCV.loadShared();
		
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);				
	}
	
	public static void main(String[] args) throws IOException, InterruptedException
	{
		var output = new ConsoleOutput();
		for (int i = 0; i < 5; i++) {
			output.write(Integer.toString(i), true);
			Thread.sleep(1000);
		}

		output.write("Starting...");
		enableLogs();
		BufferedImage img = ImageIO.read(new File("D:\\1\\test\\comp.png"));
		ColorSpaceConverter converter = new ColorSpaceConverter();

		BufferedImage result = converter.convertImage(img, ColorSpace.PROTANOPIA);
		ImageIO.write(result, "jpg", new File("D:\\1\\test\\protanopia.png"));

		output.write("Running experiments...");
		runExperiments();
		ColorCompatibilityCheck check = new ColorCompatibilityCheck();
		check.analyze(img, ColorCompatibilityCheck.ColorCombinations.COMPLEMENTARY, 2);
		check.analyze(result, ColorCompatibilityCheck.ColorCombinations.COMPLEMENTARY, 2);
		output.write("Finished experiments...");
	}
	
	private static void runExperiments() throws IOException, InterruptedException
	{
		var failures = new DataBaseResultsCollector("defects-db", false);
		var checker = new RulesSetChecker();

		//checker.addRule(new UnalignedControlsCheck());    +
		//checker.addRule(new ClippedControlCheck());       +
		//checker.addRule(new ObscuredControlCheck());      +
		//checker.addRule(new WrongLanguageCheck());        +
		//checker.addRule(new ObscuredTextCheck());         +
		//checker.addRule(new GrammarCheck());              +
		//checker.addRule(new WrongEncodingCheck());        +
		//checker.addRule(new ClippedTextCheck());          +
		//checker.addRule(new UnlocalizedIconsCheck());     +
		//checker.addRule(new MissingTranslationCheck());   +
		//checker.addRule(new MixedLanguagesStateCheck());  +
		//checker.addRule(new MixedLanguagesAppCheck());    +
		//checker.addRule(new OffensiveMessagesCheck());    + 
		//checker.addRule(new UnreadableTextCheck());       +
		//checker.addRule(new TooHardToUnderstandCheck());  +
		//checker.addRule(new MissingTextCheck());          //+
		checker.addRule(new ColorCompatibilityCheck());
		
		var apps = new File(Settings.appsFolder).listFiles(p -> p.isDirectory());
		var exec = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());		

		for (var app : apps)
		{
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
	
	private static void runChecks(File appName, ExecutorService exec, RulesSetChecker rules, ResultsCollector failures) throws IOException, InterruptedException
	{
		var appChecker = new AppChecker();
		
		appChecker.runChecks(appName, rules, exec, failures);
	}
	
	private static void enableLogs()
	{
		var logContext = (LoggerContext)LoggerFactory.getILoggerFactory();
		var log = logContext.getLogger("com.jayway.jsonpath.internal.path.CompiledPath");

		log.setLevel(Level.ERROR);
	}
}
