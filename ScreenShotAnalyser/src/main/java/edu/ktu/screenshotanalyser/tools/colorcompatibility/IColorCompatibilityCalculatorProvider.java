package edu.ktu.screenshotanalyser.tools.colorcompatibility;

import edu.ktu.screenshotanalyser.enums.ColorCombinations;

public interface IColorCompatibilityCalculatorProvider {
    IColorCompatibilityCalculator getCalculator(ColorCombinations combination);
}
