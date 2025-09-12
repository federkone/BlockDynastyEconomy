package repository.ConnectionHandler.Hibernate;

import Main.Console;
import org.h2.tools.Server;

import java.nio.charset.StandardCharsets;
import java.sql.SQLException;

public class ConnectionHibernateH2 extends ConnectionHibernate {

    public ConnectionHibernateH2(String dbFilePath,boolean enableServerConsole) {
        super();
        String url = "jdbc:h2:file:" + dbFilePath + "/h2Database"; //;AUTO_SERVER=TRUE;USER=sa;PASSWORD=Admin123
        configuration.setProperty("hibernate.connection.driver_class", "org.h2.Driver");
        configuration.setProperty("hibernate.connection.url", url);
        this.init();

        if (enableServerConsole) {
            startServerConsole(dbFilePath);
        }
    }

    // This method starts the H2 web console server.
    private void startServerConsole(String dbFilePath){
       /* try {
            System.setProperty("h2.consoleForcePassword", "false");
            Server webServer = Server.createWebServer(
                    "-web",
                    "-webAllowOthers",
                    "-webPort", "8082",
                    "-baseDir",dbFilePath);
            webServer.start();

            Console.log("H2 console started at: http://localhost:8082"
                    + "\n                               ->  JDBC URL: jdbc:h2:file:" + dbFilePath + "/h2Database"
                    );// + "\n                               ->  User: sa , Password: Admin123"
        } catch (SQLException e) {
            Console.logError("Failed to start H2 console: " + e.getMessage());
        }*/
        try {
            // Skip password requirement
            System.setProperty("h2.consoleForcePassword", "false");

            // Set default settings for the console
            System.setProperty("h2.bindAddress", "localhost");
            System.setProperty("h2.webAllowOthers", "true");
            System.setProperty("h2.webAdminPassword", "");

            // Create web server with customized settings
            Server webServer = Server.createWebServer(
                    "-web",
                    "-webAllowOthers",
                    "-webPort", "8082",
                    "-baseDir", dbFilePath);
            webServer.start();

            // Generate direct access URL with pre-filled connection details
            String jdbcUrl = "jdbc:h2:file:" + dbFilePath + "/h2Database";
            String consoleUrl = "http://localhost:8082/";
            String directAccessUrl = consoleUrl + "login.jsp?jsessionid=autocomplete&url=" +
                    java.net.URLEncoder.encode(jdbcUrl, StandardCharsets.UTF_8) +
                    "&user=sa&password=";

            Console.log("  -> SQL WEB console started at: " + consoleUrl);
            Console.log("  -> Direct access URL: " + directAccessUrl);
            Console.log("  -> JDBC URL: " + jdbcUrl);
        } catch (Exception e) {
            Console.logError("Failed to start H2 console: " + e.getMessage());
        }
    }
}