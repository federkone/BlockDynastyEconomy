package BlockDynasty.Economy.domain.events.offersEvents;

import BlockDynasty.Economy.aplication.services.OfferService;
import BlockDynasty.Economy.domain.entities.offers.Offer;
import BlockDynasty.Economy.domain.events.Event;
import BlockDynasty.Economy.domain.events.SerializableEvent;

public class OfferAccepted extends Event implements SerializableEvent,OfferEvent{
    private final Offer offer;

    public OfferAccepted(Offer offer) {
        this.offer = offer;
    }

    public Offer getOffer() {
        return offer;
    }

    @Override
    public String getEventType() {
        return "OfferAccepted";
    }

    public static OfferAccepted fromJson(String jsonString) {
        return SerializableEvent.fromJson(jsonString, OfferAccepted.class);
    }

    @Override
    public void handle(OfferService offerService) {
        offerService.removeOffer(offer);
    }
}
