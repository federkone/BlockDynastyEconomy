/**
 * Copyright 2025 Federico Barrionuevo "@federkone"
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package BlockDynasty.Economy.aplication.useCase.account;

import BlockDynasty.Economy.domain.result.ErrorCode;
import BlockDynasty.Economy.domain.result.Result;
import BlockDynasty.Economy.domain.entities.account.Account;
import BlockDynasty.Economy.domain.persistence.Exceptions.TransactionException;
import BlockDynasty.Economy.domain.persistence.entities.IRepository;
import BlockDynasty.Economy.domain.services.IAccountService;
import java.util.*;

public class SearchAccountUseCase {
    private final IAccountService accountService;
    private final IRepository dataStore;

    public SearchAccountUseCase(IAccountService accountService, IRepository dataStore) {
        this.accountService = accountService;
        this.dataStore = dataStore;
    }

    /**
     * Retrieves all system accounts.
     *
     * @return Result containing a list of system accounts or an error if none found.
     */
    public Result<List<Account>> getOfflineAccounts() {
        List<Account> accounts = accountService.getAccountsOffline();
        if (accounts.isEmpty()) {
            return Result.failure("No accounts found", ErrorCode.ACCOUNT_NOT_FOUND);
        }
        return Result.success(accounts);
    }

    /**
     * Retrieves an account by its name.
     * @param name the name of the account to retrieve
     * @return Result containing the account if found, or an error if not found
     */
    public Result<Account> getAccount(String name) {
        Account account = this.accountService.getAccount(name);
        if (account != null) {
            return Result.success(account);
        }
        return Result.failure("Account not found", ErrorCode.ACCOUNT_NOT_FOUND);
        //podemos hacer un new Account(account); como programación defensiva, o elaborar un value Object para mostrar valores hacia el exterior
    }

    /**
     * Retrieves an account by its UUID.
     * @param uuid the UUID of the account to retrieve
     * @return Result containing the account if found, or an error if not found
     */
    public Result<Account> getAccount(UUID uuid) {
        Account account =  this.accountService.getAccount(uuid);
        if (account != null) {
            return Result.success(account);
        }
        return Result.failure("Account not found", ErrorCode.ACCOUNT_NOT_FOUND);
         //podemos hacer un new Account(account); como programación defensiva, o elaborar un value Object para mostrar valores hacia el exterior
    }

    /**
     * Retrieves the top accounts for a given currency.
     * If the cache is available and contains enough accounts, it returns the cached list.
     * @param currency the currency for which to retrieve the top accounts
     * @param limit the maximum number of accounts to return
     * @param offset the offset from which to start returning accounts
     * @return Result containing a list of top accounts or an error if none found
     */
   public Result<List<Account>> getTopAccounts(String currency, int limit, int offset) {
       if (limit <= 0) {
           return Result.failure("Limit must be greater than 0", ErrorCode.INVALID_ARGUMENT);
       }
       List<Account> cache =  this.accountService.getAccountsTopList(currency);
       if (!cache.isEmpty() && cache.size() >= limit + offset) {
           //System.out.println("Cache hit");
           return Result.success(cache.subList(offset, Math.min(offset + limit, cache.size())));
       }
       if (!dataStore.isTopSupported()) {
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
           return Result.failure("Error in transaction", ErrorCode.DATA_BASE_ERROR);
       }
       if (accounts.isEmpty()) {
           return Result.failure("No accounts found for currency: "+ currency, ErrorCode.ACCOUNT_NOT_FOUND);
       }
       return Result.success(accounts);
   }

}
