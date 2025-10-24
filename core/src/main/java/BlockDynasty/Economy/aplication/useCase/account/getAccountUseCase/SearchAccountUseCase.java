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

package BlockDynasty.Economy.aplication.useCase.account.getAccountUseCase;

import BlockDynasty.Economy.domain.result.Result;
import BlockDynasty.Economy.domain.entities.account.Account;
import BlockDynasty.Economy.domain.persistence.entities.IRepository;
import BlockDynasty.Economy.domain.services.IAccountService;
import java.util.*;

@Deprecated
public class SearchAccountUseCase {
    private GetOfflineAccountsUseCase getOfflineAccountsUseCase;
    private GetOnlineAccountsUseCase getOnlineAccountsUseCase;
    private GetAccountByUUIDUseCase getAccountByUUIDUseCase;
    private GetTopAccountsUseCase getTopAccountsUseCase;
    private GetAccountByNameUseCase getAccountByNameUseCase;

    public SearchAccountUseCase(IAccountService accountService, IRepository dataStore) {
        this.getOfflineAccountsUseCase = new GetOfflineAccountsUseCase(accountService);
        this.getOnlineAccountsUseCase = new GetOnlineAccountsUseCase(accountService);
        this.getAccountByUUIDUseCase = new GetAccountByUUIDUseCase(accountService);
        this.getTopAccountsUseCase = new GetTopAccountsUseCase(accountService, dataStore);
        this.getAccountByNameUseCase = new GetAccountByNameUseCase(accountService);
    }
    public Result<Account> execute(String name) {
       return getAccountByNameUseCase.execute(name);
    }

    public Result<Account> execute(UUID uuid) {
      return getAccountByUUIDUseCase.execute(uuid);
    }

    public Result<List<Account>> getTopAccounts( String currency ,int limit, int offset) {
        return getTopAccountsUseCase.execute(currency, limit, offset);
    }

    public Result<List<Account>> getOfflineAccounts() {
        return getOfflineAccountsUseCase.execute();
    }

}
