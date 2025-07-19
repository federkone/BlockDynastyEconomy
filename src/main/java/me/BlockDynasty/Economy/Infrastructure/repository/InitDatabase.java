package me.BlockDynasty.Economy.Infrastructure.repository;

import me.BlockDynasty.Economy.Infrastructure.BukkitImplementation.utils.UtilServer;
import me.BlockDynasty.Economy.Infrastructure.repository.ConnectionHandler.Hibernate.ConnectionHibernateH2;
import me.BlockDynasty.Economy.domain.persistence.entities.IRepository;
import me.BlockDynasty.Economy.domain.result.Result;
import me.BlockDynasty.Economy.Infrastructure.repository.ConnectionHandler.Hibernate.ConnectionHibernateMysql;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import me.BlockDynasty.Economy.Infrastructure.repositoryV2.RepositorySql;

public class InitDatabase {

    public static Result<IRepository> init(Plugin plugin) {
        FileConfiguration config = plugin.getConfig();
        String strategy = config.getString("storage");

        if (strategy == null || strategy.isEmpty()) {
            return Result.failure("§cNo storage method provided. Check your files, then try again.", null);
        }

        return switch (strategy.toLowerCase()) {
            case "h2" -> h2(plugin);
            case "mysql" -> mysqlRepository(config);
            case "yaml" -> yamlRepository(config);
            case "mongodb" -> mongoDBRepository(config);
            default -> Result.failure("§cNo valid storage method provided. Check your files, then try again.", null);
        };
    }

    private static Result<IRepository>  mysqlRepository(FileConfiguration config) {
        try {
            IRepository repository = new RepositorySql(new ConnectionHibernateMysql(config.getString("mysql.host"), config.getInt("mysql.port"), config.getString("mysql.database"), config.getString("mysql.username"), config.getString("mysql.password")));
            return Result.success(repository);
        }catch (Exception e) {
            UtilServer.consoleLog(e.getMessage());
            return Result.failure("§cCannot load initial data from DataStore. Check your files, then try again.",null);
        }
    }

    private static Result<IRepository> mongoDBRepository(FileConfiguration config) {
        return Result.failure("§cMongoDB is not supported yet. Check your files, then try again.", null);
    }

    private static Result<IRepository> yamlRepository(FileConfiguration config) {
        return Result.failure( "§cYAML storage is not supported yet. Check your files, then try again.", null);
    }

    private static Result<IRepository> h2(Plugin plugin) {
        try {
            UtilServer.consoleLog(plugin.getDataFolder().getAbsolutePath());
            IRepository repository = new RepositorySql(new ConnectionHibernateH2(plugin.getDataFolder().getAbsolutePath()));
            return Result.success(repository);
        } catch (Exception e) {
            UtilServer.consoleLog(e.getMessage());
            return Result.failure("§cCannot load initial data from DataStore. Check your files, then try again.",null);
        }
    }
}
