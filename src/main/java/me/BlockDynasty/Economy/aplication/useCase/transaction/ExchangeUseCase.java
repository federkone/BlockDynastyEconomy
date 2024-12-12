package me.BlockDynasty.Economy.aplication.useCase.transaction;

import me.BlockDynasty.Economy.aplication.useCase.account.GetAccountsUseCase;
import me.BlockDynasty.Economy.aplication.useCase.currency.GetCurrencyUseCase;
import me.BlockDynasty.Economy.config.logging.AbstractLogger;
import me.BlockDynasty.Economy.domain.account.Account;
import me.BlockDynasty.Economy.aplication.bungee.UpdateForwarder;
import me.BlockDynasty.Economy.domain.currency.Currency;
import me.BlockDynasty.Economy.domain.repository.Exceptions.TransactionException;
import me.BlockDynasty.Economy.domain.repository.IRepository;

import java.math.BigDecimal;
import java.util.UUID;


// SE ENCARGA DE ACTUALIZAR LOS MONTOS DE CUENTA Y DE INTENTAR SALVAR EN LA DB
//TODO: aqui se puede agregar el impuesto por cambio de divisa segun el rate de la moneda
//todo: exchange permite a persona cambiar sus monedas con si mismo
//todo: se supone que tampoco deberia validar si la moneda es pagable o no, ya que es un exchange
//todo: falta agregar validaciones de decimal support etc
public class ExchangeUseCase {
    private  final GetCurrencyUseCase getCurrencyUseCase;
    private final IRepository dataStore;
    private final UpdateForwarder updateForwarder;
    private final AbstractLogger economyLogger;
    private final GetAccountsUseCase getAccountsUseCase;
    public ExchangeUseCase(GetCurrencyUseCase getCurrencyUseCase,GetAccountsUseCase getAccountsUseCase, IRepository dataStore,
                           UpdateForwarder updateForwarder, AbstractLogger economyLogger) {
        this.getCurrencyUseCase = getCurrencyUseCase;
        this.dataStore = dataStore;
        this.updateForwarder = updateForwarder;
        this.economyLogger = economyLogger;
        this.getAccountsUseCase = getAccountsUseCase;
    }

    public void execute(UUID accountUuid, String currencyFromName, String currencyToname, BigDecimal amountFrom, BigDecimal amountTo) {
        Account account = getAccountsUseCase.getAccount(accountUuid);
        Currency currencyFrom = getCurrencyUseCase.getCurrency(currencyFromName);
        Currency currencyTo = getCurrencyUseCase.getCurrency(currencyToname);

        performExchange(account,currencyFrom,currencyTo,amountFrom,amountTo);

    }

    public void execute(String accountString, String currencyFromName, String currencyToname, BigDecimal amountFrom, BigDecimal amountTo) {
        Account account = getAccountsUseCase.getAccount(accountString);
        Currency currencyFrom = getCurrencyUseCase.getCurrency(currencyFromName);
        Currency currencyTo = getCurrencyUseCase.getCurrency(currencyToname);

        performExchange(account,currencyFrom,currencyTo,amountFrom,amountTo);
    }

    //todo, probablemente me convenga reutilizar el caso de uso TradeCurrency, solo que se hace el trade con sigo mismo, por ej: account, account,currencyFrom,currencyTo,amountFrom,amountTo
    private void performExchange(Account account, Currency currencyFrom, Currency currencyTo, BigDecimal amountFrom, BigDecimal amountTo){


        //TODO:aca se puede calcular el ratio de impuesto a cobrar antes de preguntar si tiene suficiente fondos

        //todo: revisar metodos de actualizar valores antes de guardar en db, verificar condiciones de carrera
        account.withdraw(currencyFrom, amountFrom);
        account.deposit(currencyTo, amountTo);

        try {
            dataStore.saveAccount(account);
            updateForwarder.sendUpdateMessage("account", account.getUuid().toString());// esto es para bungee
            economyLogger.log("[EXCHANGE] Account: " + account.getNickname() + " exchanged " + currencyFrom.format(amountFrom) + " to " + currencyTo.format(amountTo));
        } catch (TransactionException e) {
            // Manejo de errores (puedes lanzar la excepción o manejarla de otra manera)
            throw new TransactionException("Failed to perform exchange: " + e.getMessage(), e);
        }
    }
}
