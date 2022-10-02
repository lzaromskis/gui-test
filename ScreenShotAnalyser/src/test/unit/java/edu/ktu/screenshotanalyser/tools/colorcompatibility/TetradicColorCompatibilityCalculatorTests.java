package edu.ktu.screenshotanalyser.tools.colorcompatibility;

import edu.ktu.screenshotanalyser.utils.models.PixelRGB;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.params.provider.Arguments.arguments;

public class TetradicColorCompatibilityCalculatorTests {
	@ParameterizedTest
	@MethodSource("perfectCompatibilityColors")
	public void calculateCompatibility_Perfect(PixelRGB color1, PixelRGB color2, PixelRGB color3, PixelRGB color4) {
		// Arrange
		var calculator = new TetradicColorCompatibilityCalculator();

		// Act
		var result = calculator.calculateCompatibility(new PixelRGB[]{color1, color2, color3, color4});

		// Assert
		assertEquals(1f, result, 0.02f);
	}

	public static Stream<Arguments> perfectCompatibilityColors() {
		return Stream.of(
				arguments(new PixelRGB(0xFFD12E6B), new PixelRGB(0xFFBCD12E), new PixelRGB(0xFF2ED194), new PixelRGB(0xFF432ED1)),
				arguments(new PixelRGB(0xFFFF7E00), new PixelRGB(0xFF02FF00), new PixelRGB(0xFF0081FF), new PixelRGB(0xFFFD00FF)),
				arguments(new PixelRGB(0xFFB58E4A), new PixelRGB(0xFF4AB559), new PixelRGB(0xFF4A71B5), new PixelRGB(0xFFB54AA6))
		);
	}

	@ParameterizedTest
	@MethodSource("badCompatibilityColors")
	public void calculateCompatibility_Bad(PixelRGB color1, PixelRGB color2, PixelRGB color3, PixelRGB color4) {
		var calculator = new TriadicColorCompatibilityCalculator();

		// Act
		var result = calculator.calculateCompatibility(new PixelRGB[]{color1, color2, color3, color4});

		// Assert
		assertEquals(0f, result, 0.02f);
	}

	public static Stream<Arguments> badCompatibilityColors() {
		return Stream.of(
				arguments(new PixelRGB(0xFFE75618), new PixelRGB(0xFF18E756), new PixelRGB(0xFF5618E7), new PixelRGB(0xFF18A9E7)),
				arguments(new PixelRGB(0xFF31EB14), new PixelRGB(0xFF14EB62), new PixelRGB(0xFF9DEB14), new PixelRGB(0xFFF0E20F)),
				arguments(new PixelRGB(0xFFEE1138), new PixelRGB(0xFFA7EE11), new PixelRGB(0xFF11EEC7), new PixelRGB(0xFF635AA5))
		);
	}
}
