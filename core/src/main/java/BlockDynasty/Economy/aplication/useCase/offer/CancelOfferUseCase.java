package BlockDynasty.Economy.aplication.useCase.offer;

import BlockDynasty.Economy.domain.result.ErrorCode;
import BlockDynasty.Economy.domain.result.Result;
import BlockDynasty.Economy.domain.services.IOfferService;

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
