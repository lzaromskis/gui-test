package edu.ktu.screenshotanalyser.tools.colorreadability;

import edu.ktu.screenshotanalyser.utils.models.PixelRGB;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.params.provider.Arguments.arguments;

public class ContrastRatioCompatibilityCalculatorTests {
    @ParameterizedTest
    @MethodSource("contrastRatioData")
    public void calculateCompatibility_Ok(PixelRGB[] colors, float expected) {
        // Arrange
        var calculator = new ContrastRatioCompatibilityCalculator();

        // Act
        var result = calculator.calculateCompatibility(colors);

        // Assert
        assertEquals(expected, result, 0.01f);
    }

    public static Stream<Arguments> contrastRatioData() {
        return Stream.of(arguments(new PixelRGB[] { new PixelRGB(0xFFFFFFFF), new PixelRGB(0xFF000000) }, 1f),
                         arguments(new PixelRGB[] { new PixelRGB(0xFFFFFFFF), new PixelRGB(0xFFFFFFFF) }, 0f),
                         arguments(new PixelRGB[] { new PixelRGB(0xFFF2800D), new PixelRGB(0xFF0E455D) }, 0.38f),
                         arguments(new PixelRGB[] { new PixelRGB(0xFFD32C84), new PixelRGB(0xFF090701) }, 0.78f));
    }
}
