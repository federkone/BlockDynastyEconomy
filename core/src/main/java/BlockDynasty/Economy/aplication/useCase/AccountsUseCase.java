package BlockDynasty.Economy.aplication.useCase;

import BlockDynasty.Economy.aplication.services.ServicesManager;
import BlockDynasty.Economy.aplication.useCase.account.CreateAccountUseCase;
import BlockDynasty.Economy.aplication.useCase.account.DeleteAccountUseCase;
import BlockDynasty.Economy.aplication.useCase.account.GetAccountsUseCase;
import BlockDynasty.Economy.aplication.useCase.balance.GetBalanceUseCase;
import BlockDynasty.Economy.domain.persistence.entities.IRepository;

public class AccountsUseCase {
     private final GetAccountsUseCase getAccountsUseCase;
     private final CreateAccountUseCase createAccountUseCase;
     private final GetBalanceUseCase getBalanceUseCase;
     private final DeleteAccountUseCase deleteAccountUseCase;

     public AccountsUseCase(ServicesManager servicesManager, IRepository repository) {
          this.getAccountsUseCase = new GetAccountsUseCase(servicesManager.getAccountService(), servicesManager.getCurrencyService(), repository);
          this.createAccountUseCase = new CreateAccountUseCase(servicesManager.getAccountService(), servicesManager.getCurrencyService(), this.getAccountsUseCase, repository);
          this.getBalanceUseCase = new GetBalanceUseCase( this.getAccountsUseCase);
          this.deleteAccountUseCase = new DeleteAccountUseCase(repository, servicesManager.getAccountService(), this.getAccountsUseCase);
     }

     public GetAccountsUseCase getGetAccountsUseCase() {
            return getAccountsUseCase;
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
}
