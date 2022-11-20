package edu.ktu.screenshotanalyser.utils.models;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.params.provider.Arguments.*;

public class PixelRGBTests {
    @Test
    public void constructor_FromSRGB() {
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

    @ParameterizedTest
    @MethodSource("calculateRelativeLuminanceArgs")
    public void calculateRelativeLuminance_OK(PixelRGB pixel, float expected) {
        // Arrange

        // Act
        var result = pixel.calculateRelativeLuminance();

        // Assert
        assertEquals(expected, result, 0.0001f);
    }

    public static Stream<Arguments> calculateRelativeLuminanceArgs() {
        return Stream.of(
            arguments(new PixelRGB(0xff000000), 0f),
            arguments(new PixelRGB(0xffffffff), 1f),
            arguments(new PixelRGB(0xffff0000), 0.2126f),
            arguments(new PixelRGB(0xff00ff00), 0.7152f),
            arguments(new PixelRGB(0xff0000ff), 0.0722f)
        );
    }

    @ParameterizedTest
    @MethodSource("calculateContrastRatioArgs")
    public void calculateContrastRatio_OK(PixelRGB lhs, PixelRGB rhs, float expected) {
        // Arrange

        // Act
        var result = PixelRGB.calculateContrastRatio(lhs, rhs);

        // Assert
        assertEquals(expected, result, 0.01f);
    }

    public static Stream<Arguments> calculateContrastRatioArgs() {
        return Stream.of(
            arguments(new PixelRGB(0xffffffff), new PixelRGB(0xffffffff), 1f),
            arguments(new PixelRGB(0xffffffff), new PixelRGB(0xff000000), 21f),
            arguments(new PixelRGB(0xffff0000), new PixelRGB(0xff00ff00), 2.91f),
            arguments(new PixelRGB(0xff80ff00), new PixelRGB(0xff000080), 12.36f)
        );
    }
}
