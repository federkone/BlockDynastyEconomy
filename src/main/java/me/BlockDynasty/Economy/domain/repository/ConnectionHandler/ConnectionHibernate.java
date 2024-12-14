package me.BlockDynasty.Economy.domain.repository.ConnectionHandler;

import me.BlockDynasty.Economy.domain.balance.Balance;
import me.BlockDynasty.Economy.domain.currency.Currency;
import me.BlockDynasty.Economy.domain.account.Account;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
//todo, podemos configurar hibernate desde aqui, como cache, etc etc
public class ConnectionHibernate {
    private final SessionFactory sessionFactory;

    public ConnectionHibernate(String host, int port, String database, String username, String password) { //puedo recibir los parametros de conexion como con sql
        Configuration configuration = new Configuration();
        configuration.setProperty("hibernate.connection.driver_class", "com.mysql.cj.jdbc.Driver");
        configuration.setProperty("hibernate.connection.url", "jdbc:mysql://" + host + ":" + port + "/" + database);
        configuration.setProperty("hibernate.connection.username", username);
        configuration.setProperty("hibernate.connection.password", password);
        configuration.setProperty("hibernate.hbm2ddl.auto", "update");
        configuration.setProperty("hibernate.connection.autocommit", "true");
        configuration.setProperty("hibernate.cache.use_second_level_cache", "false");
        configuration.setProperty("hibernate.show_sql", "false"); //todo: setup for debug
        configuration.setProperty("hibernate.format_sql", "false");  //todo: setup for debug
        configuration.setProperty("hibernate.use_sql_comments", "false");//todo: setup for debug

        // Añadir mapeos manualmente
        configuration.addAnnotatedClass(Currency.class);
        configuration.addAnnotatedClass(Account.class);
        configuration.addAnnotatedClass(Balance.class);

        try {
            sessionFactory = configuration.buildSessionFactory();
            //sessionFactory = new Configuration().configure().buildSessionFactory(); //bildea la sesion con los parametros del archivo hibernate.cfg.xml
        } catch (Throwable ex) {
            throw new ExceptionInInitializerError("Initial SessionFactory creation failed: " + ex);
        }
    }

    public SessionFactory getSession() {
        return sessionFactory;
    }

    public void close() {
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }
}