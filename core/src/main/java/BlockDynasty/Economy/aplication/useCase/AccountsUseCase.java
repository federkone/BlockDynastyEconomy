package BlockDynasty.Economy.aplication.useCase;

import BlockDynasty.Economy.aplication.services.ServicesFactory;
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

     public AccountsUseCase(ServicesFactory servicesFactory , IRepository repository) {
          this.getAccountsUseCase = new GetAccountsUseCase(servicesFactory.getAccountService(), servicesFactory.getCurrencyService(), repository);
          this.createAccountUseCase = new CreateAccountUseCase(servicesFactory.getAccountService(), servicesFactory.getCurrencyService(), this.getAccountsUseCase, repository);
          this.getBalanceUseCase = new GetBalanceUseCase( this.getAccountsUseCase);
          this.deleteAccountUseCase = new DeleteAccountUseCase(repository, servicesFactory.getAccountService(), this.getAccountsUseCase);
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
