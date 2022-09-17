package edu.ktu.screenshotanalyser.tools;

import edu.ktu.screenshotanalyser.exceptions.InvalidFileContentException;
import edu.ktu.screenshotanalyser.exceptions.MissingSettingException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Map;

public class Configuration {
    private static String _filename = "app.config";
    private final Map<String, String> _configValues;
    private static Configuration _instance;

    private Configuration() throws IOException {
        _configValues = new Hashtable<>();
        readConfiguration();
    }

    private void readConfiguration() throws IOException {
        var path = Paths.get(_filename).toAbsolutePath();
        if (Files.notExists(path)) {
            throw new FileNotFoundException(String.format("Could not open configuration file. Make sure that file '%s' exists.", path));
        }

        var lines = Files.readAllLines(path);
        for (var line: lines) {
            var trimmedLine = line.trim();
            if (trimmedLine.equals("") || trimmedLine.startsWith("#")) {
                continue;
            }

            var parts = line.split("=");
            if (parts.length != 2) {
                throw new InvalidFileContentException(String.format("Found an invalid setting '%s'. Make sure that if follows 'key=value' format.", trimmedLine));
            }

            var trimmedKey = parts[0].trim();
            if (_configValues.containsKey(trimmedKey)) {
                throw new InvalidFileContentException(String.format("Found a duplicate setting '%s' in configuration file.", trimmedKey));
            }

            var trimmedValue = parts[1].trim();

            _configValues.put(trimmedKey, trimmedValue);
        }
    }

    public static void unloadConfiguration() {
        _instance = null;
    }

    public static void setFilename(String filename) throws IllegalStateException {
        if (_instance != null)
            throw new IllegalStateException("Cannot set filename when configuration is already loaded.");
        _filename = filename;
    }

    public static Configuration instance() throws IOException {
        if (_instance == null) {
            _instance = new Configuration();
        }

        return _instance;
    }

    public String[] getRuleCodes() throws MissingSettingException {
        var rulesString = _configValues.get("ruleCodes");
        if (rulesString == null) {
            throw new MissingSettingException("Could not find value for 'ruleCodes'.");
        }

        var splitRules = rulesString.split(",");
        return Arrays.stream(splitRules)
                .map(String::trim)
                .distinct()
                .toArray(String[]::new);
    }

    public String getAppsFolderPath() throws MissingSettingException {
        return getString("appsFolderPath");
    }

    public String getAppImagesFolderPath() throws MissingSettingException {
        return getString("appImagesFolderPath");
    }

    public String getDebugFolderPath() throws MissingSettingException {
        return getString("debugFolderPath");
    }

    private String getString(String key) throws MissingSettingException {
        var value = _configValues.get(key);
        if (value == null) {
            throw new MissingSettingException(String.format("Could not find value for '%s'.", key));
        }

        return value;
    }
}
