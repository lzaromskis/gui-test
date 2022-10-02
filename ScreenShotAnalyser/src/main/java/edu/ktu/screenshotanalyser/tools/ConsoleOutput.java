package edu.ktu.screenshotanalyser.tools;

public class ConsoleOutput implements IOutput {
    @Override
    public void write(String line) {
        write(line, false);
    }

    @Override
    public void write(String line, boolean overrideLine) {
        String special = overrideLine ? "\r" : "\n";
        System.out.print(special + line);
    }
}
