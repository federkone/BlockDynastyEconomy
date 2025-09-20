package BlockDynasty.Economy.domain.events.offersEvents;

import BlockDynasty.Economy.aplication.services.OfferService;

public interface OfferEvent {

    void handle(OfferService offerService);

}
