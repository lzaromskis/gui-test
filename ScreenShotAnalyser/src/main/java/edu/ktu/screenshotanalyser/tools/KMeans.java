package edu.ktu.screenshotanalyser.tools;

import edu.ktu.screenshotanalyser.utils.methods.CollectionUtils;
import edu.ktu.screenshotanalyser.utils.methods.ImageUtils;
import edu.ktu.screenshotanalyser.utils.models.PixelRGB;

import java.awt.image.BufferedImage;
import java.util.*;

public class KMeans implements IDominantColorProvider {
    @Override
    public PixelRGB[] getDominantColors(BufferedImage image, int numberOfColors) {
        return getDominantColors(image, numberOfColors, 100);
    }

    @Override
    public PixelRGB[] getDominantColors(BufferedImage image, int numberOfColors, int maxIterations) {
        var imageData = ImageUtils.bufferedImageToPixelRGBArray(image);
        return getDominantColors(imageData, numberOfColors, maxIterations);
    }

    @Override
    public PixelRGB[] getDominantColors(PixelRGB[] data, int numberOfColors) {
        return getDominantColors(data, numberOfColors, 100);
    }

    @Override
    public PixelRGB[] getDominantColors(PixelRGB[] data, int numberOfColors, int maxIterations) {
        int dataLen = data.length;
        if (dataLen < numberOfColors) {
            return new PixelRGB[0];
        }

        PixelRGB[] centroids = initCentroids(data, numberOfColors);
        HashMap<Integer, List<PixelRGB>> groups = new HashMap<Integer, List<PixelRGB>>();
        for (int i = 0; i < numberOfColors; i++) {
            groups.put(i, new LinkedList<PixelRGB>());
        }

        PixelRGB[] oldCentroids = new PixelRGB[3];
        int currentIter = 0;
        while (!areCentroidsEqual(centroids, oldCentroids) && currentIter < numberOfColors) {
            for (int i = 0; i < numberOfColors; i++) {
                oldCentroids[i] = new PixelRGB(centroids[i]);
                groups
                    .get(i)
                    .clear();
            }

            for (int i = 0; i < dataLen; i++) {
                List<Double> distances = new ArrayList<Double>(dataLen);
                for (int j = 0; j < numberOfColors; j++) {
                    distances.add(getPixelDistance(data[i], centroids[j]));
                }
                groups
                    .get(CollectionUtils.getMinIndex(distances))
                    .add(data[i]);
            }

            for (int i = 0; i < numberOfColors; i++) {
                centroids[i] = calcCentroid(groups.get(i));
            }
            currentIter++;
        }
        return centroids;
    }

    private boolean areCentroidsEqual(PixelRGB[] centroids1, PixelRGB[] centroids2) {
        for (int i = 0; i < centroids1.length; i++) {
            if (!centroids1[i].equals(centroids2[i])) {
                return false;
            }
        }

        return true;
    }

    private PixelRGB[] initCentroids(PixelRGB[] data, int clusters) {
        int dataLen = data.length;
        PixelRGB[] centroids = new PixelRGB[clusters];

        Random rand = new Random();
        int randIndex = rand.nextInt(dataLen);
        centroids[0] = data[randIndex];
        for (int i = 1; i < clusters; i++) {
            List<Double> distances = new ArrayList<Double>(dataLen);
            for (int j = 0; j < dataLen; j++) {
                List<Double> tmp = new LinkedList<Double>();
                for (int k = 0; k < i; k++) {
                    tmp.add(getPixelDistance(data[j], centroids[k]));
                }
                distances.add(tmp
                                  .stream()
                                  .min(Double::compare)
                                  .get());
            }
            centroids[i] = data[CollectionUtils.getMaxIndex(distances)];
        }
        return centroids;
    }

    private PixelRGB calcCentroid(List<PixelRGB> data) {
        double rSum = 0d;
        double gSum = 0d;
        double bSum = 0d;
        for (PixelRGB val : data) {
            rSum += val.getRed();
            gSum += val.getGreen();
            bSum += val.getBlue();
        }
        double size = data.size();
        return new PixelRGB((int) (rSum / size), (int) (gSum / size), (int) (bSum / size));
    }

    private double getPixelDistance(PixelRGB p1, PixelRGB p2) {
        return Math.sqrt(getPixelDistanceSquare(p1, p2));
    }

    private double getPixelDistanceSquare(PixelRGB p1, PixelRGB p2) {
        return Math.pow(p1.getRed() - p2.getRed(), 2) + Math.pow(p1.getGreen() - p2.getGreen(), 2) + Math.pow(p1.getBlue() - p2.getBlue(), 2);
    }
}
