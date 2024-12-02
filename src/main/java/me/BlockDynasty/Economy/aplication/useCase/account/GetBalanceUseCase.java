package me.BlockDynasty.Economy.aplication.useCase.account;

import me.BlockDynasty.Economy.domain.account.Account;
import me.BlockDynasty.Economy.domain.account.AccountManager;
import me.BlockDynasty.Economy.domain.account.Exceptions.AccountNotFoundException;
import me.BlockDynasty.Economy.aplication.bungee.UpdateForwarder;
import me.BlockDynasty.Economy.domain.currency.Currency;
import me.BlockDynasty.Economy.domain.currency.CurrencyManager;
import me.BlockDynasty.Economy.domain.currency.Exceptions.CurrencyNotFoundException;
import me.BlockDynasty.Economy.domain.repository.IRepository;

import java.util.Map;
import java.util.UUID;

public class GetBalanceUseCase {
    private final AccountManager accountManager;
    private final CurrencyManager currencyManager;
    private final IRepository dataStore;
    private final UpdateForwarder updateForwarder;
    private final GetAccountsUseCase getAccountsUseCase;

    public GetBalanceUseCase(AccountManager accountManager, CurrencyManager currencyManager, GetAccountsUseCase getAccountsUseCase, UpdateForwarder updateForwarder, IRepository dataStore) {
        this.accountManager = accountManager;
        this.currencyManager = currencyManager;
        this.dataStore = dataStore;
        this.updateForwarder = updateForwarder;
        this.getAccountsUseCase = getAccountsUseCase;
    }


    public Map<Currency, Double> getBalances(String accountName) {
        Account account = getAccountsUseCase.getAccount(accountName);
        return performGetBalances(account);
    }

    public Map<Currency, Double> getBalances(UUID accountUUID) {
        Account account = getAccountsUseCase.getAccount(accountUUID);
        return performGetBalances(account);
    }

    private Map<Currency, Double> performGetBalances(Account account) {
        if(account == null) {
            throw new AccountNotFoundException("Account not found");
        }
        if(account.getBalances().isEmpty()) {
            throw new CurrencyNotFoundException("Account has no balances");
        }

        return account.getBalances();
    }
}
