package me.BlockDynasty.Economy.aplication.useCase.offer;

import me.BlockDynasty.Economy.aplication.useCase.transaction.TradeCurrenciesUseCase;
import me.BlockDynasty.Economy.domain.Offers.Offer;
import me.BlockDynasty.Economy.domain.Offers.OfferManager;
import me.BlockDynasty.Economy.domain.Offers.Exceptions.OffertNotFoundException;
import me.BlockDynasty.Economy.domain.account.Exceptions.InsufficientFundsException;

import java.util.UUID;

public class AcceptOfferUseCase {
    private final OfferManager offerManager;
    private final TradeCurrenciesUseCase tradeCurrenciesUseCase;

    public AcceptOfferUseCase(OfferManager offerManager, TradeCurrenciesUseCase tradeCurrenciesUseCase) {
        this.offerManager = offerManager;
        this.tradeCurrenciesUseCase = tradeCurrenciesUseCase;
    }

    public void execute (UUID playerAccetp,UUID playerOffer) {  //puede aceptarle la oferta a alguien en especifico

        Offer offer = offerManager.getOffer(playerOffer); //objener la oferta de un vendedor en especifico

        if(offer == null) {
            throw new OffertNotFoundException("Este usuario no te ha ofrecido nada");
        }

        tradeCurrenciesUseCase.execute(offer.getVendedor(), offer.getComprador(),  offer.getTipoCantidad().getSingular(), offer.getTipoMonto().getSingular(),offer.getCantidad(), offer.getMonto());
        offerManager.removeOffer(playerAccetp);

    }
}
