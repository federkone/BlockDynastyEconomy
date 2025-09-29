package repository.ConnectionHandler.Hibernate;

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