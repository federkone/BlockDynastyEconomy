package me.BlockDynasty.Economy.aplication.useCase.transaction;

import me.BlockDynasty.Economy.config.logging.AbstractLogger;
import me.BlockDynasty.Economy.domain.account.Account;
import me.BlockDynasty.Economy.domain.account.AccountManager;
import me.BlockDynasty.Economy.domain.account.Exceptions.AccountCanNotReciveException;
import me.BlockDynasty.Economy.domain.account.Exceptions.AccountNotFoundException;
import me.BlockDynasty.Economy.domain.account.Exceptions.InsufficientFundsException;
import me.BlockDynasty.Economy.aplication.useCase.account.GetAccountsUseCase;
import me.BlockDynasty.Economy.aplication.bungee.UpdateForwarder;
import me.BlockDynasty.Economy.domain.currency.Currency;
import me.BlockDynasty.Economy.domain.currency.CurrencyManager;
import me.BlockDynasty.Economy.domain.currency.Exceptions.CurrencyNotFoundException;
import me.BlockDynasty.Economy.domain.currency.Exceptions.DecimalNotSupportedException;
import me.BlockDynasty.Economy.domain.repository.Exceptions.TransactionException;
import me.BlockDynasty.Economy.domain.repository.IRepository;
import me.BlockDynasty.Economy.config.logging.EconomyLogger;
import me.BlockDynasty.Economy.utils.DecimalUtils;

import java.math.BigDecimal;
import java.util.UUID;

//TODO ESTA FUNCIONALIDAD PERMITE TRADEAR 2 MONEDAS DISTINTAS ENTRE 2 PERSONAS DISTINTAS, NO NECESITA VALIDAR SI LA MONEDA ES PAGABLE O NO, YA QUE ES UN TRADE
//trade, que 2 personas intercambien 2 monedas distintas
//TODO: TAMBIEN SE PODRIA COBRAR IMPUESTO POR TRADE
public class TradeCurrenciesUseCase {

    private final CurrencyManager currencyManager;
    private final IRepository dataStore;
    private final UpdateForwarder updateForwarder;
    private final AbstractLogger economyLogger;
    private final GetAccountsUseCase getAccountsUseCase;

    public TradeCurrenciesUseCase( CurrencyManager currencyManager,GetAccountsUseCase getAccountsUseCase, IRepository dataStore,
                                  UpdateForwarder updateForwarder, AbstractLogger economyLogger) {

        this.currencyManager = currencyManager;
        this.dataStore = dataStore;
        this.updateForwarder = updateForwarder;
        this.getAccountsUseCase = getAccountsUseCase;
        this.economyLogger = economyLogger;

    }

    public void execute(UUID userFrom, UUID userTo, String currencyFromS, String currencyToS, BigDecimal amountFrom, BigDecimal amountTo){
        Account accountFrom = getAccountsUseCase.getAccount(userFrom);
        Account accountTo = getAccountsUseCase.getAccount(userTo);
        Currency currencyFrom = currencyManager.getCurrency(currencyFromS);
        Currency currencyTo = currencyManager.getCurrency(currencyToS);

        performTrade(accountFrom, accountTo, currencyFrom, currencyTo, amountFrom, amountTo);
    }

    public void execute(String userFrom, String userTo, String currencyFromS, String currencyToS, BigDecimal amountFrom, BigDecimal amountTo){
        Account accountFrom = getAccountsUseCase.getAccount(userFrom);
        Account accountTo = getAccountsUseCase.getAccount(userTo);
        Currency currencyFrom = currencyManager.getCurrency(currencyFromS);
        Currency currencyTo = currencyManager.getCurrency(currencyToS);

        performTrade(accountFrom, accountTo, currencyFrom, currencyTo, amountFrom, amountTo);
    }

    private void performTrade ( Account accountFrom, Account accountTo, Currency currencyFrom, Currency currencyTo, BigDecimal amountFrom, BigDecimal amountTo){
        if (currencyFrom == null || currencyTo == null ) {
            throw new CurrencyNotFoundException("currencies not found");
        }

        if(accountFrom == null || accountTo == null){
            throw new AccountNotFoundException("Accounts not found");
        }

        if (!accountFrom.hasEnough(currencyFrom, amountFrom)) {
            throw new InsufficientFundsException("Insufficient balance for currency: " + currencyFrom.getUuid());
        }

        if (!accountTo.hasEnough(currencyTo, amountTo)) {
            throw new InsufficientFundsException("Insufficient balance for currency: " + currencyTo.getUuid());
        }

        if (!accountTo.canReceiveCurrency()) {
            throw new AccountCanNotReciveException("Account can't receive currency");
        }

        if (!currencyFrom.isDecimalSupported() && amountFrom.remainder(BigDecimal.ONE).compareTo(BigDecimal.ZERO) != 0) {
            throw new DecimalNotSupportedException("Currency does not support decimals");
        }

        if (!currencyTo.isDecimalSupported() && amountTo.remainder(BigDecimal.ONE).compareTo(BigDecimal.ZERO) != 0) {
            throw new DecimalNotSupportedException("Currency does not support decimals");
        }

        //preguntar si el receptor puede recibir monedas
//todo: revisar metodos de actualizar valores antes de guardar en db, verificar condiciones de carrera
        accountFrom.withdraw(currencyFrom, amountFrom);
        accountFrom.deposit(currencyTo, amountTo);
        accountTo.withdraw(currencyTo, amountTo);
        accountTo.deposit(currencyFrom, amountFrom);

        try {
            dataStore.transfer(accountFrom, accountTo); //actualizo ambos accounts de manera atomica, en una operacion segun hibernate
            updateForwarder.sendUpdateMessage("account", accountFrom.getUuid().toString());// esto es para bungee
            updateForwarder.sendUpdateMessage("account", accountTo.getUuid().toString());// esto es para bungee
            economyLogger.log("[TRADE] Account: " + accountFrom.getDisplayName() + " traded " + currencyFrom.format(amountFrom) + " to " + accountTo.getDisplayName() + " for " + currencyTo.format(amountTo));
        } catch (TransactionException e) {
            throw new TransactionException("Failed to perform trade: " + e.getMessage(), e);
        }
    }
}
