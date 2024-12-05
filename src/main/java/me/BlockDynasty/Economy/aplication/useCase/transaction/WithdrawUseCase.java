package me.BlockDynasty.Economy.aplication.useCase.transaction;

import me.BlockDynasty.Economy.config.logging.AbstractLogger;
import me.BlockDynasty.Economy.domain.account.Account;
import me.BlockDynasty.Economy.domain.account.AccountManager;
import me.BlockDynasty.Economy.domain.account.Exceptions.AccountNotFoundException;
import me.BlockDynasty.Economy.domain.account.Exceptions.InsufficientFundsException;
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
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.util.UUID;

//TODO, FUNCIONALIDAD PARA EXTRACCION DE DINERO
public class WithdrawUseCase {
    private final CurrencyManager currencyManager;
    private final IRepository dataStore;
    private final UpdateForwarder updateForwarder;
    private final AbstractLogger logger;
    private final GetAccountsUseCase getAccountsUseCase;

    public WithdrawUseCase(CurrencyManager currencyManager, GetAccountsUseCase getAccountsUseCase, IRepository dataStore,
                           UpdateForwarder updateForwarder, AbstractLogger logger){
        this.currencyManager = currencyManager;
        this.dataStore = dataStore;
        this.updateForwarder = updateForwarder;
        this.logger = logger;
        this.getAccountsUseCase = getAccountsUseCase;
    }

    public void execute(UUID targetUUID, String currencyName, double amount) {
        Account account = getAccountsUseCase.getAccount(targetUUID);
        Currency currency = currencyManager.getCurrency(currencyName);

        performWithdraw(account, currency, amount);
    }

    public void execute(String targetName, String currencyName, double amount) {
        Account account = getAccountsUseCase.getAccount(targetName);
        Currency currency = currencyManager.getCurrency(currencyName);

        performWithdraw(account, currency, amount);
    }


    //TODO: PREGUNTAR SI EL USUSARIO TIENE LA MONEDA? .DE MOMENTO TODAS LAS CUENTAS CUENTAN CON TODOS LOS TIPOS DE MONEDAS INICIALIZADAS
    private void performWithdraw(Account account, Currency currency, double amount) {
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
        if (!account.hasEnough(currency, amount)) {
            throw new InsufficientFundsException("Insufficient funds");
        }

        account.withdraw(currency, amount);

        try {
            dataStore.saveAccount(account);
            updateForwarder.sendUpdateMessage("account", account.getUuid().toString());
            logger.log("[WITHDRAW] Account: " + account.getDisplayName() + " extrajo " + currency.format(amount) + " de " + currency.getSingular());
        } catch (TransactionException e) {
            throw new TransactionException("Error saving account",e);
        }

    }

}
