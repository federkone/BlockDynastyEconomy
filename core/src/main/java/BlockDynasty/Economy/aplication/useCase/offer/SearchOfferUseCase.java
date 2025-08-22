package BlockDynasty.Economy.aplication.useCase.offer;

import BlockDynasty.Economy.domain.entities.offers.Offer;
import BlockDynasty.Economy.domain.services.IOfferService;

import java.util.List;
import java.util.UUID;

public class SearchOfferUseCase {
    private final IOfferService offerService;

    public SearchOfferUseCase(IOfferService offerService) {
        this.offerService = offerService;
    }

    public List<Offer> getOffersBuyer(UUID playerId) {
        return offerService.getOffersBuyer(playerId);
    }
    public List<Offer> getOffersSeller(UUID playerId) {
        return offerService.getOffersSeller(playerId);
    }

}
