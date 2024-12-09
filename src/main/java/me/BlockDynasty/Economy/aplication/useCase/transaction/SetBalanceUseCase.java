package me.BlockDynasty.Economy.aplication.useCase.transaction;

import me.BlockDynasty.Economy.config.logging.AbstractLogger;
import me.BlockDynasty.Economy.domain.account.Account;
import me.BlockDynasty.Economy.domain.account.AccountManager;
import me.BlockDynasty.Economy.domain.account.Exceptions.AccountNotFoundException;
import me.BlockDynasty.Economy.aplication.useCase.account.GetAccountsUseCase;
import me.BlockDynasty.Economy.aplication.bungee.UpdateForwarder;
import me.BlockDynasty.Economy.domain.currency.Currency;
import me.BlockDynasty.Economy.domain.currency.CurrencyManager;
import me.BlockDynasty.Economy.domain.currency.Exceptions.CurrencyAmountNotValidException;
import me.BlockDynasty.Economy.domain.currency.Exceptions.CurrencyNotFoundException;
import me.BlockDynasty.Economy.domain.currency.Exceptions.DecimalNotSupportedException;
import me.BlockDynasty.Economy.domain.repository.Exceptions.TransactionException;
import me.BlockDynasty.Economy.domain.repository.IRepository;
import me.BlockDynasty.Economy.config.logging.EconomyLogger;
import me.BlockDynasty.Economy.utils.DecimalUtils;

import java.math.BigDecimal;
import java.util.UUID;

public class SetBalanceUseCase {
    private final CurrencyManager currencyManager;
    private final IRepository dataStore;
    private final UpdateForwarder updateForwarder;
    private final AbstractLogger economyLogger;
    private final GetAccountsUseCase getAccountsUseCase;

    public SetBalanceUseCase(CurrencyManager currencyManager, GetAccountsUseCase getAccountsUseCase,IRepository dataStore,
                             UpdateForwarder updateForwarder, AbstractLogger economyLogger){

        this.currencyManager = currencyManager;
        this.dataStore = dataStore;
        this.updateForwarder = updateForwarder;
        this.economyLogger = economyLogger;
        this.getAccountsUseCase = getAccountsUseCase;

    }

    public void execute(UUID targetUUID, String currencyName, BigDecimal amount) {
        Account account = getAccountsUseCase.getAccount(targetUUID);
        Currency currency = currencyManager.getCurrency(currencyName);

        performSet(account, currency, amount);
    }

    public void execute(String targetName, String currencyName, BigDecimal amount) {
        Account account = getAccountsUseCase.getAccount(targetName);
        Currency currency = currencyManager.getCurrency(currencyName);

        performSet(account, currency, amount);
    }

    private void performSet(Account account, Currency currency, BigDecimal amount) {
        if (account == null) {
            throw new AccountNotFoundException("Account not found");
        }
        if (currency == null) {
            throw new CurrencyNotFoundException("Currency not found");
        }
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new CurrencyAmountNotValidException("Invalid amount");
        }

        if (!currency.isDecimalSupported() && amount.remainder(BigDecimal.ONE).compareTo(BigDecimal.ZERO) != 0) {
            throw new DecimalNotSupportedException("Currency does not support decimals");
        }

        account.setBalance(currency, amount);
        try {
            dataStore.saveAccount(account);
            updateForwarder.sendUpdateMessage("account", account.getUuid().toString());
            economyLogger.log("[BALANCE SET] Account: " + account.getDisplayName() + " were set to: " + currency.format(amount));
        } catch (TransactionException e) {
           throw new TransactionException("Error setting balance");
        }
    }
}
