package edu.ktu.screenshotanalyser.tools;

import edu.ktu.screenshotanalyser.enums.ColorSpaces;
import edu.ktu.screenshotanalyser.utils.models.Matrix3x3;
import edu.ktu.screenshotanalyser.utils.models.PixelRGB;

import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;

// Color matrices taken from: https://www.inf.ufrgs.br/%7Eoliveira/pubs_files/CVD_Simulation/CVD_Simulation.html
public class ColorSpaceConverter implements  IColorSpaceConverter {
    private final static Matrix3x3 PROTANOPIA_MATRIX = new Matrix3x3(
             0.152286f,  1.052583f, -0.204868f,
             0.114503f,  0.786281f,  0.099216f,
            -0.003882f, -0.048116f,  1.051998f
    );

    private final static Matrix3x3 DEUTERANOPIA_MATRIX = new Matrix3x3(
             0.367322f,  0.860646f, -0.227968f,
             0.280085f,  0.672501f,  0.047413f,
            -0.011820f,  0.042940f,  0.968881f
    );

    private final static Matrix3x3 TRITANOPIA_MATRIX = new Matrix3x3(
             1.255528f, -0.076749f, -0.178779f,
            -0.078411f,  0.930809f,  0.147602f,
             0.004733f,  0.691367f,  0.303900f
    );

    private final static Matrix3x3 ACHROMATOPSIA_MATRIX = new Matrix3x3(
            0.333333f, 0.333333f, 0.333333f,
            0.333333f, 0.333333f, 0.333333f,
            0.333333f, 0.333333f, 0.333333f
    );

    @Override
    public BufferedImage convertImage(BufferedImage image, ColorSpaces colorSpace) {
        switch (colorSpace) {
            case NORMAL -> {
                return image;
            }
            case PROTANOPIA -> {
                return applyMatrix(image, PROTANOPIA_MATRIX);
            }
            case DEUTERANOPIA -> {
                return applyMatrix(image, DEUTERANOPIA_MATRIX);
            }
            case TRITANOPIA -> {
                return applyMatrix(image, TRITANOPIA_MATRIX);
            }
            case ACHROMATOPSIA -> {
                return applyMatrix(image, ACHROMATOPSIA_MATRIX);
            }
            default -> {
                throw new IllegalArgumentException(String.format("Unknown color space '%s'.", colorSpace));
            }
        }
    }

    private BufferedImage applyMatrix(BufferedImage sRGBImage, Matrix3x3 matrix) {
        var width = sRGBImage.getWidth();
        var height = sRGBImage.getHeight();
        var resultImage = copyBufferedImage(sRGBImage);

        for (int row = 0; row < height; row++) {
            for (int column = 0; column < width; column++) {
                var sRGB = sRGBImage.getRGB(column, row);
                var pixel = new PixelRGB(sRGB);
                var convertedPixel = pixel.multiply(matrix);
                resultImage.setRGB(column, row, convertedPixel.toSRGB());
            }
        }

        return resultImage;
    }

    private static BufferedImage copyBufferedImage(BufferedImage bi) {
        ColorModel cm = bi.getColorModel();
        boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
        WritableRaster raster = bi.copyData(bi.getRaster().createCompatibleWritableRaster());
        return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
    }
}