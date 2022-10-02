package edu.ktu.screenshotanalyser.utils.models;

import edu.ktu.screenshotanalyser.utils.methods.MathUtils;

import java.awt.*;

public final class PixelHSV {
    private final float _hue;
    private final float _saturation;
    private final float _value;

    public PixelHSV(float hue, float saturation, float value) {
        this._hue = hue;
        this._saturation = saturation;
        this._value = value;
    }

    public PixelHSV(float[] hsv) {
        this(hsv[0], hsv[1], hsv[2]);
    }

    public PixelHSV(PixelHSV other) {
        this(other._hue, other._saturation, other._value);
    }

    public PixelHSV(PixelRGB pixelRGB) {
        this(Color.RGBtoHSB(pixelRGB.getRed(), pixelRGB.getGreen(), pixelRGB.getBlue(), null));
    }

    public float getHue() {
        return _hue;
    }

    public float getSaturation() {
        return _saturation;
    }

    public float getValue() {
        return _value;
    }

    @Override
    public String toString() {
        return "PixelHSV = [h=" + _hue + ", s=" + _saturation + ", v=" + _value + ']';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        PixelHSV pixelHSV = (PixelHSV) o;

        if (Float.compare(pixelHSV._hue, _hue) != 0) {
            return false;
        }
        if (Float.compare(pixelHSV._saturation, _saturation) != 0) {
            return false;
        }
        return Float.compare(pixelHSV._value, _value) == 0;
    }

    @Override
    public int hashCode() {
        int result = (_hue != +0.0f ? Float.floatToIntBits(_hue) : 0);
        result = 31 * result + (_saturation != +0.0f ? Float.floatToIntBits(_saturation) : 0);
        result = 31 * result + (_value != +0.0f ? Float.floatToIntBits(_value) : 0);
        return result;
    }

    public static int HueComparator(PixelHSV lhs, PixelHSV rhs) {
        if (MathUtils.equals(lhs._hue, rhs._hue)) {
            return 0;
        }

        return lhs._hue < rhs._hue ? -1 : 1;
    }
}
