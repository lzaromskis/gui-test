package edu.ktu.screenshotanalyser.utils.models;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PixelRGBTests {
	@Test
	public void constructor_FromSRGB()
	{
		// Arrange
		var srgb = 0xff112233;

		// Act
		var result = new PixelRGB(srgb);

		// Assert
		assertEquals(0x11, result.getRed());
		assertEquals(0x22, result.getGreen());
		assertEquals(0x33, result.getBlue());
	}

	@Test
	public void toLinear_OK() {
		// Arrange
		var pixel = new PixelRGB(50, 100, 150);

		// Act && Assert
		assertEquals(0.0319f, pixel.getRedLinear(), 0.001f);
		assertEquals(0.1274f, pixel.getGreenLinear(), 0.001f);
		assertEquals(0.3050f, pixel.getBlueLinear(), 0.001f);
	}

	@Test
	public void toSRGB_OK() {
		// Arrange
		var pixel = new PixelRGB(0x11, 0x22, 0x33);

		// Act
		var result = pixel.toSRGB();

		// Assert
		assertEquals(0xff112233, result);
	}
}
