package me.BlockDynasty.Economy.domain.repository;

import me.BlockDynasty.Economy.domain.result.Result;
import me.BlockDynasty.Economy.domain.repository.ConnectionHandler.ConnectionHibernate;
import org.bukkit.configuration.file.FileConfiguration;

public class InitDatabase {

    public static Result<IRepository> init(FileConfiguration config) {
        String strategy = config.getString("storage");

        if (strategy == null || strategy.isEmpty()) {
            return Result.failure("§cNo storage method provided. Check your files, then try again.", null);
        }

        return switch (strategy.toLowerCase()) {
            case "sqlite" -> sqLite(config);
            case "mysql" -> mysqlRepository(config);
            case "yaml" -> yamlRepository(config);
            case "mongodb" -> mongoDBRepository(config);
            default -> Result.failure("§cNo valid storage method provided. Check your files, then try again.", null);
        };
    }

    private static Result<IRepository>  mysqlRepository(FileConfiguration config) {
        try {
            IRepository repository = new RepositoryCriteriaApiV1(new ConnectionHibernate(config.getString("mysql.host"), config.getInt("mysql.port"), config.getString("mysql.database"), config.getString("mysql.username"), config.getString("mysql.password")));
            return Result.success(repository);
        }catch (Exception e) {
            e.printStackTrace();
            return Result.failure("§cCannot load initial data from DataStore. Check your files, then try again.",null);
        }
    }

    private static Result<IRepository> mongoDBRepository(FileConfiguration config) {
        return Result.failure("§cMongoDB is not supported yet. Check your files, then try again.", null);
    }

    private static Result<IRepository> yamlRepository(FileConfiguration config) {
        return Result.failure( "§cYAML storage is not supported yet. Check your files, then try again.", null);
    }

    private static Result<IRepository> sqLite(FileConfiguration config) {
        return Result.failure( "§cSQLITE storage is not supported yet. Check your files, then try again.", null);
    }
}
