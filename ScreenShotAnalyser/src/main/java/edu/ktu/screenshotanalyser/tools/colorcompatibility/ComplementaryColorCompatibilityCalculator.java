package edu.ktu.screenshotanalyser.tools.colorcompatibility;

import edu.ktu.screenshotanalyser.utils.methods.MathUtils;
import edu.ktu.screenshotanalyser.utils.models.PixelHSV;
import edu.ktu.screenshotanalyser.utils.models.PixelRGB;

import java.util.Arrays;

public class ComplementaryColorCompatibilityCalculator extends BaseColorCompatibilityCalculator {
    private static final float EXPECTED_HUE_DIFFERENCE = 0.5f;
    private static final float MAX_HUE_DEVIATION = 0.0278f;    // 10 difference in 0-360 scale
    private static final float MAX_SATURATION_DEVIATION = 0.2f;
    private static final float MAX_VALUE_DEVIATION = 0.2f;

    @Override
    public float calculateCompatibility(PixelRGB[] colors) {
        if (colors.length != 2) {
            return 0f;
        }

        var colorsHsv = Arrays
            .stream(colors)
            .map(PixelHSV::new)
            .toArray(PixelHSV[]::new);

        var hueCoef = calculateHueCompatibility(colorsHsv[0], colorsHsv[1], EXPECTED_HUE_DIFFERENCE, MAX_HUE_DEVIATION);
        if (MathUtils.equals(hueCoef, 0f)) {
            return 0f;
        }

        var satCoef = calculateSaturationCompatibility(colorsHsv[0], colorsHsv[1], MAX_SATURATION_DEVIATION);
        if (MathUtils.equals(satCoef, 0f)) {
            return 0f;
        }

        var valCoef = calculateValueCompatibility(colorsHsv[0], colorsHsv[1], MAX_VALUE_DEVIATION);
        if (MathUtils.equals(valCoef, 0f)) {
            return 0f;
        }

        return hueCoef * 0.7f + satCoef * 0.15f + valCoef * 0.15f;
    }
}
