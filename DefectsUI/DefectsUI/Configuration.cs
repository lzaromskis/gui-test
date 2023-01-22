namespace DefectsUI
{
    public class Configuration
    {
        private static readonly string _filename = "./app.config";
        private readonly IDictionary<string, string> _configValues;
        private static readonly Lazy<Configuration> _instance = new(() => new Configuration());

        private Configuration()
        {
            _configValues = new Dictionary<string, string>();
            ReadConfiguration();
        }

        private void ReadConfiguration()
        {
            if (!File.Exists(_filename))
            {
                throw new FileNotFoundException($"Could not open configuration file. Make sure that file '{_filename}' exists.");
            }

            var lines = File.ReadAllLines(_filename);
            foreach (var line in lines)
            {
                var trimmedLine = line.Trim();
                if (trimmedLine.Equals("") || trimmedLine.StartsWith("#"))
                {
                    continue;
                }

                var parts = line.Split("=");
                if (parts.Length != 2)
                {
                    throw new InvalidFileContentException($"Found an invalid setting '{trimmedLine}'. Make sure that if follows 'key=value' format.");
                }

                var trimmedKey = parts[0].Trim();
                if (_configValues.ContainsKey(trimmedKey))
                {
                    throw new InvalidFileContentException($"Found a duplicate setting '{trimmedKey}' in configuration file.");
                }

                var trimmedValue = parts[1].Trim();

                _configValues.Add(trimmedKey, trimmedValue);
            }
        }

        public static Configuration Instance
        {
            get => _instance.Value;
        }

        public string GetAppsFolderPath()
        {
            return Getstring("appsFolderPath");
        }

        public string GetAppImagesFolderPath()
        {
            return Getstring("appImagesFolderPath");
        }

        public string GetDebugFolderPath()
        {
            return Getstring("debugFolderPath");
        }

        public string GetDefectImagesFolderPath()
        {
            return Getstring("defectImagesFolderPath");
        }

        private string Getstring(string key)
        {
            if (_configValues.TryGetValue(key, out var value) && value is not null)
            {
                return value;
            }

            throw new MissingSettingException($"Could not find value for '{key}'.");
        }
    }
}
