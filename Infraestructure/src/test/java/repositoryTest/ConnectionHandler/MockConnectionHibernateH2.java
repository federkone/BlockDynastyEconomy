package repositoryTest.ConnectionHandler;

import BlockDynasty.repository.ConnectionHandler.Hibernate.Connection;
import BlockDynasty.repository.Models.Hibernate.*;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class MockConnectionHibernateH2 implements Connection {
    private SessionFactory sessionFactory;
    private Configuration configuration;

    public MockConnectionHibernateH2() {
        this.configuration = new Configuration();

        // Configure H2 in-memory database
        configuration.setProperty("hibernate.connection.driver_class", "org.h2.Driver");
        configuration.setProperty("hibernate.connection.url", "jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1");
        configuration.setProperty("hibernate.connection.username", "");
        configuration.setProperty("hibernate.connection.password", "");

        // Configure Hibernate properties
        configuration.setProperty("hibernate.hbm2ddl.auto", "create-drop");
        configuration.setProperty("hibernate.connection.autocommit", "true");
        configuration.setProperty("hibernate.cache.use_second_level_cache", "false");
        configuration.setProperty("hibernate.show_sql", "true");
        configuration.setProperty("hibernate.format_sql", "true");
        configuration.setProperty("hibernate.use_sql_comments", "true");
        configuration.addAnnotatedClass(CurrencyDb.class);
        configuration.addAnnotatedClass(AccountDb.class);
        configuration.addAnnotatedClass(BalanceDb.class);
        configuration.addAnnotatedClass(WalletDb.class);
        // Initialize the session factory
        try {
            this.sessionFactory = this.configuration.buildSessionFactory();
        } catch (Throwable ex) {
            throw new ExceptionInInitializerError("Initial SessionFactory creation failed: " + ex);
        }
    }

    @Override
    public SessionFactory getSession() {
        return this.sessionFactory;
    }

    @Override
    public void close() {
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }
}