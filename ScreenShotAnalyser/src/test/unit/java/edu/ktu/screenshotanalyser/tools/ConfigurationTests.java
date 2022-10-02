package edu.ktu.screenshotanalyser.tools;

import edu.ktu.screenshotanalyser.enums.ColorCombinations;
import edu.ktu.screenshotanalyser.enums.ColorSpaces;
import edu.ktu.screenshotanalyser.exceptions.MissingSettingException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public class ConfigurationTests {

    @AfterEach
    public void AfterEach() {
        Configuration.unloadConfiguration();
    }

    @ParameterizedTest
    @ValueSource(strings = {"ok.config", "empty.config"})
    public void instance_Ok(String filename) throws IOException {
        // Arrange
        Configuration.setFilename(getAbsolutePath(filename));

        // Act
        var result = Configuration.instance();

        // Assert
        assertNotNull(result);
    }

    @ParameterizedTest
    @ValueSource(strings = {"does_not_exist.config", "missing_equals.config", "multiple_equals.config", "duplicate_setting.config"})
    public void instance_ThrowsException(String filename) {
        // Arrange
        Configuration.setFilename(getAbsolutePath(filename));

        // Act & Assert
        assertThrows(IOException.class, Configuration::instance);
    }

    @Test
    public void getRulesCodes_ReturnsRuleCodes() throws IOException, MissingSettingException {
        // Arrange
        Configuration.setFilename(getAbsolutePath("ok.config"));
        var config = Configuration.instance();

        // Act
        var result = config.getRuleCodes();

        // Assert
        assertEquals(3, result.length);
        assertTrue(Arrays
                       .stream(result)
                       .toList()
                       .contains("rule1"));
        assertTrue(Arrays
                       .stream(result)
                       .toList()
                       .contains("rule2"));
        assertTrue(Arrays
                       .stream(result)
                       .toList()
                       .contains("rule3"));
    }

    @Test
    public void getRulesCodes_NoSetting_ThrowsException() throws IOException {
        // Arrange
        Configuration.setFilename(getAbsolutePath("empty.config"));
        var config = Configuration.instance();

        // Act & Assert
        assertThrows(MissingSettingException.class, config::getRuleCodes);
    }

    @Test
    public void getAppsFolderPath_ReturnsSettingValue() throws IOException, MissingSettingException {
        // Arrange
        Configuration.setFilename(getAbsolutePath("ok.config"));
        var config = Configuration.instance();

        // Act
        var result = config.getAppsFolderPath();

        // Assert
        assertEquals("appsFolder", result);
    }

    @Test
    public void getAppsFolderPath_NoSetting_ThrowsException() throws IOException {
        // Arrange
        Configuration.setFilename(getAbsolutePath("empty.config"));
        var config = Configuration.instance();

        // Act & Assert
        assertThrows(MissingSettingException.class, config::getAppsFolderPath);
    }

    @Test
    public void getAppImagesFolderPath_ReturnsSettingValue() throws IOException, MissingSettingException {
        // Arrange
        Configuration.setFilename(getAbsolutePath("ok.config"));
        var config = Configuration.instance();

        // Act
        var result = config.getAppImagesFolderPath();

        // Assert
        assertEquals("appImagesFolder", result);
    }

    @Test
    public void getAppImagesFolderPath_NoSetting_ThrowsException() throws IOException {
        // Arrange
        Configuration.setFilename(getAbsolutePath("empty.config"));
        var config = Configuration.instance();

        // Act & Assert
        assertThrows(MissingSettingException.class, config::getAppImagesFolderPath);
    }

    @Test
    public void getDebugFolderPath_ReturnsSettingValue() throws IOException, MissingSettingException {
        // Arrange
        Configuration.setFilename(getAbsolutePath("ok.config"));
        var config = Configuration.instance();

        // Act
        var result = config.getDebugFolderPath();

        // Assert
        assertEquals("debugFolder", result);
    }

    @Test
    public void getDebugFolderPath_NoSetting_ThrowsException() throws IOException {
        // Arrange
        Configuration.setFilename(getAbsolutePath("empty.config"));
        var config = Configuration.instance();

        // Act & Assert
        assertThrows(MissingSettingException.class, config::getDebugFolderPath);
    }

    @Test
    public void getColorCombination_ReturnColorCombination() throws IOException, MissingSettingException {
        // Arrange
        Configuration.setFilename(getAbsolutePath("ok.config"));
        var config = Configuration.instance();

        // Act & Assert
        assertEquals(ColorCombinations.MONOCHROMATIC, config.getColorCombination());
    }

    @Test
    public void getColorCombination_NoSetting_ThrowsMissingSettingException() throws IOException {
        // Arrange
        Configuration.setFilename(getAbsolutePath("empty.config"));
        var config = Configuration.instance();

        // Act & Assert
        assertThrows(MissingSettingException.class, config::getColorCombination);
    }

    @Test
    public void getColorCombination_InvalidSetting_ThrowsIllegalArgumentException() throws IOException {
        // Arrange
        Configuration.setFilename(getAbsolutePath("bad_color_combination.config"));
        var config = Configuration.instance();

        // Act & Assert
        assertThrows(IllegalArgumentException.class, config::getColorCombination);
    }

    @Test
    public void getColorSpaces_ReturnColorSpaces() throws IOException, MissingSettingException {
        // Arrange
        Configuration.setFilename(getAbsolutePath("ok.config"));
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
        Configuration.setFilename(getAbsolutePath("empty.config"));
        var config = Configuration.instance();

        // Act & Assert
        assertThrows(MissingSettingException.class, config::getColorSpaces);
    }

    @Test
    public void getColorSpaces_InvalidSetting_ThrowsIllegalArgumentException() throws IOException {
        // Arrange
        Configuration.setFilename(getAbsolutePath("bad_color_spaces.config"));
        var config = Configuration.instance();

        // Act & Assert
        assertThrows(IllegalArgumentException.class, config::getColorSpaces);
    }

    private static String getAbsolutePath(String filename) {
        return Paths
            .get("src", "test", "resources", "TestConfigurations", filename)
            .toAbsolutePath()
            .toString();
    }
}
