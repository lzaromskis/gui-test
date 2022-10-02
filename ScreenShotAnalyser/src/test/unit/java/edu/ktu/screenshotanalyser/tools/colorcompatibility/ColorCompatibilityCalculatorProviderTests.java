package edu.ktu.screenshotanalyser.tools.colorcompatibility;

import edu.ktu.screenshotanalyser.enums.ColorCombinations;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.params.provider.Arguments.arguments;

public class ColorCompatibilityCalculatorProviderTests {
    @SuppressWarnings("rawtypes")
    @ParameterizedTest
    @MethodSource("getCalculatorValidClasses")
    public void getCalculator_OK(ColorCombinations combination, Class expectedType) {
        // Arrange
        var provider = new ColorCompatibilityCalculatorProvider();

        // Act
        var calculator = provider.getCalculator(combination);

        // Assert
        //noinspection unchecked
        assertInstanceOf(expectedType, calculator);
    }

    public static Stream<Arguments> getCalculatorValidClasses() {
        return Stream.of(arguments(ColorCombinations.ANALOGOUS, AnalogousColorCompatibilityCalculator.class),
                         arguments(ColorCombinations.MONOCHROMATIC, MonochromaticColorCompatibilityCalculator.class),
                         arguments(ColorCombinations.COMPLEMENTARY, ComplementaryColorCompatibilityCalculator.class),
                         arguments(ColorCombinations.TRIADIC, TriadicColorCompatibilityCalculator.class),
                         arguments(ColorCombinations.TETRADIC, TetradicColorCompatibilityCalculator.class));
    }
}
