package me.BlockDynasty.Economy.aplication.useCase.transaction;

import me.BlockDynasty.Economy.aplication.useCase.account.GetAccountsUseCase;
import me.BlockDynasty.Economy.domain.result.ErrorCode;
import me.BlockDynasty.Economy.domain.result.Result;
import me.BlockDynasty.Economy.domain.entities.account.Account;
import me.BlockDynasty.Economy.domain.entities.balance.Balance;

import java.util.List;
import java.util.UUID;

public class GetBalanceUseCase {
    private final GetAccountsUseCase getAccountsUseCase;

    public GetBalanceUseCase(GetAccountsUseCase getAccountsUseCase) {
        this.getAccountsUseCase = getAccountsUseCase;
    }

    public Result<Balance> getBalance(String accountName, String currencyName) {
        Result<Account> accountResult = this.getAccountsUseCase.getAccount(accountName);
        if(!accountResult.isSuccess()) {
            return Result.failure( accountResult.getErrorMessage(),accountResult.getErrorCode());
        }
        return performGetBalance(accountResult.getValue(), currencyName);
    }

    public Result<Balance> getBalance(String accountName) { //default currency
        Result<Account> accountResult = this.getAccountsUseCase.getAccount(accountName);
        if(!accountResult.isSuccess()) {
            return Result.failure( accountResult.getErrorMessage(),accountResult.getErrorCode());
        }
        return performGetBalance(accountResult.getValue());
    }

    public Result<Balance> getBalance(UUID accountUUID, String currencyName) {
        Result<Account> accountResult = this.getAccountsUseCase.getAccount(accountUUID);
        if(!accountResult.isSuccess()) {
            return Result.failure( accountResult.getErrorMessage(),accountResult.getErrorCode());
        }
        return performGetBalance(accountResult.getValue(), currencyName);
    }

    public Result<Balance> getBalance(UUID accountUUID) { //default currency
        Result<Account> accountResult = this.getAccountsUseCase.getAccount(accountUUID);
        if(!accountResult.isSuccess()) {
            return Result.failure( accountResult.getErrorMessage(),accountResult.getErrorCode());
        }
        return performGetBalance(accountResult.getValue());
    }

    public Result<List<Balance>> getBalances(String accountName) {
        Result<Account> accountResult = this.getAccountsUseCase.getAccount(accountName);
        if(!accountResult.isSuccess()) {
            return Result.failure( accountResult.getErrorMessage(),accountResult.getErrorCode());
        }
        return performGetBalances(accountResult.getValue());
    }

    public Result<List<Balance>> getBalances(UUID accountUUID) {
        Result<Account> accountResult = this.getAccountsUseCase.getAccount(accountUUID);
        if(!accountResult.isSuccess()) {
            return Result.failure( accountResult.getErrorMessage(),accountResult.getErrorCode());
        }
        return performGetBalances(accountResult.getValue());
    }

    private Result<Balance> performGetBalance(Account account, String currencyName) {
        if(account.getWallet().isEmpty()) {
            return Result.failure("Account has no balances", ErrorCode.ACCOUNT_NOT_HAVE_BALANCE);
        }
        Balance balance = account.getBalance(currencyName);
        if(balance == null) {
            return Result.failure("Balance not found for currency: " ,ErrorCode.CURRENCY_NOT_FOUND );
        }
        return Result.success(balance);
    }

    private Result<Balance> performGetBalance(Account account) {
        if(account.getWallet().isEmpty()) {
            return Result.failure("Account has no balances", ErrorCode.ACCOUNT_NOT_HAVE_BALANCE);
        }

        Balance balance = account.getBalance();
        if(balance == null) {
            return Result.failure("Balance [currency default] not found for currency: " , ErrorCode.CURRENCY_NOT_FOUND );
        }
        return Result.success(balance);
    }

    private Result<List<Balance>> performGetBalances(Account account) {
        if(account.getWallet().isEmpty()) {
            return Result.failure("Account has no balances", ErrorCode.ACCOUNT_NOT_HAVE_BALANCE);
        }
        return Result.success(account.getWallet());
    }
}
