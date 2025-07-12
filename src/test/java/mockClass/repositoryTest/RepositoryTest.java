package mockClass.repositoryTest;

import me.BlockDynasty.Economy.domain.entities.account.Account;
import me.BlockDynasty.Economy.domain.entities.currency.Currency;
import me.BlockDynasty.Economy.Infrastructure.repository.Criteria.Criteria;
import me.BlockDynasty.Economy.domain.persistence.entities.IRepository;
import me.BlockDynasty.Economy.Infrastructure.repository.Criteria.Filter;
import me.BlockDynasty.Economy.domain.result.ErrorCode;
import me.BlockDynasty.Economy.domain.result.Result;
import me.BlockDynasty.Economy.domain.result.TransferResult;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

public class RepositoryTest implements IRepository {
    private final List<Account> accounts = new ArrayList<>();
    private final List<Currency> currencies = new ArrayList<>();
    @Override
    public void loadCurrencies() {

    }

    @Override
    public List<Currency> loadCurrencies(Criteria criteria) {
        if (criteria == null || criteria.getFilters().isEmpty()) {
            return new ArrayList<>(currencies);
        }

        List<Currency> filteredCurrencies = new ArrayList<>(currencies);
        for (Filter filter : criteria.getFilters()) {
            String field = filter.getField();
            Object value = filter.getValue();

            if ("singular".equals(field)) {
                filteredCurrencies = filteredCurrencies.stream()
                        .filter(currency -> value.equals(currency.getSingular()))
                        .collect(Collectors.toList());
            } else if ("plural".equals(field)) {
                filteredCurrencies = filteredCurrencies.stream()
                        .filter(currency -> value.equals(currency.getPlural()))
                        .collect(Collectors.toList());
            }
        }

        return filteredCurrencies;
    }

    @Override
    public void saveCurrency(Currency currency) {
        currencies.add(currency);
    }

    @Override
    public void deleteCurrency(Currency currency) {//todo create
        accounts.forEach(account -> account.getBalances().remove(currency.getSingular()));

        currencies.remove(currency);

    }

    @Override
    public List<Account> loadAccounts(Criteria criteria) {
        if (criteria == null || criteria.getFilters().isEmpty()) {
            return new ArrayList<>(accounts);
        }

        Filter filter = criteria.getFilters().iterator().next();
        String field = filter.getField();
        Object value = filter.getValue();

        return accounts.stream()
                .filter(account -> {
                    if ("nickname".equals(field)) {
                        return value.equals(account.getNickname());
                    }
                    return false;
                })
                .collect(Collectors.toList());
    }

    @Override
    public Result<Account> loadAccountByUuid(String uuid) {
        return accounts.stream()
                .filter(account -> account.getUuid().toString().equals(uuid))
                .findFirst()
                .map(Result::success)
                .orElseGet(() -> Result.failure("Account not found", ErrorCode.ACCOUNT_NOT_FOUND));
    }

    @Override
    public Result<Account> loadAccountByName(String name) {
        return accounts.stream()
                .filter(account -> account.getNickname().equals(name))
                .findFirst()
                .map(Result::success)
                .orElseGet(() -> Result.failure("Account not found", ErrorCode.ACCOUNT_NOT_FOUND));
    }

    @Override
    public void createAccount(Account account) {
        this.accounts.add(account);
    }

    @Override
    public void saveAccount(Account account) {
        this.accounts.removeIf(a -> a.getUuid().equals(account.getUuid()));
        this.accounts.add(account);
    }


    public void transfer(Account userFrom, Account userTo) {
        accounts.remove(userFrom);
        accounts.remove(userTo);
        accounts.add(userFrom);
        accounts.add(userTo);
    }

    @Override
    public Result<TransferResult> transfer(String userFrom, String userTo, Currency currency, BigDecimal amount) {
        // Simular una transferencia exitosa
        Account fromAccount = accounts.stream().filter(a -> a.getUuid().toString().equals(userFrom)).findFirst().orElse(null);
        Account toAccount = accounts.stream().filter(a -> a.getUuid().toString().equals(userTo)).findFirst().orElse(null);


        if (fromAccount== null || toAccount == null) {
            return Result.failure("Cuenta no encontrada", ErrorCode.ACCOUNT_NOT_FOUND);
        }

        Result<Void> result =fromAccount.subtract(currency, amount);
        if (!result.isSuccess()) {
            return Result.failure(result.getErrorMessage(), result.getErrorCode());
        }
        toAccount.add(currency, amount);
        return Result.success(new TransferResult(fromAccount, toAccount));
    }

    @Override
    public Result<Account> withdraw(String accountUuid, Currency currency, BigDecimal amount) {
        Account account = accounts.stream()
                .filter(a -> a.getUuid().toString().equals(accountUuid))
                .findFirst()
                .orElse(null);
        if (account == null) {
            return Result.failure("Account not found", ErrorCode.ACCOUNT_NOT_FOUND);
        }

        Result<Void> result =account.subtract(currency, amount);
        if (!result.isSuccess()) {
            return Result.failure(result.getErrorMessage(), result.getErrorCode());
        }
        return Result.success(account);

    }

