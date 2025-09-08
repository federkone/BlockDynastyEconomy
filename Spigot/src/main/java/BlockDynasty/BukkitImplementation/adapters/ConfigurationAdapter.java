package BlockDynasty.BukkitImplementation.adapters;

import BlockDynasty.BukkitImplementation.BlockDynastyEconomy;
import Main.IConfiguration;


//podemos traer la configuracion desde un archivo externo, cache refresh timer, database pool, etc
public class ConfigurationAdapter implements IConfiguration {
    @Override
    public String getHost() {
        return "localhost";
    }

    @Override
    public int getPort() {
        return 3306;
    }

    @Override
    public String getDatabase() {
        return "minecraft";
    }

    @Override
    public String getUsername() {
        return "root";
    }

    @Override
    public String getPassword() {
        return "password";
    }

    @Override
    public String getDbFilePath() {
        return BlockDynastyEconomy.getInstance().getDataFolder().getAbsolutePath()+"/database";
    }

    @Override
    public boolean enableServerConsole() {
        return true;
    }

    @Override
    public String getType() {
        return "h2";
    }
}
