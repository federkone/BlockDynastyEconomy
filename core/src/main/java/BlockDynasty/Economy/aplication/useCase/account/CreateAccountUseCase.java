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

import BlockDynasty.Economy.aplication.useCase.account.getAccountUseCase.GetAccountByUUIDUseCase;
import BlockDynasty.Economy.domain.entities.balance.Money;
import BlockDynasty.Economy.domain.result.ErrorCode;
import BlockDynasty.Economy.domain.result.Result;
import BlockDynasty.Economy.domain.entities.account.Account;
import BlockDynasty.Economy.domain.persistence.Exceptions.TransactionException;
import BlockDynasty.Economy.domain.persistence.entities.IRepository;
import BlockDynasty.Economy.domain.services.IAccountService;
import BlockDynasty.Economy.domain.services.ICurrencyService;

import java.util.UUID;
import java.util.List;
import java.util.stream.Collectors;

public class CreateAccountUseCase{
    private final IAccountService accountService;
    private final ICurrencyService currencyService;
    private final IRepository dataStore;
    private final GetAccountByUUIDUseCase getAccountByUUIDUseCase;


    public CreateAccountUseCase(IAccountService accountService, ICurrencyService currencyService, IRepository dataStore) {
        this.accountService = accountService;
        this.currencyService = currencyService;
        this.dataStore = dataStore;
        this.getAccountByUUIDUseCase = new GetAccountByUUIDUseCase( accountService);
    }

    public Result<Account> execute(UUID userUuid , String userName) {
        Result<Account> accountResult =  getAccountByUUIDUseCase.execute(userUuid);
        if (accountResult.isSuccess()) {
            return Result.failure("Account already exists for: " + accountResult.getValue().getNickname(), ErrorCode.ACCOUNT_ALREADY_EXISTS);
        }
        Account account = new Account(userUuid, userName);
        initializeAccountWithDefaultCurrencies(account);
        try {
            this.dataStore.createAccount(account);
            this.accountService.addAccountToOnline(account);
        } catch (TransactionException t) {
            return Result.failure("Error creating account for: " + account.getNickname(), ErrorCode.DATA_BASE_ERROR);
        }
        return Result.success(account);
    }

    private void initializeAccountWithDefaultCurrencies(Account account) {
        List<Money> monies =  this.currencyService.getCurrencies().stream()
                .map(Money::new)
                .collect(Collectors.toList());
        account.setBalances(monies);
    }

}
