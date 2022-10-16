package edu.ktu.screenshotanalyser.checks;

import edu.ktu.screenshotanalyser.context.State;
import edu.ktu.screenshotanalyser.tools.ConsoleOutput;
import edu.ktu.screenshotanalyser.tools.IOutput;

/**
 * Collects all analysis results in a thread safe manner.
 */
public abstract class ResultsCollector {
    private IOutput _output;

    protected ResultsCollector(boolean acceptsResultImages) {
        this.acceptsResultImages = acceptsResultImages;
        _output = ConsoleOutput.instance();
    }

    public synchronized void addFailure(CheckResult result) {
        if ((result.getMessage() != null) && (result
                                                  .getMessage()
                                                  .length() > 0)) {
            _output.writeBeforeCurrent(result.getMessage());
        }
    }

    public abstract void addFailureImage(ResultImage image);

    public abstract boolean wasChecked(State state);

    public abstract void finishRun();

    public final boolean acceptsResultImages;
}
