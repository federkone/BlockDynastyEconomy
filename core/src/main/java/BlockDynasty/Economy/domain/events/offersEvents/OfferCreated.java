package BlockDynasty.Economy.domain.events.offersEvents;

import BlockDynasty.Economy.aplication.services.OfferService;
import BlockDynasty.Economy.domain.entities.offers.Offer;
import BlockDynasty.Economy.domain.events.Event;
import BlockDynasty.Economy.domain.events.SerializableEvent;

public class OfferCreated extends Event implements SerializableEvent,OfferEvent {
    private final Offer offer;

    public OfferCreated(Offer offer) {
        this.offer = offer;
    }

    public Offer getOffer() {
        return offer;
    }

    @Override
    public String getEventType() {
        return "OfferCreated";
    }

    public static OfferCreated fromJson(String jsonString) {
        return SerializableEvent.fromJson(jsonString, OfferCreated.class);
    }

    @Override
    public void handle(OfferService offerService) {
        offerService.addOffer(offer);
    }
}
