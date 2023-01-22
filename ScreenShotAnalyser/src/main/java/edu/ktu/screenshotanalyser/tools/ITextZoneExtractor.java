package edu.ktu.screenshotanalyser.tools;

import edu.ktu.screenshotanalyser.context.State;
import edu.ktu.screenshotanalyser.utils.models.TextZoneWithBounds;

import java.awt.image.BufferedImage;

public interface ITextZoneExtractor {
    TextZoneWithBounds[] extract(State state);
}
