package BlockDynasty.Economy.aplication.useCase;

import BlockDynasty.Economy.aplication.useCase.account.GetAccountsUseCase;
import BlockDynasty.Economy.aplication.useCase.currency.GetCurrencyUseCase;
import BlockDynasty.Economy.aplication.useCase.offer.AcceptOfferUseCase;
import BlockDynasty.Economy.aplication.useCase.offer.CancelOfferUseCase;
import BlockDynasty.Economy.aplication.useCase.offer.CreateOfferUseCase;
import BlockDynasty.Economy.aplication.useCase.transaction.TradeCurrenciesUseCase;
import BlockDynasty.Economy.domain.services.IOfferService;

public class OfferUseCase {
    private final AcceptOfferUseCase acceptOfferUseCase;
    private final CreateOfferUseCase createOfferUseCase;
    private final CancelOfferUseCase cancelOfferUseCase;

    public OfferUseCase(IOfferService offerService, GetCurrencyUseCase getCurrencyUseCase, GetAccountsUseCase getAccountsUseCase, TradeCurrenciesUseCase tradeCurrenciesUseCase) {
        this.createOfferUseCase = new CreateOfferUseCase(offerService,getCurrencyUseCase, getAccountsUseCase);
        this.acceptOfferUseCase = new AcceptOfferUseCase(offerService, tradeCurrenciesUseCase);
        this.cancelOfferUseCase = new CancelOfferUseCase(offerService);
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
}
