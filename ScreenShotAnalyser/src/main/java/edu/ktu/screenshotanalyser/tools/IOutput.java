package edu.ktu.screenshotanalyser.tools;

public interface IOutput {
    void write(String line);

    void write(String line, boolean overrideLine);
}
