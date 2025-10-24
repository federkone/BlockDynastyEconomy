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

package useCaseTest.account;

import BlockDynasty.Economy.aplication.services.AccountService;
import BlockDynasty.Economy.aplication.services.CurrencyService;
import BlockDynasty.Economy.aplication.useCase.account.CreateAccountUseCase;
import BlockDynasty.Economy.aplication.useCase.account.getAccountUseCase.SearchAccountUseCase;
import BlockDynasty.Economy.aplication.useCase.account.DeleteAccountUseCase;
import BlockDynasty.Economy.domain.entities.account.Account;
import BlockDynasty.Economy.domain.persistence.entities.IRepository;
import BlockDynasty.Economy.domain.result.ErrorCode;
import BlockDynasty.Economy.domain.result.Result;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repositoryTest.FactoryRepo;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class DeleteAccountUseCaseTest {
    IRepository repository;
    SearchAccountUseCase searchAccountUseCase;
    CreateAccountUseCase createAccountUseCase;
    DeleteAccountUseCase deleteAccountUseCase;
    AccountService accountService;
    CurrencyService currencyService;

    @BeforeEach
    void setUp() {
        repository = FactoryRepo.getDb();
        currencyService = new CurrencyService(repository);
        accountService = new AccountService(5 ,repository, currencyService);
        searchAccountUseCase = new SearchAccountUseCase(accountService,repository);
        createAccountUseCase = new CreateAccountUseCase(accountService, currencyService,repository);
        deleteAccountUseCase = new DeleteAccountUseCase(repository, accountService);

    }

    @Test
    void deleteAccountByName() {
        createAccountUseCase.execute(UUID.randomUUID() , "nullplague");

        Result<Void> result = deleteAccountUseCase.execute("nullplague");
        assertTrue(result.isSuccess(), "Expected deletion to be successful");

        Result<Account> accountResult = searchAccountUseCase.execute("nullplague");
        System.out.println(accountResult.getErrorCode());
        assertEquals(ErrorCode.ACCOUNT_NOT_FOUND , accountResult.getErrorCode(), "Expected account to be not found after deletion");
    }


}
