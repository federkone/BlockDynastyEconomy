package me.BlockDynasty.Economy.domain.Offers;

import me.BlockDynasty.Economy.BlockDynastyEconomy;
import me.BlockDynasty.Economy.config.file.MessageService;
import me.BlockDynasty.Economy.domain.currency.Currency;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import me.BlockDynasty.Economy.config.file.F;

import java.math.BigDecimal;
import java.util.*;

public class OfferManager {
    public final BlockDynastyEconomy plugin;
    public List<Offer> ofertasPendientes;;

    public OfferManager(BlockDynastyEconomy plugin) {
        this.plugin = plugin;
    	this.ofertasPendientes = new ArrayList<>();

    }

    public void addOffer(Offer offer) {
        this.ofertasPendientes.add(offer);

        //todo: test
        BukkitRunnable expirationTask = new BukkitRunnable() {
            @Override
            public void run() {
                boolean expiredOffer = ofertasPendientes.remove(offer);

                if (expiredOffer) {
                    Player receiverPlayer = Bukkit.getPlayer(offer.getComprador());
                    Player senderPlayer = Bukkit.getPlayer(offer.getVendedor());
                    if (receiverPlayer != null) {
                        assert senderPlayer != null;
                        receiverPlayer.sendMessage(F.getOfferExpiredTo(senderPlayer.getName()));
                    }
                    if (senderPlayer != null) {
                        assert receiverPlayer != null;
                        senderPlayer.sendMessage(F.getOfferExpired(receiverPlayer.getName()));
                    }
                }
            }
        };
        expirationTask.runTaskLater(plugin, 1200); // 60 seconds = 1200 ticks
        offer.setExpirationTask(expirationTask);
    }

    public boolean containsOffer(UUID player) {  //todo: quizas tenga que cambiar la logica, solor si  hay una ofera de un vendedor hacia un comprador deberia retornar true
        for (Offer offer : this.ofertasPendientes) {
            if (offer.getVendedor().equals(player)) {
                return true;
            }
        }
        return false;
    }

    /*public boolean containsOffer(UUID player){
        for (Offer offer : this.ofertasPendientes) {
            if (offer.getVendedor().equals(player) || offer.getComprador().equals(player)) {
                return true;
            }
        }
        return false;
    }*/
    public boolean hasOfferTo(UUID player){ //ya tienes una oferta hecha hacia ese jugador
        for (Offer offer : this.ofertasPendientes) {
            if (offer.getComprador().equals(player)) {
                return true;
            }
        }
        return false;
    }
    public void addOffer(UUID id, Offer oferta) {
    	//this.ofertasPendientes.add(id, oferta);
    }

    public boolean removeOffer(UUID player) { //remover la oferte de uun ofertante o comprador
        Offer offerToRemove = this.ofertasPendientes.stream()
                .filter(offer -> offer.getVendedor().equals(player) || offer.getComprador().equals(player))
                .findFirst()
                .orElse(null);

        if (offerToRemove != null) {
            offerToRemove.cancelExpirationTask();
            this.ofertasPendientes.remove(offerToRemove);
            return true;
        }else {
            return false;
        }
    }

   public Offer getOffer(UUID playerId) { //obtener la oferta que hizo un vendedor
        return this.ofertasPendientes.stream()
                .filter(offer -> offer.getVendedor().equals(playerId))
                .findFirst()
                .orElse(null);
    }

    public Offer getOfferComprador(UUID playerId) { //obtener la oferta que tiene un comprador
        for (Offer offer : this.ofertasPendientes) {
            if (offer.getComprador().equals(playerId)) {
                return offer;
            }
        }
        return null;
    }
}
