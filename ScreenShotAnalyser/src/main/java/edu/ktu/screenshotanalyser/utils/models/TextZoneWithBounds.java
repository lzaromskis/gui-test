package edu.ktu.screenshotanalyser.utils.models;

import org.opencv.core.Rect;

import java.awt.image.BufferedImage;

public class TextZoneWithBounds {
    private final BufferedImage _textZone;
    private final Rect _bounds;

    public TextZoneWithBounds(BufferedImage textZone, Rect bounds) {
        _textZone = textZone;
        _bounds = bounds;
    }

    public BufferedImage getTextZone() {
        return _textZone;
    }

    public Rect getBounds() {
        return _bounds;
    }
}
