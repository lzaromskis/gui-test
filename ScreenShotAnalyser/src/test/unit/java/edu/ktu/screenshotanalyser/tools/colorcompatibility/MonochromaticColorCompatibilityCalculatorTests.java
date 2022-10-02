package edu.ktu.screenshotanalyser.tools.colorcompatibility;

import edu.ktu.screenshotanalyser.utils.models.PixelRGB;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.params.provider.Arguments.arguments;

public class MonochromaticColorCompatibilityCalculatorTests {
    @ParameterizedTest
    @MethodSource("perfectCompatibilityColors")
    public void calculateCompatibility_2Colors_Perfect(PixelRGB color1, PixelRGB color2) {
        // Arrange
        var calculator = new MonochromaticColorCompatibilityCalculator();

        // Act
        var result = calculator.calculateCompatibility(new PixelRGB[]{color1, color2});

        // Assert
        assertEquals(1f, result, 0.02f);
    }

    public static Stream<Arguments> perfectCompatibilityColors() {
        return Stream.of(arguments(new PixelRGB(0xFF4C7F99), new PixelRGB(0xFF3D7A99)), arguments(new PixelRGB(0xFF371CBC), new PixelRGB(0xFF5F44E5)));
    }

    @ParameterizedTest
    @MethodSource("badCompatibilityColors")
    public void calculateCompatibility_2Colors_Bad(PixelRGB color1, PixelRGB color2) {
        // Arrange
        var calculator = new MonochromaticColorCompatibilityCalculator();

        // Act
        var result = calculator.calculateCompatibility(new PixelRGB[]{color1, color2});

        // Assert
        assertEquals(0f, result, 0.02f);
    }

    public static Stream<Arguments> badCompatibilityColors() {
        return Stream.of(arguments(new PixelRGB(0xFF413299), new PixelRGB(0xFF903299)),
                         arguments(new PixelRGB(0xFFB8D308), new PixelRGB(0xFF2EB5D1)),
                         arguments(new PixelRGB(0xFFCEC0C0), new PixelRGB(0xFFC3CCC9)));
    }
}
