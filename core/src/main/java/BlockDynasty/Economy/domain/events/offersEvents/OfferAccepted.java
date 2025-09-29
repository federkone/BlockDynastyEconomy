package BlockDynasty.Economy.domain.events.offersEvents;

import BlockDynasty.Economy.aplication.services.OfferService;
import BlockDynasty.Economy.domain.entities.offers.Offer;
import BlockDynasty.Economy.domain.events.SerializableEvent;

public class OfferAccepted extends OfferEvent{
    private final Offer offer;

    public OfferAccepted(Offer offer) {
        this.offer = offer;
    }

    public Offer getOffer() {
        return offer;
    }

    @Override
    public void syncOffer(OfferService offerService) {
        offerService.acceptOffer(offer.getComprador().getUuid(),offer.getVendedor().getUuid());
    }
}
