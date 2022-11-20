package edu.ktu.screenshotanalyser.tools;

import edu.ktu.screenshotanalyser.checks.IAppRuleChecker;
import edu.ktu.screenshotanalyser.checks.IStateRuleChecker;
import edu.ktu.screenshotanalyser.utils.methods.MathUtils;
import org.apache.commons.lang3.time.StopWatch;

public class ProgressTracker implements IObserver {
    private IObservable[] _ruleChecks;
    private IOutput _output;
    private final IProgressBarRenderer _progressBarRenderer;
    private final StopWatch _stopWatch;

    private int _expectedProcessedCount;
    private int _processedCount;

    public ProgressTracker(
        IProgressBarRenderer progressBarRenderer,
        IObservable[] ruleChecks,
        int appCount,
        int stateImageCount,
        IOutput output) {
        _progressBarRenderer = progressBarRenderer;
        _stopWatch = new StopWatch();
        _output = output;
        prepareTracker(ruleChecks, appCount, stateImageCount);
    }

    private void prepareTracker(IObservable[] ruleChecks, int appCount, int stateImageCount) {
        _expectedProcessedCount = 0;
        _ruleChecks = ruleChecks;
        for (var ruleCheck : _ruleChecks) {
            ruleCheck.addObserver(this);
            if (ruleCheck instanceof IAppRuleChecker) {
                _expectedProcessedCount += appCount;
            }

            if (ruleCheck instanceof IStateRuleChecker) {
                _expectedProcessedCount += stateImageCount;
            }
        }

        _processedCount = 0;
    }

    public void writeProgress() {
        var progress = getProgress();
        var progressPercentageString = getProgressPercentageString(progress);
        var estimatedTimeRemainingString = getEstimatedTimeRemainingString(progress);

        _output.writeOverride(String.format("%s | %s", progressPercentageString, estimatedTimeRemainingString));
    }

    private float getProgress() {
        // Returns 0 if expected process count is 0
        return _expectedProcessedCount == 0 ? 0f : MathUtils.clamp((float) _processedCount / (float) _expectedProcessedCount, 0f, 1f);
    }

    private String getProgressPercentageString(float progress) {
        var progressBar = _progressBarRenderer.render(progress);
        return String.format("%s %.2f%%", progressBar, progress * 100f);
    }

    private String getEstimatedTimeRemainingString(float progress) {
        if (progress == 0f) {
            return "??:?? remaining";
        }

        if (progress == 1f) {
            return "00:00 remaining";
        }

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
