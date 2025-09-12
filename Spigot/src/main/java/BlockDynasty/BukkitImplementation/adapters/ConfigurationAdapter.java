package BlockDynasty.BukkitImplementation.adapters;

import BlockDynasty.BukkitImplementation.BlockDynastyEconomy;
import Main.IConfiguration;

//podemos traer la configuracion desde un archivo externo, cache refresh timer, database pool, etc
public class ConfigurationAdapter implements IConfiguration {
    @Override
    public String getHost() {
        return BlockDynastyEconomy.getInstance().getConfig().getString("mysql.host");
    }

    @Override
    public int getPort() {
        return BlockDynastyEconomy.getInstance().getConfig().getInt("mysql.port");
    }

    @Override
    public String getDatabase() {
        return BlockDynastyEconomy.getInstance().getConfig().getString("mysql.database");
    }

    @Override
    public String getUsername() {
        return BlockDynastyEconomy.getInstance().getConfig().getString("mysql.username");
    }

    @Override
    public String getPassword() {
        return BlockDynastyEconomy.getInstance().getConfig().getString("mysql.password");
    }

    @Override
    public String getDbFilePath() {
        return BlockDynastyEconomy.getInstance().getDataFolder().getAbsolutePath()+"/database";
    }

    @Override
    public boolean enableServerConsole() {
        return BlockDynastyEconomy.getInstance().getConfig().getBoolean("EnableWebEditorSqlServer");
    }

    @Override
    public String getType() {
        return BlockDynastyEconomy.getInstance().getConfig().getString("storage");
    }
}
