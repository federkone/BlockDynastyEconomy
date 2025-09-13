package BlockDynasty.adapters.config;
import BlockDynasty.SpongePlugin;
import BlockDynasty.utils.Console;
import files.Configuration;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.loader.ConfigurationLoader;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public class ConfigurationFile {
    private static SpongePlugin plugin;
    private static ConfigurationNode config;
    private static ConfigurationLoader<?> configLoader;

    public static void init(SpongePlugin plugin) {
        ConfigurationFile.plugin = plugin;
        loadDefaultConfig();
        //MessageFile.init(plugin); // Similar message file initialization could be implemented
    }

    private static void loadDefaultConfig() {
        try {
            // Get config directory and ensure it exists
            Path configDir = SpongePlugin.configPath;
            File configFile = new File(configDir.toFile(), "config.yml");

            // Create loader
            configLoader = YamlConfigurationLoader.builder()
                    .path(configFile.toPath())
                    .build();

            // Load existing config or create new one if it doesn't exist
            if (!configFile.exists()) {
                config = configLoader.createNode();
            } else {
                config = configLoader.load();
            }

            // Set header and defaults
            Configuration configuration = new Configuration();
            config.node("header").set(configuration.getHeader());

            // Add all default values
            for (String key : configuration.getParameters().keySet()) {
                if (config.node(key.split("\\.")).virtual()) {
                    config.node(key.split("\\.")).set(configuration.getParameters().get(key));
                }
            }

            // Save the config
            configLoader.save(config);
        } catch (IOException e) {
            Console.log( "Failed to load or create config file: " + e.getMessage() );
        }
    }

    public static ConfigurationNode getConfig() {
        return config;
    }

    public static void reloadConfig() {
        try {
            config = configLoader.load();
        } catch (IOException e) {
            Console.log( "Failed to reload config file: " + e.getMessage() );
        }
    }
}
