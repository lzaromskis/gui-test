package edu.ktu.screenshotanalyser.enums;

public enum ColorCombinations {
    COMPLEMENTARY,
    MONOCHROMATIC,
    ANALOGOUS,
    TRIADIC,
    TETRADIC
    ;

    public static ColorCombinations parseString(String value) {
        var lower = value.toLowerCase();
        switch (lower) {
            case "complementary" -> {
                return COMPLEMENTARY;
            }
            case "monochromatic" -> {
                return MONOCHROMATIC;
            }
            case "analogous" -> {
                return ANALOGOUS;
            }
            case "triadic" -> {
                return TRIADIC;
            }
            case "tetradic" -> {
                return TETRADIC;
            }
            default -> {
                throw new IllegalArgumentException(String.format("Color combination '%s' does not exist.", value));
            }
        }
    }
}
