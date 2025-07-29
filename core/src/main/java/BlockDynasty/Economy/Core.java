package BlockDynasty.Economy;

import BlockDynasty.Economy.aplication.listeners.OfferListener;
import BlockDynasty.Economy.aplication.services.ServicesManager;
import BlockDynasty.Economy.aplication.useCase.AccountsUseCase;
import BlockDynasty.Economy.aplication.useCase.CurrencyUseCase;
import BlockDynasty.Economy.aplication.useCase.OfferUseCase;
import BlockDynasty.Economy.aplication.useCase.TransactionsUseCase;
import BlockDynasty.Economy.domain.persistence.entities.IRepository;
import BlockDynasty.Economy.domain.services.courier.Courier;
import BlockDynasty.Economy.domain.services.log.Log;

public class Core {
    private final IRepository repository;
    private final Courier courier;

    private final ServicesManager services;
    private final AccountsUseCase accountsUseCase;
    private final CurrencyUseCase currencyUseCase;
    private final TransactionsUseCase transactionsUseCase;
    private final OfferUseCase offerUseCase;

    public Core(IRepository repository, int cacheTopMinutes, OfferListener offerListener, Courier courier, Log log) {
        this.repository = repository;
        this.courier = courier;

        this.services = new ServicesManager( repository, cacheTopMinutes, offerListener);
        this.accountsUseCase = new AccountsUseCase(services, repository);
        this.currencyUseCase = new CurrencyUseCase(services, repository, accountsUseCase, courier);
        this.transactionsUseCase = new TransactionsUseCase(currencyUseCase,accountsUseCase, repository, courier, log, services.getEventManager());
        this.offerUseCase = new OfferUseCase(services.getOfferService(), this.currencyUseCase.getGetCurrencyUseCase(),this.accountsUseCase.getGetAccountsUseCase(),this.transactionsUseCase.getTradeCurrenciesUseCase());
    }

    public ServicesManager getServicesManager() {
        return this.services;
    }
    public AccountsUseCase getAccountsUseCase() {
        return this.accountsUseCase;
    }
    public CurrencyUseCase getCurrencyUseCase() {
        return this.currencyUseCase;
    }
    public TransactionsUseCase getTransactionsUseCase() {
        return this.transactionsUseCase;
    }
    public TransactionsUseCase getTransactionsUseCase(Log log) {
        return new TransactionsUseCase(getCurrencyUseCase(), getAccountsUseCase(), this.repository, this.courier, log, this.services.getEventManager());
    }
    public OfferUseCase getOfferUseCase() {
        return this.offerUseCase;
    }
}
