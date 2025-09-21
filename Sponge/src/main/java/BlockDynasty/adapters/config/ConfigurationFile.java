package BlockDynasty.adapters.config;
import BlockDynasty.SpongePlugin;
import files.Configuration;

import java.io.File;
import java.nio.file.Path;

public class ConfigurationFile {
    private static SpongePlugin plugin;
    private static Configuration configuration;

    public static void init(SpongePlugin plugin) {
        ConfigurationFile.plugin = plugin;
        loadDefaultConfig();
        //MessageFile.init(plugin); // Similar message file initialization could be implemented
    }

    private static void loadDefaultConfig() {
        // Get config directory and ensure it exists
        Path configDir = SpongePlugin.configPath;
        File configFile = new File(configDir.toFile(), "config.yml");
        configuration= new Configuration(configFile);
    }

    public static Configuration getConfiguration() {
        return configuration;
    }
}
