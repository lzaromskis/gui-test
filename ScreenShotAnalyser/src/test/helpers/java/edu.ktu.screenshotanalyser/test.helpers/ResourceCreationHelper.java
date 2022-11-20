package edu.ktu.screenshotanalyser.test.helpers;

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
            getTestFile(imageName),
            null,
            null
        );
    }

    public static State createState(String stateName, String imageName, String stateJsonFile) {
        return new State(
            stateName,
            null,
            getTestFile(imageName),
            getTestFile(stateJsonFile),
            null
        );
    }

    private static File getTestFile(String filename) {
        return Paths
            .get("src", "test", "resources", "TestImages", filename)
            .toFile();
    }
}
