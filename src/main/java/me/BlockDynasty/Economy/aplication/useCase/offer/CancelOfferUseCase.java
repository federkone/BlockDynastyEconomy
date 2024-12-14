package me.BlockDynasty.Economy.aplication.useCase.offer;

import me.BlockDynasty.Economy.aplication.result.ErrorCode;
import me.BlockDynasty.Economy.aplication.result.Result;
import me.BlockDynasty.Economy.domain.Offers.OfferManager;
import me.BlockDynasty.Economy.domain.Offers.Exceptions.OffertNotFoundException;

import java.util.UUID;

public class CancelOfferUseCase {
    private final OfferManager offerManager;

    public CancelOfferUseCase(OfferManager offerManager) {
        this.offerManager = offerManager;
    }

    public Result<Void> execute (UUID playerCancel){

        boolean deleted = offerManager.removeOffer(playerCancel);

        if (!deleted ) {
            return Result.failure("Offer not found for cancer offer", ErrorCode.OFFER_NOT_FOUND);
            //throw new OffertNotFoundException("Offer not found for cancer offer");
        }
        return Result.success(null);
    }
}
