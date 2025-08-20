package BlockDynasty.repository;

import BlockDynasty.BukkitImplementation.utils.Console;
import BlockDynasty.repository.ConnectionHandler.Hibernate.ConnectionHibernateH2;
import BlockDynasty.repository.ConnectionHandler.Hibernate.ConnectionHibernateSQLite;
import BlockDynasty.Economy.domain.persistence.entities.IRepository;
import BlockDynasty.Economy.domain.result.Result;
import BlockDynasty.repository.ConnectionHandler.Hibernate.ConnectionHibernateMysql;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

public class InitDatabase {

    public static Result<IRepository> init(Plugin plugin) {
        FileConfiguration config = plugin.getConfig();
        String strategy = config.getString("storage");
        boolean enableServerConsole = config.getBoolean("EnableWebEditorSqlServer");

        if (strategy == null || strategy.isEmpty()) {
            return Result.failure("§cNo storage method provided. Check your files, then try again.", null);
        }

         switch (strategy.toLowerCase()) {
            case "sqlite":
                return sqlite(plugin);
            case "h2":
                return h2(plugin,enableServerConsole);
            case "mysql":
                return mysqlRepository(config);
            case "yaml":
                return yamlRepository(config);
            case "mongodb":
                return mongoDBRepository(config);
            default:
                return Result.failure("§cNo valid storage method provided. Check your files, then try again.", null);
        }
    }

    private static Result<IRepository>  mysqlRepository(FileConfiguration config) {
        try {
            IRepository repository = new RepositorySql(new ConnectionHibernateMysql(config.getString("mysql.host"), config.getInt("mysql.port"), config.getString("mysql.database"), config.getString("mysql.username"), config.getString("mysql.password")));
            return Result.success(repository);
        }catch (Exception e) {
            Console.logError(e.getMessage());
            return Result.failure("§cCannot load initial data from DataStore. Check your files, then try again.",null);
        }
    }

    private static Result<IRepository> mongoDBRepository(FileConfiguration config) {
        return Result.failure("§cMongoDB is not supported yet. Check your files, then try again.", null);
    }

    private static Result<IRepository> yamlRepository(FileConfiguration config) {
        return Result.failure( "§cYAML storage is not supported yet. Check your files, then try again.", null);
    }

    private static Result<IRepository> h2(Plugin plugin,boolean enableServerConsole) {
        try {
            IRepository repository = new RepositorySql(new ConnectionHibernateH2(plugin.getDataFolder().getAbsolutePath()+"/database",enableServerConsole));
            return Result.success(repository);
        } catch (Exception e) {
            Console.logError(e.getMessage());
            return Result.failure("§cCannot load initial data from DataStore. Check your files, then try again.",null);
        }
    }

    private static Result<IRepository> sqlite(Plugin plugin) {
        try {
            IRepository repository = new RepositorySql(new ConnectionHibernateSQLite(plugin.getDataFolder().getAbsolutePath()));
            return Result.success(repository);
        } catch (Exception e) {
            Console.logError(e.getMessage());
            return Result.failure("§cCannot load initial data from DataStore. Check your files, then try again.",null);
        }
    }
}
