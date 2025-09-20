package BlockDynasty.Economy.aplication.useCase.offer;

import BlockDynasty.Economy.domain.entities.account.Player;
import BlockDynasty.Economy.domain.result.ErrorCode;
import BlockDynasty.Economy.domain.result.Result;
import BlockDynasty.Economy.domain.services.IOfferService;

public class CancelOfferUseCase {
    private final IOfferService offerService;

    public CancelOfferUseCase(IOfferService offerService) {
        this.offerService = offerService;
    }

    public Result<Void> execute (Player playerCancel){
        boolean deleted = offerService.cancelOffer(playerCancel.getUuid());
        if (!deleted ) {
            return Result.failure("Offer not found for cancer offer", ErrorCode.OFFER_NOT_FOUND);
        }
        return Result.success(null);
    }
}
