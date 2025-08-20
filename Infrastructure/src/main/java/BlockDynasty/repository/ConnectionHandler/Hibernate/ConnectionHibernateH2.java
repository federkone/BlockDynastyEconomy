package BlockDynasty.repository.ConnectionHandler.Hibernate;
import BlockDynasty.BukkitImplementation.utils.Console;
import org.h2.tools.Server;

import java.sql.SQLException;

public class ConnectionHibernateH2 extends ConnectionHibernate {

    public ConnectionHibernateH2(String dbFilePath,boolean enableServerConsole) {
        super();
        String url = "jdbc:h2:file:" + dbFilePath + "/h2Database;AUTO_SERVER=TRUE;USER=sa;PASSWORD=Admin123";
        configuration.setProperty("hibernate.connection.driver_class", "org.h2.Driver");
        configuration.setProperty("hibernate.connection.url", url);
        this.init();

        if (enableServerConsole) {
           startServerConsole(dbFilePath);
        }
    }

    // This method starts the H2 web console server.
    private void startServerConsole(String dbFilePath){
        try {
            System.setProperty("h2.consoleForcePassword", "true");
            Server webServer = Server.createWebServer(
                    "-web",
                    "-webAllowOthers",
                    "-webPort", "8082",
                    "-baseDir",dbFilePath);
            webServer.start();

            Console.log("H2 console started at: http://localhost:8082"
                    + "\n                               ->  JDBC URL: jdbc:h2:file:" + dbFilePath + "/h2Database"
                    + "\n                               ->  User: sa , Password: Admin123" );
        } catch (SQLException e) {
            Console.logError("Failed to start H2 console: " + e.getMessage());
        }


    }
}
