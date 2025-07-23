package BlockDynasty.Economy.aplication.services;

import BlockDynasty.Economy.aplication.listeners.OfferListener;
import BlockDynasty.Economy.domain.entities.currency.Currency;
import BlockDynasty.Economy.domain.entities.offers.Offer;
import BlockDynasty.Economy.domain.services.IOfferService;
import java.math.BigDecimal;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.*;

public class OfferService implements IOfferService {
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private final Map<Offer, ScheduledFuture<?>> ofertasPendientes = new ConcurrentHashMap<>();
    private final OfferListener listener;
    private int delay = 60; // Default delay in seconds

    public OfferService(OfferListener listener, int delay) {
        this.listener = listener;
        if (delay > 0) {
            this.delay = delay;
        }
    }
    public OfferService(OfferListener listener) {
        this.listener = listener;
    }

    public void addOffer(UUID playerSender, UUID playerReceiver, BigDecimal amountCurrencyValue, BigDecimal amountCurrencyOffer, Currency currencyValue, Currency currencyOffer) {
        Offer offer = new Offer(playerSender, playerReceiver, amountCurrencyValue, amountCurrencyOffer, currencyValue, currencyOffer);
        ScheduledFuture<?> expirationTask = scheduler.schedule(() -> {
            ofertasPendientes.remove(offer);
            listener.onOfferExpired(offer);
        }, this.delay, TimeUnit.SECONDS);
        ofertasPendientes.put(offer, expirationTask);
    }

    public boolean hasOfferTo(UUID player) {
        for (Offer offer : this.ofertasPendientes.keySet()) {
            if (offer.getComprador().equals(player)) {
                return true;
            }
        }
        return false;
    }

    public boolean removeOffer(UUID player) {
        // Buscar la primera oferta que coincida con el vendedor o comprador
        Map.Entry<Offer, ScheduledFuture<?>> entryToRemove = this.ofertasPendientes.entrySet().stream()
                .filter(entry -> {
                    Offer offer = entry.getKey();
                    return offer.getVendedor().equals(player) || offer.getComprador().equals(player);
                }).findFirst().orElse(null);
        if (entryToRemove != null) {
            entryToRemove.getValue().cancel(false);
            this.ofertasPendientes.remove(entryToRemove.getKey());
            return true;
        }
        return false;
    }

    public Offer getOffer(UUID playerId) {
        return this.ofertasPendientes.keySet().stream()
                .filter(offer -> offer.getVendedor().equals(playerId))
                .findFirst()
                .orElse(null);
    }
}
