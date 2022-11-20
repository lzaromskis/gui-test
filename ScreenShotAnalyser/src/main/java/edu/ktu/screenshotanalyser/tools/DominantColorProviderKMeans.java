package edu.ktu.screenshotanalyser.tools;

import edu.ktu.screenshotanalyser.utils.methods.CollectionUtils;
import edu.ktu.screenshotanalyser.utils.methods.ImageUtils;
import edu.ktu.screenshotanalyser.utils.models.PixelRGB;
import org.opencv.core.*;

import java.awt.image.BufferedImage;
import java.util.*;

public class DominantColorProviderKMeans implements IDominantColorProvider {
    @Override
    public PixelRGB[] getDominantColors(BufferedImage image, int numberOfColors) {
        return getDominantColors(image, numberOfColors, 100);
    }

    @Override
    public PixelRGB[] getDominantColors(BufferedImage image, int numberOfColors, int maxIterations) {
        var img = ImageUtils.bufferedImageToMat(image);
        img.convertTo(img, CvType.CV_32F);
        var data = img.reshape(1, (int)img.total());

        var labels = new Mat();
        var criteria = new TermCriteria(TermCriteria.COUNT, 100, 1);
        var flags = Core.KMEANS_PP_CENTERS;
        var centers = new Mat();
        Core.kmeans(data, numberOfColors, labels, criteria, maxIterations, flags, centers);

        // TODO: Fix this. Quick hack to change BRG to RGB
        var calculatedCentroidsMat = centers.reshape(3,numberOfColors);
        calculatedCentroidsMat.convertTo(calculatedCentroidsMat, CvType.CV_8U);
        var calculatedCentroidsBufImg = ImageUtils.matToBufferedImage(calculatedCentroidsMat);
        var calculatedCentroidsPixel = ImageUtils.bufferedImageToPixelRGBArray(Objects.requireNonNull(calculatedCentroidsBufImg));
        return Arrays.stream(calculatedCentroidsPixel).map(x -> new PixelRGB(x.getBlue(), x.getGreen(), x.getRed())).toArray(PixelRGB[]::new);
    }
}
