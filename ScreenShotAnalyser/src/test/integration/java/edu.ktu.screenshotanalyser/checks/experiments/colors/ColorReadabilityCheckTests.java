package edu.ktu.screenshotanalyser.checks.experiments.colors;

import edu.ktu.screenshotanalyser.exceptions.MissingSettingException;
import edu.ktu.screenshotanalyser.test.helpers.ConfigurationHelper;
import edu.ktu.screenshotanalyser.test.helpers.ResourceCreationHelper;
import edu.ktu.screenshotanalyser.test.helpers.TestResultsCollector;
import edu.ktu.screenshotanalyser.tools.Configuration;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.opencv.core.Core;

import java.io.IOException;
import java.util.stream.Stream;

import static org.junit.jupiter.params.provider.Arguments.arguments;

public class ColorReadabilityCheckTests {
    @BeforeAll
    public static void beforeAll() {
        nu.pattern.OpenCV.loadShared();

        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    @AfterEach
    public void afterEach() {
        Configuration.unloadConfiguration();
    }

    @ParameterizedTest
    @MethodSource("readabilityOk")
    public void analyze_OK(String configName, String imageName) throws MissingSettingException, IOException {
        // Arrange
        ConfigurationHelper.SetupConfigurationFile("Integration/ColorReadability/" + configName);
        var checker = new ColorReadabilityCheck();
        var state = ResourceCreationHelper.createState("testState", imageName, "metadata.json");
        var resultsCollector = new TestResultsCollector();

        // Act
        checker.analyze(state, resultsCollector);

        // Assert
        Assertions.assertEquals(0, resultsCollector.getCheckResults().size());
    }

    public static Stream<Arguments> readabilityOk() {
        return Stream.of(arguments("Normal.config", "normal_readable.png"),
                         arguments("Protanopia.config", "normal_readable.png"),
                         arguments("Tritanopia.config", "tritanopia_readable.png"));
    }

    @ParameterizedTest
    @MethodSource("readabilityBad")
    public void analyze_Fail(String configName, String imageName) throws MissingSettingException, IOException {
        // Arrange
        ConfigurationHelper.SetupConfigurationFile("Integration/ColorReadability/" + configName);
        var checker = new ColorReadabilityCheck();
        var state = ResourceCreationHelper.createState("testState", imageName, "metadata.json");
        var resultsCollector = new TestResultsCollector();

        // Act
        checker.analyze(state, resultsCollector);

        // Assert
        Assertions.assertEquals(1, resultsCollector.getCheckResults().size());
    }

    public static Stream<Arguments> readabilityBad() {
        return Stream.of(arguments("Normal.config", "normal_unreadable.png"),
                         arguments("Protanopia.config", "tritanopia_readable.png"),
                         arguments("Tritanopia.config", "normal_unreadable.png"));
    }
}
