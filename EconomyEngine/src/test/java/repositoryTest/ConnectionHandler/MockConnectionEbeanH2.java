package repositoryTest.ConnectionHandler;
/*
import com.blockdynasty.economy.repository.ebean.ConnectionHandler.ConnectionEbean;
import io.ebean.datasource.DataSourceConfig;
import org.h2.Driver;
import org.h2.tools.Server;

public class MockConnectionEbeanH2 extends ConnectionEbean {

    public MockConnectionEbeanH2() {
        super();
        String url = "jdbc:h2:mem:testdb";
        dsConfig.setDriver(Driver.class.getName());
        dsConfig.setUrl(url);
        dsConfig.setUsername("sa");
        dsConfig.setPassword("");

        dsConfig.setMaxConnections(10);
        dsConfig.setWaitTimeoutMillis(30000);

        this.init();
    }

    @Override
    protected void stopServer() {
        // No hay server web en tests
    }
}
*/