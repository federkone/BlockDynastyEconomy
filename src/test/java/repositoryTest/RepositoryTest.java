package repositoryTest;

import me.BlockDynasty.Economy.domain.account.Account;
import me.BlockDynasty.Economy.domain.currency.Currency;
import me.BlockDynasty.Economy.domain.repository.Criteria.Criteria;
import me.BlockDynasty.Economy.domain.repository.IRepository;
import me.BlockDynasty.Economy.domain.repository.Criteria.Filter;

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

    @Override
    public void transfer(Account userFrom, Account userTo) {
        accounts.remove(userFrom);
        accounts.remove(userTo);
        accounts.add(userFrom);
        accounts.add(userTo);
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
