package BlockDynasty.Economy.aplication.services;

import BlockDynasty.Economy.aplication.events.EventManager;
import BlockDynasty.Economy.domain.persistence.entities.IRepository;
import BlockDynasty.Economy.domain.services.IAccountService;
import BlockDynasty.Economy.domain.services.ICurrencyService;
import BlockDynasty.Economy.domain.services.IOfferService;
import BlockDynasty.Economy.domain.services.courier.Courier;

public class ServicesManager {
    private final ICurrencyService currencyService;
    private final IAccountService accountService;
    private final IOfferService offerService;
    private final EventManager eventManager;

    public ServicesManager(IRepository repository, int cacheTopMinutes, Courier courier) {
        this.currencyService = new CurrencyService(repository);
        this.accountService = new AccountService(cacheTopMinutes,repository,currencyService);
        this.eventManager = new EventManager();
        this.offerService = new OfferService(courier,eventManager);
    }

    public ICurrencyService getCurrencyService() {
        return currencyService;
    }
    public IAccountService getAccountService() {
        return accountService;
    }
    public IOfferService getOfferService() {
        return offerService;
    }
    public EventManager getEventManager() {
        return eventManager;
    }
}
