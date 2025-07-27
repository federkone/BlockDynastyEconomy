package BlockDynasty.Economy.aplication.useCase;

import BlockDynasty.Economy.aplication.services.ServicesFactory;
import BlockDynasty.Economy.aplication.useCase.account.GetAccountsUseCase;
import BlockDynasty.Economy.aplication.useCase.currency.CreateCurrencyUseCase;
import BlockDynasty.Economy.aplication.useCase.currency.DeleteCurrencyUseCase;
import BlockDynasty.Economy.aplication.useCase.currency.EditCurrencyUseCase;
import BlockDynasty.Economy.aplication.useCase.currency.GetCurrencyUseCase;
import BlockDynasty.Economy.domain.persistence.entities.IRepository;
import BlockDynasty.Economy.domain.services.courier.Courier;

public class CurrencyUseCase {
    private final GetCurrencyUseCase getCurrencyUseCase;
    private final CreateCurrencyUseCase createCurrencyUseCase;
    private final DeleteCurrencyUseCase deleteCurrencyUseCase;
    private  final EditCurrencyUseCase editCurrencyUseCase;

    public CurrencyUseCase(ServicesFactory servicesFactory, IRepository repository,AccountsUseCase accountsUseCase, Courier courier) {
        this.getCurrencyUseCase = new GetCurrencyUseCase(servicesFactory.getCurrencyService(),repository);
        this.createCurrencyUseCase = new CreateCurrencyUseCase(servicesFactory.getCurrencyService(), accountsUseCase.getGetAccountsUseCase(), courier,repository);
        this.deleteCurrencyUseCase = new DeleteCurrencyUseCase( servicesFactory.getCurrencyService(), accountsUseCase.getGetAccountsUseCase(), repository, courier);
        this.editCurrencyUseCase = new EditCurrencyUseCase(servicesFactory.getCurrencyService(), courier, repository);
    }

    public GetCurrencyUseCase getGetCurrencyUseCase() {
        return getCurrencyUseCase;
    }
    public CreateCurrencyUseCase getCreateCurrencyUseCase() {
        return createCurrencyUseCase;
    }

    public DeleteCurrencyUseCase getDeleteCurrencyUseCase() {
        return deleteCurrencyUseCase;
    }
    public EditCurrencyUseCase getEditCurrencyUseCase() {
        return editCurrencyUseCase;
    }
}
