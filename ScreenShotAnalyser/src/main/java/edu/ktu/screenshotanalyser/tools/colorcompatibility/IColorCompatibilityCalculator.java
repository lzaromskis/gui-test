package edu.ktu.screenshotanalyser.tools.colorcompatibility;

import edu.ktu.screenshotanalyser.utils.models.PixelRGB;

public interface IColorCompatibilityCalculator {
    float calculateCompatibility(PixelRGB[] colors);
}
