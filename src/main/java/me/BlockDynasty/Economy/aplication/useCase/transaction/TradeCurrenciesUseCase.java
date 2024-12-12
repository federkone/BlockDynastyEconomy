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

//TODO ESTA FUNCIONALIDAD PERMITE TRADEAR 2 MONEDAS DISTINTAS ENTRE 2 PERSONAS DISTINTAS, NO NECESITA VALIDAR SI LA MONEDA ES PAGABLE O NO, YA QUE ES UN TRADE
//trade, que 2 personas intercambien 2 monedas distintas
//TODO: TAMBIEN SE PODRIA COBRAR IMPUESTO POR TRADE
public class TradeCurrenciesUseCase {

    private final GetCurrencyUseCase getCurrencyUseCase ;
    private final IRepository dataStore;
    private final UpdateForwarder updateForwarder;
    private final AbstractLogger economyLogger;
    private final GetAccountsUseCase getAccountsUseCase;

    public TradeCurrenciesUseCase( GetCurrencyUseCase getCurrencyUseCase,GetAccountsUseCase getAccountsUseCase, IRepository dataStore,
                                  UpdateForwarder updateForwarder, AbstractLogger economyLogger) {

        this.getCurrencyUseCase = getCurrencyUseCase;
        this.dataStore = dataStore;
        this.updateForwarder = updateForwarder;
        this.getAccountsUseCase = getAccountsUseCase;
        this.economyLogger = economyLogger;

    }

    public void execute(UUID userFrom, UUID userTo, String currencyFromS, String currencyToS, BigDecimal amountFrom, BigDecimal amountTo){
        Account accountFrom = getAccountsUseCase.getAccount(userFrom);
        Account accountTo = getAccountsUseCase.getAccount(userTo);
        Currency currencyFrom = getCurrencyUseCase.getCurrency(currencyFromS);
        Currency currencyTo = getCurrencyUseCase.getCurrency(currencyToS);

        performTrade(accountFrom, accountTo, currencyFrom, currencyTo, amountFrom, amountTo);
    }

    public void execute(String userFrom, String userTo, String currencyFromS, String currencyToS, BigDecimal amountFrom, BigDecimal amountTo){
        Account accountFrom = getAccountsUseCase.getAccount(userFrom);
        Account accountTo = getAccountsUseCase.getAccount(userTo);
        Currency currencyFrom = getCurrencyUseCase.getCurrency(currencyFromS);
        Currency currencyTo = getCurrencyUseCase.getCurrency(currencyToS);

        performTrade(accountFrom, accountTo, currencyFrom, currencyTo, amountFrom, amountTo);
    }

    private void performTrade (Account accountFrom, Account accountTo, Currency currencyFrom, Currency currencyTo, BigDecimal amountFrom, BigDecimal amountTo){
        //todo validar que el monto no sea 0 o negativo

        //preguntar si el receptor puede recibir monedas
//todo: revisar metodos de actualizar valores antes de guardar en db, verificar condiciones de carrera
        accountFrom.trade(accountTo,currencyFrom,currencyTo,amountFrom,amountTo);
        //accountFrom.withdraw(currencyFrom, amountFrom);
        //accountFrom.deposit(currencyTo, amountTo);
        //accountTo.withdraw(currencyTo, amountTo);
        //accountTo.deposit(currencyFrom, amountFrom);

        try {
            dataStore.transfer(accountFrom, accountTo); //actualizo ambos accounts de manera atomica, en una operacion segun hibernate
            if(updateForwarder != null && economyLogger != null) { //todo , lo puse para testear y ommitir esto
                updateForwarder.sendUpdateMessage("account", accountFrom.getUuid().toString());// esto es para bungee
                updateForwarder.sendUpdateMessage("account", accountTo.getUuid().toString());// esto es para bungee
                economyLogger.log("[TRADE] Account: " + accountFrom.getNickname() + " traded " + currencyFrom.format(amountFrom) + " to " + accountTo.getNickname() + " for " + currencyTo.format(amountTo));
            }
        } catch (TransactionException e) {
            throw new TransactionException("Failed to perform trade: " + e.getMessage(), e);
        }
    }
}
