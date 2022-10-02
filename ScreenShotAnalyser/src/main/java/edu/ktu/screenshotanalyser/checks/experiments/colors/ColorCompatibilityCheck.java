package edu.ktu.screenshotanalyser.checks.experiments.colors;

import edu.ktu.screenshotanalyser.checks.BaseRuleCheck;
import edu.ktu.screenshotanalyser.checks.IStateRuleChecker;
import edu.ktu.screenshotanalyser.checks.ResultsCollector;
import edu.ktu.screenshotanalyser.context.State;
import edu.ktu.screenshotanalyser.enums.ColorCombinations;
import edu.ktu.screenshotanalyser.tools.IDominantColorProvider;
import edu.ktu.screenshotanalyser.tools.KMeans;
import edu.ktu.screenshotanalyser.tools.colorcompatibility.ColorCompatibilityCalculatorProvider;
import edu.ktu.screenshotanalyser.tools.colorcompatibility.INumberOfColorsInCombinationProvider;
import edu.ktu.screenshotanalyser.tools.colorcompatibility.NumberOfColorsInCombinationProvider;
import edu.ktu.screenshotanalyser.utils.methods.ImageUtils;
import edu.ktu.screenshotanalyser.utils.models.PixelRGB;
import org.jetbrains.annotations.TestOnly;

import java.awt.image.BufferedImage;

public class ColorCompatibilityCheck extends BaseRuleCheck implements IStateRuleChecker {

    private final IDominantColorProvider _dominantColorProvider;
    private final INumberOfColorsInCombinationProvider _numberOfColorsInCombinationProvider;
    private final ColorCompatibilityCalculatorProvider _compatibilityCalculatorProvider;

    public ColorCompatibilityCheck() {
        super(1010, "color_compat");

        _dominantColorProvider = new KMeans();
        _numberOfColorsInCombinationProvider = new NumberOfColorsInCombinationProvider();
        _compatibilityCalculatorProvider = new ColorCompatibilityCalculatorProvider();
    }

    @TestOnly
    public ColorCompatibilityCheck(
        IDominantColorProvider dominantColorProvider,
        INumberOfColorsInCombinationProvider numberOfColorsInCombinationProvider,
        ColorCompatibilityCalculatorProvider compatibilityCalculatorProvider) {
        super(1010, "color_compat");

        _dominantColorProvider = dominantColorProvider;
        _numberOfColorsInCombinationProvider = numberOfColorsInCombinationProvider;
        _compatibilityCalculatorProvider = compatibilityCalculatorProvider;
    }

    @Override
    public void analyze(State state, ResultsCollector failures) {
        // TODO: implement
        var result = calculateCompatibility(state.getImage(), ColorCombinations.MONOCHROMATIC);
    }

    private float calculateCompatibility(BufferedImage image, ColorCombinations combination) {
        var numberOfColors = _numberOfColorsInCombinationProvider.getNumberOfColors(combination);
        PixelRGB[] colors = getDominantColors(image, numberOfColors);

        var calculator = _compatibilityCalculatorProvider.getCalculator(combination);

        return calculator == null ? 0f : calculator.calculateCompatibility(colors);
    }

    private PixelRGB[] getDominantColors(BufferedImage image, int numberOfColors) {
        var imageData = ImageUtils.bufferedImageToPixelRGBArray(image);
        return _dominantColorProvider.getDominantColors(imageData, numberOfColors);
    }
}