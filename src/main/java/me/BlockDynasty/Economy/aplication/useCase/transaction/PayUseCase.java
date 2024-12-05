package me.BlockDynasty.Economy.aplication.useCase.transaction;

import me.BlockDynasty.Economy.aplication.useCase.account.GetAccountsUseCase;
import me.BlockDynasty.Economy.config.logging.AbstractLogger;
import me.BlockDynasty.Economy.domain.account.Account;
import me.BlockDynasty.Economy.domain.account.AccountManager;
import me.BlockDynasty.Economy.domain.account.Exceptions.AccountCanNotReciveException;
import me.BlockDynasty.Economy.domain.account.Exceptions.AccountNotFoundException;
import me.BlockDynasty.Economy.domain.account.Exceptions.InsufficientFundsException;
import me.BlockDynasty.Economy.aplication.bungee.UpdateForwarder;
import me.BlockDynasty.Economy.domain.currency.Currency;
import me.BlockDynasty.Economy.domain.currency.CurrencyManager;
import me.BlockDynasty.Economy.domain.currency.Exceptions.CurrencyNotFoundException;
import me.BlockDynasty.Economy.domain.currency.Exceptions.CurrencyNotPayableException;
import me.BlockDynasty.Economy.domain.currency.Exceptions.DecimalNotSupportedException;
import me.BlockDynasty.Economy.domain.repository.Exceptions.TransactionException;
import me.BlockDynasty.Economy.domain.repository.IRepository;
import me.BlockDynasty.Economy.config.logging.EconomyLogger;

import java.util.UUID;

public class PayUseCase {
    private final CurrencyManager currencyManager;
    private final IRepository dataStore;
    private final UpdateForwarder updateForwarder;
    private final AbstractLogger economyLogger;
    private final GetAccountsUseCase getAccountsUseCase;

    public PayUseCase( CurrencyManager currencyManager,GetAccountsUseCase getAccountsUseCase, IRepository dataStore,
                                UpdateForwarder updateForwarder, AbstractLogger economyLogger) {
        this.currencyManager = currencyManager;
        this.dataStore = dataStore;
        this.updateForwarder = updateForwarder;
        this.economyLogger = economyLogger;
        this.getAccountsUseCase = getAccountsUseCase;
    }

    public void execute(UUID userFrom, UUID userTo, String currency, double amount) {
        Account accountFrom = getAccountsUseCase.getAccount(userFrom);
        Account accountTo = getAccountsUseCase.getAccount(userTo);
        Currency currencyFrom = currencyManager.getCurrency(currency);

        performPay(accountFrom, accountTo, currencyFrom, amount);
    }

    public void execute (String userFrom, String userTo, String currency, double amount) {
        Account accountFrom = getAccountsUseCase.getAccount(userFrom);
        Account accountTo = getAccountsUseCase.getAccount(userTo);
        Currency currencyFrom = currencyManager.getCurrency(currency);

        performPay(accountFrom, accountTo, currencyFrom, amount);
    }

    private void performPay (Account accountFrom, Account accountTo, Currency currencyFrom, double amount) {
        if (accountFrom == null || accountTo == null) {
            throw new AccountNotFoundException("Account not found");
        }
        if (currencyFrom == null) {
            throw new CurrencyNotFoundException("Currency not found");
        }
        if (!accountFrom.hasEnough(currencyFrom, amount)) {
            throw new InsufficientFundsException("Insufficient balance for currency: " + currencyFrom.getPlural());
        }
        if (!currencyFrom.isDecimalSupported() && amount % 1 != 0) {
            throw new DecimalNotSupportedException("Currency does not support decimals");
        }
        if (!accountTo.canReceiveCurrency()) {
            throw new AccountCanNotReciveException("Account can't receive currency");
        }

        if(!currencyFrom.isPayable()){
            throw new CurrencyNotPayableException("Currency is not payable");
        }

        accountFrom.withdraw(currencyFrom, amount);//TODO ACTUALIZA CUENTA CACHE
        accountTo.deposit(currencyFrom, amount);//TODO ACTUALIZA CUENTA CACHE

        try {

            dataStore.transfer(accountFrom, accountTo);
            updateForwarder.sendUpdateMessage("account", accountFrom.getUuid().toString());
            updateForwarder.sendUpdateMessage("account", accountTo.getUuid().toString());
            economyLogger.log("[TRANSFER] Account: " + accountFrom.getDisplayName() + " transferred " +
                    currencyFrom.format(amount) + " to " + accountTo.getDisplayName());
        } catch (TransactionException e) {
            throw new TransactionException("Failed to perform transfer: " + e.getMessage(), e);
        }
    }
}
