package edu.ktu.screenshotanalyser.checks.experiments.colors;

import edu.ktu.screenshotanalyser.checks.BaseRuleCheck;
import edu.ktu.screenshotanalyser.checks.CheckResult;
import edu.ktu.screenshotanalyser.checks.IStateRuleChecker;
import edu.ktu.screenshotanalyser.checks.ResultsCollector;
import edu.ktu.screenshotanalyser.context.State;
import edu.ktu.screenshotanalyser.enums.ColorCombinations;
import edu.ktu.screenshotanalyser.enums.ColorSpaces;
import edu.ktu.screenshotanalyser.enums.RuleCheckCodes;
import edu.ktu.screenshotanalyser.exceptions.MissingSettingException;
import edu.ktu.screenshotanalyser.tools.*;
import edu.ktu.screenshotanalyser.tools.colorcompatibility.ColorCompatibilityCalculatorProvider;
import edu.ktu.screenshotanalyser.tools.colorcompatibility.IColorCompatibilityCalculatorProvider;
import edu.ktu.screenshotanalyser.tools.colorcompatibility.INumberOfColorsInCombinationProvider;
import edu.ktu.screenshotanalyser.tools.colorcompatibility.NumberOfColorsInCombinationProvider;
import edu.ktu.screenshotanalyser.utils.methods.ImageUtils;
import org.jetbrains.annotations.TestOnly;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ColorCompatibilityCheck extends BaseRuleCheck implements IStateRuleChecker {

    private static final float FAILURE_THRESHOLD = 0.4f;
    private final IColorSpaceConverter _colorSpaceConverter;
    private final IDominantColorProvider _dominantColorProvider;
    private final INumberOfColorsInCombinationProvider _numberOfColorsInCombinationProvider;
    private final IColorCompatibilityCalculatorProvider _compatibilityCalculatorProvider;

    public ColorCompatibilityCheck() {
        super(38, RuleCheckCodes.COLOR_COMPATIBILITY_CHECK.name());

        _colorSpaceConverter = new ColorSpaceConverter();
        _dominantColorProvider = new DominantColorProviderKMeans();
        _numberOfColorsInCombinationProvider = new NumberOfColorsInCombinationProvider();
        _compatibilityCalculatorProvider = new ColorCompatibilityCalculatorProvider();
    }

    @TestOnly
    public ColorCompatibilityCheck(
        IColorSpaceConverter colorSpaceConverter,
        IDominantColorProvider dominantColorProvider,
        INumberOfColorsInCombinationProvider numberOfColorsInCombinationProvider,
        IColorCompatibilityCalculatorProvider compatibilityCalculatorProvider) {
        super(38, RuleCheckCodes.COLOR_COMPATIBILITY_CHECK.name());

        _colorSpaceConverter = colorSpaceConverter;
        _dominantColorProvider = dominantColorProvider;
        _numberOfColorsInCombinationProvider = numberOfColorsInCombinationProvider;
        _compatibilityCalculatorProvider = compatibilityCalculatorProvider;
    }

    @Override
    public void analyze(State state, ResultsCollector failures) throws IOException, MissingSettingException {
        var combination = Configuration.instance().getColorCombination();
        var colorSpaces = Configuration.instance().getColorSpaces();
        var image = state.getImage();

        var smallImage = image.getWidth() > 160 ? ImageUtils.resize(image, 160) : image;
        for (var colorSpace: colorSpaces) {
            var convertedImage = _colorSpaceConverter.convertImage(smallImage, colorSpace);
            var result = calculateCompatibility(convertedImage, combination);

            var failed = result <= FAILURE_THRESHOLD;
            if (failed) {
                failures.addFailure(new CheckResult(state, this, getFailureMessage(state, combination, colorSpace, result), 1));
            }

            // TODO: Remove this. For debug purposes only.
            if (!Configuration.instance().getIsTestInstance()) {
                var debugPath = Configuration.instance().getDebugFolderPath();
                var id = java.util.UUID.randomUUID();
                var f = new File(String.format("%s\\compat_%s_%s.png", debugPath, failed ? "failed" : "passed", id));
                ImageIO.write(convertedImage, "png", f);
            }
        }
    }

    private float calculateCompatibility(BufferedImage image, ColorCombinations combination) {
        var numberOfColors = _numberOfColorsInCombinationProvider.getNumberOfColors(combination);
        var colors = _dominantColorProvider.getDominantColors(image, numberOfColors);

        var calculator = _compatibilityCalculatorProvider.getCalculator(combination);

        return calculator == null ? 0f : calculator.calculateCompatibility(colors);
    }

    private String getFailureMessage(State state, ColorCombinations combination, ColorSpaces colorSpace, float compatibility) {
        return String.format("%s failed %s combination compatibility check in %s color space. Compatibility value: %f",
                             state.getName(),
                             combination,
                             colorSpace,
                             compatibility);
    }
}