    @Override
    public Result<Account> deposit(String accountUuid, Currency currency, BigDecimal amount) {
        Account account = accounts.stream()
                .filter(a -> a.getUuid().toString().equals(accountUuid))
                .findFirst()
                .orElse(null);
        if (account == null) {
            return Result.failure("Account not found", ErrorCode.ACCOUNT_NOT_FOUND);
        }

        Result<Void> result = account.add(currency, amount);
        if (!result.isSuccess()) {
            return Result.failure(result.getErrorMessage(), result.getErrorCode());
        }
        return Result.success(account);
    }

    @Override
    public Result<Account> exchange(String fromUuid, Currency fromCurrency, BigDecimal amountFrom, Currency toCurrency, BigDecimal amountTo) {
        Account account = accounts.stream()
                .filter(a -> a.getUuid().toString().equals(fromUuid))
                .findFirst()
                .orElse(null);
        if (account == null) {
            return Result.failure("Account not found", ErrorCode.ACCOUNT_NOT_FOUND);
        }

        Result<Void> resultSubtract = account.subtract(fromCurrency, amountFrom);
        if (!resultSubtract.isSuccess()) {

            return Result.failure(resultSubtract.getErrorMessage(), resultSubtract.getErrorCode());
        }

        Result<Void> resultAdd = account.add(toCurrency, amountTo);
        if (!resultAdd.isSuccess()) {

            return Result.failure(resultAdd.getErrorMessage(), resultAdd.getErrorCode());
        }

        return Result.success(account);
    }

    @Override
    public Result<TransferResult> trade(String fromUuid, String toUuid, Currency fromCurrency, Currency toCurrency, BigDecimal amountFrom, BigDecimal amountTo) {
        Account fromAccount = accounts.stream()
                .filter(a -> a.getUuid().toString().equals(fromUuid))
                .findFirst()
                .orElse(null);
        Account toAccount = accounts.stream()
                .filter(a -> a.getUuid().toString().equals(toUuid))
                .findFirst()
                .orElse(null);


        if (fromAccount == null) {
            return Result.failure("From account not found", ErrorCode.ACCOUNT_NOT_FOUND);
        }

        if(toAccount == null){
            return Result.failure("To account not found", ErrorCode.ACCOUNT_NOT_FOUND);
        }

        Result<Void> resultSubtractFrom = fromAccount.subtract(fromCurrency, amountFrom);
        if (!resultSubtractFrom.isSuccess()) {

            return Result.failure(resultSubtractFrom.getErrorMessage(), resultSubtractFrom.getErrorCode());
        }
        Result<Void> resultSubtractTo = toAccount.subtract(toCurrency, amountTo);
        if (!resultSubtractTo.isSuccess()) {
            return Result.failure(resultSubtractTo.getErrorMessage(), resultSubtractTo.getErrorCode());
        }
        Result<Void> resultAddFrom = fromAccount.add(toCurrency, amountTo);
        if (!resultAddFrom.isSuccess()) {
            return Result.failure(resultAddFrom.getErrorMessage(), resultAddFrom.getErrorCode());
        }
        Result<Void> resultAddTo = toAccount.add(fromCurrency, amountFrom);
        if (!resultAddTo.isSuccess()) {
            return Result.failure(resultAddTo.getErrorMessage(), resultAddTo.getErrorCode());
        }


        return Result.success(new TransferResult(fromAccount, toAccount));
    }

    @Override
    public Result<Account> setBalance(String accountUuid, Currency currency, BigDecimal amount) {
        Account account = accounts.stream()
                .filter(a -> a.getUuid().toString().equals(accountUuid))
                .findFirst()
                .orElse(null);
        if (account == null) {
            return Result.failure("Account not found", ErrorCode.ACCOUNT_NOT_FOUND);
        }
        Result<Void> result = account.setBalance(currency, amount);

        if (!result.isSuccess()) {
            return Result.failure(result.getErrorMessage(), result.getErrorCode());
        }

        return Result.success(account);
    }


    @Override
    public List<Account> getAccountsTopByCurrency(String currencyName, int limit, int offset) {
        return accounts.stream()
                // Filtrar cuentas que contienen la moneda especificada
                .filter(account -> account.hasCurrency(currencyName))
                // Ordenar por balance en la moneda especificada, en orden descendente
                .sorted((a1, a2) -> {
                    BigDecimal balance1 = a1.getBalance(currencyName).getBalance();
                    BigDecimal balance2 = a2.getBalance(currencyName).getBalance();
                    return balance2.compareTo(balance1); // Orden descendente
                })
                // Aplicar paginación
                .skip(offset)
                .limit(limit)
                // Colectar los resultados
                .collect(Collectors.toList());
    }

    @Override
    public boolean isTopSupported() {
        return true;
    }

    @Override
    public String getName() {
        return "mockRepository";
    }

    @Override
    public void close() {

    }

    @Override
    public void clearAll() {
        this.accounts.clear();
        this.currencies.clear();
    }
}
