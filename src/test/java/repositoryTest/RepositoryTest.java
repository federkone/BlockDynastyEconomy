package repositoryTest;

import me.BlockDynasty.Economy.domain.account.Account;
import me.BlockDynasty.Economy.domain.balance.Balance;
import me.BlockDynasty.Economy.domain.currency.Currency;
import me.BlockDynasty.Economy.domain.repository.Criteria.Criteria;
import me.BlockDynasty.Economy.domain.repository.IRepository;
import me.BlockDynasty.Economy.domain.repository.Criteria.Filter;
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


        if (fromAccount == null){
            return Result.failure("From account not found", ErrorCode.ACCOUNT_NOT_FOUND);
        }
        if (toAccount == null){
            return Result.failure("To account not found", ErrorCode.ACCOUNT_NOT_FOUND);
        }

        if (!fromAccount.hasEnough(currency,amount)){
            return Result.failure("To account from not have balance", ErrorCode.INSUFFICIENT_FUNDS);
        }

        Result<Void> result =fromAccount.setBalance(currency, fromAccount.getBalance(currency).getBalance().subtract(amount));
        if (!result.isSuccess()) {
            return Result.failure(result.getErrorMessage(), result.getErrorCode());
        }

        toAccount.setBalance(currency, toAccount.getBalance(currency).getBalance().add(amount));

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

        Balance balance = account.getBalance(currency.getSingular());

        if(balance.getBalance().compareTo(amount) <0){
            return Result.failure("Insufficient funds", ErrorCode.INSUFFICIENT_FUNDS);
        }
        BigDecimal newBalance = balance.getBalance().subtract(amount);
        balance.setBalance(newBalance);
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
        //depositar el balance
        Balance balance = account.getBalance(currency.getSingular());
        if (balance !=null){
            account.setBalance(currency, account.getBalance(currency).getBalance().add(amount));
            return Result.success(account);
        }else {return Result.failure("Insufficient funds", ErrorCode.CURRENCY_NOT_FOUND); }

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
        // Verificar si el balance es suficiente
        Balance fromBalance = account.getBalance(fromCurrency.getSingular());
        if (fromBalance == null || fromBalance.getBalance().compareTo(amountFrom) < 0) {
            return Result.failure("Insufficient funds", ErrorCode.INSUFFICIENT_FUNDS);
        }
        // Verificar si la moneda de destino existe
        Balance toBalance = account.getBalance(toCurrency.getSingular());
        if (toBalance == null) {
            return Result.failure("Currency not found", ErrorCode.CURRENCY_NOT_FOUND);
        }
        // Realizar la transacción
        fromBalance.setBalance(fromBalance.getBalance().subtract(amountFrom));
        toBalance.setBalance(toBalance.getBalance().add(amountTo));
        // Actualizar la cuenta
        accounts.remove(account);
        accounts.add(account);
        // Retornar el resultado
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
        if (toAccount == null) {
            return Result.failure("To account not found", ErrorCode.ACCOUNT_NOT_FOUND);
        }

        Balance fromBalance = fromAccount.getBalance(fromCurrency.getSingular());
        if (fromBalance == null || fromBalance.getBalance().compareTo(amountFrom) < 0) {
            return Result.failure("Insufficient funds in the from account", ErrorCode.INSUFFICIENT_FUNDS);
        }
        Balance toBalance = toAccount.getBalance(toCurrency.getSingular());
        if (toBalance == null || toBalance.getBalance().compareTo(amountTo) < 0) {
            return Result.failure("Insufficient funds in the to account", ErrorCode.INSUFFICIENT_FUNDS);
        }
        // Perform the trade
        // fromAccount: pierde fromCurrency, gana toCurrency
        fromBalance.setBalance(fromBalance.getBalance().subtract(amountFrom));
        Balance fromToBalance = fromAccount.getBalance(toCurrency.getSingular());
        fromToBalance.setBalance(fromToBalance.getBalance().add(amountTo));

// toAccount: pierde toCurrency, gana fromCurrency
        Balance toFromBalance = toAccount.getBalance(fromCurrency.getSingular());
        toFromBalance.setBalance(toFromBalance.getBalance().add(amountFrom));
        toBalance.setBalance(toBalance.getBalance().subtract(amountTo));

        // Return the transfer result
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
        // Establecer el balance
        Balance balance = account.getBalance(currency.getSingular());
        if (balance != null) {
            balance.setBalance(amount);
            return Result.success(account);
        } else {
            return Result.failure("Currency not found", ErrorCode.CURRENCY_NOT_FOUND);
        }
    }


    @Override
    public List<Account> getAccountsTopByCurrency(String currencyName, int limit, int offset) {
        return accounts.stream()
                // Filtrar cuentas que contienen la moneda especificada
                .filter(account -> account.haveCurrency(currencyName))
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
        return "fakeRepository";
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
