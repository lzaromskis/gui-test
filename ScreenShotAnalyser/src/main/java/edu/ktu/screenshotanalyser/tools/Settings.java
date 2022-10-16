package edu.ktu.screenshotanalyser.tools;

import java.io.File;

public class Settings {
    public static final String appsFolder = "D:\\TestApps\\apps";
    public static final File appImagesFolder = new File("D:\\TestData\\small_data_test");
    public static final String debugFolder = "D:\\1\\debug";//"e:/1/";
}


/*

package edu.ktu.screenshotanalyser;

import java.io.File;

import com.sampullara.cli.Argument;

public class AnalyzerSettings {
	public String getTextsDir() {
		return textsDir;
	}

	public void setTextsDir(String textsDir) {
		this.textsDir = textsDir;
	}

	public String getImagesDir() {
		return imagesDir;
	}

	public void setImagesDir(String imagesDir) {
		this.imagesDir = imagesDir;
	}

	public String getResourcesDir() {
		return resourcesDir;
	}

	public void setResourcesDir(String resourcesDir) {
		this.resourcesDir = resourcesDir;
	}

	

	public String getOutputDir() {
		return outputDir;
	}

	public String getCvLibrary() {
		return new File(cvLibrary).getAbsolutePath();
	}
	
	public String getTessDataFolder() {
		return tessDataFolder;
	}

	public float getPrecision() {
		return precision;
	}

	public boolean isRunChecks() {
		return runChecks;
	}

	@Argument(alias = "i", description = "Droidbot images directory", required = false)
	private String imagesDir = ".\\samples\\images";
	
	@Argument(alias = "r", description = "Resources directory", required = false)
	private String resourcesDir = ".\\samples\\res";
	
	@Argument(alias = "o", description = "Output directory", required = false)
	private String outputDir =  ".\\out\\results";

	@Argument(alias = "t", description = "Texts directory", required = false)
	private String textsDir =  ".\\out\\texts";
	
	@Argument(alias = "cv", description = "Path to opencv dll file", required = false)
	private String cvLibrary = ".\\lib\\opencv_java340.dll";

	@Argument(alias = "td", description = "Path to tessdata", required = false )
	private String tessDataFolder = ".\\tessdata";


	@Argument(alias = "p", description = "Precision for words detection", required = false)
	private float precision = 0.60f;

	@Argument(alias = "lang", description = "Language", required = false)
	private String language = "nor";
	
	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}


	public void setOutputDir(String outputDir) {
		this.outputDir = outputDir;
	}

	public void setCvLibrary(String cvLibrary) {
		this.cvLibrary = cvLibrary;
	}

	public void setTessDataFolder(String tessDataFolder) {
		this.tessDataFolder = tessDataFolder;
	}

	public void setPrecision(float precision) {
		this.precision = precision;
	}

	public void setRunChecks(boolean runChecks) {
		this.runChecks = runChecks;
	}

	

	@Argument(alias = "c", description = "Flag if to run checks", required = false)
	private boolean runChecks = false;
	
}


*/
