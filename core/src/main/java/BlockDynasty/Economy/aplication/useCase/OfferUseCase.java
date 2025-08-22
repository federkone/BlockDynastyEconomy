package BlockDynasty.Economy.aplication.useCase;

import BlockDynasty.Economy.aplication.useCase.account.SearchAccountUseCase;
import BlockDynasty.Economy.aplication.useCase.currency.SearchCurrencyUseCase;
import BlockDynasty.Economy.aplication.useCase.offer.AcceptOfferUseCase;
import BlockDynasty.Economy.aplication.useCase.offer.CancelOfferUseCase;
import BlockDynasty.Economy.aplication.useCase.offer.CreateOfferUseCase;
import BlockDynasty.Economy.aplication.useCase.offer.SearchOfferUseCase;
import BlockDynasty.Economy.aplication.useCase.transaction.TradeCurrenciesUseCase;
import BlockDynasty.Economy.domain.services.IOfferService;

public class OfferUseCase {
    private final AcceptOfferUseCase acceptOfferUseCase;
    private final CreateOfferUseCase createOfferUseCase;
    private final CancelOfferUseCase cancelOfferUseCase;
    private final SearchOfferUseCase searchOfferUseCase;

    public OfferUseCase(IOfferService offerService, SearchCurrencyUseCase searchCurrencyUseCase, SearchAccountUseCase searchAccountUseCase, TradeCurrenciesUseCase tradeCurrenciesUseCase) {
        this.createOfferUseCase = new CreateOfferUseCase(offerService, searchCurrencyUseCase, searchAccountUseCase);
        this.acceptOfferUseCase = new AcceptOfferUseCase(offerService, tradeCurrenciesUseCase);
        this.cancelOfferUseCase = new CancelOfferUseCase(offerService);
        this.searchOfferUseCase = new SearchOfferUseCase(offerService);
    }

    public AcceptOfferUseCase getAcceptOfferUseCase() {
        return acceptOfferUseCase;
    }
    public CreateOfferUseCase getCreateOfferUseCase() {
        return createOfferUseCase;
    }
    public CancelOfferUseCase getCancelOfferUseCase() {
        return cancelOfferUseCase;
    }
    public SearchOfferUseCase getSearchOfferUseCase() {
        return searchOfferUseCase;
    }
}
