package me.BlockDynasty.Economy.Infrastructure.repository.ConnectionHandler.Hibernate;

import me.BlockDynasty.Economy.domain.account.Account;
import me.BlockDynasty.Economy.domain.balance.Balance;
import me.BlockDynasty.Economy.domain.currency.Currency;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public abstract class ConnectionHibernate implements Connection {
        private SessionFactory sessionFactory;
        protected  Configuration configuration = new Configuration();

        public ConnectionHibernate() {
            configuration.setProperty("hibernate.hbm2ddl.auto", "update");
            configuration.setProperty("hibernate.connection.autocommit", "true");
            configuration.setProperty("hibernate.cache.use_second_level_cache", "false");
            configuration.setProperty("hibernate.show_sql", "false"); //todo: setup for debug
            configuration.setProperty("hibernate.format_sql", "false");  //todo: setup for debug
            configuration.setProperty("hibernate.use_sql_comments", "false");//todo: setup for debug
            configuration.addAnnotatedClass(Currency.class);
            configuration.addAnnotatedClass(Account.class);
            configuration.addAnnotatedClass(Balance.class);
        }

        protected   void init(){
            try {
                this.sessionFactory = this.configuration.buildSessionFactory();
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