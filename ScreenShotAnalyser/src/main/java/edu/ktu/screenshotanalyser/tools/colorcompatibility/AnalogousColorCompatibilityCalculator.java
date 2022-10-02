package edu.ktu.screenshotanalyser.tools.colorcompatibility;

import edu.ktu.screenshotanalyser.utils.methods.MathUtils;
import edu.ktu.screenshotanalyser.utils.models.PixelHSV;
import edu.ktu.screenshotanalyser.utils.models.PixelRGB;

import java.util.Arrays;

public class AnalogousColorCompatibilityCalculator extends BaseColorCompatibilityCalculator {
    private static final float EXPECTED_HUE_DIFFERENCE = 0.08333f; // 30 difference in 0-360 scale
    private static final float MAX_HUE_DEVIATION = 0.0278f; // 10 difference in 0-360 scale
    private static final float MAX_SATURATION_DEVIATION = 0.2f;
    private static final float MAX_VALUE_DEVIATION = 0.2f;

    @Override
    public float calculateCompatibility(PixelRGB[] colors) {
        if (colors.length != 3) {
            return 0f;
        }

        var colorsHsv = Arrays
            .stream(colors)
            .map(PixelHSV::new)
            .toArray(PixelHSV[]::new);

        var middleColor = getMiddleColor(colorsHsv);
        var otherColors = Arrays
            .stream(colorsHsv)
            .filter(c -> c != middleColor)
            .toArray(PixelHSV[]::new);

        var hueCoefSum = 0f;
        var satCoefSum = 0f;
        var valCoefSum = 0f;
        for (var c : otherColors) {
            var hueCoef = calculateHueCompatibility(c, middleColor, EXPECTED_HUE_DIFFERENCE, MAX_HUE_DEVIATION);
            if (MathUtils.equals(hueCoef, 0f)) {
                return 0f;
            }

            hueCoefSum += hueCoef;

            var satCoef = calculateSaturationCompatibility(c, middleColor, MAX_SATURATION_DEVIATION);
            if (MathUtils.equals(satCoef, 0f)) {
                return 0f;
            }

            satCoefSum += satCoef;

            var valCoef = calculateValueCompatibility(c, middleColor, MAX_VALUE_DEVIATION);
            if (MathUtils.equals(valCoef, 0f)) {
                return 0f;
            }

            valCoefSum += valCoef;
        }

        return (hueCoefSum / 2f) * 0.7f + (satCoefSum / 2f) * 0.15f + (valCoefSum / 2f) * 0.15f;
    }

    private PixelHSV getMiddleColor(PixelHSV[] colors) {
        PixelHSV middle = null;
        float minDiff = Float.MAX_VALUE;

        for (var color : colors) {
            var diffSum = 0f;
            for (var other : colors) {
                if (color == other) {
                    continue;
                }

                diffSum += MathUtils.circularDifference(color.getHue(), other.getHue(), 0f, 1f);

            }
            if (diffSum < minDiff) {
                minDiff = diffSum;
                middle = color;
            }
        }

        return middle;
    }
}
