package BlockDynasty.Economy.aplication.useCase.offer;

import BlockDynasty.Economy.aplication.events.EventManager;
import BlockDynasty.Economy.domain.entities.account.Player;
import BlockDynasty.Economy.domain.entities.offers.Offer;
import BlockDynasty.Economy.domain.events.offersEvents.OfferCanceled;
import BlockDynasty.Economy.domain.result.ErrorCode;
import BlockDynasty.Economy.domain.result.Result;
import BlockDynasty.Economy.domain.services.IOfferService;
import BlockDynasty.Economy.domain.services.courier.Courier;

public class CancelOfferUseCase {
    private final IOfferService offerService;
    private final Courier courier;
    private final EventManager eventManager;

    public CancelOfferUseCase(IOfferService offerService, Courier courier, EventManager eventmanager) {
        this.offerService = offerService;
        this.courier = courier;
        this.eventManager = eventmanager;
    }

    public Result<Void> execute (Player playerCancel){
        Offer offer = offerService.getOfferBuyer(playerCancel.getUuid());
        if (offer == null){
            offer= offerService.getOfferSeller(playerCancel.getUuid());
        }
        boolean deleted = offerService.cancelOffer(playerCancel.getUuid());
        if (!deleted ) {
            return Result.failure("Offer not found for cancer offer", ErrorCode.OFFER_NOT_FOUND);
        }
        eventManager.emit(new OfferCanceled(offer));
        courier.sendUpdateMessage("event", new OfferCanceled( offer).toJson(),offer.getVendedor().getUuid().toString());
        courier.sendUpdateMessage("event", new OfferCanceled( offer).toJson(),offer.getComprador().getUuid().toString());
        return Result.success();
    }
}
