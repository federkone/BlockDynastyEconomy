package net.blockdynasty.economy.core.aplication.useCase.transaction.genericOperations;

import net.blockdynasty.economy.core.aplication.useCase.account.getAccountUseCase.GetAccountByNameUseCase;
import net.blockdynasty.economy.core.aplication.useCase.account.getAccountUseCase.GetAccountByPlayerUseCase;
import net.blockdynasty.economy.core.aplication.useCase.account.getAccountUseCase.GetAccountByUUIDUseCase;
import net.blockdynasty.economy.core.aplication.useCase.currency.SearchCurrencyUseCase;
import net.blockdynasty.economy.core.domain.persistence.entities.IRepository;
import net.blockdynasty.economy.core.domain.services.IAccountService;
import net.blockdynasty.economy.core.domain.services.ICurrencyService;

public abstract class Operation {
    protected final SearchCurrencyUseCase searchCurrencyUseCase;
    protected final GetAccountByUUIDUseCase getAccountByUUIDUseCase;
    protected final GetAccountByNameUseCase getAccountByNameUseCase;
    protected final GetAccountByPlayerUseCase getAccountByPlayerUseCase;

    public Operation (IAccountService accountService, ICurrencyService currencyService, IRepository dataStore) {
        this.searchCurrencyUseCase = new SearchCurrencyUseCase(currencyService, dataStore);
        this.getAccountByNameUseCase = new GetAccountByNameUseCase(accountService);
        this.getAccountByUUIDUseCase = new GetAccountByUUIDUseCase(accountService);
        this.getAccountByPlayerUseCase = new GetAccountByPlayerUseCase(accountService);
    }
}
