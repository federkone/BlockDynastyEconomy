package BlockDynasty.adapters.config;

import BlockDynasty.SpongePlugin;
import Main.IConfiguration;
import files.Configuration;

public class ConfigurationAdapter implements IConfiguration {

    @Override
    public Configuration getConfig() {
        return ConfigurationFile.getConfiguration();
    }

    @Override
    public String getDbFilePath() {
        return SpongePlugin.databasePath;
    }


}
