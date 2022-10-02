package edu.ktu.screenshotanalyser.context;

import junit.framework.Assert;
import org.junit.jupiter.api.Test;
import org.opencv.core.Rect;

public class ControlTests {
    @Test
    void testIsOverlapping() {
        var control1 = new Control(null, null, null, new Rect(0, 0, 100, 100), null, null, null, false, null);
        var control2 = new Control(null, null, null, new Rect(50, 50, 100, 100), null, null, null, false, null);

        var result = control2.isOverlapping(control1);

        Assert.assertTrue(result);
    }

    @Test
    void testIsOverlapingNoIntersection() {
        var control3 = new Control(null, null, null, new Rect(0, 0, 100, 100), null, null, null, false, null);
        var control4 = new Control(null, null, null, new Rect(150, 150, 100, 100), null, null, null, false, null);

        var result2 = control3.isOverlapping(control4);

        Assert.assertFalse(result2);
    }
}
