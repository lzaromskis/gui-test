package edu.ktu.screenshotanalyser.checks;

import edu.ktu.screenshotanalyser.checks.experiments.*;
import edu.ktu.screenshotanalyser.checks.experiments.colors.ColorCompatibilityCheck;

public class RuleCheckProvider {
    public BaseRuleCheck getRuleCheck(String code) {
        switch (code) {
            case RuleCheckCodes.CLIPPED_CONTROL_CHECK -> {
                return new ClippedControlCheck();
            }
            case RuleCheckCodes.CLIPPED_TEXT_CHECK -> {
                return new ClippedTextCheck();
            }
            case RuleCheckCodes.COLOR_COMPATIBILITY_CHECK -> {
                return new ColorCompatibilityCheck();
            }
            case RuleCheckCodes.GRAMMAR_CHECK -> {
                return new GrammarCheck();
            }
            case RuleCheckCodes.MISSING_TEXT_CHECK -> {
                return new MissingTextCheck();
            }
            case RuleCheckCodes.MISSING_TRANSLATION_CHECK -> {
                return new MissingTranslationCheck();
            }
            default -> {
                throw new IllegalArgumentException(String.format("Cannot create rule check with code '%s'.", code));
            }
        }
    }
}
