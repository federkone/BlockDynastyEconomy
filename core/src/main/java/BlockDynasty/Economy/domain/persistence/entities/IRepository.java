package BlockDynasty.Economy.domain.persistence.entities;

import BlockDynasty.Economy.domain.persistence.transaction.ITransactions;
import BlockDynasty.Economy.domain.entities.account.Account;
import BlockDynasty.Economy.domain.entities.currency.Currency;
import BlockDynasty.Economy.domain.result.Result;

import java.util.List;

//todo: evaluar hacer solo operaciones CRUD
public interface IRepository extends ITransactions {

    List<Currency> loadCurrencies();
    Result<Currency> loadCurrencyByName(String name);
    Result<Currency> loadCurrencyByUuid(String uuid);
    Result<Currency> loadDefaultCurrency(); //carga la moneda por defecto, si no hay devuelve una lista vacia
    void saveCurrency(Currency currency); //hace de update tambien
    void deleteCurrency(Currency currency);

    List<Account> loadAccounts(); //una cuenta de un criterio
    Result<Account> loadAccountByUuid(String uuid);
    Result<Account> loadAccountByName(String name);
    void createAccount(Account account);
    void saveAccount(Account account);
    Result<Void> deleteAccount(Account account);

    List<Account> getAccountsTopByCurrency(String currencyName, int limit, int offset);

    boolean isTopSupported();
    String getName();
    void close();
    void clearAll();

}