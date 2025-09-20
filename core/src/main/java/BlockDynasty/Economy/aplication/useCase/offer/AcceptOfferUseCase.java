package BlockDynasty.Economy.aplication.useCase.offer;

import BlockDynasty.Economy.domain.result.ErrorCode;
import BlockDynasty.Economy.domain.result.Result;
import BlockDynasty.Economy.aplication.useCase.transaction.TradeCurrenciesUseCase;
import BlockDynasty.Economy.domain.entities.offers.Offer;
import BlockDynasty.Economy.domain.services.IOfferService;

import java.util.UUID;

public class AcceptOfferUseCase {
    private final IOfferService offerService;
    private final TradeCurrenciesUseCase tradeCurrenciesUseCase;

    public AcceptOfferUseCase(IOfferService offerService, TradeCurrenciesUseCase tradeCurrenciesUseCase) {
        this.offerService = offerService;
        this.tradeCurrenciesUseCase = tradeCurrenciesUseCase;
    }

    public Result<Void> execute (UUID playerAccept, UUID playerOffer) {  //puede aceptarle la oferta a alguien en especifico
        Offer offer = offerService.getOffer(playerOffer); //objener la oferta de un vendedor en especifico
        if(offer == null) {
            return Result.failure("This user has not offered you anything", ErrorCode.OFFER_NOT_FOUND);
        }
        Result<Void> tradeResult = tradeCurrenciesUseCase.execute(offer.getVendedor().getUuid(), offer.getComprador().getUuid(),  offer.getTipoCantidad().getSingular(), offer.getTipoMonto().getSingular(),offer.getCantidad(), offer.getMonto());
        if (!tradeResult.isSuccess()) {
            offerService.cancelOffer(playerAccept);
            return Result.failure(tradeResult.getErrorMessage(), tradeResult.getErrorCode());
        }
        offerService.acceptOffer(playerAccept);
        return Result.success(null);
    }
}
