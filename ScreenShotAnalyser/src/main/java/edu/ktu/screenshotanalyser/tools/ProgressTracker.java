package edu.ktu.screenshotanalyser.tools;

import edu.ktu.screenshotanalyser.utils.methods.MathUtils;
import org.apache.commons.lang3.time.StopWatch;

public class ProgressTracker implements IObserver {
    private int _expectedImagesCount;
    private IObservable[] _ruleChecks;
    private IOutput _output;
    private final IProgressBarRenderer _progressBarRenderer;
    private final StopWatch _stopWatch;

    private int _expectedProcessedCount;
    private int _processedCount;

    public ProgressTracker(IProgressBarRenderer progressBarRenderer) {
        _progressBarRenderer = progressBarRenderer;
        _stopWatch = new StopWatch();
    }

    public void setExpectedImagesCount(int expectedImagesCount) {
        _expectedImagesCount = expectedImagesCount;
    }

    public void setRuleChecks(IObservable[] ruleChecks) {
        if (_ruleChecks != null) {
            for (var ruleCheck : _ruleChecks) {
                ruleCheck.deleteObserver(this);
            }
        }

        _ruleChecks = ruleChecks;
        for (var ruleCheck : _ruleChecks) {
            ruleCheck.addObserver(this);
        }

        _expectedProcessedCount = _expectedImagesCount * _ruleChecks.length;
        _processedCount = 0;
    }

    public void setOutput(IOutput output) {
        _output = output;
    }

    public void writeProgress() {
        var progressPercentageString = getProgressPercentageString();
        var estimatedTimeRemainingString = getEstimatedTimeRemainingString();

        _output.write(String.format("%s | %s", progressPercentageString, estimatedTimeRemainingString), true);
    }

    private float getProgress() {
        // Returns 0 if expected process count is 0
        return _expectedProcessedCount == 0 ? 0f : MathUtils.clamp((float) _processedCount / (float) _expectedProcessedCount, 0f, 1f);
    }

    private String getProgressPercentageString() {
        var progress = getProgress();
        var progressBar = _progressBarRenderer.render(progress);

        return String.format("%s %.2f%%", progressBar, progress * 100f);
    }

    private String getEstimatedTimeRemainingString() {
        var elapsedTimeMillis = _stopWatch.getTime();
        var todoProcess = _expectedProcessedCount - _processedCount;
        var estimatedTimeMillis = (elapsedTimeMillis / _processedCount) * todoProcess;

        var estimatedTimeWholeMinutes = estimatedTimeMillis / 60000;
        var estimatedTimeWholeSeconds = (estimatedTimeMillis - estimatedTimeWholeMinutes * 60000) / 1000;
        var estimatedTimeWholeSecondsRounded = estimatedTimeWholeSeconds + (5 - (estimatedTimeWholeSeconds % 5));

        if (estimatedTimeWholeSecondsRounded >= 60) {
            estimatedTimeWholeMinutes++;
            estimatedTimeWholeSecondsRounded -= 60;
        }

        return String.format("%02d:%02d remaining", estimatedTimeWholeMinutes, estimatedTimeWholeSecondsRounded);
    }

    @Override
    public void notifyObserver() {
        // Start counting time from the first update
        if (_stopWatch.isStopped()) {
            _stopWatch.start();
        }

        _processedCount++;
        writeProgress();
    }
}
