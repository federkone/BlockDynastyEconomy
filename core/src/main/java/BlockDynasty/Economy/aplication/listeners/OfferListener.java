package BlockDynasty.Economy.aplication.listeners;

import BlockDynasty.Economy.domain.entities.offers.Offer;

public interface OfferListener {
    void onOfferExpired(Offer offer);
    void onOfferCreated(Offer offer);
    void onOfferCanceled(Offer offer);
}