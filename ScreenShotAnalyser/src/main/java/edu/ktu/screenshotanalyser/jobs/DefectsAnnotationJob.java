package edu.ktu.screenshotanalyser.jobs;

import edu.ktu.screenshotanalyser.checks.IStateRuleChecker;
import edu.ktu.screenshotanalyser.checks.ResultImage;
import edu.ktu.screenshotanalyser.checks.ResultsCollector;
import edu.ktu.screenshotanalyser.checks.experiments.ClippedTextCheck;
import edu.ktu.screenshotanalyser.checks.experiments.MixedLanguagesStateCheck;
import edu.ktu.screenshotanalyser.checks.experiments.UnlocalizedIconsCheck;
import edu.ktu.screenshotanalyser.context.DefaultContextProvider;
import edu.ktu.screenshotanalyser.context.State;
import edu.ktu.screenshotanalyser.exceptions.MissingSettingException;
import edu.ktu.screenshotanalyser.tools.Configuration;
import edu.ktu.screenshotanalyser.tools.Settings;
import edu.ktu.screenshotanalyser.utils.models.Tuple;
import org.opencv.core.Core;

import java.io.File;
import java.io.IOException;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DefectsAnnotationJob implements Runnable {
    public static void main(String[] args) throws IOException {
        new DefectsAnnotationJob().run();
    }

    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    private DefectsAnnotationJob() throws IOException {
        String imagesFolderPath;
        try {
            imagesFolderPath = Configuration
                .instance().getAppImagesFolderPath();
        } catch (MissingSettingException | IOException e) {
            imagesFolderPath = Settings.appImagesFolder.getAbsolutePath();
        }
        this.contextProvider = new DefaultContextProvider(new File(imagesFolderPath));
    }

    public void run() {
        while (this.running) {
            try {
                var task = getAnnotationTask();

                if (null != task) {
                    task.first.analyze(task.second, new ImagesLogger(task.third));
                } else {
                    System.out.println("idle...");
                    Thread.sleep(1000);
                }
            } catch (InterruptedException | IOException ex) {
                ex.printStackTrace();
            } catch (MissingSettingException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private Tuple<IStateRuleChecker, State, Long> getAnnotationTask() throws IOException {
        try (var connection = DriverManager.getConnection(this.connectionUrl)) {
            try (var statement = connection.prepareStatement("" + "" + "" + "SELECT TOP 1 TestRunDefect.DefectTypeId, [Application].ApkFile, ScreenShot.FileName, TestRunDefectImage.Id " + "\r\n" + "\r\n" + "\r\n" + "FROM TestRunDefectImage \r\n" + "LEFT JOIN TestRunDefect ON TestRunDefectImage.TestRunDefectId = TestRunDefect.Id\r\n" + "LEFT JOIN ScreenShot ON ScreenShot.Id = TestRunDefect.ScreenShotId\r\n" + "LEFT JOIN TestDevice ON TestDevice.Id = ScreenShot.TestDeviceId\r\n" + "LEFT JOIN [Application] ON [Application].Id = ScreenShot.ApplicationId\r\n" + "\r\n" + "\r\n" + "WHERE TestRunDefectImage.ImageData IS NULL\r\n" + "" + "" + "")) {
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        var apkFile = resultSet.getString("ApkFile");
                        apkFile = apkFile.substring(0, apkFile.length() - 4);
                        var imageFile = resultSet.getString("FileName");

                        String appsFolderPath;
                        try {
                            appsFolderPath = Configuration
                                .instance().getAppsFolderPath();
                        } catch (MissingSettingException | IOException e) {
                            appsFolderPath = Settings.appsFolder;
                        }

                        String imagesFolderPath;
                        try {
                            imagesFolderPath = Configuration
                                .instance().getAppImagesFolderPath();
                        } catch (MissingSettingException | IOException e) {
                            imagesFolderPath = Settings.appImagesFolder.getAbsolutePath();
                        }

                        var ff = new File(appsFolderPath + apkFile);

                        if (!ff.exists()) {
                            ff = new File(appsFolderPath + apkFile.substring(0, apkFile.length() - 3));
                        }



                        var context = this.contextProvider.getContext(ff);
                        String finalImagesFolderPath = imagesFolderPath;
                        var state = context
                            .getStates()
                            .stream()
                            .filter(p -> p
                                .getImageFile()
                                .getAbsolutePath()
                                .substring(finalImagesFolderPath.length() + 1)
                                .equals(imageFile))
                            .findFirst()
                            .get();

                        var check = switch (resultSet.getInt("DefectTypeId")) {
                            //							case 11:
                            //							yield new ClippedControlCheck();
                            case 5:
                                yield new ClippedTextCheck();
                            case 7:
                                yield new MixedLanguagesStateCheck();
                            case 33:
                                yield new UnlocalizedIconsCheck();
                            default:
                                yield null;
                        };

                        return new Tuple<>(check, state, resultSet.getLong("Id"));
                    }
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return null;
    }

    class ImagesLogger extends ResultsCollector {
        public ImagesLogger(long testRunDefectImageId) {
            super(true);

            this.testRunDefectImageId = testRunDefectImageId;
        }

        @Override
        public void addFailureImage(ResultImage image) {
            try (var connection = DriverManager.getConnection(connectionUrl)) {
                try (PreparedStatement statement = connection.prepareStatement("UPDATE TestRunDefectImage SET ImageData = ? WHERE Id = ?")) {
                    statement.setObject(1, image.encodeToPng());
                    statement.setObject(2, this.testRunDefectImageId);

                    statement.executeUpdate();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        @Override
        public boolean wasChecked(State state) {
            return false;
        }

        @Override
        public void finishRun() {
        }

        private final long testRunDefectImageId;
    }

    private final DefaultContextProvider contextProvider;
    private boolean running = true;
    protected String connectionUrl = "jdbc:sqlserver://localhost;database=defects-db;integratedSecurity=true;";
}
