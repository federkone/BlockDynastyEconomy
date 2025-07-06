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

    public Result<Void> execute(UUID userFrom, UUID userTo, String currency, BigDecimal amount) {
        Result<Account> accountFromResult = getAccountsUseCase.getAccount(userFrom);
        if (!accountFromResult.isSuccess()) {
            return Result.failure(accountFromResult.getErrorMessage(), accountFromResult.getErrorCode());
        }

        Result<Account> accountToResult = getAccountsUseCase.getAccount(userTo);
        if (!accountToResult.isSuccess()) {
            return Result.failure(accountToResult.getErrorMessage(), accountToResult.getErrorCode());
        }

        Result<Currency> currencyResult = getCurrencyUseCase.getCurrency(currency);
        if (!currencyResult.isSuccess()) {
            return Result.failure(currencyResult.getErrorMessage(), currencyResult.getErrorCode());
        }


        return performTransfer(accountFromResult.getValue(), accountToResult.getValue(), currencyResult.getValue(), amount);
    }

    public Result<Void> execute (String userFrom, String userTo, String currency, BigDecimal amount) {
        Result<Account> accountFromResult = getAccountsUseCase.getAccount(userFrom);
        if (!accountFromResult.isSuccess()) {
            return Result.failure(accountFromResult.getErrorMessage(), accountFromResult.getErrorCode());
        }

        Result<Account> accountToResult = getAccountsUseCase.getAccount(userTo);
        if (!accountToResult.isSuccess()) {
            return Result.failure(accountToResult.getErrorMessage(), accountToResult.getErrorCode());
        }

        Result<Currency> currencyResult = getCurrencyUseCase.getCurrency(currency);
        if (!currencyResult.isSuccess()) {
            return Result.failure(currencyResult.getErrorMessage(), currencyResult.getErrorCode());
        }

        return performTransfer(accountFromResult.getValue(), accountToResult.getValue(), currencyResult.getValue(), amount);
    }

    private Result<Void> performTransfer(Account accountFrom, Account accountTo, Currency currency, BigDecimal amount){
        //se realizo un cambio de paradigma:
        //se validan opciones estaticas de cache, como monedas y cuentas.
        //y luego se realiza la transaccion, la cual valida de manera atomica, INSUFFICENT:FOUNDS,DATABASE_ERROR.
        //de esta manera tengo un control de errores hibrido, donde dejo de tener en cuenta la caché para hacer la transaccion

        if(amount.doubleValue() <= 0){
            return Result.failure("Amount must be greater than 0", ErrorCode.INVALID_AMOUNT);
        }
        if(!currency.isDecimalSupported() && amount.remainder(BigDecimal.ONE).compareTo(BigDecimal.ZERO) != 0){
            return Result.failure("Decimal not supported", ErrorCode.DECIMAL_NOT_SUPPORTED);
        }
        if (!accountTo.canReceiveCurrency()) {
            return Result.failure("Target account can't receive currency", ErrorCode.ACCOUNT_CAN_NOT_RECEIVE);
        }

        Result<TransferResult> result = dataStore.transfer(accountFrom.getUuid().toString(),accountTo.getUuid().toString(),currency, amount);
        if(!result.isSuccess()){
            economyLogger.log("[TRANSFER Failed] Account: " + accountFrom.getNickname() + " pay " + currency.format(amount) + " to " + accountTo.getNickname() + " Error: " + result.getErrorMessage() + " Code: " + result.getErrorCode());
            return Result.failure(result.getErrorMessage(), result.getErrorCode());
        }

        //actualizar cache con las cuentas contenidas en result
        getAccountsUseCase.updateAccountCache(accountTo);
        getAccountsUseCase.updateAccountCache(accountFrom);

        //.................
        updateForwarder.sendUpdateMessage("account", accountFrom.getUuid().toString());
        updateForwarder.sendUpdateMessage("account", accountTo.getUuid().toString());
        economyLogger.log("[TRANSFER] Account: " + accountFrom.getNickname() + " pay " + currency.format(amount) + " to " + accountTo.getNickname());
        //................

        return Result.success(null);
    }
}