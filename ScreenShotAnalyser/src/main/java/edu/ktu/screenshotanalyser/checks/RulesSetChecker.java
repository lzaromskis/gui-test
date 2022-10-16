package edu.ktu.screenshotanalyser.checks;

import edu.ktu.screenshotanalyser.context.AppContext;
import edu.ktu.screenshotanalyser.context.State;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;

public class RulesSetChecker {
    public void addRule(BaseRuleCheck rule) {
        this.rulesCheckers.add(rule);
    }

    public void runStateChecks(State state, ExecutorService exec, ResultsCollector failures) {
        if (failures.wasChecked(state)) {
            //System.out.println("Skip: " + state.getImageFile().getAbsolutePath());

            return;
        }

        var checksFinished = new ArrayList<IStateRuleChecker>();
        var checksAvailable = this.rulesCheckers
            .stream()
            .filter(IStateRuleChecker.class::isInstance)
            .map(IStateRuleChecker.class::cast)
            .collect(Collectors.toList());

        exec.submit(() -> {
            while (true) {
                synchronized (checksFinished) {
                    if (checksAvailable.size() == checksFinished.size()) {
                        break;
                    }
                }

                var finsihedRule = new BaseRuleCheck(34, "Finished") {
                };
                var result = new CheckResult(state, finsihedRule, "", 0);

                failures.addFailure(result);
            }
        });

        for (var check : checksAvailable) {
            exec.submit(() -> {
                try {
                    if (!failures.wasChecked(state)) {
                        check.analyze(state, failures);
                    }
                } catch (Throwable ex) {
                    ex.printStackTrace();
                } finally {
                    ((BaseRuleCheck) check).notifyObservers();
                    synchronized (checksFinished) {
                        checksFinished.add(check);
                    }
                }
            });
        }
    }

    public void runAppChecks(AppContext context, ExecutorService exec, ResultsCollector failures) {
        for (var ruleCheck : this.rulesCheckers) {
            if (ruleCheck instanceof IAppRuleChecker check) {
                exec.submit(() -> {
                    check.analyze(context, failures);
                    ((BaseRuleCheck) check).notifyObservers();
                });
            }
        }
    }

    private final List<BaseRuleCheck> rulesCheckers = new ArrayList<>();
}
