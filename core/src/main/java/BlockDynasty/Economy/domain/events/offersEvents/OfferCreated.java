package BlockDynasty.Economy.domain.events.offersEvents;

import BlockDynasty.Economy.aplication.services.OfferService;
import BlockDynasty.Economy.domain.entities.offers.Offer;

public class OfferCreated extends OfferEvent {
    private final Offer offer;

    public OfferCreated(Offer offer) {
        this.offer = offer;
    }

    public Offer getOffer() {
        return offer;
    }

    @Override
    public void syncOffer(OfferService offerService) {
        offerService.addOfferFromEvent(offer);
    }
}
