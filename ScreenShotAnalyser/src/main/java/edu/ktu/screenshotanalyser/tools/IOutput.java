package edu.ktu.screenshotanalyser.tools;

public interface IOutput {
    void write(String line);
    void writeOverride(String line);
    void writeBeforeCurrent(String line);
}
