package me.BlockDynasty.Economy.domain.repository.ConnectionHandler;
import dev.morphia.Datastore;
import dev.morphia.Morphia;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import me.BlockDynasty.Economy.domain.repository.Models.MongoDb.*;

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
