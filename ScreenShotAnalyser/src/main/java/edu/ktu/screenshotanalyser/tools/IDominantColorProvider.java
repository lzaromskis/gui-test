package edu.ktu.screenshotanalyser.tools;

import edu.ktu.screenshotanalyser.utils.models.PixelRGB;

import java.awt.image.BufferedImage;

public interface IDominantColorProvider {
    PixelRGB[] getDominantColors(BufferedImage image, int numberOfColors);
    PixelRGB[] getDominantColors(BufferedImage image, int numberOfColors, int maxIterations);
}
