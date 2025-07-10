package me.BlockDynasty.Economy.Infrastructure.repository.ConnectionHandler.Hibernate;

public class ConnectionHibernateH2 extends ConnectionHibernate {
    public ConnectionHibernateH2(String dbFilePath) {
        super();
        configuration.setProperty("hibernate.connection.driver_class", "org.h2.Driver");
        configuration.setProperty("hibernate.connection.url", "jdbc:h2:file:" + dbFilePath + "/database");

        this.init();
    }
}
