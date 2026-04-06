package net.blockdynasty.economy.engine.repository.ebean;

import net.blockdynasty.economy.engine.platform.files.IConfigurationEngine;
import net.blockdynasty.economy.engine.repository.ebean.ConnectionHandler.*;

import java.util.Map;
import java.util.function.Function;

public class ConnectionFactory{
    private Connection connection;

    private final Map<DbConfig.TYPE, Function<DbConfig, Connection>> connectionStrategies= Map.ofEntries(
            Map.entry(DbConfig.TYPE.MYSQL, ConnectionEbeanMysql::new),
            Map.entry(DbConfig.TYPE.MARIADB, ConnectionEbeanMariaDb::new),
            Map.entry(DbConfig.TYPE.H2, ConnectionEbeanH2::new),
            Map.entry(DbConfig.TYPE.SQLITE, ConnectionEbeanSQLite::new)
    );

    public ConnectionFactory(IConfigurationEngine fileConfig){
        DbConfig dbConfig = new DbConfig(fileConfig);
        this.connection = connectionStrategies.get(dbConfig.getType()).apply(dbConfig);
    }

    public Connection get(){
        return this.connection;
    }
}
