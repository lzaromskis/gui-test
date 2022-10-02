package edu.ktu.screenshotanalyser.tools.colorcompatibility;

import edu.ktu.screenshotanalyser.enums.ColorCombinations;

public class NumberOfColorsInCombinationProvider implements INumberOfColorsInCombinationProvider {
    @Override
    public int getNumberOfColors(ColorCombinations combination) {
        switch (combination) {
            case MONOCHROMATIC, COMPLEMENTARY -> {
                return 2;
            }
            case ANALOGOUS, TRIADIC -> {
                return 3;
            }
            case TETRADIC -> {
                return 4;
            }
            default -> {
                return 0;
            }
        }
    }
}
