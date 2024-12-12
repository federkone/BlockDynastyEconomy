package repositoryTest;

import me.BlockDynasty.Economy.domain.account.Account;
import me.BlockDynasty.Economy.domain.currency.CachedTopListEntry;
import me.BlockDynasty.Economy.domain.currency.Currency;
import me.BlockDynasty.Economy.domain.repository.Callback;
import me.BlockDynasty.Economy.domain.repository.Criteria.Criteria;
import me.BlockDynasty.Economy.domain.repository.IRepository;
import me.BlockDynasty.Economy.domain.repository.Criteria.Filter;

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
    public void getTopList(Currency currency, int offset, int amount, Callback<LinkedList<CachedTopListEntry>> callback) {

    }

    @Override
    public List<Account> getAccountsByCurrency(String currencyName, int limit, int offset) {
        // Simulate getting accounts by currency
        return accounts.stream()
                .filter(account -> account.haveCurrency(currencyName))
                .skip(offset)
                .limit(limit)
                .collect(Collectors.toList());
    }
    @Override
    public boolean isTopSupported() {
        return false;
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

    }
}
