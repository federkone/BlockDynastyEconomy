package me.BlockDynasty.Economy.aplication.useCase.transaction;

import me.BlockDynasty.Economy.aplication.useCase.account.GetAccountsUseCase;
import me.BlockDynasty.Economy.aplication.useCase.currency.GetCurrencyUseCase;
import me.BlockDynasty.Economy.config.logging.AbstractLogger;
import me.BlockDynasty.Economy.domain.account.Account;
import me.BlockDynasty.Economy.aplication.bungee.UpdateForwarder;
import me.BlockDynasty.Economy.domain.currency.Currency;
import me.BlockDynasty.Economy.domain.currency.Exceptions.CurrencyNotPayableException;
import me.BlockDynasty.Economy.domain.repository.Exceptions.TransactionException;
import me.BlockDynasty.Economy.domain.repository.IRepository;

import java.math.BigDecimal;
import java.util.UUID;

public class PayUseCase {
    private final GetCurrencyUseCase getCurrencyUseCase;
    private final IRepository dataStore;
    private final UpdateForwarder updateForwarder;
    private final AbstractLogger economyLogger;
    private final GetAccountsUseCase getAccountsUseCase;

    public PayUseCase( GetCurrencyUseCase getCurrencyUseCase,GetAccountsUseCase getAccountsUseCase, IRepository dataStore,
                                UpdateForwarder updateForwarder, AbstractLogger economyLogger) {
        this.getCurrencyUseCase = getCurrencyUseCase;
        this.dataStore = dataStore;
        this.updateForwarder = updateForwarder;
        this.economyLogger = economyLogger;
        this.getAccountsUseCase = getAccountsUseCase;
    }

    public void execute(UUID userFrom, UUID userTo, String currencyName, BigDecimal amount) {
        Account accountFrom = getAccountsUseCase.getAccount(userFrom);
        Account accountTo = getAccountsUseCase.getAccount(userTo);
        Currency currency = getCurrencyUseCase.getCurrency(currencyName);

        performPay(accountFrom, accountTo, currency, amount);
    }

    public void execute (String userFrom, String userTo, String currencyName, BigDecimal amount) {
        Account accountFrom = getAccountsUseCase.getAccount(userFrom);
        Account accountTo = getAccountsUseCase.getAccount(userTo);
        Currency currency = getCurrencyUseCase.getCurrency(currencyName);

        performPay(accountFrom, accountTo, currency, amount);
    }

    private void performPay (Account accountFrom, Account accountTo, Currency currency, BigDecimal amount) {
        if(!currency.isPayable()){
            throw new CurrencyNotPayableException("Currency is not payable");
        }

        accountFrom.transfer(accountTo,currency,amount);

    //todo: revisar metodos de actualizar valores antes de guardar en db, verificar condiciones de carrera
        try {
            dataStore.transfer(accountFrom, accountTo);

            if(updateForwarder != null && economyLogger != null){ //todo , lo puse para testear y ommitir esto
                updateForwarder.sendUpdateMessage("account", accountFrom.getUuid().toString());
                updateForwarder.sendUpdateMessage("account", accountTo.getUuid().toString());
                economyLogger.log("[TRANSFER] Account: " + accountFrom.getNickname() + " transferred " +
                        currency.format(amount) + " to " + accountTo.getNickname());
            }
            //todo aca actualizaria la cache con el metodo de GetAccountsUseCase.updateAccountCache
        } catch (TransactionException e) {
            throw new TransactionException("Failed to perform transfer: " + e.getMessage(), e);
        }
    }
}
