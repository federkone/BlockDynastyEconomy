package me.BlockDynasty.Economy.aplication.useCase.transaction;

import me.BlockDynasty.Economy.domain.result.ErrorCode;
import me.BlockDynasty.Economy.domain.result.Result;
import me.BlockDynasty.Economy.aplication.useCase.currency.GetCurrencyUseCase;
import me.BlockDynasty.Economy.config.logging.AbstractLogger;
import me.BlockDynasty.Economy.domain.account.Account;
import me.BlockDynasty.Economy.aplication.useCase.account.GetAccountsUseCase;
import me.BlockDynasty.Integrations.bungee.UpdateForwarder;
import me.BlockDynasty.Economy.domain.currency.Currency;
import me.BlockDynasty.Economy.domain.repository.Exceptions.TransactionException;
import me.BlockDynasty.Economy.domain.repository.IRepository;

import java.math.BigDecimal;
import java.util.UUID;

//TODO, FUNCIONALIDAD PARA EXTRACCION DE DINERO
public class WithdrawUseCase {
    private final GetCurrencyUseCase getCurrencyUseCase;
    private final IRepository dataStore;
    private final UpdateForwarder updateForwarder;
    private final AbstractLogger logger;
    private final GetAccountsUseCase getAccountsUseCase;

    public WithdrawUseCase(GetCurrencyUseCase getCurrencyUseCase, GetAccountsUseCase getAccountsUseCase, IRepository dataStore, UpdateForwarder updateForwarder, AbstractLogger logger){
        this.getCurrencyUseCase = getCurrencyUseCase;
        this.dataStore = dataStore;
        this.updateForwarder = updateForwarder;
        this.logger = logger;
        this.getAccountsUseCase = getAccountsUseCase;
    }

    public Result<Void> execute(UUID targetUUID, String currencyName, BigDecimal amount) {
        Result<Account> accountResult = getAccountsUseCase.getAccount(targetUUID);
        if (!accountResult.isSuccess()) {
            return Result.failure(accountResult.getErrorMessage(), accountResult.getErrorCode());
        }
        return execute(accountResult.getValue(), currencyName, amount);
    }

    public Result<Void> execute(String targetName, String currencyName, BigDecimal amount) {
        Result<Account> accountResult = getAccountsUseCase.getAccount(targetName);
        if (!accountResult.isSuccess()) {
            return Result.failure(accountResult.getErrorMessage(), accountResult.getErrorCode());
        }
        return execute(accountResult.getValue(), currencyName, amount);
    }

    public Result<Void> execute(UUID targetUUID, BigDecimal amount) {
        return execute(targetUUID, null, amount);
    }

    public Result<Void> execute(String targetName, BigDecimal amount) {
        return execute(targetName, null, amount);
    }

    private Result<Currency> getCurrency(String currencyName) {
        if (currencyName == null) {
            return getCurrencyUseCase.getDefaultCurrency();
        }
        return getCurrencyUseCase.getCurrency(currencyName);
    }

    private Result<Void> execute(Account account, String currencyName, BigDecimal amount) {
        Result<Currency> currencyResult = getCurrency(currencyName);
        if (!currencyResult.isSuccess()) {
            return Result.failure(currencyResult.getErrorMessage(), currencyResult.getErrorCode());
        }
        return performWithdraw(account, currencyResult.getValue(), amount);
    }

    private Result<Void> performWithdraw(Account account, Currency currency, BigDecimal amount) {
        if(amount.doubleValue() <= 0){
            return Result.failure("Amount must be greater than 0", ErrorCode.INVALID_AMOUNT);
        }
        if(!currency.isDecimalSupported() && amount.remainder(BigDecimal.ONE).compareTo(BigDecimal.ZERO) != 0){
            return Result.failure("Decimal not supported", ErrorCode.DECIMAL_NOT_SUPPORTED);
        }

        Result<Account> result = dataStore.withdraw(account.getUuid().toString(), currency, amount);
        if(!result.isSuccess()){
            logger.log("[WITHDRAW Failure] Account: " + account.getNickname() + " extrajo " + currency.format(amount) + " de " + currency.getSingular()+ " - Error: " + result.getErrorMessage() + " - Code: " + result.getErrorCode());
            return Result.failure( result.getErrorMessage(), result.getErrorCode());
        }

        getAccountsUseCase.updateAccountCache(result.getValue());
        updateForwarder.sendUpdateMessage("account", account.getUuid().toString());
        logger.log("[WITHDRAW] Account: " + account.getNickname() + " extrajo " + currency.format(amount) + " de " + currency.getSingular());

    return Result.success(null);
    }
}