package BlockDynasty.BukkitImplementation.listeners;

import BlockDynasty.BukkitImplementation.GUI.RegisterGuiModule;
import BlockDynasty.BukkitImplementation.config.file.Message;
import BlockDynasty.Economy.aplication.listeners.OfferListener;
import BlockDynasty.Economy.domain.entities.currency.Currency;
import BlockDynasty.Economy.domain.entities.offers.Offer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.math.BigDecimal;

public class OfferListenerImpl implements OfferListener {
    @Override
    public void onOfferExpired(Offer offer) {
        Player receiver = Bukkit.getPlayer(offer.getComprador());
        Player sender = Bukkit.getPlayer(offer.getVendedor());

        if (receiver != null && sender != null) {
            receiver.sendMessage(Message.getOfferExpiredTo(sender.getName()));
            sender.sendMessage(Message.getOfferExpired(receiver.getName()));
        }

        RegisterGuiModule.getGuiService().refresh(offer.getComprador());
    }

    @Override
    public void onOfferCreated(Offer offer) {
        Player receiver = Bukkit.getPlayer(offer.getComprador());
        Player sender = Bukkit.getPlayer(offer.getVendedor());
        Currency currencyOffered = offer.getTipoCantidad();
        BigDecimal amountOffered = offer.getCantidad();
        Currency currencyValue = offer.getTipoMonto();
        BigDecimal amountValue = offer.getMonto();

        if (receiver != null && sender != null) {
            //receiver.sendMessage(Message.getOfferSendMessage(target.getName(),tipoCantidad,BigDecimal.valueOf(cantidad),tipoMonto,BigDecimal.valueOf(monto)));
            //sender.sendMessage(Message.getOfferReceiveMessage(player.getName(),tipoCantidad,BigDecimal.valueOf(cantidad),tipoMonto,BigDecimal.valueOf(monto)));
            sender.sendMessage("You have sent an offer to " + receiver.getName() +
                    " offering " + amountOffered + " " + currencyOffered.getSingular() +
                    " in exchange for " + amountValue + " " + currencyValue.getSingular());

            receiver.sendMessage("You have received an offer from " + sender.getName() +
                    " offering " + amountOffered + " " + currencyOffered.getSingular() +
                    " in exchange for " + amountValue + " " + currencyValue.getSingular());
            //testing
            receiver.playSound(receiver.getLocation(), "block.note_block.pling" , 1.0f, 1.0f);
        }
        RegisterGuiModule.getGuiService().refresh(offer.getComprador());
    }

    @Override
    public void onOfferCanceled(Offer offer) {
        RegisterGuiModule.getGuiService().refresh(offer.getComprador());
        RegisterGuiModule.getGuiService().refresh(offer.getVendedor());

        Player sender = Bukkit.getPlayer(offer.getVendedor());
        Player receiver = Bukkit.getPlayer(offer.getComprador());
        /*if (receiver != null) {
            receiver.playSound(receiver.getLocation(), "entity.villager.no" , 1.0f, 1.0f);
        }
        if (sender != null) {
            sender.playSound(sender.getLocation(), "entity.villager.no" , 1.0f, 1.0f);
        }*/
    }
}