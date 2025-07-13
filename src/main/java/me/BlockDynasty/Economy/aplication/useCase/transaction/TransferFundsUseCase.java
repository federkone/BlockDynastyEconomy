package me.BlockDynasty.Economy.aplication.useCase.transaction;

import me.BlockDynasty.Economy.domain.services.courier.Courier;
import me.BlockDynasty.Economy.domain.services.log.Log;
import me.BlockDynasty.Economy.domain.result.ErrorCode;
import me.BlockDynasty.Economy.domain.result.Result;
import me.BlockDynasty.Economy.aplication.useCase.currency.GetCurrencyUseCase;
import me.BlockDynasty.Economy.domain.entities.account.Account;
import me.BlockDynasty.Economy.aplication.useCase.account.GetAccountsUseCase;
import me.BlockDynasty.Economy.domain.result.TransferResult;
import me.BlockDynasty.Economy.domain.entities.currency.Currency;
import me.BlockDynasty.Economy.domain.persistence.entities.IRepository;

import java.math.BigDecimal;
import java.util.UUID;

public class TransferFundsUseCase {
    private final IRepository dataStore;
    private final Courier updateForwarder;
    private final Log economyLogger;
    private final GetAccountsUseCase getAccountsUseCase;
    private final GetCurrencyUseCase getCurrencyUseCase;

    public TransferFundsUseCase(GetCurrencyUseCase getCurrencyUseCase, GetAccountsUseCase getAccountsUseCase, IRepository dataStore,
                                Courier updateForwarder, Log economyLogger) {
        this.getCurrencyUseCase = getCurrencyUseCase;
        this.dataStore = dataStore;
        this.updateForwarder = updateForwarder;
        this.economyLogger = economyLogger;
        this.getAccountsUseCase = getAccountsUseCase;
    }

    public Result<Void> execute(UUID userFrom, UUID userTo, String currency, BigDecimal amount) {
        Result<Account> accountFromResult = this.getAccountsUseCase.getAccount(userFrom);
        if (!accountFromResult.isSuccess()) {
            // messageservice.sendMessage(userFrom, accountFromResult.getErrorMessage(), accountFromResult.getErrorCode());
            return Result.failure(accountFromResult.getErrorMessage(), accountFromResult.getErrorCode());
        }

        Result<Account> accountToResult = this.getAccountsUseCase.getAccount(userTo);
        if (!accountToResult.isSuccess()) {
            // messageservice.sendMessage(userTo, accountToResult.getErrorMessage(), accountToResult.getErrorCode());
            return Result.failure(accountToResult.getErrorMessage(), accountToResult.getErrorCode());
        }

        Result<Currency> currencyResult = this.getCurrencyUseCase.getCurrency(currency);
        if (!currencyResult.isSuccess()) {
            // messageservice.sendMessage(currency, currencyResult.getErrorMessage(), currencyResult.getErrorCode());
            return Result.failure(currencyResult.getErrorMessage(), currencyResult.getErrorCode());
        }


        return performTransfer(accountFromResult.getValue(), accountToResult.getValue(), currencyResult.getValue(), amount);
    }

    public Result<Void> execute (String userFrom, String userTo, String currency, BigDecimal amount) {
        Result<Account> accountFromResult = this.getAccountsUseCase.getAccount(userFrom);
        if (!accountFromResult.isSuccess()) {
            // messageservice.sendMessage(userFrom, accountFromResult.getErrorMessage(), accountFromResult.getErrorCode());
            return Result.failure(accountFromResult.getErrorMessage(), accountFromResult.getErrorCode());
        }

        Result<Account> accountToResult = this.getAccountsUseCase.getAccount(userTo);
        if (!accountToResult.isSuccess()) {
            // messageservice.sendMessage(userTo, accountToResult.getErrorMessage(), accountToResult.getErrorCode());
            return Result.failure(accountToResult.getErrorMessage(), accountToResult.getErrorCode());
        }

        Result<Currency> currencyResult = this.getCurrencyUseCase.getCurrency(currency);
        if (!currencyResult.isSuccess()) {
            // messageservice.sendMessage(currency, currencyResult.getErrorMessage(), currencyResult.getErrorCode());
            return Result.failure(currencyResult.getErrorMessage(), currencyResult.getErrorCode());
        }

        return performTransfer(accountFromResult.getValue(), accountToResult.getValue(), currencyResult.getValue(), amount);
    }

    private Result<Void> performTransfer(Account accountFrom, Account accountTo, Currency currency, BigDecimal amount){
        if (!accountTo.canReceiveCurrency()) {
            //messageservice.sendMessage( accountTo,currency,ErrorCode.ACCOUNT_CAN_NOT_RECEIVE, "Target account can't receive currency");
            return Result.failure("Target account can't receive currency", ErrorCode.ACCOUNT_CAN_NOT_RECEIVE);
        }

        if(amount.compareTo(BigDecimal.ZERO) <= 0){
            //messageservice.sendMessage( accountFrom,currency,ErrorCode.INVALID_AMOUNT, "Amount must be greater than 0");
            return Result.failure("Amount must be greater than 0", ErrorCode.INVALID_AMOUNT);
        }
        if(!currency.isValidAmount(amount)){
            //messageservice.sendMessage( currency,amount,ErrorCode.DECIMAL_NOT_SUPPORTED, "Decimal not supported");
            return Result.failure("Decimal not supported", ErrorCode.DECIMAL_NOT_SUPPORTED);
        }
        
        Result<TransferResult> result = this.dataStore.transfer(accountFrom.getUuid().toString(),accountTo.getUuid().toString(),currency, amount);
        if(!result.isSuccess()){
            //messageservice.sendMessage( TransferResult,currency,result.getErrorCode(), "Transfer failed: " + result.getErrorMessage());
            this.economyLogger.log("[TRANSFER Failed] Account: " + accountFrom.getNickname() + " pay " + currency.format(amount) + " to " + accountTo.getNickname() + " Error: " + result.getErrorMessage() + " Code: " + result.getErrorCode());
            return Result.failure(result.getErrorMessage(), result.getErrorCode());
        }

        //actualizar cache con las cuentas contenidas en result
        this.getAccountsUseCase.updateAccountCache(accountTo);
        this.getAccountsUseCase.updateAccountCache(accountFrom);

        //messageService.sendMessage(TransferResult, currency, amount, "transfer successful");
        //.................
        this.updateForwarder.sendUpdateMessage("account", accountFrom.getUuid().toString());
        this.updateForwarder.sendUpdateMessage("account", accountTo.getUuid().toString());
        this.economyLogger.log("[TRANSFER] Account: " + accountFrom.getNickname() + " pay " + currency.format(amount) + " to " + accountTo.getNickname());
        //................

        return Result.success(null);
    }
}