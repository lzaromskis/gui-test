package edu.ktu.screenshotanalyser.test.helpers;

import edu.ktu.screenshotanalyser.checks.ResultsCollector;
import edu.ktu.screenshotanalyser.context.State;

import java.io.File;
import java.nio.file.Paths;

public final class ResourceCreationHelper {
    private ResourceCreationHelper() {

    }

    public static State createState(String stateName, String imageName) {
        return new State(
            stateName,
            null,
            getTestImageFile(imageName),
            null,
            null
        );
    }

    private static File getTestImageFile(String filename) {
        return Paths
            .get("src", "test", "resources", "TestImages", filename)
            .toFile();
    }
}
