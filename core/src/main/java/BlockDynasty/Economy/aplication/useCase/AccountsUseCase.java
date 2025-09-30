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

package BlockDynasty.Economy.aplication.useCase;

import BlockDynasty.Economy.aplication.services.ServicesManager;
import BlockDynasty.Economy.aplication.useCase.account.CreateAccountUseCase;
import BlockDynasty.Economy.aplication.useCase.account.DeleteAccountUseCase;
import BlockDynasty.Economy.aplication.useCase.account.EditAccountUseCase;
import BlockDynasty.Economy.aplication.useCase.account.SearchAccountUseCase;
import BlockDynasty.Economy.aplication.useCase.balance.GetBalanceUseCase;
import BlockDynasty.Economy.domain.persistence.entities.IRepository;
import BlockDynasty.Economy.domain.services.courier.Courier;

public class AccountsUseCase {
     private final SearchAccountUseCase searchAccountUseCase;
     private final CreateAccountUseCase createAccountUseCase;
     private final GetBalanceUseCase getBalanceUseCase;
     private final DeleteAccountUseCase deleteAccountUseCase;
     private final EditAccountUseCase editAccountUseCase;

     public AccountsUseCase(ServicesManager servicesManager, IRepository repository, Courier courier) {
          this.searchAccountUseCase = new SearchAccountUseCase(servicesManager.getAccountService(), repository);
          this.createAccountUseCase = new CreateAccountUseCase(servicesManager.getAccountService(), servicesManager.getCurrencyService(), this.searchAccountUseCase, repository);
          this.getBalanceUseCase = new GetBalanceUseCase( this.searchAccountUseCase);
          this.editAccountUseCase = new EditAccountUseCase(this.searchAccountUseCase, repository, courier);
          this.deleteAccountUseCase = new DeleteAccountUseCase(repository, servicesManager.getAccountService(), this.searchAccountUseCase);
     }

     public SearchAccountUseCase getGetAccountsUseCase() {
            return searchAccountUseCase;
     }
     public CreateAccountUseCase getCreateAccountUseCase() {
            return createAccountUseCase;
     }
     public GetBalanceUseCase getGetBalanceUseCase() {
            return getBalanceUseCase;
     }
     public DeleteAccountUseCase getDeleteAccountUseCase() {
                return deleteAccountUseCase;
        }
     public EditAccountUseCase getEditAccountUseCase() {
                return editAccountUseCase;
     }
}
