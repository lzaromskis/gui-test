package edu.ktu.screenshotanalyser.enums;

public enum ColorSpaces {
    /**
     * Normal vision
     */
    NORMAL,
    /**
     * Blind to red color
     */
    PROTANOPIA,
    /**
     * Blind to green color
     */
    DEUTERANOPIA,
    /**
     * Blind to blue color
     */
    TRITANOPIA,
    /**
     * Blind to all colors, sees in grayscale
     */
    ACHROMATOPSIA;

    public static ColorSpaces parseString(String value) {
        var lower = value.toLowerCase();
        switch (lower) {
            case "normal" -> {
                return NORMAL;
            }
            case "protanopia" -> {
                return PROTANOPIA;
            }
            case "deuteranopia" -> {
                return DEUTERANOPIA;
            }
            case "tritanopia" -> {
                return TRITANOPIA;
            }
            case "achromatopsia" -> {
                return ACHROMATOPSIA;
            }
            default -> {
                throw new IllegalArgumentException(String.format("Color combination '%s' does not exist.", value));
            }
        }
    }
}
