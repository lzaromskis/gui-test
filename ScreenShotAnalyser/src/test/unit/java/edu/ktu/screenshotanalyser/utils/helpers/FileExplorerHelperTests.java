package edu.ktu.screenshotanalyser.utils.helpers;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public class FileExplorerHelperTests {
    @Test
    void getAppStateImageAbsolutePaths_returnsAbsolutePaths() throws IOException {
        // Arrange
        var appDirectory = getAppDirectory();

        // Act
        var result = FileExplorerHelper.getAppStateImageFiles(appDirectory);

        // Assert
        assertEquals(8, result.length);
        assertTrue(Arrays.stream(result).allMatch(x -> x.getAbsolutePath().startsWith(appDirectory.getAbsolutePath())));
        assertTrue(Arrays.stream(result).allMatch(x -> x.getAbsolutePath().contains("screen_2018-06-05")));
        assertTrue(Arrays.stream(result).allMatch(x -> x.getAbsolutePath().endsWith(".png")));
    }

    @Test
    void getAppViewImageAbsolutePaths_returnsAbsolutePaths() throws IOException {
        // Arrange
        var appDirectory = getAppDirectory();

        // Act
        var result = FileExplorerHelper.getAppViewImageFiles(appDirectory);

        // Assert
        assertEquals(25, result.length);
        assertTrue(Arrays.stream(result).allMatch(x -> x.getAbsolutePath().startsWith(appDirectory.getAbsolutePath())));
        assertTrue(Arrays.stream(result).allMatch(x -> x.getAbsolutePath().contains("view_")));
        assertTrue(Arrays.stream(result).allMatch(x -> x.getAbsolutePath().endsWith(".png")));
    }

    @Test
    void getChildDirectoryAbsolutePaths_returnsAbsolutePaths() throws IOException {
        // Arrange
        var appDirectory = getAppDirectory();

        // Act
        var result = FileExplorerHelper.getChildDirectories(appDirectory);

        // Assert
        assertEquals(5, result.length);
        assertTrue(Arrays.stream(result).allMatch(x -> x.getAbsolutePath().startsWith(appDirectory.getAbsolutePath())));
        assertTrue(result[0].getAbsolutePath().endsWith("events"));
        assertTrue(result[1].getAbsolutePath().endsWith("states"));
        assertTrue(result[2].getAbsolutePath().endsWith("stylesheets"));
        assertTrue(result[3].getAbsolutePath().endsWith("temp"));
        assertTrue(result[4].getAbsolutePath().endsWith("views"));
    }

    private File getAppDirectory() {
        return Paths
            .get("src", "test", "resources", "TestApp")
            .toFile();
    }
}
