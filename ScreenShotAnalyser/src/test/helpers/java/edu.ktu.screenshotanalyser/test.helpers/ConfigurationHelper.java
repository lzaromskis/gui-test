package edu.ktu.screenshotanalyser.test.helpers;

import edu.ktu.screenshotanalyser.tools.Configuration;

import java.nio.file.Paths;

public final class ConfigurationHelper {
    private ConfigurationHelper() {

    }

    public static void SetupConfigurationFile(String filename) {
        Configuration.setFilename(getTestConfigurationPath(filename));
    }

    private static String getTestConfigurationPath(String filename) {
        return Paths
            .get("src", "test", "resources", "TestConfigurations", filename)
            .toAbsolutePath()
            .toString();
    }
}
