package edu.ktu.screenshotanalyser.tools;

import edu.ktu.screenshotanalyser.utils.models.PixelRGB;

public interface IDominantColorProvider {
    PixelRGB[] getDominantColors(PixelRGB[] data, int numberOfColors);

    PixelRGB[] getDominantColors(PixelRGB[] data, int numberOfColors, int maxIterations);
}
