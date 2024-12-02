package me.BlockDynasty.Economy.aplication.useCase.transaction;

import me.BlockDynasty.Economy.aplication.useCase.account.GetAccountsUseCase;
import me.BlockDynasty.Economy.domain.account.Account;
import me.BlockDynasty.Economy.domain.account.AccountManager;
import me.BlockDynasty.Economy.domain.account.Exceptions.AccountNotFoundException;
import me.BlockDynasty.Economy.aplication.bungee.UpdateForwarder;
import me.BlockDynasty.Economy.domain.currency.Currency;
import me.BlockDynasty.Economy.domain.currency.CurrencyManager;
import me.BlockDynasty.Economy.domain.currency.Exceptions.CurrencyAmountNotValidException;
import me.BlockDynasty.Economy.domain.currency.Exceptions.CurrencyNotFoundException;
import me.BlockDynasty.Economy.domain.currency.Exceptions.DecimalNotSupportedException;
import me.BlockDynasty.Economy.domain.repository.Exceptions.TransactionException;
import me.BlockDynasty.Economy.domain.repository.IRepository;
import me.BlockDynasty.Economy.config.logging.EconomyLogger;

import java.util.UUID;

public class DepositUseCase {
    private final AccountManager accountManager;
    private final CurrencyManager currencyManager;
    private final IRepository dataStore;
    private final UpdateForwarder updateForwarder;
    private final EconomyLogger economyLogger;
    private final GetAccountsUseCase getAccountsUseCase;

    public DepositUseCase(AccountManager accountManager, CurrencyManager currencyManager,GetAccountsUseCase getAccountsUseCase, IRepository dataStore,
                          UpdateForwarder updateForwarder, EconomyLogger economyLogger){

        this.accountManager = accountManager;
        this.currencyManager = currencyManager;
        this.dataStore = dataStore;
        this.updateForwarder = updateForwarder;
        this.economyLogger = economyLogger;
        this.getAccountsUseCase = getAccountsUseCase;

    }

    //accountManager.deposit(target,currency, amount))
    public void execute(UUID targetUUID, String currencyName, double amount) {
        Account account = getAccountsUseCase.getAccount(targetUUID);
        Currency currency = currencyManager.getCurrency(currencyName);

        performDeposit(account, currency, amount,true);
    }


    public void execute(String targetName, String currencyName, double amount) {
        Account account = getAccountsUseCase.getAccount(targetName);
        Currency currency = currencyManager.getCurrency(currencyName);

        performDeposit(account, currency, amount,true);
    }

    public void execute(UUID targetUUID, String currencyName, double amount,boolean enableLog) {
        Account account = getAccountsUseCase.getAccount(targetUUID);
        Currency currency = currencyManager.getCurrency(currencyName);

        performDeposit(account, currency, amount,enableLog);
    }

    public void execute(String targetName, String currencyName, double amount,boolean enableLog) {
        Account account = getAccountsUseCase.getAccount(targetName);
        Currency currency = currencyManager.getCurrency(currencyName);

        performDeposit(account, currency, amount,enableLog);
    }

    //TODO: PREGUNTAR SI EL USUSARIO TIENE LA MONEDA? .DE MOMENTO TODAS LAS CUENTAS CUENTAN CON TODOS LOS TIPOS DE MONEDAS INICIALIZADAS
    private void performDeposit(Account account, Currency currency, double amount,boolean enableLog) {
        if (account == null) {
            throw new AccountNotFoundException("Account not found");
        }
        if (currency == null) {
            throw new CurrencyNotFoundException("Currency not found");
        }
        if (amount <= 0) {
            throw new CurrencyAmountNotValidException("Invalid amount");
        }
        if (!currency.isDecimalSupported() && amount % 1 != 0) {
            throw new DecimalNotSupportedException("Currency does not support decimals");
        }

        account.deposit(currency, amount);

        try {
            dataStore.saveAccount(account);
            updateForwarder.sendUpdateMessage("account", account.getUuid().toString());
            if(enableLog) //todo: esto puedo agregarlo al archivo de configuracion, para permitir que el usuario habilite el log de VAULT y todo lo que haga vault con este plugin
                economyLogger.log("[DEPOSIT] Account: " + account.getDisplayName() + " recibió un deposito de " + currency.format(amount) + " de " + currency.getSingular());
        } catch (TransactionException e) {
            throw new TransactionException("Error saving account",e);
        }
    }
}
