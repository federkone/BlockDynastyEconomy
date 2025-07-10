package me.BlockDynasty.Economy.Infrastructure.repository.ConnectionHandler.Hibernate;

//todo, podemos configurar hibernate desde aqui, como cache, etc etc
public class ConnectionHibernateMysql extends ConnectionHibernate {

    public ConnectionHibernateMysql(String host, int port, String database, String username, String password) {
        super();
        configuration.setProperty("hibernate.connection.driver_class", "com.mysql.cj.jdbc.Driver");
        configuration.setProperty("hibernate.connection.url", "jdbc:mysql://" + host + ":" + port + "/" + database);
        configuration.setProperty("hibernate.connection.username", username);
        configuration.setProperty("hibernate.connection.password", password);

        this.init();
    }
}