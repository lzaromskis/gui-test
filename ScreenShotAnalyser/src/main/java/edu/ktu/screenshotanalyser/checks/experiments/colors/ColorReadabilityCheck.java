package edu.ktu.screenshotanalyser.checks.experiments.colors;

import edu.ktu.screenshotanalyser.checks.*;
import edu.ktu.screenshotanalyser.context.State;
import edu.ktu.screenshotanalyser.enums.ColorSpaces;
import edu.ktu.screenshotanalyser.enums.RuleCheckCodes;
import edu.ktu.screenshotanalyser.exceptions.MissingSettingException;
import edu.ktu.screenshotanalyser.tools.*;
import edu.ktu.screenshotanalyser.tools.colorreadability.ContrastRatioCompatibilityCalculator;
import edu.ktu.screenshotanalyser.tools.colorreadability.IContrastRatioCompatibilityCalculator;
import edu.ktu.screenshotanalyser.utils.methods.ImageUtils;
import org.jetbrains.annotations.TestOnly;
import org.opencv.core.CvType;
import org.opencv.core.Mat;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ColorReadabilityCheck extends BaseRuleCheck implements IStateRuleChecker {

    private static final float FAILURE_THRESHOLD = 0.4f;
    private final ITextZoneExtractor _textZoneExtractor;
    private final IColorSpaceConverter _colorSpaceConverter;
    private final IDominantColorProvider _dominantColorProvider;
    private final IContrastRatioCompatibilityCalculator _correctContrastRationCalculator;

    public ColorReadabilityCheck() {
        super(39, RuleCheckCodes.COLOR_READABILITY_CHECK.name());
        _textZoneExtractor = new TextZoneExtractor();
        _colorSpaceConverter = new ColorSpaceConverter();
        _dominantColorProvider = new DominantColorProviderKMeans();
        _correctContrastRationCalculator = new ContrastRatioCompatibilityCalculator();
    }

    @TestOnly
    public ColorReadabilityCheck(
        ITextZoneExtractor textZoneExtractor,
        IColorSpaceConverter colorSpaceConverter,
        IDominantColorProvider dominantColorProvider,
        IContrastRatioCompatibilityCalculator correctContrastRatioCalculator) {
        super(39, RuleCheckCodes.COLOR_READABILITY_CHECK.name());
        _textZoneExtractor = textZoneExtractor;
        _colorSpaceConverter = colorSpaceConverter;
        _dominantColorProvider = dominantColorProvider;
        _correctContrastRationCalculator = correctContrastRatioCalculator;
    }

    @Override
    public void analyze(State state, ResultsCollector failures) throws IOException, MissingSettingException {
        var colorSpaces = Configuration.instance().getColorSpaces();

        var textZones = _textZoneExtractor.extract(state);

        for (var textZone: textZones) {
            for (var colorSpace: colorSpaces) {
                var convertedTextZone = _colorSpaceConverter.convertImage(textZone.getTextZone(), colorSpace);
                var result = calculateCompatibility(convertedTextZone);

                var failed = result <= FAILURE_THRESHOLD;
                if (failed) {
                    var image = state.getImage();
                    var convertedImage = _colorSpaceConverter.convertImage(image, colorSpace);
                    var resultImage = new ResultImage(convertedImage);
                    resultImage.drawBounds(textZone.getBounds());

                    failures.addFailure(new CheckResult(
                        state,
                        this,
                        getFailureMessage(state, colorSpace, result),
                        1,
                        resultImage));
                }

                // TODO: Remove this. For debug purposes only.
                if (!Configuration.instance().getIsTestInstance()) {
                    var debugPath = Configuration.instance().getDebugFolderPath();
                    var id = java.util.UUID.randomUUID();
                    var f = new File(String.format("%s\\readab_%s_%s.png", debugPath, failed ? "failed" : "passed", id));
                    ImageIO.write(convertedTextZone, "png", f);
                }
            }
        }
    }

    private BufferedImage resizeTextZone(BufferedImage textZone) {
        var mat = ImageUtils.bufferedImageToMat(textZone);
        var dst = new Mat(textZone.getHeight(), textZone.getWidth(), CvType.CV_8UC1);
        var ratio = getRatio(textZone);

        if (ratio >= 1f && ratio <= 4f && textZone.getWidth() > 80)
            return ImageUtils.resize(textZone, 80);

        if (textZone.getWidth() > 200)
            return ImageUtils.resize(textZone, textZone.getWidth() / 3);

        return textZone;
    }

    private float getRatio(BufferedImage image) {
        var width = (float)image.getWidth();
        var height = (float)image.getHeight();

        return width / height;
    }

    private float calculateCompatibility(BufferedImage textZone) {
        var colors = _dominantColorProvider.getDominantColors(textZone, 2);
        return _correctContrastRationCalculator.calculateCompatibility(colors);
    }

    private String getFailureMessage(State state, ColorSpaces colorSpace, float compatibility) {
        return String.format("%s failed color readability check in %s color space. Compatibility value: %f",
                             state.getName(),
                             colorSpace,
                             compatibility);
    }
}
