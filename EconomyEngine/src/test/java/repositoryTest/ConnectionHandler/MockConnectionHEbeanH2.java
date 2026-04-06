package repositoryTest.ConnectionHandler;

import io.ebean.Database;
import io.ebean.DatabaseFactory;
import io.ebean.config.DatabaseConfig;
import io.ebean.datasource.DataSourceConfig;
import net.blockdynasty.economy.engine.repository.ebean.ConnectionHandler.Connection;
import net.blockdynasty.economy.engine.repository.ebean.Models.AccountDb;
import net.blockdynasty.economy.engine.repository.ebean.Models.BalanceDb;
import net.blockdynasty.economy.engine.repository.ebean.Models.CurrencyDb;
import net.blockdynasty.economy.engine.repository.ebean.Models.WalletDb;
import org.h2.Driver;

public class MockConnectionHEbeanH2 implements Connection {
    private Database database;
    protected DatabaseConfig config = new DatabaseConfig();
    protected DataSourceConfig dsConfig = new DataSourceConfig();

    public MockConnectionHEbeanH2() {
        config.setDdlGenerate(true);
        config.setDdlRun(true);
        //config.setDdlCreateOnly(false);

        config.addClass(CurrencyDb.class);
        config.addClass(AccountDb.class);
        config.addClass(BalanceDb.class);
        config.addClass(WalletDb.class);

        String url = "jdbc:h2:mem:testdb";
        dsConfig.setDriver(Driver.class.getName());
        dsConfig.setUrl(url);
        dsConfig.setUsername("");
        dsConfig.setPassword("");

        dsConfig.setMaxConnections(10);
        dsConfig.setWaitTimeoutMillis(30000);

        try {
            this.config.setDataSourceConfig(dsConfig);
            this.database = DatabaseFactory.create(config);
        } catch (Exception ex) {
            throw new RuntimeException("Error al iniciar el motor de Ebean: " + ex);
        }
    }

    @Override
    public Database getDatabase() {
        return this.database;
    }

    @Override
    public void close() {
        if (database != null) {
            database.shutdown(); // Cierra el pool y libera recursos
        }
    }
}
