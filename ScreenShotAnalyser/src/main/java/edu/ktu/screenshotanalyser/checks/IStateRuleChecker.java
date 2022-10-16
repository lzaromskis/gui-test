package edu.ktu.screenshotanalyser.checks;

import edu.ktu.screenshotanalyser.context.State;
import edu.ktu.screenshotanalyser.exceptions.MissingSettingException;

import java.io.IOException;

/**
 * Checks one application's screenshot for defects.
 */
public interface IStateRuleChecker {
    void analyze(State state, ResultsCollector failures) throws IOException, MissingSettingException;
}
