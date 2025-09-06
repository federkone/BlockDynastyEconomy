package repository;

import BlockDynasty.Economy.domain.entities.account.Account;
import BlockDynasty.Economy.domain.entities.currency.Currency;
import BlockDynasty.Economy.domain.persistence.entities.IRepository;
import BlockDynasty.Economy.domain.result.ErrorCode;
import BlockDynasty.Economy.domain.result.Result;
import BlockDynasty.Economy.domain.result.TransferResult;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class RepositorySql implements IRepository {
    private static List<Account> accounts = new ArrayList<>();
    private static List<Currency> currencies = new ArrayList<>();

    @Override
    public List<Currency> loadCurrencies() {
        return new ArrayList<>();
    }

    @Override
    public Result<Currency> loadCurrencyByName(String name) {
        return currencies.stream().filter(currency -> currency.getSingular().equalsIgnoreCase(name) || currency.getPlural().equalsIgnoreCase(name) || currency.getSymbol().equalsIgnoreCase(name)).findFirst()
                .map(Result::success)
                .orElseGet(() -> Result.failure("Not found", ErrorCode.CURRENCY_NOT_FOUND));
    }

    @Override
    public Result<Currency> loadCurrencyByUuid(String uuid) {
        return currencies.stream().filter( currency -> currency.getUuid().equals(UUID.fromString(uuid))).findFirst()
                .map(Result::success)
                .orElseGet(() -> Result.failure("Not found", ErrorCode.CURRENCY_NOT_FOUND));
    }

    @Override
    public Result<Currency> loadDefaultCurrency() {
        return currencies.stream().filter( Currency::isDefaultCurrency).findFirst()
                .map(Result::success)
                .orElseGet(() -> Result.failure("Not found", ErrorCode.CURRENCY_NOT_FOUND));
    }

    @Override
    public void saveCurrency(Currency currency) {
        Currency fromCurrency = currencies.stream().filter( c -> c.getUuid().equals(currency.getUuid())).findFirst().orElse(null);
        if (fromCurrency != null) {
            currencies.remove(fromCurrency);
            currencies.add(currency);
            return;
        }
        currencies.add(currency);

    }

    @Override
    public void deleteCurrency(Currency currency) {
        currencies.remove(currency);
    }

    @Override
    public List<Account> loadAccounts() {
        return accounts;
    }

    @Override
    public Result<Account> loadAccountByUuid(String uuid) {
        return accounts.stream().filter( account -> account.getUuid().equals(UUID.fromString(uuid))).findFirst()
                .map(Result::success)
                .orElseGet(() -> Result.failure("Not found", ErrorCode.ACCOUNT_NOT_FOUND));
    }

    @Override
    public Result<Account> loadAccountByName(String name) {
        return accounts.stream().filter( account -> account.getNickname().equalsIgnoreCase(name)).findFirst()
                .map(Result::success)
                .orElseGet(() -> Result.failure("Not found", ErrorCode.ACCOUNT_NOT_FOUND));
    }

    @Override
    public void createAccount(Account account) {
        accounts.add(account);
    }

    @Override
    public void saveAccount(Account account) { //save is replace in this case
        Account fromAccount = accounts.stream().filter( a -> a.getUuid().equals(account.getUuid())).findFirst().orElse(null);
        if (fromAccount != null) {
            accounts.remove(fromAccount);
            accounts.add(account);
        } else {
            accounts.add(account);
        }
    }

    @Override
    public Result<Void> deleteAccount(Account account) {
        return accounts.remove(account) ? Result.success(null) : Result.failure("Not found", ErrorCode.ACCOUNT_NOT_FOUND);
    }

    @Override
    public List<Account> getAccountsTopByCurrency(String currencyName, int limit, int offset) {
        return accounts.stream().skip(offset).limit(limit).collect(Collectors.toList());
    }

    @Override
    public boolean isTopSupported() {
        return true;
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

    @Override
    public Result<TransferResult> transfer(String fromUuid, String toUuid, Currency currency, BigDecimal amount) {

        Account fromAccount = accounts.stream().filter( account -> account.getUuid().equals(UUID.fromString(fromUuid))).findFirst().orElse(null);
        Account toAccount = accounts.stream().filter( account -> account.getUuid().equals(UUID.fromString(toUuid))).findFirst().orElse(null);

        if (fromAccount == null || toAccount == null) {
            return Result.failure("Account not found", ErrorCode.ACCOUNT_NOT_FOUND);
        }

        fromAccount.getMoney().subtract(amount);
        toAccount.getMoney().add(amount);

        return Result.success(new TransferResult(fromAccount, toAccount));
    }

    @Override
    public Result<Account> withdraw(String accountUuid, Currency currency, BigDecimal amount) {
        Account account = accounts.stream().filter( acc -> acc.getUuid().equals(UUID.fromString(accountUuid))).findFirst().orElse(null);
        if (account != null) {
            account.getMoney().subtract(amount);
            return Result.success(account);
        }
        return Result.failure("account not found" , ErrorCode.ACCOUNT_NOT_FOUND);
    }

    @Override
    public Result<Account> deposit(String accountUuid, Currency currency, BigDecimal amount) {
        Account account = accounts.stream().filter( acc -> acc.getUuid().equals(UUID.fromString(accountUuid))).findFirst().orElse(null);
        if (account != null) {
            account.getMoney().add(amount);
            return Result.success(account);
        }
        return Result.failure("account not found" , ErrorCode.ACCOUNT_NOT_FOUND);
    }

    @Override
    public Result<Account> exchange(String fromUuid, Currency fromCurrency, BigDecimal amountFrom, Currency toCurrency, BigDecimal amountTo) {
        return Result.failure("Not implemented" , ErrorCode.ACCOUNT_NOT_FOUND);
    }

    @Override
    public Result<TransferResult> trade(String fromUuid, String toUuid, Currency fromCurrency, Currency toCurrency, BigDecimal amountFrom, BigDecimal amountTo) {
        Account fromAccount = accounts.stream().filter( account -> account.getUuid().equals(UUID.fromString(fromUuid))).findFirst().orElse(null);
        Account toAccount = accounts.stream().filter( account -> account.getUuid().equals(UUID.fromString(toUuid))).findFirst().orElse(null);
        if (fromAccount == null || toAccount == null) {
            return Result.failure("Account not found", ErrorCode.ACCOUNT_NOT_FOUND);
        }
        fromAccount.getMoney().subtract(amountFrom);
        fromAccount.getMoney().add(amountTo);
        toAccount.getMoney().subtract(amountTo);
        toAccount.getMoney().add(amountFrom);
        return Result.success(new TransferResult(fromAccount, toAccount));
    }

    @Override
    public Result<Account> setBalance(String accountUuid, Currency currency, BigDecimal amount) {
        Account account = accounts.stream().filter( acc -> acc.getUuid().equals(UUID.fromString(accountUuid))).findFirst().orElse(null);
        if (account != null) {
            account.getMoney().setAmount(amount);
            return Result.success(account);
        }
        return Result.failure("Not implemented" , ErrorCode.ACCOUNT_NOT_FOUND);
    }
}
