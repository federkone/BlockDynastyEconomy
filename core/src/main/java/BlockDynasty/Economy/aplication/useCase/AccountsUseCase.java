package BlockDynasty.Economy.aplication.useCase;

import BlockDynasty.Economy.aplication.services.ServicesManager;
import BlockDynasty.Economy.aplication.useCase.account.CreateAccountUseCase;
import BlockDynasty.Economy.aplication.useCase.account.DeleteAccountUseCase;
import BlockDynasty.Economy.aplication.useCase.account.EditAccountUseCase;
import BlockDynasty.Economy.aplication.useCase.account.SearchAccountUseCase;
import BlockDynasty.Economy.aplication.useCase.balance.GetBalanceUseCase;
import BlockDynasty.Economy.domain.persistence.entities.IRepository;

public class AccountsUseCase {
     private final SearchAccountUseCase searchAccountUseCase;
     private final CreateAccountUseCase createAccountUseCase;
     private final GetBalanceUseCase getBalanceUseCase;
     private final DeleteAccountUseCase deleteAccountUseCase;
     private final EditAccountUseCase editAccountUseCase;

     public AccountsUseCase(ServicesManager servicesManager, IRepository repository) {
          this.searchAccountUseCase = new SearchAccountUseCase(servicesManager.getAccountService(), repository);
          this.createAccountUseCase = new CreateAccountUseCase(servicesManager.getAccountService(), servicesManager.getCurrencyService(), this.searchAccountUseCase, repository);
          this.getBalanceUseCase = new GetBalanceUseCase( this.searchAccountUseCase);
          this.editAccountUseCase = new EditAccountUseCase(this.searchAccountUseCase, repository);
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
