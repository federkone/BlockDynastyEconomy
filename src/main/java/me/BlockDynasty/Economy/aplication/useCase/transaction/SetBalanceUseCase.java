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

public class SetBalanceUseCase {
    private final GetCurrencyUseCase getCurrencyUseCase;
    private final IRepository dataStore;
    private final UpdateForwarder updateForwarder;
    private final AbstractLogger economyLogger;
    private final GetAccountsUseCase getAccountsUseCase;

    public SetBalanceUseCase(GetCurrencyUseCase getCurrencyUseCase, GetAccountsUseCase getAccountsUseCase,IRepository dataStore,
                             UpdateForwarder updateForwarder, AbstractLogger economyLogger){

        this.getCurrencyUseCase = getCurrencyUseCase;
        this.dataStore = dataStore;
        this.updateForwarder = updateForwarder;
        this.economyLogger = economyLogger;
        this.getAccountsUseCase = getAccountsUseCase;

    }

    public Result<Void> execute(UUID targetUUID, String currencyName, BigDecimal amount) {
        Result<Account> accountResult = getAccountsUseCase.getAccount(targetUUID);
        if (!accountResult.isSuccess()) {
            return Result.failure(accountResult.getErrorMessage(), accountResult.getErrorCode());
        }

        Result<Currency> currencyResult = getCurrencyUseCase.getCurrency(currencyName);
        if (!currencyResult.isSuccess()) {
            return Result.failure(currencyResult.getErrorMessage(), currencyResult.getErrorCode());
        }

        return performSet(accountResult.getValue(), currencyResult.getValue(), amount);
    }

    public Result<Void> execute(String targetName, String currencyName, BigDecimal amount) {
        Result<Account> accountResult = getAccountsUseCase.getAccount(targetName);
        if (!accountResult.isSuccess()) {
            return Result.failure(accountResult.getErrorMessage(), accountResult.getErrorCode());
        }

        Result<Currency> currencyResult = getCurrencyUseCase.getCurrency(currencyName);
        if (!currencyResult.isSuccess()) {
            return Result.failure(currencyResult.getErrorMessage(), currencyResult.getErrorCode());
        }

        return performSet(accountResult.getValue(), currencyResult.getValue(), amount); //return performSet(accountResult.getValue(), currencyResult.getValue(), amount);
    }

    private Result<Void> performSet(Account account, Currency currency, BigDecimal amount) {
        if(amount.doubleValue() < 0){
            return Result.failure("Amount must be greater than 0", ErrorCode.INVALID_AMOUNT);
        }

        if(!currency.isDecimalSupported() && amount.remainder(BigDecimal.ONE).compareTo(BigDecimal.ZERO) != 0){
            return Result.failure("Decimal not supported", ErrorCode.DECIMAL_NOT_SUPPORTED);
        }

        Result<Account> result = dataStore.setBalance(account.getUuid().toString(), currency, amount);
        if (!result.isSuccess()) {
            economyLogger.log("[BALANCE SET failed] Account: " + account.getNickname() + " were set to: " + currency.format(amount) + " Error: " + result.getErrorMessage() + " Code: " + result.getErrorCode());
            return Result.failure(result.getErrorMessage(), result.getErrorCode());
        }

        getAccountsUseCase.updateAccountCache(result.getValue());
        updateForwarder.sendUpdateMessage("account", account.getUuid().toString());
        economyLogger.log("[BALANCE SET] Account: " + account.getNickname() + " were set to: " + currency.format(amount));

        return Result.success(null);
    }
}
