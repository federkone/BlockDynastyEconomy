package me.BlockDynasty.Economy.aplication.useCase.transaction;

import me.BlockDynasty.Economy.Infrastructure.BukkitImplementation.Integrations.bungee.Courier;
import me.BlockDynasty.Economy.Infrastructure.BukkitImplementation.config.log.Log;
import me.BlockDynasty.Economy.domain.result.ErrorCode;
import me.BlockDynasty.Economy.domain.result.Result;
import me.BlockDynasty.Economy.aplication.useCase.account.GetAccountsUseCase;
import me.BlockDynasty.Economy.aplication.useCase.currency.GetCurrencyUseCase;
import me.BlockDynasty.Economy.domain.account.Account;
import me.BlockDynasty.Economy.domain.currency.Currency;
import me.BlockDynasty.Economy.domain.persistence.entities.IRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.UUID;


// SE ENCARGA DE ACTUALIZAR LOS MONTOS DE CUENTA Y DE INTENTAR SALVAR EN LA DB
//TODO: aqui se puede agregar el impuesto por cambio de divisa segun el rate de la moneda
//todo: exchange permite a persona cambiar sus monedas con si mismo
//todo: se supone que tampoco deberia validar si la moneda es pagable o no, ya que es un exchange
//todo: falta agregar validaciones de decimal support etc
public class ExchangeUseCase {
    private  final GetCurrencyUseCase getCurrencyUseCase;
    private final IRepository dataStore;
    private final Courier updateForwarder;
    private final Log economyLogger;
    private final GetAccountsUseCase getAccountsUseCase;
    public ExchangeUseCase(GetCurrencyUseCase getCurrencyUseCase,GetAccountsUseCase getAccountsUseCase, IRepository dataStore,
                           Courier updateForwarder, Log economyLogger) {
        this.getCurrencyUseCase = getCurrencyUseCase;
        this.dataStore = dataStore;
        this.updateForwarder = updateForwarder;
        this.economyLogger = economyLogger;
        this.getAccountsUseCase = getAccountsUseCase;
    }

    public Result<BigDecimal> execute(UUID accountUuid, String currencyFromName, String currencyToname, BigDecimal amountFrom, BigDecimal amountTo) {

        Result<Account> accountResult = this.getAccountsUseCase.getAccount(accountUuid);

        if (!accountResult.isSuccess()) {
            return Result.failure(accountResult.getErrorMessage(), accountResult.getErrorCode());
        }

        Result<Currency> currencyFromResult = this.getCurrencyUseCase.getCurrency(currencyFromName);
        if (!currencyFromResult.isSuccess()) {
            return Result.failure(currencyFromResult.getErrorMessage(), currencyFromResult.getErrorCode());
        }

        Result<Currency> currencyToResult = this.getCurrencyUseCase.getCurrency(currencyToname);
        if (!currencyToResult.isSuccess()) {
            return Result.failure(currencyToResult.getErrorMessage(), currencyToResult.getErrorCode());
        }

        return performExchange(accountResult.getValue(), currencyFromResult.getValue(), currencyToResult.getValue(), amountFrom, amountTo);
    }

    public Result<BigDecimal> execute(String accountString, String currencyFromName, String currencyToname, BigDecimal amountFrom, BigDecimal amountTo) {
        Result<Account> accountResult = this.getAccountsUseCase.getAccount(accountString);
        if (!accountResult.isSuccess()) {
            return Result.failure(accountResult.getErrorMessage(), accountResult.getErrorCode());
        }

        Result<Currency> currencyFromResult = this.getCurrencyUseCase.getCurrency(currencyFromName);
        if (!currencyFromResult.isSuccess()) {
            return Result.failure(currencyFromResult.getErrorMessage(), currencyFromResult.getErrorCode());
        }

        Result<Currency> currencyToResult = this.getCurrencyUseCase.getCurrency(currencyToname);
        if (!currencyToResult.isSuccess()) {
            return Result.failure(currencyToResult.getErrorMessage(), currencyToResult.getErrorCode());
        }

        return performExchange(accountResult.getValue(), currencyFromResult.getValue(), currencyToResult.getValue(), amountFrom, amountTo);
    }

    private Result<BigDecimal> performExchange(Account account, Currency currencyFrom, Currency currencyTo, BigDecimal amountFrom, BigDecimal amountTo){
        if (!account.canReceiveCurrency()) {
            return Result.failure("Target account can't receive currency", ErrorCode.ACCOUNT_CAN_NOT_RECEIVE);
        }

        if(!currencyTo.isValidAmount(amountTo)){
            return Result.failure("Decimal not supported", ErrorCode.DECIMAL_NOT_SUPPORTED);
        }

        if (amountFrom == null) { //calculo atumatico segun el ratio
            amountFrom = amountTo.multiply(BigDecimal.valueOf(currencyFrom.getExchangeRate()))
                    .divide(BigDecimal.valueOf(currencyTo.getExchangeRate()), 4, RoundingMode.HALF_UP);
        }

        if (!currencyFrom.isValidAmount(amountFrom)) {
            return Result.failure("Decimal not supported for currency ", ErrorCode.DECIMAL_NOT_SUPPORTED);
        }

        Result<Account>  result =this.dataStore.exchange(account.getUuid().toString(),currencyFrom,amountFrom,currencyTo,amountTo);

        if(!result.isSuccess()){
            this.economyLogger.log("[EXCHANGE failed] Account: " + account.getNickname() + " exchanged " + currencyFrom.format(amountFrom) + " to " + currencyTo.format(amountTo) + " Error: " + result.getErrorMessage() + " Code: " + result.getErrorCode());
            return Result.failure(result.getErrorMessage(), result.getErrorCode());
        }

        this.getAccountsUseCase.updateAccountCache(result.getValue());
        this.updateForwarder.sendUpdateMessage("account", account.getUuid().toString());// esto es para bungee
        this.economyLogger.log("[EXCHANGE] Account: " + account.getNickname() + " exchanged " + currencyFrom.format(amountFrom) + " to " + currencyTo.format(amountTo));

        return Result.success(amountFrom);
    }
}
