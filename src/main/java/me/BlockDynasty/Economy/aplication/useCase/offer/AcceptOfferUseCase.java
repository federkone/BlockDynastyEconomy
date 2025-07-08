package me.BlockDynasty.Economy.aplication.useCase.offer;

import me.BlockDynasty.Economy.domain.result.ErrorCode;
import me.BlockDynasty.Economy.domain.result.Result;
import me.BlockDynasty.Economy.aplication.useCase.transaction.TradeCurrenciesUseCase;
import me.BlockDynasty.Economy.domain.offers.Offer;
import me.BlockDynasty.Economy.Infrastructure.services.OfferService;
import me.BlockDynasty.Economy.domain.services.IOfferService;

import java.util.UUID;

public class AcceptOfferUseCase {
    private final IOfferService offerService;
    private final TradeCurrenciesUseCase tradeCurrenciesUseCase;

    public AcceptOfferUseCase(IOfferService offerService, TradeCurrenciesUseCase tradeCurrenciesUseCase) {
        this.offerService = offerService;
        this.tradeCurrenciesUseCase = tradeCurrenciesUseCase;
    }

    public Result<Void> execute (UUID playerAccetp, UUID playerOffer) {  //puede aceptarle la oferta a alguien en especifico
        Offer offer = offerService.getOffer(playerOffer); //objener la oferta de un vendedor en especifico
        if(offer == null) {
            return Result.failure("This user has not offered you anything", ErrorCode.OFFER_NOT_FOUND);
        }
        Result<Void> tradeResult = tradeCurrenciesUseCase.execute(offer.getVendedor(), offer.getComprador(),  offer.getTipoCantidad().getSingular(), offer.getTipoMonto().getSingular(),offer.getCantidad(), offer.getMonto());
        if (!tradeResult.isSuccess()) {
            offerService.removeOffer(playerAccetp);
            return Result.failure(tradeResult.getErrorMessage(), tradeResult.getErrorCode());
        }
        offerService.removeOffer(playerAccetp);
        return Result.success(null);
    }
}
