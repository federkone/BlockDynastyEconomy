package BlockDynasty.Economy.aplication.services;

import BlockDynasty.Economy.aplication.listeners.OfferListener;
import BlockDynasty.Economy.domain.entities.currency.Currency;
import BlockDynasty.Economy.domain.entities.offers.Offer;
import BlockDynasty.Economy.domain.services.IOfferService;
import java.math.BigDecimal;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.*;
import java.util.stream.Collectors;

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

        //prevent multiple offers from the same sender to the same receiver
        ScheduledFuture<?> oldTask = this.ofertasPendientes.get(offer);
        if (oldTask != null) {
            oldTask.cancel(false);
            ofertasPendientes.remove(offer);
        }

        ScheduledFuture<?> expirationTask = scheduler.schedule(() -> {
            ofertasPendientes.remove(offer);
            listener.onOfferExpired(offer);
        }, this.delay, TimeUnit.SECONDS);

        ofertasPendientes.put(offer, expirationTask);
        listener.onOfferCreated(offer); // Assuming you have a method to notify about the offer creation
    }

    //si el comprador tiene oferta
    public boolean hasOfferTo(UUID player) {
        for (Offer offer : this.ofertasPendientes.keySet()) {
            if (offer.getComprador().equals(player)) {
                return true;
            }
        }
        return false;
    }

    public boolean removeOffer(UUID player) {
        // Buscar la oferta que coincida con el vendedor o comprador
        Map.Entry<Offer, ScheduledFuture<?>> entryToRemove = this.ofertasPendientes.entrySet().stream()
                .filter(entry -> {
                    Offer offer = entry.getKey();
                    return offer.getVendedor().equals(player) || offer.getComprador().equals(player);
                }).findFirst().orElse(null);
        if (entryToRemove != null) {
            entryToRemove.getValue().cancel(false);
            this.ofertasPendientes.remove(entryToRemove.getKey());
            listener.onOfferCanceled(entryToRemove.getKey());
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

    public List<Offer> getOffersSeller(UUID playerId) {
        return this.ofertasPendientes.keySet().stream()
                .filter(offer -> offer.getVendedor().equals(playerId))
                .collect(Collectors.toList());
    }

    public List<Offer> getOffersBuyer(UUID playerId) {
        return this.ofertasPendientes.keySet().stream()
                .filter(offer -> offer.getComprador().equals(playerId))
                .collect(Collectors.toList());
    }
}
