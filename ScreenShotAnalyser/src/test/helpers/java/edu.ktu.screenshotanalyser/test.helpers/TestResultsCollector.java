package edu.ktu.screenshotanalyser.test.helpers;

import edu.ktu.screenshotanalyser.checks.CheckResult;
import edu.ktu.screenshotanalyser.checks.ResultImage;
import edu.ktu.screenshotanalyser.checks.ResultsCollector;
import edu.ktu.screenshotanalyser.context.State;

import java.util.ArrayList;
import java.util.List;

public class TestResultsCollector extends ResultsCollector {
    private final List<CheckResult> _checkResults;
    private final List<ResultImage> _resultImages;

    public TestResultsCollector() {
        super(false);

        _checkResults = new ArrayList<>();
        _resultImages = new ArrayList<>();
    }

    public List<CheckResult> getCheckResults() {
        return _checkResults;
    }

    public List<ResultImage> getResultImages() {
        return _resultImages;
    }

    @Override
    public void addFailure(CheckResult result) {
        _checkResults.add(result);
    }

    @Override
    public void addFailureImage(ResultImage image) {
        _resultImages.add(image);
    }

    @Override
    public boolean wasChecked(State state) {
        return false;
    }

    @Override
    public void finishRun() {

    }
}
