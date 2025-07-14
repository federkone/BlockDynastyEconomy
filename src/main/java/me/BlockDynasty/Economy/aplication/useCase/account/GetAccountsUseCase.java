package me.BlockDynasty.Economy.aplication.useCase.account;

import me.BlockDynasty.Economy.domain.result.ErrorCode;
import me.BlockDynasty.Economy.domain.result.Result;
import me.BlockDynasty.Economy.domain.entities.account.Account;
import me.BlockDynasty.Economy.domain.entities.balance.Balance;
import me.BlockDynasty.Economy.Infrastructure.repository.Exceptions.TransactionException;
import me.BlockDynasty.Economy.domain.persistence.entities.IRepository;
import me.BlockDynasty.Economy.domain.services.IAccountService;
import me.BlockDynasty.Economy.domain.services.ICurrencyService;

import java.util.*;
import java.util.stream.Collectors;

public class GetAccountsUseCase {
    private final IAccountService accountService;
    private final IRepository dataStore;
    private final ICurrencyService currencyService;

    public GetAccountsUseCase(IAccountService accountService, ICurrencyService currencyService, IRepository dataStore) {
        this.accountService = accountService;
        this.dataStore = dataStore;
        this.currencyService = currencyService;
    }

    public Result<Account> getAccount(String name) {
        Account account = this.accountService.getAccountCache(name);
       if(account == null){
            Result<Account> result = this.dataStore.loadAccountByName(name);
            if(result.isSuccess()) {
                account = result.getValue();
                syncWalletWithSystemCurrencies(account);
            }else{
                return Result.failure(result.getErrorMessage(), result.getErrorCode());
            }
       }
       return Result.success(account);
    }

    public Result<Account> getAccount(UUID uuid) {
        Account account =  this.accountService.getAccountCache(uuid);
       if(account == null){
            Result<Account> result = this.dataStore.loadAccountByUuid(uuid.toString());
           if(result.isSuccess()) {
                account = result.getValue();
               syncWalletWithSystemCurrencies(account);
            }else{
                return Result.failure(result.getErrorMessage(), result.getErrorCode());
            }
       }
       return Result.success(account);
    }

    public void syncCacheWithAccount(Account account) {
        UUID uuid = account.getUuid();
        Account cachedAccount = this.accountService.getAccountCache(uuid);
        if (cachedAccount != null) {
            for (Balance updatedBalance : account.getWallet()) {
                Balance cachedBalance = cachedAccount.getBalance(updatedBalance.getCurrency());
                if (cachedBalance != null) {
                    cachedBalance.setAmount(updatedBalance.getAmount());
                } else {
                    cachedAccount.getWallet().add(new Balance(updatedBalance.getCurrency(), updatedBalance.getAmount()));
                }
            }
        }
    }

    public void syncDbWithCache() {
        Collection<Account> accounts = this.accountService.getAccountsCache();
        for (Account account : accounts) {
            syncWalletWithSystemCurrencies(account);
            try {
                dataStore.saveAccount(account);
                System.out.println("Cuenta guardada con el nuevo balance.");
            } catch (TransactionException e) {
                throw new TransactionException("Error in transaction", e);
            }
        }
    } //este metodo se debe eliminar

    public void syncCache(UUID uuid){
        Account accountCache = this.accountService.getAccountCache(uuid);
        Result<Account> result =  this.dataStore.loadAccountByUuid(uuid.toString());
        if (result.isSuccess()){
            if (accountCache != null){
                syncWalletWithSystemCurrencies(result.getValue());
                accountCache.setWallet(result.getValue().getWallet());
            }
        }
    }

    private void syncWalletWithSystemCurrencies(Account account) {
        List<Balance> updatedBalances =  this.currencyService.getCurrencies().stream()
                .map(systemCurrency ->
                        account.getWallet().stream()
                                .filter(balance -> balance.getCurrency().getUuid().equals(systemCurrency.getUuid()))
                                .findFirst() // Busca si ya existe el balance para esta moneda
                                .orElseGet(() -> { // Si no existe, crea un nuevo balance para esta moneda
                                    return new Balance(systemCurrency);
                                }))
                .collect(Collectors.toList());
        account.setWallet(updatedBalances);
    }

   public Result<List<Account>> getTopAccounts(String currency, int limit, int offset) {
       if (limit <= 0) {
           //throw new IllegalArgumentException("Limit must be greater than 0");
           return Result.failure("Limit must be greater than 0", ErrorCode.INVALID_ARGUMENT);
       }
       List<Account> cache =  this.accountService.getAccountsTopList(currency);
       if (!cache.isEmpty() && cache.size() >= limit + offset) {
           //System.out.println("Cache hit");
           return Result.success(cache.subList(offset, Math.min(offset + limit, cache.size())));
       }
       if (!dataStore.isTopSupported()) {
           //throw new RepositoryNotSupportTopException("Repository not support top");
           return Result.failure("Repository not support top", ErrorCode.REPOSITORY_NOT_SUPPORT_TOP);
       }
       List<Account> accounts;
       try {
           //System.out.println("DATABASE HIT");
           accounts =  this.dataStore.getAccountsTopByCurrency(currency, limit, offset);
           for (Account account : accounts) {
               this.accountService.addAccountToTopList(account, currency);
           }
       } catch (TransactionException e) {
           //throw new TransactionException("Error in transaction", e);
           return Result.failure("Error in transaction", ErrorCode.DATA_BASE_ERROR);
       }
       if (accounts.isEmpty()) {
           //throw new AccountNotFoundException("No accounts found for currency: " + currency);
           return Result.failure("No accounts found for currency: "+ currency, ErrorCode.ACCOUNT_NOT_FOUND);
       }
       return Result.success(accounts);
   }

}
