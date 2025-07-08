package me.BlockDynasty.Economy.aplication.useCase.offer;

import me.BlockDynasty.Economy.domain.result.ErrorCode;
import me.BlockDynasty.Economy.domain.result.Result;
import me.BlockDynasty.Economy.Infrastructure.services.OfferService;
import me.BlockDynasty.Economy.domain.services.IOfferService;

import java.util.UUID;

public class CancelOfferUseCase {
    private final IOfferService offerService;

    public CancelOfferUseCase(IOfferService offerService) {
        this.offerService = offerService;
    }

    public Result<Void> execute (UUID playerCancel){
        boolean deleted = offerService.removeOffer(playerCancel);
        if (!deleted ) {
            return Result.failure("Offer not found for cancer offer", ErrorCode.OFFER_NOT_FOUND);
        }
        return Result.success(null);
    }
}
