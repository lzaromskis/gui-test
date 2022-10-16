package edu.ktu.screenshotanalyser.tools;

public class ConsoleOutput implements IOutput {
    private static ConsoleOutput _instance;
    private String _lastWrittenLine = null;

    private ConsoleOutput() {

    }

    public static synchronized ConsoleOutput instance() {
        if (_instance == null) {
            _instance = new ConsoleOutput();
        }

        return _instance;
    }

    @Override
    public synchronized void write(String line) {
        write(line, false, false);
    }

    @Override
    public synchronized void writeOverride(String line) {
        write(line, true, false);
    }

    @Override
    public synchronized void writeBeforeCurrent(String line) {
        write(line, false, true);
    }

    private synchronized void write(String line, boolean overrideLine, boolean writeBeforeCurrent) {
        if (overrideLine && writeBeforeCurrent) {
            throw new IllegalArgumentException("Cannot both override a line and write before current line");
        }

        String special = overrideLine || writeBeforeCurrent ? "\r" : "\n";
        String endString = writeBeforeCurrent ? "\n" + _lastWrittenLine : "";
        System.out.print(special + line + endString);

        if (!writeBeforeCurrent) {
            _lastWrittenLine = line;
        }
    }
}
