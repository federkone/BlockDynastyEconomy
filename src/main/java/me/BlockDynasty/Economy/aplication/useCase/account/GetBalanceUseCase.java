package me.BlockDynasty.Economy.aplication.useCase.account;

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


    public List<Balance> getBalances(String accountName) {
        Account account = getAccountsUseCase.getAccount(accountName);
        return performGetBalances(account);
    }

    public List<Balance> getBalances(UUID accountUUID) {
        Account account = getAccountsUseCase.getAccount(accountUUID);
        return performGetBalances(account);
    }

    private List<Balance> performGetBalances(Account account) {
        if(account == null) {
            throw new AccountNotFoundException("Account not found");
        }
        if(account.getBalances().isEmpty()) {
            throw new CurrencyNotFoundException("Account has no balances");
        }

        return account.getBalances();
    }
}
