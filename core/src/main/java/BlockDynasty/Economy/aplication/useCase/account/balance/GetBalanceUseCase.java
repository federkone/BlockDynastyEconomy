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

package BlockDynasty.Economy.aplication.useCase.account.balance;

import BlockDynasty.Economy.aplication.useCase.account.SearchAccountUseCase;
import BlockDynasty.Economy.domain.entities.balance.Money;
import BlockDynasty.Economy.domain.result.ErrorCode;
import BlockDynasty.Economy.domain.result.Result;
import BlockDynasty.Economy.domain.entities.account.Account;

import java.util.List;
import java.util.UUID;

public class GetBalanceUseCase {
    private final SearchAccountUseCase searchAccountUseCase;

    public GetBalanceUseCase(SearchAccountUseCase searchAccountUseCase) {
        this.searchAccountUseCase = searchAccountUseCase;
    }

    public Result<Money> execute(String accountName, String currencyName) {
        Result<Account> accountResult = this.searchAccountUseCase.getAccount(accountName);
        if(!accountResult.isSuccess()) {
            return Result.failure( accountResult.getErrorMessage(),accountResult.getErrorCode());
        }
        return performGetBalance(accountResult.getValue(), currencyName);
    }

    public Result<Money> execute(String accountName) { //default currency
        Result<Account> accountResult = this.searchAccountUseCase.getAccount(accountName);
        if(!accountResult.isSuccess()) {
            return Result.failure( accountResult.getErrorMessage(),accountResult.getErrorCode());
        }
        return performGetBalance(accountResult.getValue());
    }

    public Result<Money> execute(UUID accountUUID, String currencyName) {
        Result<Account> accountResult = this.searchAccountUseCase.getAccount(accountUUID);
        if(!accountResult.isSuccess()) {
            return Result.failure( accountResult.getErrorMessage(),accountResult.getErrorCode());
        }
        return performGetBalance(accountResult.getValue(), currencyName);
    }

    public Result<Money> execute(UUID accountUUID) { //default currency
        Result<Account> accountResult = this.searchAccountUseCase.getAccount(accountUUID);
        if(!accountResult.isSuccess()) {
            return Result.failure( accountResult.getErrorMessage(),accountResult.getErrorCode());
        }
        return performGetBalance(accountResult.getValue());
    }

    public Result<List<Money>> getBalances(String accountName) {
        Result<Account> accountResult = this.searchAccountUseCase.getAccount(accountName);
        if(!accountResult.isSuccess()) {
            return Result.failure( accountResult.getErrorMessage(),accountResult.getErrorCode());
        }
        return performGetBalances(accountResult.getValue());
    }

    public Result<List<Money>> getBalances(UUID accountUUID) {
        Result<Account> accountResult = this.searchAccountUseCase.getAccount(accountUUID);
        if(!accountResult.isSuccess()) {
            return Result.failure( accountResult.getErrorMessage(),accountResult.getErrorCode());
        }
        return performGetBalances(accountResult.getValue());
    }

    private Result<Money> performGetBalance(Account account, String currencyName) {
        if(account.getBalances().isEmpty()) {
            return Result.failure("Account has no balances", ErrorCode.ACCOUNT_NOT_HAVE_BALANCE);
        }
        Money money = account.getMoney(currencyName);
        if(money == null) {
            return Result.failure("Balance not found for currency: " ,ErrorCode.CURRENCY_NOT_FOUND );
        }
        return Result.success(money);
    }

    private Result<Money> performGetBalance(Account account) {
        if(account.getBalances().isEmpty()) {
            return Result.failure("Account has no balances", ErrorCode.ACCOUNT_NOT_HAVE_BALANCE);
        }

        Money money = account.getMoney();
        if(money == null) {
            return Result.failure("Balance [currency default] not found for currency: " , ErrorCode.CURRENCY_NOT_FOUND );
        }
        return Result.success(money);
    }

    private Result<List<Money>> performGetBalances(Account account) {
        if(account.getBalances().isEmpty()) {
            return Result.failure("Account has no balances", ErrorCode.ACCOUNT_NOT_HAVE_BALANCE);
        }
        return Result.success(account.getBalances());
    }
}
