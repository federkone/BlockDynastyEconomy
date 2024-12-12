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

public class TransferFundsUseCase {

    private final GetCurrencyUseCase getCurrencyUseCase;
    private final IRepository dataStore;
    private final UpdateForwarder updateForwarder;
    private final AbstractLogger economyLogger;
    private final GetAccountsUseCase getAccountsUseCase;

    public TransferFundsUseCase(GetCurrencyUseCase getCurrencyUseCase, GetAccountsUseCase getAccountsUseCase, IRepository dataStore,
                                UpdateForwarder updateForwarder, AbstractLogger economyLogger) {
        this.getCurrencyUseCase = getCurrencyUseCase;
        this.dataStore = dataStore;
        this.updateForwarder = updateForwarder;
        this.economyLogger = economyLogger;
        this.getAccountsUseCase = getAccountsUseCase;
    }

    public void execute(UUID userFrom, UUID userTo, String currency, BigDecimal amount) {
        Account accountFrom = getAccountsUseCase.getAccount(userFrom);
        Account accountTo = getAccountsUseCase.getAccount(userTo);
        Currency currencyFrom = getCurrencyUseCase.getCurrency(currency);

        performTransfer(accountFrom, accountTo, currencyFrom, amount);
    }

    public void execute (String userFrom, String userTo, String currency, BigDecimal amount) {
        Account accountFrom = getAccountsUseCase.getAccount(userFrom);
        Account accountTo = getAccountsUseCase.getAccount(userTo);
        Currency currencyFrom = getCurrencyUseCase.getCurrency(currency);

        performTransfer(accountFrom, accountTo, currencyFrom, amount);
    }

    private void performTransfer(Account accountFrom, Account accountTo, Currency currencyFrom, BigDecimal amount){

//todo: revisar metodos de actualizar valores antes de guardar en db, verificar condiciones de carrera

        accountFrom.withdraw(currencyFrom, amount);
        accountTo.deposit(currencyFrom, amount);

        try {
            dataStore.transfer(accountFrom, accountTo);
            if(updateForwarder != null && economyLogger != null) { //todo , lo puse para testear y ommitir esto
                updateForwarder.sendUpdateMessage("account", accountFrom.getUuid().toString());
                updateForwarder.sendUpdateMessage("account", accountTo.getUuid().toString());
                economyLogger.log("[TRANSFER] Account: " + accountFrom.getNickname() + " transferred " +
                        currencyFrom.format(amount) + " to " + accountTo.getNickname());
            }
        } catch (TransactionException e) {
            throw new TransactionException("Failed to perform transfer: " + e.getMessage(), e);
        }
    }
}