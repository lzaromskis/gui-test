package edu.ktu.screenshotanalyser.checks.experiments.colors;

import edu.ktu.screenshotanalyser.exceptions.MissingSettingException;
import edu.ktu.screenshotanalyser.test.helpers.ConfigurationHelper;
import edu.ktu.screenshotanalyser.test.helpers.ResourceCreationHelper;
import edu.ktu.screenshotanalyser.test.helpers.TestResultsCollector;
import edu.ktu.screenshotanalyser.tools.Configuration;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class ColorCompatibilityCheckTests {
    @AfterEach
    public void afterEach() {
        Configuration.unloadConfiguration();
    }

    @Test
    public void analyze_Complementary_Normal_OK() throws MissingSettingException, IOException {
        // Arrange
        ConfigurationHelper.SetupConfigurationFile("Integration/ColorCompatibility/ComplementaryNormal.config");
        var checker = new ColorCompatibilityCheck();
        var state = ResourceCreationHelper.createState("testState", "complementary.png");
        var resultsCollector = new TestResultsCollector();

        // Act
        checker.analyze(state, resultsCollector);

        // Assert
        Assertions.assertEquals(0, resultsCollector.getCheckResults().size());
    }

    @Test
    public void analyze_Complementary_Normal_Fail() throws MissingSettingException, IOException {
        // Arrange
        ConfigurationHelper.SetupConfigurationFile("Integration/ColorCompatibility/ComplementaryNormal.config");
        var checker = new ColorCompatibilityCheck();
        var state = ResourceCreationHelper.createState("testState", "analogous.png");
        var resultsCollector = new TestResultsCollector();

        // Act
        checker.analyze(state, resultsCollector);

        // Assert
        Assertions.assertEquals(1, resultsCollector.getCheckResults().size());
    }
}
