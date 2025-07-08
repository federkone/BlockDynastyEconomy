package me.BlockDynasty.Economy.domain.services;

import me.BlockDynasty.Economy.domain.currency.Currency;
import me.BlockDynasty.Economy.domain.offers.Offer;

import java.math.BigDecimal;
import java.util.UUID;

public interface IOfferService {
    void addOffer(UUID playerSender, UUID playerReciber, BigDecimal amountCurrencyValue, BigDecimal amountCurrencyOffer, Currency currencyValue, Currency currencyOffer );
    Offer getOffer(UUID playerId);
    boolean removeOffer(UUID player);
    boolean hasOfferTo(UUID player);
}
