package BlockDynasty.Economy.aplication.services;

import BlockDynasty.Economy.aplication.events.EventManager;
import BlockDynasty.Economy.aplication.events.EventRegistry;
import BlockDynasty.Economy.domain.entities.account.Player;
import BlockDynasty.Economy.domain.entities.currency.Currency;
import BlockDynasty.Economy.domain.entities.offers.Offer;
import BlockDynasty.Economy.domain.events.Event;
import BlockDynasty.Economy.domain.events.SerializableEvent;
import BlockDynasty.Economy.domain.events.offersEvents.*;
import BlockDynasty.Economy.domain.services.IOfferService;
import BlockDynasty.Economy.domain.services.courier.Courier;

import java.math.BigDecimal;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.*;
import java.util.stream.Collectors;

public class OfferService implements IOfferService {
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private final Map<Offer, ScheduledFuture<?>> ofertasPendientes = new ConcurrentHashMap<>();
    private final Courier courier;
    private final EventManager eventManager;
    private int delay = 60; // Default delay in seconds

    public OfferService(Courier courier,EventManager eventManager, int delay) {
        this.courier = courier;
        this.eventManager = eventManager;
        if (delay > 0) {
            this.delay = delay;
        }
    }

    public OfferService(Courier courier,EventManager eventManager) {
        this.courier = courier;
        this.eventManager = eventManager;
    }

    public void createOffer(Player playerSender, Player playerReceiver, BigDecimal amountCurrencyValue, BigDecimal amountCurrencyOffer, Currency currencyValue, Currency currencyOffer) {
        Offer offer = new Offer(playerSender, playerReceiver, amountCurrencyValue, amountCurrencyOffer, currencyValue, currencyOffer);
        createOffer(offer);
    }

    public void createOffer(Offer offer){
        //prevent multiple offers from the same sender to the same receiver
        addOffer(offer);
        eventManager.emit( new OfferCreated(offer));
        courier.sendUpdateMessage("event",new OfferCreated(offer).toJson(), offer.getComprador().getUuid().toString());
    }

    public void addOffer(Offer offer){
        ScheduledFuture<?> oldTask = this.ofertasPendientes.get(offer);
        if (oldTask != null) {
            oldTask.cancel(false);
            this.ofertasPendientes.remove(offer);
        }

        ScheduledFuture<?> expirationTask = scheduler.schedule(() -> {
            this.ofertasPendientes.remove(offer);
            this.eventManager.emit(new OfferExpired(offer));
            courier.sendUpdateMessage("event", new OfferExpired(offer).toJson(), offer.getComprador().getUuid().toString());
            //courier.sendUpdateMessage("event", new OfferExpired(offer).toJson(), offer.getVendedor().getUuid().toString());
        }, this.delay, TimeUnit.SECONDS);

        ofertasPendientes.put(offer, expirationTask);
    }

    public void addOfferFromEvent(Offer offer){
        ScheduledFuture<?> oldTask = this.ofertasPendientes.get(offer);
        if (oldTask != null) {
            oldTask.cancel(false);
            this.ofertasPendientes.remove(offer);
        }

        ScheduledFuture<?> expirationTask = scheduler.schedule(() -> {
            this.ofertasPendientes.remove(offer);
            this.eventManager.emit(new OfferExpired(offer));
            //courier.sendUpdateMessage("event", new OfferExpired(offer).toJson(), offer.getComprador().getUuid().toString());
            //courier.sendUpdateMessage("event", new OfferExpired(offer).toJson(), offer.getVendedor().getUuid().toString());
        }, this.delay, TimeUnit.SECONDS);

        ofertasPendientes.put(offer, expirationTask);
    }

    public void removeOffer(Offer offer){
        ScheduledFuture<?> oldTask = this.ofertasPendientes.get(offer);
        if (oldTask != null) {
            oldTask.cancel(false);
            this.ofertasPendientes.remove(offer);
        }
    }

    public boolean hasOfferTo(UUID player) {
        for (Offer offer : this.ofertasPendientes.keySet()) {
            if (offer.getComprador().getUuid().equals(player)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void processNetworkEvent(String data) {
        Event event = EventRegistry.deserializeEvent(data);
        if (event instanceof OfferEvent){
            ((OfferEvent) event).handle(this);
        }
    }

    //como no lo encuentra de manera local no emite evento?
    public boolean cancelOffer(UUID initiator) {
        // Buscar la oferta que coincida con el vendedor o comprador
        Map.Entry<Offer, ScheduledFuture<?>> entryToRemove = this.ofertasPendientes.entrySet().stream()
                .filter(entry -> {
                    Offer offer = entry.getKey();
                    return offer.getVendedor().getUuid().equals(initiator) || offer.getComprador().getUuid().equals(initiator);
                }).findFirst().orElse(null);
        if (entryToRemove != null) {
            Offer offer = entryToRemove.getKey();
            entryToRemove.getValue().cancel(false);
            this.ofertasPendientes.remove(offer);

            eventManager.emit(new OfferCanceled(offer));
            courier.sendUpdateMessage("event", new OfferCanceled(offer).toJson(), offer.getComprador().getUuid().toString());
            courier.sendUpdateMessage("event", new OfferCanceled(offer).toJson(), offer.getVendedor().getUuid().toString());
            return true;
        }
        return false;
    }

    public boolean acceptOffer(UUID initiator) {
        Map.Entry<Offer, ScheduledFuture<?>> entryToRemove = this.ofertasPendientes.entrySet().stream()
                .filter(entry -> {
                    Offer offer = entry.getKey();
                    return offer.getVendedor().getUuid().equals(initiator) || offer.getComprador().getUuid().equals(initiator);
                }).findFirst().orElse(null);
        if (entryToRemove != null) {
            Offer offer = entryToRemove.getKey();
            entryToRemove.getValue().cancel(false);
            this.ofertasPendientes.remove(offer);

            //eventManager.emit(new OfferAccepted(offer));
            courier.sendUpdateMessage("event", new OfferAccepted(offer).toJson(), offer.getComprador().getUuid().toString());
            courier.sendUpdateMessage("event", new OfferAccepted(offer).toJson(), offer.getVendedor().getUuid().toString());
            return true;
        }
        return false;
    }

    public Offer getOffer(UUID playerId) {
        return this.ofertasPendientes.keySet().stream()
                .filter(offer ->offer.getVendedor().getUuid().equals(playerId))
                .findFirst()
                .orElse(null);
    }

    public List<Offer> getOffersSeller(UUID playerId) {
        return this.ofertasPendientes.keySet().stream()
                .filter(offer -> offer.getVendedor().getUuid().equals(playerId))
                .collect(Collectors.toList());
    }

    public List<Offer> getOffersBuyer(UUID playerId) {
        return this.ofertasPendientes.keySet().stream()
                .filter(offer -> offer.getComprador().getUuid().equals(playerId))
                .collect(Collectors.toList());
    }
}
