package edu.ktu.screenshotanalyser.tools.colorcompatibility;

import edu.ktu.screenshotanalyser.utils.models.PixelRGB;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.junit.jupiter.api.Assertions.assertEquals;

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
		return Stream.of(
			arguments(new PixelRGB(0xFFE91617), new PixelRGB(0xFFE97F16), new PixelRGB(0xFFE91680)),
			arguments(new PixelRGB(0xFF3A41C5), new PixelRGB(0xFF793AC5), new PixelRGB(0xFF3A86C5)),
			arguments(new PixelRGB(0xFF66CF30), new PixelRGB(0xFF30CF49), new PixelRGB(0xFFB6CF30))
		);
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
		return Stream.of(
			arguments(new PixelRGB(0xFFCDDA25), new PixelRGB(0xFF73DA25), new PixelRGB(0xFFE41BB2)),
			arguments(new PixelRGB(0xFFD52AAF), new PixelRGB(0xFFAFD52A), new PixelRGB(0xFF2AAFD5)),
			arguments(new PixelRGB(0xFFE7C718), new PixelRGB(0xFFF70873), new PixelRGB(0xFF15EA1F))
		);
	}
}
