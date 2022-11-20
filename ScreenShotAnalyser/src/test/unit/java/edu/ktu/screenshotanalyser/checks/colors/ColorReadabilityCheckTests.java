package edu.ktu.screenshotanalyser.checks.colors;

import edu.ktu.screenshotanalyser.checks.experiments.colors.ColorReadabilityCheck;
import edu.ktu.screenshotanalyser.test.helpers.ConfigurationHelper;
import edu.ktu.screenshotanalyser.checks.ResultsCollector;
import edu.ktu.screenshotanalyser.context.State;
import edu.ktu.screenshotanalyser.exceptions.MissingSettingException;
import edu.ktu.screenshotanalyser.tools.Configuration;
import edu.ktu.screenshotanalyser.tools.IColorSpaceConverter;
import edu.ktu.screenshotanalyser.tools.IDominantColorProvider;
import edu.ktu.screenshotanalyser.tools.ITextZoneExtractor;
import edu.ktu.screenshotanalyser.tools.colorreadability.IContrastRatioCompatibilityCalculator;
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
public class ColorReadabilityCheckTests {

    @Mock
    private ITextZoneExtractor textZoneExtractor;

    @Mock
    private IColorSpaceConverter colorSpaceConverter;

    @Mock
    private IDominantColorProvider dominantColorProvider;

    @Mock
    private IContrastRatioCompatibilityCalculator contrastRatioCompatibilityCalculator;

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
        ConfigurationHelper.SetupConfigurationFile("Unit/ColorReadability/OneColorSpace.config");
        Mockito.when(textZoneExtractor.extract(state)).thenReturn(new BufferedImage[] {bufferedImage});
        Mockito.when(contrastRatioCompatibilityCalculator.calculateCompatibility(Mockito.any())).thenReturn(1f);

        var colorReadabilityCheck = createCheck();

        // Act
        colorReadabilityCheck.analyze(state, resultsCollector);

        Mockito.verify(resultsCollector, Mockito.never()).addFailure(Mockito.any());
    }

    @Test
    public void analyze_OneColorSpace_Failure() throws MissingSettingException, IOException {
        // Arrange
        ConfigurationHelper.SetupConfigurationFile("Unit/ColorReadability/OneColorSpace.config");
        Mockito.when(textZoneExtractor.extract(state)).thenReturn(new BufferedImage[] {bufferedImage});
        Mockito.when(contrastRatioCompatibilityCalculator.calculateCompatibility(Mockito.any())).thenReturn(0f);
        Mockito.when(state.getImageFile()).thenReturn(imageFile);
        Mockito.when(imageFile.getAbsolutePath()).thenReturn("absolutePath");

        var colorReadabilityCheck = createCheck();

        // Act
        colorReadabilityCheck.analyze(state, resultsCollector);

        Mockito.verify(resultsCollector, Mockito.times(1)).addFailure(Mockito.any());
    }

    @Test
    public void analyze_ThreeColorSpaces_Success() throws MissingSettingException, IOException {
        // Arrange
        ConfigurationHelper.SetupConfigurationFile("Unit/ColorReadability/ThreeColorSpaces.config");
        Mockito.when(textZoneExtractor.extract(state)).thenReturn(new BufferedImage[] {bufferedImage});
        Mockito.when(contrastRatioCompatibilityCalculator.calculateCompatibility(Mockito.any())).thenReturn(1f);

        var colorReadabilityCheck = createCheck();

        // Act
        colorReadabilityCheck.analyze(state, resultsCollector);

        Mockito.verify(resultsCollector, Mockito.never()).addFailure(Mockito.any());
    }

    @Test
    public void analyze_ThreeColorSpaces_Failure() throws MissingSettingException, IOException {
        // Arrange
        ConfigurationHelper.SetupConfigurationFile("Unit/ColorReadability/ThreeColorSpaces.config");
        Mockito.when(textZoneExtractor.extract(state)).thenReturn(new BufferedImage[] {bufferedImage});
        Mockito.when(contrastRatioCompatibilityCalculator.calculateCompatibility(Mockito.any())).thenReturn(0f);
        Mockito.when(state.getImageFile()).thenReturn(imageFile);
        Mockito.when(imageFile.getAbsolutePath()).thenReturn("absolutePath");

        var colorReadabilityCheck = createCheck();

        // Act
        colorReadabilityCheck.analyze(state, resultsCollector);

        Mockito.verify(resultsCollector, Mockito.times(3)).addFailure(Mockito.any());
    }

    private ColorReadabilityCheck createCheck() {
        return new ColorReadabilityCheck(
            textZoneExtractor,
            colorSpaceConverter,
            dominantColorProvider,
            contrastRatioCompatibilityCalculator
        );
    }
}
