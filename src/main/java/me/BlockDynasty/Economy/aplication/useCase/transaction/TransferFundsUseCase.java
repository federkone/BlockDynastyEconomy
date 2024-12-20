package me.BlockDynasty.Economy.aplication.useCase.transaction;

import me.BlockDynasty.Economy.aplication.result.ErrorCode;
import me.BlockDynasty.Economy.aplication.result.Result;
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
        Result<Void> result = accountFrom.transfer(accountTo,currency,amount);
        if(!result.isSuccess()){
            return result;
        }

        try {
            dataStore.transfer(accountFrom, accountTo);
            if(updateForwarder != null && economyLogger != null){ //todo , lo puse para testear y ommitir esto
                updateForwarder.sendUpdateMessage("account", accountFrom.getUuid().toString());
                updateForwarder.sendUpdateMessage("account", accountTo.getUuid().toString());
                economyLogger.log("[TRANSFER] Account: " + accountFrom.getNickname() + " transferred " +
                        currency.format(amount) + " to " + accountTo.getNickname());
            }
        } catch (TransactionException e) {
            return Result.failure("Failed to perform transfer: " , ErrorCode.DATA_BASE_ERROR);
        }
        return Result.success(null);
    }
}