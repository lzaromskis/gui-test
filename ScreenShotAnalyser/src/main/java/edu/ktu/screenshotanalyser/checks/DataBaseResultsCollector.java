package edu.ktu.screenshotanalyser.checks;

import edu.ktu.screenshotanalyser.context.State;
import edu.ktu.screenshotanalyser.exceptions.MissingSettingException;
import edu.ktu.screenshotanalyser.tools.Configuration;
import edu.ktu.screenshotanalyser.tools.Settings;
import edu.ktu.screenshotanalyser.tools.StatisticsManager;

import java.io.IOException;
import java.util.HashSet;

public class DataBaseResultsCollector extends ResultsCollector {
    public DataBaseResultsCollector(String name, boolean resume) {
        super(false);

        this.testsRunId = this.statisticsManager.startTestsRun(name, resume);
    }

    @Override
    public synchronized void addFailure(CheckResult result) {
        super.addFailure(result);

        this.statisticsManager.logDetectedDefect(this.testsRunId, result);
    }

    @Override
    public boolean wasChecked(State state) {
        var fileName = state
            .getImageFile()
            .getAbsolutePath();

        String imagesFolderPath;
        try {
            imagesFolderPath = Configuration
                .instance().getAppImagesFolderPath();
        } catch (MissingSettingException | IOException e) {
            imagesFolderPath = Settings.appImagesFolder.getAbsolutePath();
        }

        if (fileName.startsWith(imagesFolderPath)) {
            fileName = fileName.substring(imagesFolderPath.length() + 1);
        }

        synchronized (this) {
            if (null == this.checkedStates) {
                this.checkedStates = new HashSet<String>(this.statisticsManager.getCheckedStates(this.testsRunId));
            }
        }

        return this.checkedStates.contains(fileName);


        //return this.statisticsManager.wasChecked(this.testsRunId, state);
    }

    @Override
    public void finishRun() {
        this.statisticsManager.finishRun(this.testsRunId);
    }

    @Override
    public void addFailureImage(ResultImage image) {
    }

    private final long testsRunId;
    private final StatisticsManager statisticsManager = new StatisticsManager();
    private HashSet<String> checkedStates = null;
}
