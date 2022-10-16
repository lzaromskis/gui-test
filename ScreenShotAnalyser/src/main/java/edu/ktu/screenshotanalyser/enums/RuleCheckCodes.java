package edu.ktu.screenshotanalyser.enums;

public enum RuleCheckCodes {
    // experiments
    CLIPPED_CONTROL_CHECK,
    CLIPPED_TEXT_CHECK,
    GRAMMAR_CHECK,
    MISSING_TEXT_CHECK,
    MISSING_TRANSLATION_CHECK,
    MIXED_LANGUAGES_APP_CHECK,
    MIXED_LANGUAGES_STATE_CHECK,
    OBSCURED_CONTROL_CHECK,
    OBSCURED_TEXT_CHECK,
    OFFENSIVE_MESSAGES_CHECK,
    TOO_HARD_TO_UNDERSTAND_CHECK,
    UNALIGNED_CONTROLS_CHECK,
    UNLOCALIZED_ICONS_CHECK,
    UNREADABLE_TEXT_CHECK,
    WRONG_ENCODING_CHECK,
    WRONG_LANGUAGE_CHECK,
    // experiments.colors
    COLOR_COMPATIBILITY_CHECK,
    COLOR_READABILITY_CHECK
    ;

    @SuppressWarnings("SpellCheckingInspection")
    public static RuleCheckCodes parseString(String value) {
        var lower = value.toLowerCase();
        switch (lower) {
            case "colorcompatibilitycheck" -> {
                return COLOR_COMPATIBILITY_CHECK;
            }
            case "colorreadabilitycheck" -> {
                return COLOR_READABILITY_CHECK;
            }
            default -> {
                throw new IllegalArgumentException(String.format("Rule check '%s' does not exist.", value));
            }
        }
    }

}
