package edu.ktu.screenshotanalyser.tools;

import edu.ktu.screenshotanalyser.context.State;

import java.awt.image.BufferedImage;

public interface ITextZoneExtractor {
    BufferedImage[] extract(State state);
}
