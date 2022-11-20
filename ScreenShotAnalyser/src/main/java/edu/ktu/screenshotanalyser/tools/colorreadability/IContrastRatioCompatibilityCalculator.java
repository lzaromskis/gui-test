package edu.ktu.screenshotanalyser.tools.colorreadability;

import edu.ktu.screenshotanalyser.utils.models.PixelRGB;

public interface IContrastRatioCompatibilityCalculator {
    float calculateCompatibility(PixelRGB[] colors);
}
