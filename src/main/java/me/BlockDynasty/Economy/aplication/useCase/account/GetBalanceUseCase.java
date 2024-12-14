package me.BlockDynasty.Economy.aplication.useCase.account;

import me.BlockDynasty.Economy.aplication.result.ErrorCode;
import me.BlockDynasty.Economy.aplication.result.Result;
import me.BlockDynasty.Economy.domain.account.Account;
import me.BlockDynasty.Economy.domain.account.Exceptions.AccountNotFoundException;
import me.BlockDynasty.Economy.domain.balance.Balance;
import me.BlockDynasty.Economy.domain.currency.Currency;
import me.BlockDynasty.Economy.domain.currency.Exceptions.CurrencyNotFoundException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class GetBalanceUseCase {
    private final GetAccountsUseCase getAccountsUseCase;

    public GetBalanceUseCase(GetAccountsUseCase getAccountsUseCase) {

        this.getAccountsUseCase = getAccountsUseCase;
    }


    public Result<List<Balance>> getBalances(String accountName) {
        //Account account = getAccountsUseCase.getAccount(accountName);
        Result<Account> accountResult = getAccountsUseCase.getAccount(accountName);
        if(!accountResult.isSuccess()) {
            return Result.failure( accountResult.getErrorMessage(),accountResult.getErrorCode());
        }
        return performGetBalances(accountResult.getValue());
    }

    public Result<List<Balance>> getBalances(UUID accountUUID) {
        //Account account = getAccountsUseCase.getAccount(accountUUID);
        Result<Account> accountResult = getAccountsUseCase.getAccount(accountUUID);
        if(!accountResult.isSuccess()) {
            return Result.failure( accountResult.getErrorMessage(),accountResult.getErrorCode());
        }
        return performGetBalances(accountResult.getValue());
    }

    private Result<List<Balance>> performGetBalances(Account account) {
        if(account.getBalances().isEmpty()) {
            return Result.failure("Account has no balances", ErrorCode.ACCOUNT_NOT_HAVE_BALANCE);
        }

        return Result.success(account.getBalances());
    }
}
