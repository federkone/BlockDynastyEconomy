package me.BlockDynasty.Economy.aplication.useCase.transaction;

import me.BlockDynasty.Economy.aplication.useCase.currency.GetCurrencyUseCase;
import me.BlockDynasty.Economy.config.logging.AbstractLogger;
import me.BlockDynasty.Economy.domain.account.Account;
import me.BlockDynasty.Economy.aplication.useCase.account.GetAccountsUseCase;
import me.BlockDynasty.Economy.aplication.bungee.UpdateForwarder;
import me.BlockDynasty.Economy.domain.currency.Currency;
import me.BlockDynasty.Economy.domain.repository.Exceptions.TransactionException;
import me.BlockDynasty.Economy.domain.repository.IRepository;

import java.math.BigDecimal;
import java.util.UUID;

public class SetBalanceUseCase {
    private final GetCurrencyUseCase getCurrencyUseCase;
    private final IRepository dataStore;
    private final UpdateForwarder updateForwarder;
    private final AbstractLogger economyLogger;
    private final GetAccountsUseCase getAccountsUseCase;

    public SetBalanceUseCase(GetCurrencyUseCase getCurrencyUseCase, GetAccountsUseCase getAccountsUseCase,IRepository dataStore,
                             UpdateForwarder updateForwarder, AbstractLogger economyLogger){

        this.getCurrencyUseCase = getCurrencyUseCase;
        this.dataStore = dataStore;
        this.updateForwarder = updateForwarder;
        this.economyLogger = economyLogger;
        this.getAccountsUseCase = getAccountsUseCase;

    }

    public void execute(UUID targetUUID, String currencyName, BigDecimal amount) {
        Account account = getAccountsUseCase.getAccount(targetUUID);
        Currency currency = getCurrencyUseCase.getCurrency(currencyName);

        performSet(account, currency, amount);
    }

    public void execute(String targetName, String currencyName, BigDecimal amount) {
        Account account = getAccountsUseCase.getAccount(targetName);
        Currency currency = getCurrencyUseCase.getCurrency(currencyName);

        performSet(account, currency, amount);
    }

    private void performSet(Account account, Currency currency, BigDecimal amount) {

        account.setBalance(currency, amount);
        try {
            dataStore.saveAccount(account);
            if(updateForwarder != null && economyLogger != null) { //todo , lo puse para testear y ommitir esto
                updateForwarder.sendUpdateMessage("account", account.getUuid().toString());
                economyLogger.log("[BALANCE SET] Account: " + account.getNickname() + " were set to: " + currency.format(amount));
            }
        } catch (TransactionException e) {
           throw new TransactionException("Error setting balance");
        }
    }
}
