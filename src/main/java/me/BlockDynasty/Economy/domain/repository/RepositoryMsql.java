package me.BlockDynasty.Economy.domain.repository;

import com.zaxxer.hikari.HikariDataSource;
import me.BlockDynasty.Economy.domain.account.Account;
import me.BlockDynasty.Economy.domain.currency.CachedTopList;
import me.BlockDynasty.Economy.domain.currency.CachedTopListEntry;
import me.BlockDynasty.Economy.domain.currency.Currency;
import me.BlockDynasty.Economy.domain.repository.Criteria.Criteria;
import me.BlockDynasty.Economy.domain.repository.Criteria.Filter;
import me.BlockDynasty.Economy.domain.repository.Criteria.Translators.CriteriaMysqlConverter;
import me.BlockDynasty.Economy.domain.repository.ConnectionHandler.ConnectionMysql;
import me.BlockDynasty.Economy.utils.SchedulerUtils;
import me.BlockDynasty.Economy.utils.UtilServer;
import org.bukkit.ChatColor;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class RepositoryMsql  {
    /*private HikariDataSource hikari;
    private boolean isTopSupported = true;

    private String currencyTable = "currencies";
    private String accountsTable = "accounts";

    private final LinkedHashMap<UUID, CachedTopList> topList = new LinkedHashMap<>();

    public RepositoryMsql(ConnectionMysql connectionMysql) throws SQLException {
        this.hikari = connectionMysql.getConnection();
        this.initialize();
    }

    public void initialize() {
        try (Connection connection = hikari.getConnection()) {
            setupTables(connection);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private void setupTables(Connection connection) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement("CREATE TABLE IF NOT EXISTS " + this.currencyTable + " (uuid VARCHAR(255) NOT NULL PRIMARY KEY, name_singular VARCHAR(255), name_plural VARCHAR(255),    default_balance DECIMAL,    symbol VARCHAR(10),    decimals_supported INT,    is_default INT,    payable INT,    color VARCHAR(255),    exchange_rate DECIMAL);")) {
            ps.execute();
        }
        try (PreparedStatement ps = connection.prepareStatement("CREATE TABLE IF NOT EXISTS " + this.accountsTable + " (nickname VARCHAR(255), uuid VARCHAR(255) NOT NULL PRIMARY KEY, payable BOOLEAN, balance_data LONGTEXT NULL);")) {
            ps.execute();
        }
    }

    @Override
    public void loadCurrencies() {

    }

    @Override
    public List<Currency> loadCurrencies(Criteria criteria) {
        String sql = new CriteriaMysqlConverter(this.currencyTable).convert(criteria);
        List<Currency> currencies = new ArrayList<>();

        try (Connection connection = hikari.getConnection()) {
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet set = stmt.executeQuery();
            while (set.next()) {
                UUID uuid = UUID.fromString(set.getString("uuid"));
                String singular = set.getString("name_singular");
                String plural = set.getString("name_plural");
                BigDecimal defaultBalance = set.getBigDecimal("default_balance");
                String symbol = set.getString("symbol");
                boolean decimals = set.getInt("decimals_supported") == 1;
                boolean isDefault = set.getInt("is_default") == 1;
                boolean payable = set.getInt("payable") == 1;
                ChatColor color = ChatColor.valueOf(set.getString("color"));
                double exchangeRate = set.getDouble("exchange_rate");
                Currency currency = new Currency(uuid, singular, plural);
                currency.setDefaultBalance(defaultBalance);
                currency.setSymbol(symbol);
                currency.setDecimalSupported(decimals);
                currency.setDefaultCurrency(isDefault);
                currency.setPayable(payable);
                currency.setColor(color);
                currency.setExchangeRate(exchangeRate);

                currencies.add(currency);
                UtilServer.consoleLog("Loaded currency: " + currency.getSingular() + " (" + currency.getPlural() + ")");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return currencies;
    }


    @Override
    public void saveCurrency(Currency currency) {  //old saveCurrency , actualiza o inserta, hace las dos
        String sql ="INSERT INTO `"+currencyTable+"` (`uuid`, `name_singular`, `name_plural`, `default_balance`, `symbol`, `decimals_supported`, `is_default`, `payable`, `color`, `exchange_rate`) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE `uuid` = VALUES(`uuid`), `name_singular` = VALUES(`name_singular`), `name_plural` = VALUES(`name_plural`), `default_balance` = VALUES(`default_balance`), `symbol` = VALUES(`symbol`), `decimals_supported` = VALUES(`decimals_supported`), `is_default` = VALUES(`is_default`), `payable` = VALUES(`payable`), `color` = VALUES(`color`), `exchange_rate` = VALUES(`exchange_rate`)";
        try (Connection connection = hikari.getConnection()) {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, currency.getUuid().toString());
            stmt.setString(2, currency.getSingular());
            stmt.setString(3, currency.getPlural());
            stmt.setBigDecimal(4, currency.getDefaultBalance());
            stmt.setString(5, currency.getSymbol());
            stmt.setInt(6, currency.isDecimalSupported() ? 1 : 0);
            stmt.setInt(7, currency.isDefaultCurrency() ? 1 : 0);
            stmt.setInt(8, currency.isPayable() ? 1 : 0);
            stmt.setString(9, currency.getColor().name());
            stmt.setDouble(10, currency.getExchangeRate());

            stmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


    @Override
    public void deleteCurrency(Currency currency) {
        try (Connection connection = hikari.getConnection()) {
            PreparedStatement stmt = connection.prepareStatement("DELETE FROM " + this.currencyTable + " WHERE uuid = ?");
            stmt.setString(1, currency.getUuid().toString());
            stmt.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public List<Account> loadAccounts(Criteria criteria) { //sirve para buscar por uuid o nickname segun lo que traiga criteria con .filter(nickname, "nombre") o .filter(uuid, "uuid")
        String sql = new CriteriaMysqlConverter(this.accountsTable).convert(criteria);
        return getAccounts(sql,criteria);
    }

    private List<Account> getAccounts (String sql , Criteria criteria){
        List<Account> accounts = new ArrayList<>();

        try (Connection connection = hikari.getConnection()) {
            PreparedStatement stmt = connection.prepareStatement(sql);
            int index = 1;  // El índice de los parámetros es 1-based
            for (Filter filter : criteria.getFilters()) {
                stmt.setObject(index++, filter.getValue());
            }

            ResultSet set = stmt.executeQuery();
            while (set.next()) {
                Account account = new Account(UUID.fromString(set.getString("uuid")), set.getString("nickname"));
                account.setCanReceiveCurrency(set.getBoolean("payable"));
                String json = set.getString("balance_data");
                JSONParser parser = new JSONParser();
                JSONObject data = (JSONObject) parser.parse(json); // Solo un casteo

                Map<Currency, BigDecimal> currencies = new HashMap<>();
                // Iterar sobre las entradas del JSONObject
                for (Object entryObj : data.entrySet()) {
                    Map.Entry<String, Object> entry = (Map.Entry<String, Object>) entryObj; // Cast explícito

                    // Obtener la clave (UUID como String)
                    String uuid = entry.getKey();

                    // Obtener el valor (Balance como Number)
                    Number value = (Number) entry.getValue();

                    // Crear una instancia de Currency (usando el UUID)
                    Currency currency = new Currency(UUID.fromString(uuid), "null", "null");

                    currencies.put(currency, BigDecimal.valueOf(value.doubleValue()));
                }

                account.setBalances(currencies);
                //Currency currency = new Currency(UUID.fromString("00000000-0000-0000-0000-000000000000"), "null", "null");
                //for(Currency currency : plugin.getCurrencyManager().getCurrencies()){ //para cada currency en el sistema
                //    Number amount = (Number) data.get(currency.getUuid().toString()); //obtiene el valor de la currency en la cuenta
                //    if(amount != null) {
                //        account.getBalances().put(currency, amount.doubleValue());
                //    }else{
                //        account.getBalances().put(currency, currency.getDefaultBalance());
                //    }
                //}
                accounts.add(account);
                //ahora necesito por fuera actualizar los balances de las cuentas
                //estoy retornandu un usuario con monedas temporales, donde lo unico que me sirve es el uuid y el valor
                //por fuera deberia buscar esas monedas basadas en el uuid de las monedas temporales y actualizarlas alas reales que se encuentran en
                //CurrencyManager().getCurrencies()
            }
            set.close();
        } catch (SQLException | ParseException e) {
            e.printStackTrace();
        }

        return accounts;
    }


    public void loadAccounts(Criteria criteria, Callback<Account> callback) {
        SchedulerUtils.runAsync(() -> {
            Account account = this.loadAccounts(criteria).get(0);
            SchedulerUtils.run(() -> callback.call(account));
        });
    }

    @Override
    public void createAccount(Account account) { //old createAcount, hace insert account
        String SAVE_ACCOUNT = "INSERT INTO `" + this.accountsTable + "` (`nickname`, `uuid`, `payable`, `balance_data`) VALUES (?, ?, ?, ?)";
        try (Connection connection = hikari.getConnection()) {
            PreparedStatement stmt = connection.prepareStatement(SAVE_ACCOUNT);
            stmt.setString(1, account.getNickname());
            stmt.setString(2, account.getUuid().toString());
            stmt.setInt(3, account.canReceiveCurrency() ? 1 : 0);

            JSONObject obj = new JSONObject();
            for (Map.Entry<Currency, BigDecimal> entry : account.getBalances().entrySet()) {
                obj.put(entry.getKey().getUuid().toString(), entry.getValue());
            }
            String json = obj.toJSONString();
            stmt.setString(4, json);
            stmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void saveAccount(Account account) { //old saveAccount, hace update sobre una cuenta
        String UPDATE_ACCOUNT = "UPDATE `" + this.accountsTable + "` SET `nickname` = ?, `payable` = ?, `balance_data` = ? WHERE `uuid` = ?";
        try (Connection connection = hikari.getConnection()) {
            PreparedStatement stmt = connection.prepareStatement(UPDATE_ACCOUNT);
            stmt.setString(1, account.getNickname());
            stmt.setInt(2, account.canReceiveCurrency() ? 1 : 0);

            JSONObject obj = new JSONObject();
            for (Map.Entry<Currency, BigDecimal> entry : account.getBalances().entrySet()) {
                obj.put(entry.getKey().getUuid().toString(), entry.getValue());
            }
            String json = obj.toJSONString();
            stmt.setString(3, json);
            stmt.setString(4, account.getUuid().toString());
            stmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


    @Override
    public void transfer(Account userFrom, Account userTo) {

    }

    //@Override
    public void deleteAccount(Account account) {
        try (Connection connection = hikari.getConnection()) {
            PreparedStatement stmt = connection.prepareStatement("DELETE FROM " + this.accountsTable + " WHERE uuid = ? LIMIT 1");
            stmt.setString(1, account.getUuid().toString());
            stmt.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getTopList(Currency currency, int offset, int amount, Callback<LinkedList<CachedTopListEntry>> callback) {
        if (this.topList.containsKey(currency.getUuid())) {
            CachedTopList cache = this.topList.get(currency.getUuid());
            if (!cache.isExpired()) {
                LinkedList<CachedTopListEntry> searchResults = new LinkedList<>();
                int collected = 0;
                for(int i = offset; i < cache.getResults().size(); i++){
                    if(collected == amount)break;
                    searchResults.add(cache.getResults().get(i));
                    collected++;
                }
                SchedulerUtils.run(() -> callback.call(searchResults));
                return;
            }
        }
        SchedulerUtils.runAsync(() -> {
            LinkedList<CachedTopListEntry> topListEntries = new LinkedList<>();
            String query = "SELECT nickname, JSON_UNQUOTE(JSON_EXTRACT(balance_data, '$." + currency.getUuid().toString() + "')) AS balance " +
                    "FROM " + this.accountsTable + " " +
                    "WHERE JSON_EXTRACT(balance_data, '$." + currency.getUuid().toString() + "') IS NOT NULL " +
                    "ORDER BY balance DESC " +
                    "LIMIT ? OFFSET ?";
            try (Connection connection = hikari.getConnection();
                 PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setInt(1, amount);
                stmt.setInt(2, offset);
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    String nickname = rs.getString("nickname");
                    BigDecimal balance = rs.getBigDecimal("balance");
                    topListEntries.add(new CachedTopListEntry(nickname, balance));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            SchedulerUtils.run(() -> callback.call(topListEntries));
        });
    }

    @Override
    public List<Account> getAccountsByCurrency(String currencyName, int limit, int offset) {
        return null;
    }

    public boolean isTopSupported() {
        return isTopSupported;
    }

    public String getName(){
        return "MySql";
    }
    public void close() {
        if (hikari != null) {
            hikari.close();
        }
    }
    
     */
}