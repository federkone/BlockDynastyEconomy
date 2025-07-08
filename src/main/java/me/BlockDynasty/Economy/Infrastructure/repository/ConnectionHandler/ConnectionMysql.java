package me.BlockDynasty.Economy.Infrastructure.repository.ConnectionHandler;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.SQLException;

public class ConnectionMysql {
    private final HikariDataSource hikari;

    public ConnectionMysql(String host, int port, String database, String username, String password) throws SQLException {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:mysql://" + host + ":" + port + "/" + database);
        config.setUsername(username);
        config.setPassword(password);
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        HikariDataSource ds = new HikariDataSource(config);
        this.hikari = ds;
    }

    public HikariDataSource getConnection() {
        return this.hikari;
    }

    public void close() {
            if (hikari != null) {
                hikari.close();
            }
    }
}
