package BlockDynasty.BukkitImplementation.config.file;

import BlockDynasty.BukkitImplementation.BlockDynastyEconomy;
import files.Configuration;

import java.io.File;

public class ConfigurationFile {
    private static BlockDynastyEconomy plugin;
    private static Configuration configuration;

    public static void init(BlockDynastyEconomy plugin) {
        ConfigurationFile.plugin = plugin;
        loadDefaultConfig();
    }

    private static void loadDefaultConfig() {
        configuration = new Configuration(new File(plugin.getDataFolder(), "config.yaml"));
    }

    public static Configuration getConfiguration() {
        return configuration;
    }

}