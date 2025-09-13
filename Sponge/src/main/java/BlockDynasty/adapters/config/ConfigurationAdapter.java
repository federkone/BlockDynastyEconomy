package BlockDynasty.adapters.config;

import BlockDynasty.SpongePlugin;
import Main.IConfiguration;
import org.spongepowered.configurate.ConfigurationNode;

public class ConfigurationAdapter implements IConfiguration {
    private final ConfigurationNode rootNode;

    public ConfigurationAdapter() {
        /*try {
            // Automatically append config.yml to the directory path
            Path filePath = directoryPath.resolve("config.yml");

            if (Files.exists(filePath)) {
                YamlConfigurationLoader loader = YamlConfigurationLoader.builder()
                        .path(filePath)
                        .build();

                rootNode = loader.load();
            } else {
                Console.log("Configuration file not found at: " + filePath);
                throw new RuntimeException("Configuration file not found");
            }
        } catch (ConfigurateException e) {
            Console.log(e.getMessage());
            throw new RuntimeException("Failed to load configuration", e);
        }*/
        rootNode= ConfigurationFile.getConfig();
    }

    @Override
    public String getHost() {
        return rootNode.node("mysql", "host").getString("localhost");
    }

    @Override
    public int getPort() {
        return rootNode.node("mysql", "port").getInt(3306);
    }

    @Override
    public String getDatabase() {
        return rootNode.node("mysql", "database").getString("minecraft");
    }

    @Override
    public String getUsername() {
        return rootNode.node("mysql", "username").getString("minecraft");
    }

    @Override
    public String getPassword() {
        return rootNode.node("mysql", "password").getString("password");
    }

    @Override
    public String getDbFilePath() {
        return SpongePlugin.databasePath;
    }

    @Override
    public boolean enableServerConsole() {
        return  rootNode.node("EnableWebEditorSqlServer").getBoolean(true);
    }

    @Override
    public String getType() {
        return rootNode.node("storage").getString("h2");
    }
}
