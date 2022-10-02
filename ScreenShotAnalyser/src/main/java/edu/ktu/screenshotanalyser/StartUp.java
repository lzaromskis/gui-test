package edu.ktu.screenshotanalyser;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import edu.ktu.screenshotanalyser.checks.AppChecker;
import edu.ktu.screenshotanalyser.checks.DataBaseResultsCollector;
import edu.ktu.screenshotanalyser.checks.ResultsCollector;
import edu.ktu.screenshotanalyser.checks.RulesSetChecker;
import edu.ktu.screenshotanalyser.checks.experiments.colors.ColorCompatibilityCheck;
import edu.ktu.screenshotanalyser.enums.ColorSpaces;
import edu.ktu.screenshotanalyser.tools.*;
import edu.ktu.screenshotanalyser.utils.methods.RGBUtils;
import org.opencv.core.Core;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

class QuickTest implements IObservable {

    public QuickTest() {

    }

    @Override
    public void addObserver(IObserver observer) {

    }

    @Override
    public void deleteObserver(IObserver observer) {

    }

    @Override
    public void notifyObservers() {

    }
}


public class StartUp {
    static {
        nu.pattern.OpenCV.loadShared();

        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        var output = new ConsoleOutput();

        for (Integer i = -10; i < 265; i++) {
            Float res = RGBUtils.toLinearRGBChannel(i);
            output.write(i.toString() + " = " + res.toString());
        }

        for (Float j = -0.1f; j < 1.1f; j += 0.002f) {
            Integer res = RGBUtils.toSRGBChannel(j);
            output.write(j.toString() + " = " + res.toString());
        }
		/*
		var r = new Random();

		var renderer = new ProgressBarRenderer(
				20,
				"[",
				"]",
				"=",
				new String[] {"-", "\\", "|", "/"},
				" "
		);

		var imageCount = 47;
		var rules = new QuickTest[] {
				new QuickTest(),
				new QuickTest(),
				new QuickTest(),
				new QuickTest()
		};

		var tracker = new ProgressTracker(renderer);
		tracker.setOutput(output);
		tracker.setExpectedImagesCount(imageCount);
		tracker.setRuleChecks(rules);

		for (int i = 0; i < imageCount * rules.length; i++) {
			tracker.notifyObserver();

			Thread.sleep((int)(r.nextFloat() * 750));
		}
*/
	/*
		output.write(renderer.render(0f));
		output.write(renderer.render(0.2f));
		output.write(renderer.render(0.4f));
		output.write(renderer.render(0.4f));
		output.write(renderer.render(0.4f));
		output.write(renderer.render(0.4f));
		output.write(renderer.render(0.4f));
		output.write(renderer.render(0.4f));
		output.write(renderer.render(0.6f));
		output.write(renderer.render(0.8f));
		output.write(renderer.render(1f));
	 */
        output.write("Starting...");
        enableLogs();
        BufferedImage img = ImageIO.read(new File("D:\\1\\test\\a_00.png"));
        ColorSpaceConverter converter = new ColorSpaceConverter();

        BufferedImage result = converter.convertImage(img, ColorSpaces.PROTANOPIA);
        ImageIO.write(result, "png", new File("D:\\1\\test\\a_01.png"));

        result = converter.convertImage(img, ColorSpaces.DEUTERANOPIA);
        ImageIO.write(result, "png", new File("D:\\1\\test\\a_02.png"));

        result = converter.convertImage(img, ColorSpaces.TRITANOPIA);
        ImageIO.write(result, "png", new File("D:\\1\\test\\a_03.png"));

        result = converter.convertImage(img, ColorSpaces.ACHROMATOPSIA);
        ImageIO.write(result, "png", new File("D:\\1\\test\\a_04.png"));

        result = converter.convertImage(img, ColorSpaces.NORMAL);
        ImageIO.write(result, "png", new File("D:\\1\\test\\a_05.png"));
        output.write("Finished converting");
        // output.write("Running experiments...");
        // runExperiments();
        // ColorCompatibilityCheck check = new ColorCompatibilityCheck();
        // check.analyze(img, ColorCompatibilityCheck.ColorCombinations.COMPLEMENTARY, 2);
        // check.analyze(result, ColorCompatibilityCheck.ColorCombinations.COMPLEMENTARY, 2);
        // output.write("Finished experiments...");


    }

    private static void runExperiments() throws IOException, InterruptedException {
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
        var exec = Executors.newFixedThreadPool(Runtime
                                                    .getRuntime()
                                                    .availableProcessors());

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
