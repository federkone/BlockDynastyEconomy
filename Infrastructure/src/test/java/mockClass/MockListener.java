package mockClass;

import BlockDynasty.Economy.aplication.listeners.OfferListener;
import BlockDynasty.Economy.domain.entities.offers.Offer;

public class MockListener implements OfferListener {

    @Override
    public void onOfferExpired(Offer offer) {
        System.out.println("Offer with seller " + offer.getVendedor() + " has expired.");
    }
}
