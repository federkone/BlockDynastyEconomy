package me.BlockDynasty.Economy.Infrastructure.BukkitImplementation.listeners;

import me.BlockDynasty.Economy.Infrastructure.BukkitImplementation.config.file.F;
import me.BlockDynasty.Economy.aplication.listeners.OfferListener;
import me.BlockDynasty.Economy.domain.entities.offers.Offer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class OfferListenerImpl implements OfferListener {
    @Override
    public void onOfferExpired(Offer offer) {
        Player receiver = Bukkit.getPlayer(offer.getComprador());
        Player sender = Bukkit.getPlayer(offer.getVendedor());

        if (receiver != null && sender != null) {
            receiver.sendMessage(F.getOfferExpiredTo(sender.getName()));
            sender.sendMessage(F.getOfferExpired(receiver.getName()));
        }
    }
}