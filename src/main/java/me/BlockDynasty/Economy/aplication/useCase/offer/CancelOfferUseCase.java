package me.BlockDynasty.Economy.aplication.useCase.offer;

import me.BlockDynasty.Economy.domain.Offers.OfferManager;
import me.BlockDynasty.Economy.domain.Offers.Exceptions.OffertNotFoundException;

import java.util.UUID;

public class CancelOfferUseCase {
    private final OfferManager offerManager;

    public CancelOfferUseCase(OfferManager offerManager) {
        this.offerManager = offerManager;
    }

    public void execute (UUID playerCancel){

        boolean deleted = offerManager.removeOffer(playerCancel);

        if (!deleted ) {
            throw new OffertNotFoundException("Offer not found for cancer offer");
        }

    }
}
