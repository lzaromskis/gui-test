package edu.ktu.screenshotanalyser.tools;

public interface IObservable {
    void addObserver(IObserver observer);

    void deleteObserver(IObserver observer);

    void notifyObservers();
}
