package me.BlockDynasty.Economy.Infrastructure.repository;

/*import com.mongodb.client.MongoDatabase;
import me.BlockDynasty.Economy.domain.account.Account;
import me.BlockDynasty.Economy.domain.balance.Balance;
import me.BlockDynasty.Economy.domain.currency.Currency;
import me.BlockDynasty.Economy.domain.repository.ConnectionHandler.ConnectionMongoDb;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;

import me.BlockDynasty.Economy.domain.repository.Criteria.Criteria;
import me.BlockDynasty.Economy.domain.repository.Criteria.Translators.CriteriaMongoDbConverter;
import me.BlockDynasty.Economy.utils.ChatColorConverter;
import org.bson.Document;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;*/


//TODO: INTEGRAR TODO CON https://www.mongodb.com/resources/languages/morphia, MAPEAR OBJETOS A DOCUMENTOS CON UN ODM
public class RepositoryMongoDb {
    /*private final MongoDatabase database;
    private final MongoCollection<Document> collection;

    public RepositoryMongoDb(ConnectionMongoDb connection) {
        ;
        database = connection.getDatabase();
        collection = database.getCollection("blockdynastyEconomy");
    }

    @Override
    public void loadCurrencies() {

    }

    @Override
    public List<Currency> loadCurrencies(Criteria criteria) {
        return List.of();
    }

    @Override
    public void saveCurrency(Currency currency) {

    }

    @Override
    public void deleteCurrency(Currency currency) {
        Document doc = new Document("uuid", currency.getUuid().toString());
        collection.deleteOne(doc);
    }
    @Override
    public List<Account> loadAccounts(Criteria criteria) {
        return List.of();
    }

    @Override
    public void createAccount(Account account) {

    }

    @Override
    public void saveAccount(Account account) {

    }

    @Override
    public void transfer(Account userFrom, Account userTo) {

    }

    @Override
    public List<Account> getAccountsTopByCurrency(String currencyName, int limit, int offset) {
        return List.of();
    }

    @Override
    public boolean isTopSupported() {
        return false;
    }

    @Override
    public String getName() {
        return "";
    }

    @Override
    public void close() {

    }

    @Override
    public void clearAll() {

    }

    private void insertAccount(Account account) {
        List<Document> balances = account.getBalances().stream().map(balance -> {
            Document balanceDoc = new Document("currencyUuid", balance.getCurrency().getUuid().toString())
                    .append("amount", balance.getBalance());
            return balanceDoc;
        }).toList();

        Document doc = new Document("uuid", account.getUuid().toString())
                .append("nickName", account.getNickname())
                .append("canReceive", account.canReceiveCurrency())
                .append("balances", balances);

        collection.insertOne(doc);
    }

    private void insertCurrency(Currency currency) {
        Document doc = new Document("uuid", currency.getUuid().toString())
                .append("plural", currency.getPlural())
                .append("singular", currency.getSingular())
                .append("symbol", currency.getSymbol())
                .append("color", currency.getColor())
                .append("decimalSupported", currency.isDecimalSupported())
                .append("isDefault", currency.isDefaultCurrency())
                .append("startBalance", currency.getDefaultBalance())
                .append("isPayable", currency.isPayable())
                .append("exchangeRate", currency.getExchangeRate());
        collection.insertOne(doc);
    }

    private void deleteAccount(Account account) {
        Document doc = new Document("uuid", account.getUuid().toString());
        collection.deleteOne(doc);
    }


    private List<Account> matchingAccounts(Criteria criteria) {
        String queryMongo = new CriteriaMongoDbConverter().convert(criteria);  //obtengo consulta segun criteria

        Document queryDocument = Document.parse(queryMongo);
        Document query = queryDocument.get("query", Document.class);//extraer query y options
        Document options = queryDocument.get("options", Document.class);

        FindIterable<Document> result = collection.find(query)
                .sort(options.get("sort", Document.class))
                .limit(options.getInteger("limit", 0))
                .skip(options.getInteger("skip", 0));

        return getAccounts(result);
    }


    private List<Currency> matchingCurrencies(Criteria criteria) {
        String queryMongo = new CriteriaMongoDbConverter().convert(criteria);  //obtengo consulta segun criteria

        Document queryDocument = Document.parse(queryMongo);
        Document query = queryDocument.get("query", Document.class);//extraer query y options
        Document options = queryDocument.get("options", Document.class);

        FindIterable<Document> result = collection.find(query)
                .sort(options.get("sort", Document.class))
                .limit(options.getInteger("limit", 0))
                .skip(options.getInteger("skip", 0));

        return gerCurrency(result);
    }

    private List<Account> getAccounts(FindIterable<Document> result) {
        List<Account> accounts = new ArrayList<>();
        for (Document doc : result) {
            UUID uuid = UUID.fromString(doc.getString("uuid"));
            String name = doc.getString("nickName");
            boolean canReceive = doc.getBoolean("canReceive");

            accounts.add(new Account());
        }
        return accounts;
    }

    private List<Balance> getBalances(FindIterable<Document> result) {
        List<Balance> balances = new ArrayList<>();
        for (Document doc : result) {
            UUID currencyUuid = UUID.fromString(doc.getString("currencyUuid"));
            BigDecimal balance = BigDecimal.valueOf(doc.getDouble("balance"));

            balances.add(new Balance());
        }
        return balances;
    }

    private List<Currency> gerCurrency(FindIterable<Document> result) {
        List<Currency> currencies = new ArrayList<>();
        for (Document doc : result) {
            UUID uuid = UUID.fromString(doc.getString("uuid"));
            String plural = doc.getString("plural");
            String singular = doc.getString("singular");
            String symbol = doc.getString("symbol");
            String color = doc.getString("color");
            boolean decimalSupported = doc.getBoolean("decimalSupported");
            boolean isDefault = doc.getBoolean("isDefault");
            double startBalance = doc.getDouble("startBalance");
            boolean isPayable = doc.getBoolean("isPayable");
            double exchangeRate = doc.getDouble("exchangeRate");

            currencies.add(new Currency());
        }
        return currencies;
    }

    private Currency getCurrencyByUuid(UUID currencyUuid) {
        Document query = new Document("uuid", currencyUuid.toString());
        Document result = collection.find(query).first();
        if (result != null) {
            Currency currency = new Currency();
            currency.setUuid(UUID.fromString(result.getString("uuid")));
            currency.setPlural(result.getString("plural"));
            currency.setSingular(result.getString("singular"));
            currency.setSymbol(result.getString("symbol"));
            //currency.setColor(result.getString("color"));
            currency.setDecimalSupported(result.getBoolean("decimalSupported"));
            currency.setDefaultCurrency(result.getBoolean("isDefault"));
            currency.setStartBalance(BigDecimal.valueOf(result.getDouble("startBalance")));
            currency.setPayable(result.getBoolean("isPayable"));
            currency.setExchangeRate(result.getDouble("exchangeRate"));
            return currency;


        }
        return null; // Maneja este caso según sea necesario
    }*/
}
