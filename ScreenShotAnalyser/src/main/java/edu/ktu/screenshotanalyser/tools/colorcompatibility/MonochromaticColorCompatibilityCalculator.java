package edu.ktu.screenshotanalyser.tools.colorcompatibility;

import edu.ktu.screenshotanalyser.utils.methods.MathUtils;
import edu.ktu.screenshotanalyser.utils.models.PixelHSV;
import edu.ktu.screenshotanalyser.utils.models.PixelRGB;

import java.util.Arrays;

public class MonochromaticColorCompatibilityCalculator extends BaseColorCompatibilityCalculator {
	private static final float MAX_HUE_DEVIATION = 0.0278f;	// 10 difference in 0-360 scale

	@Override
	public float calculateCompatibility(PixelRGB[] colors) {
		var colorsHsv = Arrays.stream(colors).map(PixelHSV::new).toArray(PixelHSV[]::new);

		var totalHue = 0f;
		for (PixelHSV c : colorsHsv) {
			totalHue += c.getHue();
		}

		var hueAvg = totalHue / colorsHsv.length;
		var tempColor = new PixelHSV(hueAvg, 0f, 0f);
		var totalCoef = 0f;
		for (PixelHSV c : colorsHsv) {
			var hueCoef = calculateHueCompatibility(c, tempColor, 0f, MAX_HUE_DEVIATION);
			if (MathUtils.equals(hueCoef, 0f))
				return 0f;

			totalCoef += hueCoef;
		}

		return totalCoef / colorsHsv.length;
	}
}
