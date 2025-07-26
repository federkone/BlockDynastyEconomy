package BlockDynasty.BukkitImplementation.listeners;

import BlockDynasty.BukkitImplementation.config.file.Message;
import BlockDynasty.Economy.aplication.listeners.OfferListener;
import BlockDynasty.Economy.domain.entities.offers.Offer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class OfferListenerImpl implements OfferListener {
    @Override
    public void onOfferExpired(Offer offer) {
        Player receiver = Bukkit.getPlayer(offer.getComprador());
        Player sender = Bukkit.getPlayer(offer.getVendedor());

        if (receiver != null && sender != null) {
            receiver.sendMessage(Message.getOfferExpiredTo(sender.getName()));
            sender.sendMessage(Message.getOfferExpired(receiver.getName()));
        }
    }
}