package files;

import java.util.HashMap;
import java.util.Map;

public class Configuration {
    private String header;
    private final Map<String, Object> parameters = new HashMap<>();

    public Configuration(){
        header = "Economy Plugin"
                + "\n"
                + "Version: " + 1.0
                + "\nMain Configuration file."
                + "\n"
                + "Developer(s): " + "Nullplague"
                + "\n\n"
                + "You have three valid storage methods, mysql or sqlite/h2. If you choose mysql or mongodb you would have to enter the database credentials down below."
                + "\n"
                + "enableDistanceLimitOffer true,maxDistanceOffer is the maximum distance in blocks that a player can be to make/accept an offer/trade currencies. example: 5 blocks, 10 blocks, etc."
                + "\n"
                + "expireCacheTopMinutes is the time in minutes that the cache of the top balances will expire. example: 5 minutes, 10 minutes, etc."
                + "\n"
                + "EnableWebEditorSqlServer is to enable the web sql server for h2 database in http://localhost:8082, if you want to use the web console of h2 database, you have to set this to true. with this you can manage the h2 database from a web browser."
                + "\n"
                +"'online' Select your server's operating mode. Online is for official accounts where UUIDs are unique and are the source of truth, with support for name change detection from Mojang. With online=false, only the player's name will be taken as the source of truth in Case Sensitivity mode.";

        parameters.put("online", true);
        parameters.put("storage", "h2");
        parameters.put("EnableWebEditorSqlServer", false);
        parameters.put("debug", false);
        parameters.put("vault", true);
        parameters.put("transaction_log", true);
        parameters.put("transaction_log_vault",false);

        parameters.put("mysql.database", "minecraft");
        //config.addDefault("mysql.tableprefix", "BlockDynastyEconomy");
        parameters.put("mysql.host", "localhost");
        parameters.put("mysql.port", 3306);
        parameters.put("mysql.username", "root");
        parameters.put("mysql.password", "password");

        //config.addDefault("mongoUri", "mongodb://localhost:27017");
        //  config.addDefault("sqlite.file", "database.sqlite");

        parameters.put("enableDistanceLimitOffer", true);
        parameters.put("maxDistanceOffer", 5.0);
        parameters.put("expireCacheTopMinutes",5);
    }

    public String getHeader() {
        return header;
    }
    public Map<String, Object> getParameters() {
        return parameters;
    }

    /*        config.options().header(plugin.getDescription().getName()
                + "\n"
                + "Version: " + plugin.getDescription().getVersion()
                + "\nMain Configuration file."
                + "\n"
                + "Developer(s): " + plugin.getDescription().getAuthors()
                + "\n\n"
                + "You have three valid storage methods, mysql or sqlite/h2. If you choose mysql or mongodb you would have to enter the database credentials down below."
                + "\n"
                + "enableDistanceLimitOffer true,maxDistanceOffer is the maximum distance in blocks that a player can be to make/accept an offer/trade currencies. example: 5 blocks, 10 blocks, etc."
                + "\n"
                + "expireCacheTopMinutes is the time in minutes that the cache of the top balances will expire. example: 5 minutes, 10 minutes, etc."
                + "\n"
                + "EnableWebEditorSqlServer is to enable the web sql server for h2 database in http://localhost:8082, if you want to use the web console of h2 database, you have to set this to true. with this you can manage the h2 database from a web browser."
                + "\n"
                +"'online' Select your server's operating mode. Online is for official accounts where UUIDs are unique and are the source of truth, with support for name change detection from Mojang. With online=false, only the player's name will be taken as the source of truth in Case Sensitivity mode. "
                );


        config.addDefault("online", true);
        config.addDefault("storage", "h2");
        config.addDefault("EnableWebEditorSqlServer", false);
        config.addDefault("debug", false);
        config.addDefault("vault", true);
        config.addDefault("transaction_log", true);
        config.addDefault("transaction_log_vault",false);

        config.addDefault("mysql.database", "minecraft");
        //config.addDefault("mysql.tableprefix", "BlockDynastyEconomy");
        config.addDefault("mysql.host", "localhost");
        config.addDefault("mysql.port", 3306);
        config.addDefault("mysql.username", "root");
        config.addDefault("mysql.password", "password");

        //config.addDefault("mongoUri", "mongodb://localhost:27017");
      //  config.addDefault("sqlite.file", "database.sqlite");

        config.addDefault("enableDistanceLimitOffer", true);
        config.addDefault("maxDistanceOffer", 5.0);
        config.addDefault("expireCacheTopMinutes",5);
        //config.addDefault("cheque.material", Material.PAPER.toString());
        //config.addDefault("cheque.name", "&aBank Note");
        //config.addDefault("cheque.lore", Arrays.asList("&7Worth: {value}.", "&7&oWritten by {player}"));
        //config.addDefault("cheque.console_name", "Console");
        //config.addDefault("cheque.enabled", false);
*/
}
