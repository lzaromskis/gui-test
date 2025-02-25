package edu.ktu.screenshotanalyser.checks.experiments.colors;

import edu.ktu.screenshotanalyser.exceptions.MissingSettingException;
import edu.ktu.screenshotanalyser.test.helpers.ConfigurationHelper;
import edu.ktu.screenshotanalyser.test.helpers.ResourceCreationHelper;
import edu.ktu.screenshotanalyser.test.helpers.TestResultsCollector;
import edu.ktu.screenshotanalyser.tools.Configuration;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.opencv.core.Core;

import java.io.IOException;
import java.util.stream.Stream;

import static org.junit.jupiter.params.provider.Arguments.arguments;

public class ColorCompatibilityCheckTests {
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
    @MethodSource("compatibilityOk")
    public void analyze_OK(String configName, String imageName) throws MissingSettingException, IOException {
        // Arrange
        ConfigurationHelper.SetupConfigurationFile("Integration/ColorCompatibility/" + configName);
        var checker = new ColorCompatibilityCheck();
        var state = ResourceCreationHelper.createState("testState", imageName);
        var resultsCollector = new TestResultsCollector();

        // Act
        checker.analyze(state, resultsCollector);

        // Assert
        Assertions.assertEquals(0, resultsCollector.getCheckResults().size());
    }

    public static Stream<Arguments> compatibilityOk() {
        return Stream.of(arguments("ComplementaryNormal.config", "complementary.png"),
                         arguments("AnalogousNormal.config", "analogous.png"),
                         arguments("MonochromaticNormal.config", "monochromatic.png"),
                         arguments("MonochromaticProtanopia.config", "monochromatic.png"));
    }

    @ParameterizedTest
    @MethodSource("compatibilityFail")
    public void analyze_Fail(String configName, String imageName) throws MissingSettingException, IOException {
        // Arrange
        ConfigurationHelper.SetupConfigurationFile("Integration/ColorCompatibility/" + configName);
        var checker = new ColorCompatibilityCheck();
        var state = ResourceCreationHelper.createState("testState", imageName);
        var resultsCollector = new TestResultsCollector();

        // Act
        checker.analyze(state, resultsCollector);

        // Assert
        Assertions.assertEquals(1, resultsCollector.getCheckResults().size());
    }

    public static Stream<Arguments> compatibilityFail() {
        return Stream.of(arguments("ComplementaryNormal.config", "analogous.png"),
                         arguments("AnalogousNormal.config", "monochromatic.png"),
                         arguments("MonochromaticNormal.config", "complementary.png"),
                         arguments("MonochromaticProtanopia.config", "complementary.png"));
    }
}
