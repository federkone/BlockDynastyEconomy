package BlockDynasty.repository.ConnectionHandler.MongoDb;
import dev.morphia.Datastore;
import dev.morphia.Morphia;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import BlockDynasty.repository.Models.MongoDb.AccountMongoDb;
import BlockDynasty.repository.Models.MongoDb.BalanceMongoDb;
import BlockDynasty.repository.Models.MongoDb.CurrencyMongoDb;

public class ConnectionMorphia {
    private static Datastore datastore;

    public ConnectionMorphia(String uri, String database) {
        // Conectar con MongoDB usando MongoClient
        MongoClient mongoClient = MongoClients.create(uri);

        datastore = Morphia.createDatastore(mongoClient, database);

        datastore.getMapper().map(CurrencyMongoDb.class);
        datastore.getMapper().map(AccountMongoDb.class);
        datastore.getMapper().map(BalanceMongoDb.class);

        // Crear los índices necesarios
        datastore.ensureIndexes();
    }

    public static Datastore getDatastore() {
        return datastore;
    }
}
