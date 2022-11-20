package edu.ktu.screenshotanalyser.tools.colorreadability;

import edu.ktu.screenshotanalyser.utils.models.PixelRGB;

public class ContrastRatioCompatibilityCalculator implements IContrastRatioCompatibilityCalculator {
    // https://www.w3.org/TR/WCAG21/#contrast-minimum
    private static final float NOT_COMPATIBLE_BELOW = 3.5f;
    private static final float FULLY_COMPATIBLE_ABOVE = 4.5f;

    @Override
    public float calculateCompatibility(PixelRGB[] colors) {
        if (colors.length != 2) {
            return 0f;
        }

        var contrastRatio = PixelRGB.calculateContrastRatio(colors[0], colors[1]);
        if (contrastRatio < NOT_COMPATIBLE_BELOW) {
            return 0f;
        }

        if (contrastRatio >= FULLY_COMPATIBLE_ABOVE) {
            return 1f;
        }

        var diff = FULLY_COMPATIBLE_ABOVE - NOT_COMPATIBLE_BELOW;
        var val = contrastRatio - NOT_COMPATIBLE_BELOW;

        return val / diff;
    }
}
