package edu.ktu.screenshotanalyser.checks;

import edu.ktu.screenshotanalyser.checks.experiments.*;
import edu.ktu.screenshotanalyser.checks.experiments.colors.ColorCompatibilityCheck;
import edu.ktu.screenshotanalyser.checks.experiments.colors.ColorReadabilityCheck;
import edu.ktu.screenshotanalyser.enums.RuleCheckCodes;

public class RuleCheckProvider {
    public BaseRuleCheck getRuleCheck(RuleCheckCodes code) {
        switch (code) {
            case COLOR_COMPATIBILITY_CHECK -> {
                return new ColorCompatibilityCheck();
            }
            case COLOR_READABILITY_CHECK -> {
                return new ColorReadabilityCheck();
            }
            default -> {
                throw new IllegalArgumentException(String.format("Cannot create rule check with code '%s'.", code));
            }
        }
    }
}
