package me.BlockDynasty.Economy.aplication.useCase.transaction;

import me.BlockDynasty.Economy.domain.result.ErrorCode;
import me.BlockDynasty.Economy.domain.result.Result;
import me.BlockDynasty.Economy.aplication.useCase.currency.GetCurrencyUseCase;
import me.BlockDynasty.Economy.config.logging.AbstractLogger;
import me.BlockDynasty.Economy.domain.account.Account;
import me.BlockDynasty.Economy.aplication.useCase.account.GetAccountsUseCase;
import me.BlockDynasty.Economy.domain.result.TransferResult;
import me.BlockDynasty.Integrations.bungee.UpdateForwarder;
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

    public Result<Void> execute(UUID userFrom, UUID userTo, String currencyFromS, String currencyToS, BigDecimal amountFrom, BigDecimal amountTo){
        Result<Account> accountFromResult = getAccountsUseCase.getAccount(userFrom);
        if (!accountFromResult.isSuccess()) {
            return Result.failure(accountFromResult.getErrorMessage(), accountFromResult.getErrorCode());
        }

        Result<Account> accountToResult = getAccountsUseCase.getAccount(userTo);
        if (!accountToResult.isSuccess()) {
            return Result.failure(accountToResult.getErrorMessage(), accountToResult.getErrorCode());
        }

        Result<Currency> currencyFromResult = getCurrencyUseCase.getCurrency(currencyFromS);
        if (!currencyFromResult.isSuccess()) {
            return Result.failure(currencyFromResult.getErrorMessage(), currencyFromResult.getErrorCode());
        }

        Result<Currency> currencyToResult = getCurrencyUseCase.getCurrency(currencyToS);
        if (!currencyToResult.isSuccess()) {
            return Result.failure(currencyToResult.getErrorMessage(), currencyToResult.getErrorCode());
        }

        return performTrade(accountFromResult.getValue(), accountToResult.getValue(), currencyFromResult.getValue(), currencyToResult.getValue(), amountFrom, amountTo);

    }

    public Result<Void> execute(String userFrom, String userTo, String currencyFromS, String currencyToS, BigDecimal amountFrom, BigDecimal amountTo){
        Result<Account> accountFromResult = getAccountsUseCase.getAccount(userFrom);
        if (!accountFromResult.isSuccess()) {
            return Result.failure(accountFromResult.getErrorMessage(), accountFromResult.getErrorCode());
        }

        Result<Account> accountToResult = getAccountsUseCase.getAccount(userTo);
        if (!accountToResult.isSuccess()) {
            return Result.failure(accountToResult.getErrorMessage(), accountToResult.getErrorCode());
        }

        Result<Currency> currencyFromResult = getCurrencyUseCase.getCurrency(currencyFromS);
        if (!currencyFromResult.isSuccess()) {
            return Result.failure(currencyFromResult.getErrorMessage(), currencyFromResult.getErrorCode());
        }

        Result<Currency> currencyToResult = getCurrencyUseCase.getCurrency(currencyToS);
        if (!currencyToResult.isSuccess()) {
            return Result.failure(currencyToResult.getErrorMessage(), currencyToResult.getErrorCode());
        }

        return performTrade(accountFromResult.getValue(), accountToResult.getValue(), currencyFromResult.getValue(), currencyToResult.getValue(), amountFrom, amountTo);
    }

    private Result<Void> performTrade (Account accountFrom, Account accountTo, Currency currencyFrom, Currency currencyTo, BigDecimal amountFrom, BigDecimal amountTo){

        if(!accountTo.canReceiveCurrency()){
            return Result.failure("Account can't receive currency", ErrorCode.ACCOUNT_CAN_NOT_RECEIVE);
        }

        if(amountFrom.doubleValue() <= 0){
            return Result.failure("Amount must be greater than 0", ErrorCode.INVALID_AMOUNT);
        }
        if(!currencyFrom.isDecimalSupported() && amountFrom.remainder(BigDecimal.ONE).compareTo(BigDecimal.ZERO) != 0){
            return Result.failure("Decimal not supported", ErrorCode.DECIMAL_NOT_SUPPORTED);
        }

        if(amountTo.doubleValue() <= 0){
            return Result.failure("Amount must be greater than 0", ErrorCode.INVALID_AMOUNT);
        }
        if(!currencyTo.isDecimalSupported() && amountTo.remainder(BigDecimal.ONE).compareTo(BigDecimal.ZERO) != 0){
            return Result.failure("Decimal not supported", ErrorCode.DECIMAL_NOT_SUPPORTED);
        }

        Result<TransferResult> result = dataStore.trade( accountFrom.getUuid().toString(), accountTo.getUuid().toString(), currencyFrom, currencyTo, amountFrom, amountTo);
        if(!result.isSuccess()){
            economyLogger.log("[TRADE failed] Account: " + accountFrom.getNickname() + " traded " + currencyFrom.format(amountFrom) + " to " + accountTo.getNickname() + " for " + currencyTo.format(amountTo) + " - Error: " + result.getErrorMessage() + " - Code: " + result.getErrorCode());
            return Result.failure(result.getErrorMessage(), result.getErrorCode());
        }

        getAccountsUseCase.updateAccountCache(result.getValue().getTo());
        getAccountsUseCase.updateAccountCache(result.getValue().getFrom());
        updateForwarder.sendUpdateMessage("account", accountFrom.getUuid().toString());
        updateForwarder.sendUpdateMessage("account", accountTo.getUuid().toString());
        economyLogger.log("[TRADE] Account: " + accountFrom.getNickname() + " traded " + currencyFrom.format(amountFrom) + " to " + accountTo.getNickname() + " for " + currencyTo.format(amountTo));

        return Result.success(null);
    }
}
