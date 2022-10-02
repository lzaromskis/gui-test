package edu.ktu.screenshotanalyser.tools.colorcompatibility;

import edu.ktu.screenshotanalyser.utils.methods.MathUtils;
import edu.ktu.screenshotanalyser.utils.models.PixelHSV;

public abstract class BaseColorCompatibilityCalculator implements IColorCompatibilityCalculator {
	protected float calculateHueCompatibility(PixelHSV color1, PixelHSV color2, float expectedHueDifference, float maxHueDeviation) {
		var hueDiff = MathUtils.circularDifference(color1.getHue(), color2.getHue(), 0f, 1f);
		return 1f - MathUtils.clamp(Math.abs(hueDiff - expectedHueDifference) / maxHueDeviation, 0f, 1f);
	}

	protected float calculateSaturationCompatibility(PixelHSV color1, PixelHSV color2, float maxSaturationDeviation) {
		var satDiff = Math.abs(color1.getSaturation() - color2.getSaturation());
		return 1f - MathUtils.clamp(satDiff / maxSaturationDeviation, 0f, 1f);
	}

	protected float calculateValueCompatibility(PixelHSV color1, PixelHSV color2, float maxValueDeviation) {
		var valDiff = Math.abs(color1.getValue() - color2.getValue());
		return 1f - MathUtils.clamp(valDiff / maxValueDeviation, 0f, 1f);
	}

	protected float calculateHueCompatibilityWithNeighbours(PixelHSV[] colors, int index, float expectedHueDifference, float maxHueDeviation) {
		var color = colors[index];
		var leftColor = colors[Math.floorMod(index - 1, colors.length)];
		var rightColor = colors[Math.floorMod(index + 1, colors.length)];

		var leftHueCoef = calculateHueCompatibility(color, leftColor, expectedHueDifference, maxHueDeviation);
		var rightHueCoef = calculateHueCompatibility(color, rightColor, expectedHueDifference, maxHueDeviation);

		return (leftHueCoef + rightHueCoef) / 2f;
	}

	protected float calculateSaturationCompatibilityWithNeighbours(PixelHSV[] colors, int index, float maxSaturationDeviation) {
		var color = colors[index];
		var leftColor = colors[Math.floorMod(index - 1, colors.length)];
		var rightColor = colors[Math.floorMod(index + 1, colors.length)];

		var leftSatCoef = calculateSaturationCompatibility(color, leftColor, maxSaturationDeviation);
		var rightSatCoef = calculateSaturationCompatibility(color, rightColor, maxSaturationDeviation);

		return (leftSatCoef + rightSatCoef) / 2f;
	}

	protected float calculateValueCompatibilityWithNeighbours(PixelHSV[] colors, int index, float maxValueDeviation) {
		var color = colors[index];
		var leftColor = colors[Math.floorMod(index - 1, colors.length)];
		var rightColor = colors[Math.floorMod(index + 1, colors.length)];

		var leftValCoef = calculateValueCompatibility(color, leftColor, maxValueDeviation);
		var rightValCoef = calculateValueCompatibility(color, rightColor, maxValueDeviation);

		return (leftValCoef + rightValCoef) / 2f;
	}
}
