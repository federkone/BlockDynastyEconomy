package me.BlockDynasty.Economy.Infrastructure.services;

import me.BlockDynasty.Economy.Infrastructure.BukkitImplementation.BlockDynastyEconomy;
import me.BlockDynasty.Economy.domain.offers.Offer;
import me.BlockDynasty.Economy.domain.currency.Currency;
import me.BlockDynasty.Economy.domain.services.IOfferService;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import me.BlockDynasty.Economy.Infrastructure.BukkitImplementation.config.file.F;

import java.math.BigDecimal;
import java.util.*;

public class OfferService implements IOfferService {
    public final BlockDynastyEconomy plugin;
    private final Map<Offer, BukkitRunnable> ofertasPendientes;

    public OfferService(BlockDynastyEconomy plugin) {
        this.plugin = plugin;
        this.ofertasPendientes = new HashMap<>();
    }
    public void addOffer(UUID playerSender,UUID playerReciber, BigDecimal amountCurrencyValue, BigDecimal amountCurrencyOffer, Currency currencyValue, Currency currencyOffer ){
        Offer offer = new Offer(playerSender, playerReciber, amountCurrencyValue, amountCurrencyOffer, currencyValue, currencyOffer);

        BukkitRunnable expirationTask = new BukkitRunnable() {
            @Override
            public void run() {
                BukkitRunnable expiredOffer = ofertasPendientes.remove(offer);
                if (expiredOffer != null && !expiredOffer.isCancelled()) {
                    Player receiverPlayer = Bukkit.getPlayer(offer.getComprador());
                    Player senderPlayer = Bukkit.getPlayer(offer.getVendedor());
                    if (receiverPlayer != null && senderPlayer != null) {
                        receiverPlayer.sendMessage(F.getOfferExpiredTo(senderPlayer.getName()));
                        senderPlayer.sendMessage(F.getOfferExpired(receiverPlayer.getName()));
                    }
                }
            }
        };
        this.ofertasPendientes.put(offer,expirationTask);
        expirationTask.runTaskLater(plugin, 1200); // 60 seconds = 1200 ticks
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
        Map.Entry<Offer, BukkitRunnable> entryToRemove = this.ofertasPendientes.entrySet().stream()
                .filter(entry -> {
                    Offer offer = entry.getKey();
                    return offer.getVendedor().equals(player) || offer.getComprador().equals(player);
                })
                .findFirst()
                .orElse(null);

        if (entryToRemove != null) {
            // Cancelar la tarea de expiración
            entryToRemove.getValue().cancel();

            // Remover la oferta del mapa
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
