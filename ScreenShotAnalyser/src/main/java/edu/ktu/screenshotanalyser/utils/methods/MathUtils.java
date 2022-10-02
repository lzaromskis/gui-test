package edu.ktu.screenshotanalyser.utils.methods;

public final class MathUtils {
    private MathUtils() {

    }

    public static float clamp(float val, float min, float max) {
        if (val < min)
            return min;

        if (val > max)
            return max;

        return val;
    }

    public static boolean equals(float lhs, float rhs, float epsilon) {
        return lhs == rhs || Math.abs(lhs - rhs) < epsilon;
    }

    public static boolean equals(float lhs, float rhs) {
        return MathUtils.equals(lhs, rhs, 0.00001f);
    }

    public static float circularDifference(float lhs, float rhs, float scaleMin, float scaleMax) {
        if (scaleMax <= scaleMin)
            throw new IllegalArgumentException("scaleMax must be greater than scaleMin");

        var scale = scaleMax - scaleMin;
        var halfScale = scale / 2f;

        float diff;
        if (lhs < rhs)
            diff = lhs - rhs;
        else
            diff = rhs - lhs;

        if (Math.abs(diff) > halfScale)
            return diff + scale;
        else
            return Math.abs(diff);
    }
}
