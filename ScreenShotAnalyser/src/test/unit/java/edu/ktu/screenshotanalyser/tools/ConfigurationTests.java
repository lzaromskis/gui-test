package edu.ktu.screenshotanalyser.tools;

import edu.ktu.screenshotanalyser.test.helpers.ConfigurationHelper;
import edu.ktu.screenshotanalyser.enums.ColorCombinations;
import edu.ktu.screenshotanalyser.enums.ColorSpaces;
import edu.ktu.screenshotanalyser.exceptions.MissingSettingException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.IOException;
import java.util.Arrays;

import static edu.ktu.screenshotanalyser.enums.RuleCheckCodes.COLOR_COMPATIBILITY_CHECK;
import static edu.ktu.screenshotanalyser.enums.RuleCheckCodes.COLOR_READABILITY_CHECK;
import static org.junit.jupiter.api.Assertions.*;

public class ConfigurationTests {

    @AfterEach
    public void AfterEach() {
        Configuration.unloadConfiguration();
    }

    @ParameterizedTest
    @ValueSource(strings = {"OK.config", "Unit/Configuration/Empty.config"})
    public void instance_Ok(String filename) throws IOException {
        // Arrange
        ConfigurationHelper.SetupConfigurationFile(filename);

        // Act
        var result = Configuration.instance();

        // Assert
        assertNotNull(result);
    }

    @ParameterizedTest
    @ValueSource(strings = {"Unit/Configuration/does_not_exist.config", "Unit/Configuration/MissingEquals.config", "Unit/Configuration/MultipleEquals.config", "Unit/Configuration/DuplicateSetting.config"})
    public void instance_ThrowsException(String filename) {
        // Arrange
        ConfigurationHelper.SetupConfigurationFile(filename);

        // Act & Assert
        assertThrows(IOException.class, Configuration::instance);
    }

    @Test
    public void getRulesCodes_ReturnsRuleCodes() throws IOException, MissingSettingException {
        // Arrange
        ConfigurationHelper.SetupConfigurationFile("OK.config");
        var config = Configuration.instance();

        // Act
        var result = config.getRuleCodes();

        // Assert
        assertEquals(2, result.length);
        assertTrue(Arrays
                       .stream(result)
                       .toList()
                       .contains(COLOR_COMPATIBILITY_CHECK));
        assertTrue(Arrays
                       .stream(result)
                       .toList()
                       .contains(COLOR_READABILITY_CHECK));
    }

    @Test
    public void getRulesCodes_NoSetting_ThrowsException() throws IOException {
        // Arrange
        ConfigurationHelper.SetupConfigurationFile("Unit/Configuration/Empty.config");
        var config = Configuration.instance();

        // Act & Assert
        assertThrows(MissingSettingException.class, config::getRuleCodes);
    }

    @Test
    public void getRulesCodes_InvalidSetting_ThrowsIllegalArgumentException() throws IOException {
        // Arrange
        ConfigurationHelper.SetupConfigurationFile("Unit/Configuration/BadRuleCode.config");
        var config = Configuration.instance();

        // Act & Assert
        assertThrows(IllegalArgumentException.class, config::getRuleCodes);
    }

    @Test
    public void getAppsFolderPath_ReturnsSettingValue() throws IOException, MissingSettingException {
        // Arrange
        ConfigurationHelper.SetupConfigurationFile("OK.config");
        var config = Configuration.instance();

        // Act
        var result = config.getAppsFolderPath();

        // Assert
        assertEquals("appsFolder", result);
    }

    @Test
    public void getAppsFolderPath_NoSetting_ThrowsException() throws IOException {
        // Arrange
        ConfigurationHelper.SetupConfigurationFile("Unit/Configuration/Empty.config");
        var config = Configuration.instance();

        // Act & Assert
        assertThrows(MissingSettingException.class, config::getAppsFolderPath);
    }

    @Test
    public void getAppImagesFolderPath_ReturnsSettingValue() throws IOException, MissingSettingException {
        // Arrange
        ConfigurationHelper.SetupConfigurationFile("OK.config");
        var config = Configuration.instance();

        // Act
        var result = config.getAppImagesFolderPath();

        // Assert
        assertEquals("appImagesFolder", result);
    }

    @Test
    public void getAppImagesFolderPath_NoSetting_ThrowsException() throws IOException {
        // Arrange
        ConfigurationHelper.SetupConfigurationFile("Unit/Configuration/Empty.config");
        var config = Configuration.instance();

        // Act & Assert
        assertThrows(MissingSettingException.class, config::getAppImagesFolderPath);
    }

    @Test
    public void getDebugFolderPath_ReturnsSettingValue() throws IOException, MissingSettingException {
        // Arrange
        ConfigurationHelper.SetupConfigurationFile("OK.config");
        var config = Configuration.instance();

        // Act
        var result = config.getDebugFolderPath();

        // Assert
        assertEquals("debugFolder", result);
    }

    @Test
    public void getDebugFolderPath_NoSetting_ThrowsException() throws IOException {
        // Arrange
        ConfigurationHelper.SetupConfigurationFile("Unit/Configuration/Empty.config");
        var config = Configuration.instance();

        // Act & Assert
        assertThrows(MissingSettingException.class, config::getDebugFolderPath);
    }

    @Test
    public void getColorCombination_ReturnColorCombination() throws IOException, MissingSettingException {
        // Arrange
        ConfigurationHelper.SetupConfigurationFile("OK.config");
        var config = Configuration.instance();

        // Act & Assert
        assertEquals(ColorCombinations.MONOCHROMATIC, config.getColorCombination());
    }

    @Test
    public void getColorCombination_NoSetting_ThrowsMissingSettingException() throws IOException {
        // Arrange
        ConfigurationHelper.SetupConfigurationFile("Unit/Configuration/Empty.config");
        var config = Configuration.instance();

        // Act & Assert
        assertThrows(MissingSettingException.class, config::getColorCombination);
    }

    @Test
    public void getColorCombination_InvalidSetting_ThrowsIllegalArgumentException() throws IOException {
        // Arrange
        ConfigurationHelper.SetupConfigurationFile("Unit/Configuration/BadColorCombination.config");
        var config = Configuration.instance();

        // Act & Assert
        assertThrows(IllegalArgumentException.class, config::getColorCombination);
    }

    @Test
    public void getColorSpaces_ReturnColorSpaces() throws IOException, MissingSettingException {
        // Arrange
        ConfigurationHelper.SetupConfigurationFile("OK.config");
        var config = Configuration.instance();

        // Act
        var result = config.getColorSpaces();
        var resultList = Arrays.asList(result);

        // Assert
        assertEquals(3, resultList.size());
        assertTrue(resultList.contains(ColorSpaces.NORMAL));
        assertTrue(resultList.contains(ColorSpaces.PROTANOPIA));
        assertTrue(resultList.contains(ColorSpaces.TRITANOPIA));
    }

    @Test
    public void getColorSpaces_NoSetting_ThrowsMissingSettingException() throws IOException {
        // Arrange
        ConfigurationHelper.SetupConfigurationFile("Unit/Configuration/Empty.config");
        var config = Configuration.instance();

        // Act & Assert
        assertThrows(MissingSettingException.class, config::getColorSpaces);
    }

    @Test
    public void getColorSpaces_InvalidSetting_ThrowsIllegalArgumentException() throws IOException {
        // Arrange
        ConfigurationHelper.SetupConfigurationFile("Unit/Configuration/BadColorSpaces.config");
        var config = Configuration.instance();

        // Act & Assert
        assertThrows(IllegalArgumentException.class, config::getColorSpaces);
    }
}
