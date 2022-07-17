package edu.ktu.screenshotanalyser.checks.experiments;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Stream;

import edu.ktu.screenshotanalyser.checks.BaseRuleCheck;
import edu.ktu.screenshotanalyser.checks.IStateRuleChecker;
import edu.ktu.screenshotanalyser.checks.ResultsCollector;
import edu.ktu.screenshotanalyser.context.State;

public class ColorCompatabilityCheck extends BaseRuleCheck implements IStateRuleChecker {

	public ColorCompatabilityCheck(long id, String ruleCode) {
		super(id, ruleCode);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void analyze(State state, ResultsCollector failures) {
		// TODO Auto-generated method stub
		
	}
	
	public void analyze(BufferedImage image, ColorCombinations combination, int numberOfColors) {
		Pixel[] colors = getDominantColors(image, numberOfColors);
		for (int i = 0; i < numberOfColors; i++) {
			System.out.println("Color" + i + " = " + colors[i].toString());
		}
		
		float compatability = 1f;
		
		if (combination == ColorCombinations.COMPLEMENTARY)
			compatability = calculateComplementaryCompatability(colors);
		else if (combination == ColorCombinations.MONOCHROMATIC)
			compatability = calculateMonochromaticCompatability(colors);

		System.out.println(combination + " compatability = " + compatability);
	}
	
	public Pixel[] getDominantColors(BufferedImage image, int numberOfColors) {
		System.out.println("Getting dominant colors...");
		Pixel[] pixels = getImagePixels(image);
		
		return kMeans(pixels, numberOfColors, 100);
	}
	
	private static final float MAX_DEVIATION = 35f;
	
	public float calculateComplementaryCompatability(Pixel[] colors) {
		if (colors.length != 2)
			return 0f;
		
		int rDif = Math.abs(colors[0].r + colors[1].r - 0xFF);
		int gDif = Math.abs(colors[0].g + colors[1].g - 0xFF);
		int bDif = Math.abs(colors[0].b + colors[1].b - 0xFF);
		
		float rCoef = clamp((float)rDif / MAX_DEVIATION, 0f, 1f);
		float gCoef = clamp((float)gDif / MAX_DEVIATION, 0f, 1f);
		float bCoef = clamp((float)bDif / MAX_DEVIATION, 0f, 1f);
		
		return 1f - (rCoef + gCoef + bCoef) / 3f;
	}
	
	private static final float MAX_HUE_DEVIATION = 0.05f;
	
	public float calculateMonochromaticCompatability(Pixel[] colors) {
		PixelHSV[] colorsHSV = pixelToPixelHSV(colors);

		float totalHue = 0f;
		for (PixelHSV c : colorsHSV) {
			totalHue += c.h;
		}
		float hueAvg = totalHue / colorsHSV.length;
		
		float totalCoef = 0f;
		for (PixelHSV c : colorsHSV) {
			float diff = Math.abs(c.h - hueAvg);
			float invCoef = diff / MAX_HUE_DEVIATION;
			float coef = 1f - invCoef;
			float clampedCoef = clamp(coef, 0f, 1f);
			totalCoef += clampedCoef;
		}
		
		return totalCoef / colorsHSV.length;
	}
	
	private float clamp(float val, float min, float max) {
		if (val < min)
			return min;
		if (val > max)
			return max;
		return val;
	}
	
	private PixelHSV[] pixelToPixelHSV(Pixel[] pixels) {
		PixelHSV[] newPixels = new PixelHSV[pixels.length];
		for (int i = 0; i < pixels.length; i++) {
			//float[] hsv = new float[3];
			float[] hsv = Color.RGBtoHSB(pixels[i].r, pixels[i].g, pixels[i].b, null);
			newPixels[i] = new PixelHSV(hsv);
		}
		return newPixels;
	}
	
	public enum ColorCombinations {
		COMPLEMENTARY,
		MONOCHROMATIC,
		ANALOGOUS,
		TRIADIC,
		TETRADIC
	}
	
	private Pixel[] getImagePixels(BufferedImage image) {
		int width = image.getWidth();
		int height = image.getHeight();
		
		Pixel[] pixels = new Pixel[width * height];
		
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				int index = i * height + j;
				int rgb = image.getRGB(i, j);
				int r = (rgb >> 16) & 255;
				int g = (rgb >> 8) & 255;
				int b = rgb & 255;
				try {
					pixels[index] = new Pixel(r, g, b);
				}
				catch (Exception ex) {
					System.out.println(ex.toString());
				}
			}
		}
		
