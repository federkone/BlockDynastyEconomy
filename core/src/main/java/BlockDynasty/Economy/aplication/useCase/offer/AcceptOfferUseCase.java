package BlockDynasty.Economy.aplication.useCase.offer;

import BlockDynasty.Economy.aplication.events.EventManager;
import BlockDynasty.Economy.domain.events.offersEvents.OfferAccepted;
import BlockDynasty.Economy.domain.events.offersEvents.OfferCanceled;
import BlockDynasty.Economy.domain.result.ErrorCode;
import BlockDynasty.Economy.domain.result.Result;
import BlockDynasty.Economy.aplication.useCase.transaction.TradeCurrenciesUseCase;
import BlockDynasty.Economy.domain.entities.offers.Offer;
import BlockDynasty.Economy.domain.services.IOfferService;
import BlockDynasty.Economy.domain.services.courier.Courier;

import java.util.UUID;

public class AcceptOfferUseCase {
    private final IOfferService offerService;
    private final TradeCurrenciesUseCase tradeCurrenciesUseCase;
    private final Courier courier;
    private final EventManager eventManager;

    public AcceptOfferUseCase(IOfferService offerService, Courier courier, EventManager eventmanager, TradeCurrenciesUseCase tradeCurrenciesUseCase) {
        this.offerService = offerService;
        this.tradeCurrenciesUseCase = tradeCurrenciesUseCase;
        this.courier = courier;
        this.eventManager = eventmanager;
    }

    public Result<Void> execute (UUID playerAccept, UUID playerOffer) {  //puede aceptarle la oferta a alguien en especifico
        Offer offer = offerService.getOffer(playerAccept,playerOffer); //objener la oferta de un vendedor en especifico
        if(offer == null) {
            return Result.failure("This user has not offered you anything", ErrorCode.OFFER_NOT_FOUND);
        }
        Result<Void> tradeResult = tradeCurrenciesUseCase.execute(offer.getVendedor().getUuid(), offer.getComprador().getUuid(),  offer.getTipoCantidad().getSingular(), offer.getTipoMonto().getSingular(),offer.getCantidad(), offer.getMonto());
        if (!tradeResult.isSuccess()) {
            offerService.cancelOffer(playerAccept);
            eventManager.emit(new OfferCanceled(offer));
            courier.sendUpdateMessage("event", new OfferCanceled( offer).toJson(),playerOffer.toString());
            return Result.failure(tradeResult.getErrorMessage(), tradeResult.getErrorCode());
        }
        offerService.acceptOffer(playerAccept,playerOffer);
        eventManager.emit(new OfferAccepted(offer));
        courier.sendUpdateMessage("event", new OfferAccepted( offer).toJson(),playerOffer.toString());
        return Result.success(null);
    }
}
