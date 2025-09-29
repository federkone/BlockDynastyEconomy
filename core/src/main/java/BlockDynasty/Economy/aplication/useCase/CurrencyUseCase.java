package BlockDynasty.Economy.aplication.useCase;

import BlockDynasty.Economy.aplication.services.ServicesManager;
import BlockDynasty.Economy.aplication.useCase.currency.CreateCurrencyUseCase;
import BlockDynasty.Economy.aplication.useCase.currency.DeleteCurrencyUseCase;
import BlockDynasty.Economy.aplication.useCase.currency.EditCurrencyUseCase;
import BlockDynasty.Economy.aplication.useCase.currency.SearchCurrencyUseCase;
import BlockDynasty.Economy.domain.persistence.entities.IRepository;
import BlockDynasty.Economy.domain.services.courier.Courier;

public class CurrencyUseCase {
    private final SearchCurrencyUseCase searchCurrencyUseCase;
    private final CreateCurrencyUseCase createCurrencyUseCase;
    private final DeleteCurrencyUseCase deleteCurrencyUseCase;
    private  final EditCurrencyUseCase editCurrencyUseCase;

    public CurrencyUseCase(ServicesManager servicesManager, IRepository repository, AccountsUseCase accountsUseCase, Courier courier) {
        this.searchCurrencyUseCase = new SearchCurrencyUseCase(servicesManager.getCurrencyService(),repository);
        this.createCurrencyUseCase = new CreateCurrencyUseCase(servicesManager.getCurrencyService(), servicesManager.getAccountService(), courier,repository);
        this.deleteCurrencyUseCase = new DeleteCurrencyUseCase( servicesManager.getCurrencyService(), servicesManager.getAccountService(), repository, courier);
        this.editCurrencyUseCase = new EditCurrencyUseCase(servicesManager.getCurrencyService(), courier, repository);
    }

    public SearchCurrencyUseCase getGetCurrencyUseCase() {
        return searchCurrencyUseCase;
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
