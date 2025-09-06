package BlockDynasty.listeners;

import BlockDynasty.Economy.domain.entities.offers.Offer;
import lib.gui.GUIFactory;

public class OfferListener implements BlockDynasty.Economy.aplication.listeners.OfferListener {


    @Override
    public void onOfferExpired(Offer offer) {
        GUIFactory.getGuiService().refresh(offer.getComprador());

    }

    @Override
    public void onOfferCreated(Offer offer) {
        GUIFactory.getGuiService().refresh(offer.getComprador());
    }

    @Override
    public void onOfferCanceled(Offer offer) {
        GUIFactory.getGuiService().refresh(offer.getComprador());
        GUIFactory.getGuiService().refresh(offer.getVendedor());
    }
}
