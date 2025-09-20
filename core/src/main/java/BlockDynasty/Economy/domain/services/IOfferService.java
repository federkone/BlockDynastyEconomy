package BlockDynasty.Economy.domain.services;

import BlockDynasty.Economy.domain.entities.account.Player;
import BlockDynasty.Economy.domain.entities.currency.Currency;
import BlockDynasty.Economy.domain.entities.offers.Offer;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public interface IOfferService {
    void createOffer(Player playerSender, Player playerReciber, BigDecimal amountCurrencyValue, BigDecimal amountCurrencyOffer, Currency currencyValue, Currency currencyOffer );
    Offer getOffer(UUID playerId);
    List<Offer> getOffersBuyer(UUID playerId);
    List<Offer> getOffersSeller(UUID playerId);
    boolean cancelOffer(UUID player);
    boolean acceptOffer(UUID player);
    boolean hasOfferTo(UUID player);
    void processNetworkEvent(String data);
}
