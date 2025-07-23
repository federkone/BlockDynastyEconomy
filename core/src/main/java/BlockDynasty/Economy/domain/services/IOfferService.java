package BlockDynasty.Economy.domain.services;

import BlockDynasty.Economy.domain.entities.currency.Currency;
import BlockDynasty.Economy.domain.entities.offers.Offer;

import java.math.BigDecimal;
import java.util.UUID;

public interface IOfferService {
    void addOffer(UUID playerSender, UUID playerReciber, BigDecimal amountCurrencyValue, BigDecimal amountCurrencyOffer, Currency currencyValue, Currency currencyOffer );
    Offer getOffer(UUID playerId);
    boolean removeOffer(UUID player);
    boolean hasOfferTo(UUID player);
}
