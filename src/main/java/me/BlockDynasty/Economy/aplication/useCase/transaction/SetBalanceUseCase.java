package me.BlockDynasty.Economy.aplication.useCase.transaction;

import me.BlockDynasty.Economy.domain.services.courier.Courier;
import me.BlockDynasty.Economy.domain.services.log.Log;
import me.BlockDynasty.Economy.domain.result.ErrorCode;
import me.BlockDynasty.Economy.domain.result.Result;
import me.BlockDynasty.Economy.aplication.useCase.currency.GetCurrencyUseCase;
import me.BlockDynasty.Economy.domain.entities.account.Account;
import me.BlockDynasty.Economy.aplication.useCase.account.GetAccountsUseCase;
import me.BlockDynasty.Economy.domain.entities.currency.Currency;
import me.BlockDynasty.Economy.domain.persistence.entities.IRepository;

import java.math.BigDecimal;
import java.util.UUID;

public class SetBalanceUseCase {
    private final GetCurrencyUseCase getCurrencyUseCase;
    private final IRepository dataStore;
    private final Courier updateForwarder;
    private final Log economyLogger;
    private final GetAccountsUseCase getAccountsUseCase;

    public SetBalanceUseCase(GetCurrencyUseCase getCurrencyUseCase, GetAccountsUseCase getAccountsUseCase,IRepository dataStore,
                             Courier updateForwarder, Log economyLogger){

        this.getCurrencyUseCase = getCurrencyUseCase;
        this.dataStore = dataStore;
        this.updateForwarder = updateForwarder;
        this.economyLogger = economyLogger;
        this.getAccountsUseCase = getAccountsUseCase;
    }

    public Result<Void> execute(UUID targetUUID, String currencyName, BigDecimal amount) {
        Result<Account> accountResult =  this.getAccountsUseCase.getAccount(targetUUID);
        if (!accountResult.isSuccess()) {
            //messageService.sendMessage(targetUUID, accountResult);
            return Result.failure(accountResult.getErrorMessage(), accountResult.getErrorCode());
        }

        Result<Currency> currencyResult =  this.getCurrencyUseCase.getCurrency(currencyName);
        if (!currencyResult.isSuccess()) {
            //messageService.sendMessage(targetUUID, currencyResult);
            return Result.failure(currencyResult.getErrorMessage(), currencyResult.getErrorCode());
        }

        return performSet(accountResult.getValue(), currencyResult.getValue(), amount);
    }

    public Result<Void> execute(String targetName, String currencyName, BigDecimal amount) {
        Result<Account> accountResult =  this.getAccountsUseCase.getAccount(targetName);
        if (!accountResult.isSuccess()) {
            //messageService.sendMessage(targetName, accountResult);
            return Result.failure(accountResult.getErrorMessage(), accountResult.getErrorCode());
        }

        Result<Currency> currencyResult =  this.getCurrencyUseCase.getCurrency(currencyName);
        if (!currencyResult.isSuccess()) {
            //messageService.sendMessage(currencyName, currencyResult);
            return Result.failure(currencyResult.getErrorMessage(), currencyResult.getErrorCode());
        }

        return performSet(accountResult.getValue(), currencyResult.getValue(), amount); //return performSet(accountResult.getValue(), currencyResult.getValue(), amount);
    }

    private Result<Void> performSet(Account account, Currency currency, BigDecimal amount) {
        if(amount.compareTo(BigDecimal.ZERO) < 0){
            //messageService.sendMessage(currency, ErrorCode.INVALID_AMOUNT + ": Amount must be greater than -1");
            return Result.failure("Amount must be greater than -1", ErrorCode.INVALID_AMOUNT);
        }

        if(!currency.isValidAmount(amount)){
            //messageService.sendMessage(currency, ErrorCode.DECIMAL_NOT_SUPPORTED + ": Decimal not supported");
            return Result.failure("Decimal not supported", ErrorCode.DECIMAL_NOT_SUPPORTED);
        }

        Result<Account> result =  this.dataStore.setBalance(account.getUuid().toString(), currency, amount);
        if (!result.isSuccess()) {
            //messageService.sendMessage(account, result);
            this.economyLogger.log("[BALANCE SET failed] Account: " + account.getNickname() + " were set to: " + currency.format(amount) + " Error: " + result.getErrorMessage() + " Code: " + result.getErrorCode());
            return Result.failure(result.getErrorMessage(), result.getErrorCode());
        }

        this.getAccountsUseCase.updateAccountCache(result.getValue());
        //messageService.serndMessage(account,currency,amount ErrorCode.SET_BALANCE_SUCCESS);
        this.updateForwarder.sendUpdateMessage("account", account.getUuid().toString());
        this.economyLogger.log("[BALANCE SET] Account: " + account.getNickname() + " were set to: " + currency.format(amount));

        return Result.success(null);
    }
}
