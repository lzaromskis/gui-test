package edu.ktu.screenshotanalyser.tools.colorcompatibility;

import edu.ktu.screenshotanalyser.utils.methods.MathUtils;
import edu.ktu.screenshotanalyser.utils.models.PixelHSV;
import edu.ktu.screenshotanalyser.utils.models.PixelRGB;

import java.util.Arrays;

public class TetradicColorCompatibilityCalculator extends BaseColorCompatibilityCalculator {
    private static final float EXPECTED_HUE_DIFFERENCE = 0.25f; // 120 difference in 0-360 scale
    private static final float MAX_HUE_DEVIATION = 0.0278f; // 10 difference in 0-360 scale
    private static final float MAX_SATURATION_DEVIATION = 0.2f;
    private static final float MAX_VALUE_DEVIATION = 0.2f;

    @Override
    public float calculateCompatibility(PixelRGB[] colors) {
        if (colors.length != 4) {
            return 0f;
        }

        var colorsHsv = Arrays
            .stream(colors)
            .map(PixelHSV::new)
            .sorted(PixelHSV::HueComparator)
            .toArray(PixelHSV[]::new);

        var hueCoefSum = 0f;
        var satCoefSum = 0f;
        var valCoefSum = 0f;

        for (int i = 0; i < colorsHsv.length; i++) {
            var hueCoef = calculateHueCompatibilityWithNeighbours(colorsHsv, i, EXPECTED_HUE_DIFFERENCE, MAX_HUE_DEVIATION);
            //if (MathUtils.equals(hueCoef, 0f)) {
            //    return 0f;
            //}

            hueCoefSum += hueCoef;

            var satCoef = calculateSaturationCompatibilityWithNeighbours(colorsHsv, i, MAX_SATURATION_DEVIATION);
            //if (MathUtils.equals(satCoef, 0f)) {
            //    return 0f;
            //}

            satCoefSum += satCoef;

            var valCoef = calculateValueCompatibilityWithNeighbours(colorsHsv, i, MAX_VALUE_DEVIATION);
            //if (MathUtils.equals(valCoef, 0f)) {
            //    return 0f;
            //}

            valCoefSum += valCoef;
        }

        return (hueCoefSum / 4f) * 0.7f + (satCoefSum / 4f) * 0.15f + (valCoefSum / 4f) * 0.15f;
    }
}
