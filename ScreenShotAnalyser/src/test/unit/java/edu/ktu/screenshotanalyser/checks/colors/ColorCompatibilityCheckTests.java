package edu.ktu.screenshotanalyser.checks.colors;

import edu.ktu.screenshotanalyser.test.helpers.ConfigurationHelper;
import edu.ktu.screenshotanalyser.checks.ResultsCollector;
import edu.ktu.screenshotanalyser.checks.experiments.colors.ColorCompatibilityCheck;
import edu.ktu.screenshotanalyser.context.State;
import edu.ktu.screenshotanalyser.exceptions.MissingSettingException;
import edu.ktu.screenshotanalyser.tools.Configuration;
import edu.ktu.screenshotanalyser.tools.IColorSpaceConverter;
import edu.ktu.screenshotanalyser.tools.IDominantColorProvider;
import edu.ktu.screenshotanalyser.tools.colorcompatibility.IColorCompatibilityCalculator;
import edu.ktu.screenshotanalyser.tools.colorcompatibility.IColorCompatibilityCalculatorProvider;
import edu.ktu.screenshotanalyser.tools.colorcompatibility.INumberOfColorsInCombinationProvider;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

@RunWith(MockitoJUnitRunner.class)
public class ColorCompatibilityCheckTests {

    @Mock
    private IColorSpaceConverter colorSpaceConverter;

    @Mock
    private IDominantColorProvider dominantColorProvider;

    @Mock
    private INumberOfColorsInCombinationProvider numberOfColorsInCombinationProvider;

    @Mock
    private IColorCompatibilityCalculatorProvider colorCompatibilityCalculatorProvider;

    @Mock
    private IColorCompatibilityCalculator colorCompatibilityCalculator;

    @Mock
    private State state;

    @Mock
    private BufferedImage bufferedImage;

    @Mock
    private File imageFile;

    @Mock
    private ResultsCollector resultsCollector;

    @SuppressWarnings("EmptyTryBlock")
    @BeforeEach
    public void beforeEach() throws Exception {
        try (var ignored = MockitoAnnotations.openMocks(this)) {

        }
    }

    @AfterEach
    public void afterEach() {
        Configuration.unloadConfiguration();
    }

    @Test
    public void analyze_OneColorSpace_Success() throws MissingSettingException, IOException {
        // Arrange
        ConfigurationHelper.SetupConfigurationFile("Unit/ColorCompatibility/OneColorSpace.config");
        Mockito.when(state.getImage()).thenReturn(bufferedImage);
        Mockito.when(colorCompatibilityCalculatorProvider.getCalculator(Mockito.any())).thenReturn(colorCompatibilityCalculator);
        Mockito.when(colorCompatibilityCalculator.calculateCompatibility(Mockito.any())).thenReturn(0.8f);

        var colorCompatibilityCheck = createCheck();

        // Act
        colorCompatibilityCheck.analyze(state, resultsCollector);

        // Assert
        Mockito.verify(resultsCollector, Mockito.never()).addFailure(Mockito.any());
    }

    @Test
    public void analyze_OneColorSpace_Failure() throws MissingSettingException, IOException {
        // Arrange
        ConfigurationHelper.SetupConfigurationFile("Unit/ColorCompatibility/OneColorSpace.config");
        Mockito.when(state.getImage()).thenReturn(bufferedImage);
        Mockito.when(colorCompatibilityCalculatorProvider.getCalculator(Mockito.any())).thenReturn(colorCompatibilityCalculator);
        Mockito.when(colorCompatibilityCalculator.calculateCompatibility(Mockito.any())).thenReturn(0.2f);
        Mockito.when(state.getImageFile()).thenReturn(imageFile);
        Mockito.when(imageFile.getAbsolutePath()).thenReturn("absolutePath");

        var colorCompatibilityCheck = createCheck();

        // Act
        colorCompatibilityCheck.analyze(state, resultsCollector);

        // Assert
        Mockito.verify(resultsCollector, Mockito.times(1)).addFailure(Mockito.any());
    }

    @Test
    public void analyze_ThreeColorSpaces_Success() throws MissingSettingException, IOException {
        // Arrange
        ConfigurationHelper.SetupConfigurationFile("Unit/ColorCompatibility/ThreeColorSpaces.config");
        Mockito.when(state.getImage()).thenReturn(bufferedImage);
        Mockito.when(colorCompatibilityCalculatorProvider.getCalculator(Mockito.any())).thenReturn(colorCompatibilityCalculator);
        Mockito.when(colorCompatibilityCalculator.calculateCompatibility(Mockito.any())).thenReturn(1f);

        var colorCompatibilityCheck = createCheck();

        // Act
        colorCompatibilityCheck.analyze(state, resultsCollector);

        // Assert
        Mockito.verify(resultsCollector, Mockito.never()).addFailure(Mockito.any());
    }

    @Test
    public void analyze_ThreeColorSpaces_Failure() throws MissingSettingException, IOException {
        // Arrange
        ConfigurationHelper.SetupConfigurationFile("Unit/ColorCompatibility/ThreeColorSpaces.config");
        Mockito.when(state.getImage()).thenReturn(bufferedImage);
        Mockito.when(colorCompatibilityCalculatorProvider.getCalculator(Mockito.any())).thenReturn(colorCompatibilityCalculator);
        Mockito.when(colorCompatibilityCalculator.calculateCompatibility(Mockito.any())).thenReturn(0f);
        Mockito.when(state.getImageFile()).thenReturn(imageFile);
        Mockito.when(imageFile.getAbsolutePath()).thenReturn("absolutePath");

        var colorCompatibilityCheck = createCheck();

        // Act
        colorCompatibilityCheck.analyze(state, resultsCollector);

        // Assert
        Mockito.verify(resultsCollector, Mockito.times(3)).addFailure(Mockito.any());
    }

    private ColorCompatibilityCheck createCheck() {
        return new ColorCompatibilityCheck(
            colorSpaceConverter,
            dominantColorProvider,
            numberOfColorsInCombinationProvider,
            colorCompatibilityCalculatorProvider
        );
    }
}
