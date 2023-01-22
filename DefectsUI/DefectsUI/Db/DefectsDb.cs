using System.Data;
using System.Data.SqlClient;

namespace DefectsUI.Db
{
    public class DefectsDb
    {
        private readonly SqlConnection _connection;

        public DefectsDb(string? connectionString = null)
        {
            _connection = new(connectionString ?? "server=localhost;database=defects-db;user=gui;password=gui;encrypt=true;trustServerCertificate=true;");
        }

        public void Query(DataTable dt)
        {
            var imagePathKey = "AppImagePath";
            var imageKey = "App Image";
            
            var discoveredOnUnixKey = "DiscoverdOnUnix";
            var discoveredOnKey = "Discovered On";

            var defectImagePathKey = "DefectImagePath";
            var defectImageKey = "Defect Image";

            try
            {
                _connection.Open();
                var command = new SqlCommand($"""
                    SELECT TOP 100 
                        TestRunDefect.DiscoveredOn AS {discoveredOnUnixKey},
                        TestRunDefect.DefectImagePath AS {defectImagePathKey},
                        DefectType.Description AS 'Defect Name',
                        ScreenShot.FileName AS {imagePathKey},
                        Application.Name AS 'Application Name',
                        Application.Package AS 'Application Package',
                        Application.Version AS 'Application Version',
                        TestDevice.Name AS 'Device Name',
                        TestDevice.Resolution AS 'Device Screen Resolution',
                        TestDevice.Size AS 'Device Screen Size',
                        TestDevice.Language AS 'Device Language'
                    FROM TestRunDefect
                    INNER JOIN DefectType ON TestRunDefect.DefectTypeId = DefectType.Id
                    INNER JOIN ScreenShot ON TestRunDefect.ScreenShotId = ScreenShot.Id
                    INNER JOIN Application ON ScreenShot.ApplicationId = Application.Id
                    INNER JOIN TestDevice ON ScreenShot.TestDeviceId = TestDevice.Id
                    WHERE TestRunDefect.DiscoveredOn IS NOT NULL AND TestRunDefect.DefectTypeId != 34
                    ORDER BY TestRunDefect.DiscoveredOn DESC
                    """, _connection);
                var adapter = new SqlDataAdapter(command);
                var dataTableCount = adapter.Fill(dt);

                try
                {
                    dt.Columns.Add(imageKey, typeof(Bitmap));
                    dt.Columns.Add(defectImageKey, typeof(Bitmap));
                    dt.Columns.Add(discoveredOnKey, typeof(DateTime));

                    foreach (DataRow row in dt.Rows)
                    {
                        // var imagePath = $"D:\\TestData\\data\\{row[imagePathKey]}";
                        var imagePath = $"{Configuration.Instance.GetAppImagesFolderPath()}\\{row[imagePathKey]}";
                        using var image = new Bitmap(imagePath);
                        var resized = new Bitmap(image, ResizeKeepAspect(image.Size, 300, 600, true));
                        row[imageKey] = resized;

                        var defectImagePath = (string)row[defectImagePathKey];
                        using var defectImage = new Bitmap(defectImagePath);
                        var resizedDefectImage = new Bitmap(defectImage, ResizeKeepAspect(defectImage.Size, 300, 600, true));
                        row[defectImageKey] = resizedDefectImage;

                        var unixTime = (long)row[discoveredOnUnixKey];
                        var time = DateTimeOffset.FromUnixTimeSeconds(unixTime);
                        row[discoveredOnKey] = time.LocalDateTime;
                    }
                }
                catch (Exception ex)
                {
                    MessageBox.Show($"Failed to add images: {ex.Message}", "Failed to add images", MessageBoxButtons.OK, MessageBoxIcon.Error);
                }

                dt.Columns.Remove(imagePathKey);
                dt.Columns.Remove(defectImagePathKey);
                dt.Columns.Remove(discoveredOnUnixKey);
                dt.Columns[discoveredOnKey]!.SetOrdinal(0);
            }
            finally
            {
                _connection.Close();
            }
        }

        public static Size ResizeKeepAspect(Size src, int maxWidth, int maxHeight, bool enlarge = false)
        {
            maxWidth = enlarge ? maxWidth : Math.Min(maxWidth, src.Width);
            maxHeight = enlarge ? maxHeight : Math.Min(maxHeight, src.Height);

            decimal rnd = Math.Min(maxWidth / (decimal)src.Width, maxHeight / (decimal)src.Height);
            return new Size((int)Math.Round(src.Width * rnd), (int)Math.Round(src.Height * rnd));
        }
    }
}
