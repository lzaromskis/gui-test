package edu.ktu.screenshotanalyser.tools;

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
}
