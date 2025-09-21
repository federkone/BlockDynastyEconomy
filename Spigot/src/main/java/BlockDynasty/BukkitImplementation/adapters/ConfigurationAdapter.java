package BlockDynasty.BukkitImplementation.adapters;

import BlockDynasty.BukkitImplementation.BlockDynastyEconomy;
import BlockDynasty.BukkitImplementation.config.file.ConfigurationFile;
import Main.IConfiguration;
import files.Configuration;

public class ConfigurationAdapter implements IConfiguration {

    @Override
    public Configuration getConfig() {
        return ConfigurationFile.getConfiguration();
    }

    @Override
    public String getDbFilePath(){
        return BlockDynastyEconomy.getInstance().getDataFolder().getAbsolutePath()+"/database";
    }
}
