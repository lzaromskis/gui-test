package edu.ktu.screenshotanalyser.tools.colorcompatibility;

import edu.ktu.screenshotanalyser.utils.models.PixelRGB;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.params.provider.Arguments.arguments;

public class ComplementaryColorCompatibilityCalculatorTests {
    @ParameterizedTest
    @MethodSource("perfectCompatibilityColors")
    public void calculateCompatibility_Perfect(PixelRGB color1, PixelRGB color2) {
        // Arrange
        var calculator = new ComplementaryColorCompatibilityCalculator();

        // Act
        var result = calculator.calculateCompatibility(new PixelRGB[]{color1, color2});

        // Assert
        assertEquals(1f, result, 0.02f);
    }

    public static Stream<Arguments> perfectCompatibilityColors() {
        return Stream.of(arguments(new PixelRGB(0xFFFF0000), new PixelRGB(0xFF00FFFF)),
                         arguments(new PixelRGB(0xFF8B33CC), new PixelRGB(0xFF74CC33)),
                         arguments(new PixelRGB(0xFFDCF00F), new PixelRGB(0xFF230FF0)),
                         arguments(new PixelRGB(0xFF816798), new PixelRGB(0xFF7E9867)));
    }

    @ParameterizedTest
    @MethodSource("badCompatibilityColors")
    public void calculateCompatibility_Bad(PixelRGB color1, PixelRGB color2) {
        // Arrange
        var calculator = new ComplementaryColorCompatibilityCalculator();

        // Act
        var result = calculator.calculateCompatibility(new PixelRGB[]{color1, color2});

        // Assert
        assertEquals(0f, result, 0.02f);
    }

    public static Stream<Arguments> badCompatibilityColors() {
        return Stream.of(arguments(new PixelRGB(0xFFFF0000), new PixelRGB(0xFF0000FF)),
                         arguments(new PixelRGB(0xFFD728AD), new PixelRGB(0xFFCF8830)),
                         arguments(new PixelRGB(0xFF35CA8F), new PixelRGB(0xFF708F8D)));
    }
}
