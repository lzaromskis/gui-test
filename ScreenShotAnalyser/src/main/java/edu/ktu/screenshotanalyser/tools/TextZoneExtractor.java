package edu.ktu.screenshotanalyser.tools;

import edu.ktu.screenshotanalyser.context.State;
import org.opencv.core.Rect;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class TextZoneExtractor implements ITextZoneExtractor{
    private static final int MINIMUM_ZONE_WIDTH = 6;
    private static final int MINIMUM_ZONE_HEIGHT = 6;

    @Override
    public BufferedImage[] extract(State state) {
        var controls = state.getActualControls();
        var stateImage = state.getImage();
        var textZones = new ArrayList<BufferedImage>();

        for (var control: controls) {
            var bounds = control.getBounds();
            var text = control.getText();
            if (text == null || !control.isVisible() || !isInView(stateImage, bounds)) {
                continue;
            }

            try {
                var textZone = stateImage.getSubimage(
                    bounds.x,
                    bounds.y,
                    getCorrectMeasurement(bounds.x, bounds.width, stateImage.getWidth()),
                    getCorrectMeasurement(bounds.y, bounds.height, stateImage.getHeight())
                );
                // Filter out small zones
                if (textZone.getWidth() >= MINIMUM_ZONE_WIDTH && textZone.getHeight() >= MINIMUM_ZONE_HEIGHT) {
                    textZones.add(textZone);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        return textZones.toArray(BufferedImage[]::new);
    }

    private int getCorrectMeasurement(int coord, int length, int maxLength) {
        return Math.min(length, maxLength - coord - 1);
    }

    private boolean isInView(BufferedImage image, Rect bounds) {
        return bounds.x < image.getWidth() && bounds.y < image.getHeight();
    }
}
