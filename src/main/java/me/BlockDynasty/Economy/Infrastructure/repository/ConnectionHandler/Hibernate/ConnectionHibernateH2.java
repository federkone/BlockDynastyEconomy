package me.BlockDynasty.Economy.Infrastructure.repository.ConnectionHandler.Hibernate;
import org.h2.server.web.WebServer;
import org.h2.tools.Server;

import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionHibernateH2 extends ConnectionHibernate {
    //start with h2 database
    /*public ConnectionHibernateH2(String dbFilePath) {
        super();
        configuration.setProperty("hibernate.connection.driver_class", "org.h2.Driver");
        configuration.setProperty("hibernate.connection.url", "jdbc:h2:file:" + dbFilePath + "/database");
        this.init();
    }*/

    //start with h2 database with web console H2
    public ConnectionHibernateH2(String dbFilePath,boolean enableServerConsole) {
        super();
        String url = "jdbc:h2:file:" + dbFilePath + "/database;AUTO_SERVER=TRUE;USER=sa;PASSWORD=Admin123";

        configuration.setProperty("hibernate.connection.driver_class", "org.h2.Driver");
        configuration.setProperty("hibernate.connection.url", url);
        this.init();

        if (enableServerConsole) {
           startServerConsole(dbFilePath);
        }
    }

    private void startServerConsole(String dbFilePath){
        try {
            System.setProperty("h2.consoleForcePassword", "true");
            Server webServer = Server.createWebServer("-web", "-webAllowOthers", "-webPort", "8082","-baseDir",dbFilePath);
            webServer.start();
        } catch (SQLException e) {
            System.err.println("Failed to start H2 console: " + e.getMessage());
        }
    }

    //start with sqlite, lo mas probable es que la web console tambien funcione con el dialecto sqlite
    /*public  ConnectionHibernateH2(String dbFilePath) {
        super();
        configuration.setProperty("hibernate.connection.driver_class", "org.sqlite.JDBC");
        configuration.setProperty("hibernate.connection.url", "jdbc:sqlite:" + dbFilePath+ "/database.db");
        configuration.setProperty("hibernate.dialect", "org.hibernate.community.dialect.SQLiteDialect");
        this.init();
    }*/
}
