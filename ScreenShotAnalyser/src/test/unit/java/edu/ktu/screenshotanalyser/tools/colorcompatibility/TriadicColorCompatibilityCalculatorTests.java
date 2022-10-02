package edu.ktu.screenshotanalyser.tools.colorcompatibility;

import edu.ktu.screenshotanalyser.utils.models.PixelRGB;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.params.provider.Arguments.arguments;

public class TriadicColorCompatibilityCalculatorTests {
    @ParameterizedTest
    @MethodSource("perfectCompatibilityColors")
    public void calculateCompatibility_Perfect(PixelRGB color1, PixelRGB color2, PixelRGB color3) {
        // Arrange
        var calculator = new TriadicColorCompatibilityCalculator();

        // Act
        var result = calculator.calculateCompatibility(new PixelRGB[]{color1, color2, color3});

        // Assert
        assertEquals(1f, result, 0.02f);
    }

    public static Stream<Arguments> perfectCompatibilityColors() {
        return Stream.of(arguments(new PixelRGB(0xFFD42BAB), new PixelRGB(0xFFABD42B), new PixelRGB(0xFF2BABD4)),
                         arguments(new PixelRGB(0xFF5A2ED1), new PixelRGB(0xFFD15A2E), new PixelRGB(0xFF2ED15A)),
                         arguments(new PixelRGB(0xFF3CEF10), new PixelRGB(0xFF103CEF), new PixelRGB(0xFFEF103C)));
    }

    @ParameterizedTest
    @MethodSource("badCompatibilityColors")
    public void calculateCompatibility_Bad(PixelRGB color1, PixelRGB color2, PixelRGB color3) {
        var calculator = new TriadicColorCompatibilityCalculator();

        // Act
        var result = calculator.calculateCompatibility(new PixelRGB[]{color1, color2, color3});

        // Assert
        assertEquals(0f, result, 0.02f);
    }

    public static Stream<Arguments> badCompatibilityColors() {
        return Stream.of(arguments(new PixelRGB(0xFF8300FF), new PixelRGB(0xFFFF00FB), new PixelRGB(0xFF0400FF)),
                         arguments(new PixelRGB(0xFF6E29D6), new PixelRGB(0xFFD66E29), new PixelRGB(0xFFD1ED12)),
                         arguments(new PixelRGB(0xFFEA5415), new PixelRGB(0xFF40EA15), new PixelRGB(0xFF15ABEA)));
    }
}
