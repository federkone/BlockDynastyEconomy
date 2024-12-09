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
import me.BlockDynasty.Economy.utils.DecimalUtils;

import java.math.BigDecimal;
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

    public void execute(UUID userFrom, UUID userTo, String currencyName, BigDecimal amount) {
        Account accountFrom = getAccountsUseCase.getAccount(userFrom);
        Account accountTo = getAccountsUseCase.getAccount(userTo);
        Currency currency = currencyManager.getCurrency(currencyName);

        performPay(accountFrom, accountTo, currency, amount);
    }

    public void execute (String userFrom, String userTo, String currencyName, BigDecimal amount) {
        Account accountFrom = getAccountsUseCase.getAccount(userFrom);
        Account accountTo = getAccountsUseCase.getAccount(userTo);
        Currency currency = currencyManager.getCurrency(currencyName);

        performPay(accountFrom, accountTo, currency, amount);
    }

    private void performPay (Account accountFrom, Account accountTo, Currency currency, BigDecimal amount) {
        if (accountFrom == null || accountTo == null) {
            throw new AccountNotFoundException("Account not found");
        }
        if (currency == null) {
            throw new CurrencyNotFoundException("Currency not found");
        }
        if (!accountFrom.hasEnough(currency, amount)) {
            throw new InsufficientFundsException("Insufficient balance for currency: " + currency.getPlural());
        }

        if (!accountTo.canReceiveCurrency()) {
            throw new AccountCanNotReciveException("Account can't receive currency");
        }

        if(!currency.isPayable()){
            throw new CurrencyNotPayableException("Currency is not payable");
        }

        if (!currency.isDecimalSupported() && amount.remainder(BigDecimal.ONE).compareTo(BigDecimal.ZERO) != 0) {
            throw new DecimalNotSupportedException("Currency does not support decimals");
        }

        accountFrom.withdraw(currency, amount);
        accountTo.deposit(currency, amount);

//todo: revisar metodos de actualizar valores antes de guardar en db, verificar condiciones de carrera
        try {
            dataStore.transfer(accountFrom, accountTo);
            updateForwarder.sendUpdateMessage("account", accountFrom.getUuid().toString());
            updateForwarder.sendUpdateMessage("account", accountTo.getUuid().toString());
            economyLogger.log("[TRANSFER] Account: " + accountFrom.getDisplayName() + " transferred " +
                    currency.format(amount) + " to " + accountTo.getDisplayName());
            //todo aca actualizaria la cache con el metodo de GetAccountsUseCase.updateAccountCache
        } catch (TransactionException e) {
            throw new TransactionException("Failed to perform transfer: " + e.getMessage(), e);
        }
    }
}
