package edu.ktu.screenshotanalyser.utils.methods;

public final class RGBUtils {
    private RGBUtils() {

    }

    // https://entropymine.com/imageworsener/srgbformula/
    public static float toLinearRGBChannel(int sRGBChannel) {
        if (sRGBChannel <= 0) {
            return 0f;
        }
        if (sRGBChannel >= 255) {
            return 1f;
        }
        float fv = sRGBChannel / 255f;
        if (fv < 0.04045f) {
            return fv / 12.92f;
        }
        return (float) Math.pow((fv + 0.055f) / 1.055f, 2.4f);
    }

    // https://entropymine.com/imageworsener/srgbformula/
    public static int toSRGBChannel(float linearRGBChannel) {
        if (linearRGBChannel <= 0f) {
            return 0;
        }
        if (linearRGBChannel >= 1f) {
            return 0xFF;
        }
        if (linearRGBChannel < 0.0031308f) {
            return (int) (0.5f + (linearRGBChannel * 12.92f * 255f));
        }
        return (int) (0f + 255f * ((float) Math.pow(linearRGBChannel, 1f / 2.4f) * 1.055f - 0.055f)) & 0xFF;
    }

    // https://www.w3.org/TR/WCAG20/#relativeluminancedef
    public static float calculateChannelRelativeLuminance(int channel) {
        var channelSRGB = (float)channel / 255f;
        return channelSRGB <= 0.03928f
            ? channelSRGB / 12.92f
            : (float)Math.pow((channelSRGB + 0.055f) / 1.055f, 2.4f);
    }
}
