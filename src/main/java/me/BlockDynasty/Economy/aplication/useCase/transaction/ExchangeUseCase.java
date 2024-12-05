package me.BlockDynasty.Economy.aplication.useCase.transaction;

import me.BlockDynasty.Economy.aplication.useCase.account.GetAccountsUseCase;
import me.BlockDynasty.Economy.config.logging.AbstractLogger;
import me.BlockDynasty.Economy.domain.account.Account;
import me.BlockDynasty.Economy.domain.account.AccountManager;
import me.BlockDynasty.Economy.domain.account.Exceptions.AccountNotFoundException;
import me.BlockDynasty.Economy.domain.account.Exceptions.InsufficientFundsException;
import me.BlockDynasty.Economy.aplication.bungee.UpdateForwarder;
import me.BlockDynasty.Economy.domain.currency.Currency;
import me.BlockDynasty.Economy.domain.currency.CurrencyManager;
import me.BlockDynasty.Economy.domain.currency.Exceptions.CurrencyNotFoundException;
import me.BlockDynasty.Economy.domain.currency.Exceptions.DecimalNotSupportedException;
import me.BlockDynasty.Economy.domain.repository.Exceptions.TransactionException;
import me.BlockDynasty.Economy.domain.repository.IRepository;
import me.BlockDynasty.Economy.config.logging.EconomyLogger;

import java.util.UUID;


// SE ENCARGA DE ACTUALIZAR LOS MONTOS DE CUENTA Y DE INTENTAR SALVAR EN LA DB
//TODO: aqui se puede agregar el impuesto por cambio de divisa segun el rate de la moneda
//exchange permite a persona cambiar sus monedas con si mismo
//todo: se supone que tampoco deberia validar si la moneda es pagable o no, ya que es un exchange
//todo: falta agregar validaciones de decimal support etc
public class ExchangeUseCase {
    private final CurrencyManager currencyManager;
    private final IRepository dataStore;
    private final UpdateForwarder updateForwarder;
    private final AbstractLogger economyLogger;
    private final GetAccountsUseCase getAccountsUseCase;
    public ExchangeUseCase(CurrencyManager currencyManager,GetAccountsUseCase getAccountsUseCase, IRepository dataStore,
                           UpdateForwarder updateForwarder, AbstractLogger economyLogger) {
        this.currencyManager = currencyManager;
        this.dataStore = dataStore;
        this.updateForwarder = updateForwarder;
        this.economyLogger = economyLogger;
        this.getAccountsUseCase = getAccountsUseCase;
    }

    public void execute(UUID accountUuid, String currencyFromName, String currencyToname, double amountFrom, double amountTo) {
        Account account = getAccountsUseCase.getAccount(accountUuid);
        Currency currencyFrom = currencyManager.getCurrency(currencyFromName);
        Currency currencyTo = currencyManager.getCurrency(currencyToname);

        performExchange(account,currencyFrom,currencyTo,amountFrom,amountTo);

    }

    public void execute(String accountString, String currencyFromName, String currencyToname, double amountFrom, double amountTo) {
        Account account = getAccountsUseCase.getAccount(accountString);
        Currency currencyFrom = currencyManager.getCurrency(currencyFromName);
        Currency currencyTo = currencyManager.getCurrency(currencyToname);

        performExchange(account,currencyFrom,currencyTo,amountFrom,amountTo);
    }


    private void performExchange(Account account, Currency currencyFrom, Currency currencyTo,double amountFrom,double amountTo){
        if(currencyFrom == null || currencyTo == null ) {
            throw new CurrencyNotFoundException("currency not found"); // Manejo de errores
        }

        if (account == null){
            throw new AccountNotFoundException("Account or One or both currencies not found"); // Manejo de errores
        }

        if (!currencyFrom.isDecimalSupported() && amountFrom % 1 != 0) {
            throw new DecimalNotSupportedException("Currency does not support decimals");
        }

        if (!currencyTo.isDecimalSupported() && amountTo % 1 != 0) {
            throw new DecimalNotSupportedException("Currency does not support decimals");
        }
        //TODO:aca se puede calcular el ratio de impuesto a cobrar antes de preguntar si tiene suficiente fondos
        if (!account.hasEnough(currencyFrom, amountFrom)) {
            throw new InsufficientFundsException("Insufficient balance for currency: " + currencyFrom.getUuid()); // Manejo de errores
        }

        account.withdraw(currencyFrom, amountFrom);
        account.deposit(currencyTo, amountTo);

        try {
            dataStore.saveAccount(account);
            updateForwarder.sendUpdateMessage("account", account.getUuid().toString());// esto es para bungee
            economyLogger.log("[EXCHANGE] Account: " + account.getDisplayName() + " exchanged " + currencyFrom.format(amountFrom) + " to " + currencyTo.format(amountTo));
        } catch (TransactionException e) {
            // Manejo de errores (puedes lanzar la excepción o manejarla de otra manera)
            throw new TransactionException("Failed to perform exchange: " + e.getMessage(), e);
        }
    }
}