		return pixels;
	}
	
	private class Pixel {
		@Override
		public String toString() {
			return "Pixel [r=" + r + ", g=" + g + ", b=" + b + "]";
		}
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getEnclosingInstance().hashCode();
			result = prime * result + Objects.hash(b, g, r);
			return result;
		}
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Pixel other = (Pixel) obj;
			if (!getEnclosingInstance().equals(other.getEnclosingInstance()))
				return false;
			return b == other.b && g == other.g && r == other.r;
		}

		public Pixel(int r, int g, int b) {
			this.r = r;
			this.g = g;
			this.b = b;
		}
		
		public Pixel(Pixel other) {
			this.r = other.r;
			this.g = other.g;
			this.b = other.b;
		}
		
		public int r;
		public int g;
		public int b;
		private ColorCompatabilityCheck getEnclosingInstance() {
			return ColorCompatabilityCheck.this;
		}
	}
	
	private class PixelHSV {
		public float h;
		public float s;
		public float v;
		
		public PixelHSV(float h, float s, float v) {
			this.h = h;
			this.s = s;
			this.v = v;
		}
		
		public PixelHSV(float[] hsv) {
			this(hsv[0], hsv[1], hsv[2]);
		}
	}
	
	private Pixel[] kMeans(Pixel[] data, int clusters, int maxIters) {
		int dataLen = data.length;
		if (dataLen < clusters)
			return new Pixel[0];
		
		Pixel[] centroids = initCentroids(data, clusters);
		HashMap<Integer, List<Pixel>> groups = new HashMap<Integer, List<Pixel>>();
		for(int i = 0; i < clusters; i++)
			groups.put(i, new LinkedList<Pixel>());
		
		Pixel[] oldCentroids = new Pixel[3];
		int currentIter = 0;
		while (!areCentroidsEqual(centroids, oldCentroids) && currentIter < maxIters) {
			System.out.println("KMeans iteration #" + currentIter);
			for (int i = 0; i < clusters; i++) {
				oldCentroids[i] = new Pixel(centroids[i]);
				groups.get(i).clear();
			}
			
			for (int i = 0; i < dataLen; i++) {
				List<Double> distances = new ArrayList<Double>(dataLen);
				for (int j = 0; j < clusters; j++) { 
					distances.add(getPixelDistance(data[i], centroids[j]));
				}
				groups.get(getMinIndex(distances)).add(data[i]);
			}
			
			for (int i = 0; i < clusters; i++) {
				centroids[i] = calcCentroid(groups.get(i));
			}
			currentIter++;
		}
		return centroids;
	}
	
	private boolean areCentroidsEqual(Pixel[] centroids1, Pixel[] centroids2) {
		for (int i = 0; i < centroids1.length; i++)
			if (!centroids1[i].equals(centroids2[i]))
				return false;
		return true;
	}
	
	private Pixel[] initCentroids(Pixel[] data, int clusters) {
		int dataLen = data.length;
		Pixel[] centroids = new Pixel[clusters];
		
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
				distances.add(tmp.stream().min(Double::compare).get());
			}
			centroids[i] = data[getMaxIndex(distances)];
		}
		return centroids;
	}
	
	private Pixel calcCentroid(List<Pixel> data) {
		double rSum = 0d; 
		double gSum = 0d;
		double bSum = 0d;
		for (int i = 0; i < data.size(); i++) {
			Pixel val = data.get(i);
			rSum += val.r;
			gSum += val.g;
			bSum += val.b;
		}
		double size = data.size();
		return new Pixel((int)(rSum / size), (int)(gSum / size), (int)(bSum / size));
	}
	
	private int getMaxIndex(List<Double> data) {
		double mx = Double.MIN_VALUE;
		int index = -1;
		for (int i = 0; i < data.size(); i++) {
			double val = data.get(i);
			if (val > mx) {
				mx = val;
				index = i;
			}
		}
		return index;
	}
	
	private int getMinIndex(List<Double> data) {
		double mn = Double.MAX_VALUE;
		int index = -1;
		for (int i = 0; i < data.size(); i++) {
			double val = data.get(i);
			if (val < mn) {
				mn = val;
				index = i;
			}
		}
		return index;
	}
	
	private double getPixelDistance(Pixel p1, Pixel p2) {
		return Math.sqrt(getPixelDistanceSquare(p1, p2));
	}
	
	private double getPixelDistanceSquare(Pixel p1, Pixel p2) {
		return Math.pow(p1.r - p2.r, 2) + Math.pow(p1.g - p2.g, 2) + Math.pow(p1.b - p2.b, 2);
	}
}
