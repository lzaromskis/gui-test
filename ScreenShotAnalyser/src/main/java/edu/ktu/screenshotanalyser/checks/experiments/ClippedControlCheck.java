package edu.ktu.screenshotanalyser.checks.experiments;

import edu.ktu.screenshotanalyser.checks.BaseTextRuleCheck;
import edu.ktu.screenshotanalyser.checks.CheckResult;
import edu.ktu.screenshotanalyser.checks.IStateRuleChecker;
import edu.ktu.screenshotanalyser.checks.ResultsCollector;
import edu.ktu.screenshotanalyser.context.Control;
import edu.ktu.screenshotanalyser.context.State;

import java.util.stream.Collectors;

public class ClippedControlCheck extends BaseTextRuleCheck implements IStateRuleChecker {
    public ClippedControlCheck() {
        super(11, "Clipped Control");
    }

    @Override
    public void analyze(State state, ResultsCollector failures) {
        var controls = state
            .getActualControls()
            .stream()
            .filter(p -> !shouldSkipControl(p, state));
        var clippedControls = controls
            .filter(p -> isClipped(p, state))
            .collect(Collectors.toList());

        if (!clippedControls.isEmpty()) {
            failures.addFailure(new CheckResult(state, this, "1", clippedControls.size()));
        }
    }

    private boolean isClipped(Control control, State state) {
        if ((control.getBounds().x + control.getBounds().width > state.getImageSize().width + 5) || (control.getBounds().y + control.getBounds().height > state.getImageSize().height + 5)) {
            System.out.println("clipped " + control
                .getBounds()
                .toString() + " | " + control.getSignature() + " | " + (control.getBounds().x + control.getBounds().width) + " > " + state.getImageSize().width + " | " + (control.getBounds().y + control.getBounds().height) + " > " + state.getImageSize().height);

            return true;
        }

        return false;
    }

    private boolean shouldSkipControl(Control control, State state) {
        if (!control.isVisible()) {
            return true;
        }

        if (("Test Ad".equals(control.getText())) || (isAd(control))) {
            return true;
        }

        var bounds = control.getBounds();

        if ((bounds.width <= 0) || (bounds.height <= 0)) {
            return true;
        }

        if ((bounds.width <= 3) || (bounds.height <= 3)) {
            return true;
        }

        if ((bounds.x >= state.getImageSize().width) || (bounds.y >= state.getImageSize().height)) {
            return true;
        }

        if ((bounds.x + bounds.width <= 0) || (bounds.y + bounds.height <= 0)) {
            return true;
        }

        // if ((control.getBounds().x + control.getBounds().width >= state.getImageSize().width) || (control.getBounds().y + control.getBounds().height >= state.getImageSize().height))
        // {
        // return true;
        // }

        return false;
    }
}
