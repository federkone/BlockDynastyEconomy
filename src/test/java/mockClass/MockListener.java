package mockClass;

import me.BlockDynasty.Economy.aplication.listeners.OfferListener;
import me.BlockDynasty.Economy.domain.entities.offers.Offer;

public class MockListener implements OfferListener {

    @Override
    public void onOfferExpired(Offer offer) {
        System.out.println("Offer with seller " + offer.getVendedor() + " has expired.");
    }
}
