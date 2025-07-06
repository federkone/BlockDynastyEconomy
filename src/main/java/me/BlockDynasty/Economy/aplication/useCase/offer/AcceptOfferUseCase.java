package me.BlockDynasty.Economy.aplication.useCase.offer;

import me.BlockDynasty.Economy.domain.result.ErrorCode;
import me.BlockDynasty.Economy.domain.result.Result;
import me.BlockDynasty.Economy.aplication.useCase.transaction.TradeCurrenciesUseCase;
import me.BlockDynasty.Economy.domain.Offers.Offer;
import me.BlockDynasty.Economy.domain.Offers.OfferManager;

import java.util.UUID;

public class AcceptOfferUseCase {
    private final OfferManager offerManager;
    private final TradeCurrenciesUseCase tradeCurrenciesUseCase;

    public AcceptOfferUseCase(OfferManager offerManager, TradeCurrenciesUseCase tradeCurrenciesUseCase) {
        this.offerManager = offerManager;
        this.tradeCurrenciesUseCase = tradeCurrenciesUseCase;
    }

    public Result<Void> execute (UUID playerAccetp, UUID playerOffer) {  //puede aceptarle la oferta a alguien en especifico
        Offer offer = offerManager.getOffer(playerOffer); //objener la oferta de un vendedor en especifico
        if(offer == null) {
            return Result.failure("This user has not offered you anything", ErrorCode.OFFER_NOT_FOUND);
        }
        Result<Void> tradeResult = tradeCurrenciesUseCase.execute(offer.getVendedor(), offer.getComprador(),  offer.getTipoCantidad().getSingular(), offer.getTipoMonto().getSingular(),offer.getCantidad(), offer.getMonto());
        if (!tradeResult.isSuccess()) {
            offerManager.removeOffer(playerAccetp);
            return Result.failure(tradeResult.getErrorMessage(), tradeResult.getErrorCode());
        }
        offerManager.removeOffer(playerAccetp);
        return Result.success(null);
    }
}
