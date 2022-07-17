package edu.ktu.screenshotanalyser.tools;

import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;

public class ColorSpaceConverter {
	
	/// Based on https://github.com/DaltonLens/libDaltonLens/blob/master/libDaltonLens.c
	public BufferedImage convertImage(BufferedImage image, ColorSpace colorSpace) {
		return simulate_vienot1999(image, colorSpace);
	}
	
	private static float toLinearRGB(final int v) {
		float fv = v / 255f;
		if (fv < 0.04045f)
			return fv / 12.92f;
		return (float) Math.pow((fv + 0.055f) / 1.055f, 2.4f);
	}
	
	private static int toSRGB(final float v) {
		if (v <= 0f)
			return 0;
		if (v >= 1f)
			return 0xFF;
		if (v < 0.0031308f)
			return (int)(0.5f + (v * 12.92f * 255f));
		return (int)(0f + 255f * ((float) Math.pow(v, 1f / 2.4f) * 1.055f - 0.055f)) & 0xFF;
	}
	
	private final class DLBrettel1997Params {
		public final float[] rgbCvdFromRgb1;
		public final float[] rgbCvdFromRgb2;
		public final float[] separationPlaneNormalInRgb;
		
		public DLBrettel1997Params(float[] rgbCvd1, float[] rgbCvd2, float[] separationPlaneNormal) {
			rgbCvdFromRgb1 = rgbCvd1;
			rgbCvdFromRgb2 = rgbCvd2;
			separationPlaneNormalInRgb = separationPlaneNormal;
		}
		
	private final DLBrettel1997Params BRETTEL_PROTAN_PARAMS = new DLBrettel1997Params(
			new float[] { 0.14980f,  1.19548f, -0.34528f,
						  0.10764f,  0.84864f,  0.04372f,
						  0.00384f, -0.00540f,  1.00156f },
			new float[] { 0.14570f,  1.16172f, -0.30742f,
						  0.10816f,  0.85291f,  0.03892f,
						  0.00386f, -0.00524f,  1.00139f },
			new float[] { 0.00048f,  0.00393f, -0.00441f });
	}
	
	private static final float[] VienotProtanRgbCvdFromRgb = { 0.11238f,  0.88762f,  0.00000f,
															   0.11238f,  0.88762f, -0.00000f,
															   0.00401f, -0.00401f,  1.00000f};

	private BufferedImage simulate_vienot1999 (BufferedImage srgbImage, ColorSpace colorSpace) {
		int width = srgbImage.getWidth();
		int height = srgbImage.getHeight();
		BufferedImage resultImage = copyBufferedImage(srgbImage);
		float[] params;
		switch(colorSpace) {
		case PROTANOPIA:
			params = VienotProtanRgbCvdFromRgb;
			break;
		default:
			params = new float[9];
			break;
		}
		
		for (int row = 0; row < height; row++) {
			for (int column = 0; column < width; column++) {
				int rgb = srgbImage.getRGB(column, row);
				float r = toLinearRGB((rgb >> 16) & 255);
				float g = toLinearRGB((rgb >> 8) & 255);
				float b = toLinearRGB(rgb & 255);
				
				float rCvd = params[0] * r + params[1] * g + params[2] * b;
				float gCvd = params[3] * r + params[4] * g + params[5] * b;
				float bCvd = params[6] * r + params[7] * g + params[8] * b;
				
				int rRes = toSRGB(rCvd);
				int gRes = toSRGB(gCvd);
				int bRes = toSRGB(bCvd);
				
				rgb &= 0xFF000000;
				rgb |= rRes << 16;
				rgb |= gRes << 8;
				rgb |= bRes;
				
				resultImage.setRGB(column, row, rgb);
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
