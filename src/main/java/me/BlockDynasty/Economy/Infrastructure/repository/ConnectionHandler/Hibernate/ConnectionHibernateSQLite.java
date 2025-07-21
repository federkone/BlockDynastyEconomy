package me.BlockDynasty.Economy.Infrastructure.repository.ConnectionHandler.Hibernate;

public class ConnectionHibernateSQLite  extends   ConnectionHibernate{
    public ConnectionHibernateSQLite(String dbFilePath) {
        super();
        configuration.setProperty("hibernate.connection.driver_class", "org.sqlite.JDBC");
        configuration.setProperty("hibernate.connection.url", "jdbc:sqlite:" + dbFilePath+ "/database.db");
        configuration.setProperty("hibernate.dialect", "org.hibernate.dialect.SQLiteDialect");
        this.init();
    }
}
