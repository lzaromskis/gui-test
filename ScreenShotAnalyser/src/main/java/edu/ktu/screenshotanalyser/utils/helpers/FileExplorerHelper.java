package edu.ktu.screenshotanalyser.utils.helpers;

import edu.ktu.screenshotanalyser.exceptions.MissingSettingException;
import edu.ktu.screenshotanalyser.tools.Configuration;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.stream.Stream;

public final class FileExplorerHelper {
    private static final String AppExtension = ".apk";
    private static final String[] ImageExtensions = new String[]{".png", ".jpg"};
    private static final String StatesFolder = "states";
    private static final String StateImagePrefix = "screen_";
    private static final String ViewsFolder = "views";
    private static final String ViewImagePrefix = "view_";

    private FileExplorerHelper() {

    }

    public static File[] getAllStateImageFiles() throws IOException, MissingSettingException {
        var appImagesDirectory = getAppImagesFolder();

        return Arrays
            .stream(getChildDirectories(appImagesDirectory))
            .flatMap(x -> {
                try {
                    return Arrays
                        .stream(getChildDirectories(x))
                        .flatMap(y -> {
                            try {
                                return Arrays.stream(getAppStateImageFiles(y));
                            } catch (IOException e) {
                                return Stream.empty();
                            }
                        });
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            })
            .toArray(File[]::new);

        // return Arrays
        //     .stream(getChildDirectories(appImagesDirectory))
        //     .flatMap(x -> {
        //         try {
        //             return Arrays.stream(getAppStateImageFiles(x));
        //         } catch (IOException e) {
        //             throw new RuntimeException(e);
        //         }
        //     })
        //     .toArray(File[]::new);
    }

    public static File[] getAllViewImageFiles() throws IOException, MissingSettingException {
        var appImagesDirectory = getAppImagesFolder();

        return Arrays
            .stream(getChildDirectories(appImagesDirectory))
            .flatMap(x -> {
                try {
                    return Arrays
                        .stream(getChildDirectories(x))
                        .flatMap(y -> {
                            try {
                                return Arrays.stream(getAppViewImageFiles(y));
                            } catch (IOException e) {
                                return Stream.empty();
                            }
                        });
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            })
            .toArray(File[]::new);

        //return Arrays
        //    .stream(getChildDirectories(appImagesDirectory))
        //    .flatMap(x -> {
        //        try {
        //            return Arrays.stream(getAppViewImageFiles(x));
        //        } catch (IOException e) {
        //            throw new RuntimeException(e);
        //        }
        //    })
        //    .toArray(File[]::new);
    }

    public static File[] getAllAppImagesDirectories() throws IOException, MissingSettingException {
        var appImagesDirectory = getAppImagesFolder();

        return getChildDirectories(appImagesDirectory);
    }

    public static File[] getAllAppDirectories() throws IOException, MissingSettingException {
        var appDirectory = getAppsFolder();
        var appImagesDirectory = getAppImagesFolder();

        var appImages = Arrays
            .stream(getChildDirectories(appImagesDirectory))
            .flatMap(x -> {
                try {
                    return Arrays.stream(getChildDirectories(x));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            })
            .map(File::getName)
            .distinct()
            .toList();
        return Arrays
            .stream(getChildDirectories(appDirectory))
            .filter(x -> appImages.contains(x.getName()))
            .toArray(File[]::new);
    }

    public static File[] getAppStateImageFiles(File appDirectory) throws IOException {
        return getImageFiles(appDirectory, StatesFolder, StateImagePrefix);
    }

    public static File[] getAppViewImageFiles(File appDirectory) throws IOException {
        return getImageFiles(appDirectory, ViewsFolder, ViewImagePrefix);
    }

    public static File[] getChildDirectories(File directory) throws IOException {
        if (!directory.isDirectory()) {
            throw new IOException(String.format("%s must be a directory.", directory.getAbsolutePath()));
        }

        //noinspection ConstantConditions
        return Arrays
            .stream(directory.listFiles())
            .filter(File::isDirectory)
            .toArray(File[]::new);
    }

    private static File getAppImagesFolder() throws IOException, MissingSettingException {
        var pathString = Configuration
            .instance()
            .getAppImagesFolderPath();

        return Paths
            .get(pathString)
            .toFile();
    }

    private static File getAppsFolder() throws IOException, MissingSettingException {
        var pathString = Configuration
            .instance()
            .getAppsFolderPath();

        return Paths
            .get(pathString)
            .toFile();
    }

    private static File[] getImageFiles(File appDirectory, String imageFolder, String imagePrefix) throws IOException {
        if (!appDirectory.isDirectory()) {
            throw new IOException(String.format("%s must be a directory.", appDirectory.getAbsolutePath()));
        }

        var statesDirectory = Paths
            .get(appDirectory.getAbsolutePath(), imageFolder)
            .toFile();
        if (!statesDirectory.isDirectory()) {
            throw new IOException(String.format("%s must be a directory.", statesDirectory.getAbsolutePath()));
        }

        return statesDirectory.listFiles((f, name) -> name.startsWith(imagePrefix) && Arrays
            .stream(ImageExtensions)
            .anyMatch(name::endsWith));
    }
}
