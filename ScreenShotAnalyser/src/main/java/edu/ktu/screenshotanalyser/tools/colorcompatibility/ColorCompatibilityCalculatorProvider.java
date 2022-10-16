package edu.ktu.screenshotanalyser.tools.colorcompatibility;

import edu.ktu.screenshotanalyser.enums.ColorCombinations;

public class ColorCompatibilityCalculatorProvider implements IColorCompatibilityCalculatorProvider {
    public IColorCompatibilityCalculator getCalculator(ColorCombinations combination) {
        switch (combination) {
            case COMPLEMENTARY -> {
                return new ComplementaryColorCompatibilityCalculator();
            }
            case MONOCHROMATIC -> {
                return new MonochromaticColorCompatibilityCalculator();
            }
            case ANALOGOUS -> {
                return new AnalogousColorCompatibilityCalculator();
            }
            case TRIADIC -> {
                return new TriadicColorCompatibilityCalculator();
            }
            case TETRADIC -> {
                return new TetradicColorCompatibilityCalculator();
            }
            default -> {
                return null;
            }
        }
    }
}
