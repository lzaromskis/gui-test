package edu.ktu.screenshotanalyser.tools;

import edu.ktu.screenshotanalyser.enums.ColorSpaces;

import java.awt.image.BufferedImage;

/**
Interface for converting images to a specific color space.
 */
public interface IColorSpaceConverter {
    /**
    Converts a given image to a specific color space.
    @param image Image to convert.
    @param colorSpace Color space to convert the image to.
    @returns Image that is converted to the specified color space.
     */
    BufferedImage convertImage(BufferedImage image, ColorSpaces colorSpace);
}
