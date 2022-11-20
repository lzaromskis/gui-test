package edu.ktu.screenshotanalyser.utils.models;

import edu.ktu.screenshotanalyser.utils.methods.RGBUtils;
import io.vavr.Function2;

import java.io.Serializable;

public final class PixelRGB implements Serializable {
    private final int _red;
    private final int _green;
    private final int _blue;

    public PixelRGB(int red, int green, int blue) {
        this._red = red;
        this._green = green;
        this._blue = blue;
    }

    public PixelRGB(int[] rgb) {
        this(rgb[0], rgb[1], rgb[2]);
    }

    public PixelRGB(int sRGB) {
        this._red = (sRGB >> 16) & 0xFF;
        this._green = (sRGB >> 8) & 0xFF;
        this._blue = sRGB & 0xFF;
    }

    public PixelRGB(PixelRGB other) {
        this(other._red, other._green, other._blue);
    }

    public int getRed() {
        return _red;
    }

    public float getRedLinear() {
        return RGBUtils.toLinearRGBChannel(_red);
    }

    public int getGreen() {
        return _green;
    }

    public float getGreenLinear() {
        return RGBUtils.toLinearRGBChannel(_green);
    }

    public int getBlue() {
        return _blue;
    }

    public float getBlueLinear() {
        return RGBUtils.toLinearRGBChannel(_blue);
    }

    public int toSRGB() {
        return 0xFF000000 | (_red << 16) | (_green << 8) | _blue;
    }

    public PixelRGB multiply(Matrix3x3 matrix) {
        var linearRed = getRedLinear();
        var linearGreen = getGreenLinear();
        var linearBlue = getBlueLinear();

        float convertedRed = matrix.get(0) * linearRed + matrix.get(1) * linearGreen + matrix.get(2) * linearBlue;
        float convertedGreen = matrix.get(3) * linearRed + matrix.get(4) * linearGreen + matrix.get(5) * linearBlue;
        float convertedBlue = matrix.get(6) * linearRed + matrix.get(7) * linearGreen + matrix.get(8) * linearBlue;

        return new PixelRGB(RGBUtils.toSRGBChannel(convertedRed), RGBUtils.toSRGBChannel(convertedGreen), RGBUtils.toSRGBChannel(convertedBlue));
    }

    // https://www.w3.org/TR/WCAG20/#relativeluminancedef
    public float calculateRelativeLuminance() {
        var rLuminance = RGBUtils.calculateChannelRelativeLuminance(getRed());
        var gLuminance = RGBUtils.calculateChannelRelativeLuminance(getGreen());
        var bLuminance = RGBUtils.calculateChannelRelativeLuminance(getBlue());

        return 0.2126f * rLuminance + 0.7152f * gLuminance + 0.0722f * bLuminance;
    }

    public static float calculateContrastRatio(PixelRGB lhs, PixelRGB rhs) {
        var lhsL = lhs.calculateRelativeLuminance();
        var rhsL = rhs.calculateRelativeLuminance();

        var l1 = Math.max(lhsL, rhsL);
        var l2 = Math.min(lhsL, rhsL);

        return (l1 + 0.05f) / (l2 + 0.05f);
    }

    @Override
    public String toString() {
        return "PixelRGB [r=" + _red + ", g=" + _green + ", b=" + _blue + "]";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        PixelRGB pixelRGB = (PixelRGB) o;

        if (_red != pixelRGB._red) {
            return false;
        }
        if (_green != pixelRGB._green) {
            return false;
        }
        return _blue == pixelRGB._blue;
    }

    @Override
    public int hashCode() {
        int result = _red;
        result = 31 * result + _green;
        result = 31 * result + _blue;
        return result;
    }
}
