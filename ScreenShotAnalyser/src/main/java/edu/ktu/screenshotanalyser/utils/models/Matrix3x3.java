package edu.ktu.screenshotanalyser.utils.models;

public final class Matrix3x3 {
    private final float[] _data;

    public Matrix3x3(float v0, float v1, float v2, float v3, float v4, float v5, float v6, float v7, float v8) {
        _data = new float[]{v0, v1, v2, v3, v4, v5, v6, v7, v8};
    }

    public float get(int index) {
        return _data[index % 9];
    }
}
