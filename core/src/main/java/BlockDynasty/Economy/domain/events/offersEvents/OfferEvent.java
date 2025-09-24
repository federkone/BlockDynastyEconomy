package BlockDynasty.Economy.domain.events.offersEvents;

import BlockDynasty.Economy.aplication.services.OfferService;
import BlockDynasty.Economy.domain.events.SerializableEvent;

public abstract class OfferEvent extends SerializableEvent {
    public abstract void syncOffer(OfferService offerService);
}
