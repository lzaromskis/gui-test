package edu.ktu.screenshotanalyser.tools.colorcompatibility;

import edu.ktu.screenshotanalyser.utils.models.PixelRGB;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.params.provider.Arguments.arguments;

public class AnalogousColorCompatibilityCalculatorTests {
    @ParameterizedTest
    @MethodSource("perfectCompatibilityColors")
    public void calculateCompatibility_Perfect(PixelRGB color1, PixelRGB color2, PixelRGB color3) {
        // Arrange
        var calculator = new AnalogousColorCompatibilityCalculator();

        // Act
        var result = calculator.calculateCompatibility(new PixelRGB[]{color1, color2, color3});

        // Assert
        assertEquals(1f, result, 0.02f);
    }

    public static Stream<Arguments> perfectCompatibilityColors() {
        return Stream.of(arguments(new PixelRGB(0xFFE91617), new PixelRGB(0xFFE97F16), new PixelRGB(0xFFE91680)),
                         arguments(new PixelRGB(0xFF3A41C5), new PixelRGB(0xFF793AC5), new PixelRGB(0xFF3A86C5)),
                         arguments(new PixelRGB(0xFF66CF30), new PixelRGB(0xFF30CF49), new PixelRGB(0xFFB6CF30)));
    }

    @ParameterizedTest
    @MethodSource("badCompatibilityColors")
    public void calculateCompatibility_Bad(PixelRGB color1, PixelRGB color2, PixelRGB color3) {
        var calculator = new AnalogousColorCompatibilityCalculator();

        // Act
        var result = calculator.calculateCompatibility(new PixelRGB[]{color1, color2, color3});

        // Assert
        assertEquals(0f, result, 0.02f);
    }

    public static Stream<Arguments> badCompatibilityColors() {
        return Stream.of(arguments(new PixelRGB(0xFFD42BAB), new PixelRGB(0xFF86994A), new PixelRGB(0xFF414F54)),
                         arguments(new PixelRGB(0xFF5A2ED1), new PixelRGB(0xFF601900), new PixelRGB(0xFF519964)),
                         arguments(new PixelRGB(0xFF3CEF10), new PixelRGB(0xFF26387F), new PixelRGB(0xFFB2596B)));
    }
}
