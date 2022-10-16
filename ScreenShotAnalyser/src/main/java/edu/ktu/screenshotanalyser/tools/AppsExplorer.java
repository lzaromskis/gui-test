package edu.ktu.screenshotanalyser.tools;

import edu.ktu.screenshotanalyser.exceptions.MissingSettingException;

import java.io.IOException;

public class AppsExplorer {
    private static AppsExplorer _instance;

    private int _imageCount = -1;

    private AppsExplorer() {

    }

    public static AppsExplorer instance() {
        if (_instance == null) {
            _instance = new AppsExplorer();
        }

        return _instance;
    }

    public int discoverImageCount() throws IOException, MissingSettingException {
        if (_imageCount != -1) {
            return _imageCount;
        }

        var appFolder = Configuration.instance().getAppImagesFolderPath();

        return _imageCount;
    }
}
