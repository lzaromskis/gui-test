package edu.ktu.screenshotanalyser.tools.colorcompatibility;

import edu.ktu.screenshotanalyser.enums.ColorCombinations;

public interface INumberOfColorsInCombinationProvider {
    int getNumberOfColors(ColorCombinations combination);
}
