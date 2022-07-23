package edu.ktu.screenshotanalyser.tools;

public class ProgressTracker implements IObserver {
    private int _expectedImagesCount;
    private IObservable[] _ruleChecks;
    private IOutput _output;

    public ProgressTracker() {

    }

    public void setExpectedImagesCount(int expectedImagesCount) {
        _expectedImagesCount = expectedImagesCount;
    }

    public void setRuleChecks(IObservable[] ruleChecks) {
        for (var ruleCheck : _ruleChecks) {
            ruleCheck.deleteObserver(this);
        }

        _ruleChecks = ruleChecks;
        for (var ruleCheck : _ruleChecks) {
            ruleCheck.addObserver(this);
        }
    }

    public void setOutput(IOutput output) {
        _output = output;
    }

    @Override
    public void notifyObserver() {

    }
}
