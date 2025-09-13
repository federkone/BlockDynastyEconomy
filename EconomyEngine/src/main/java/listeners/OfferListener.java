package listeners;

import BlockDynasty.Economy.domain.entities.currency.Currency;
import BlockDynasty.Economy.domain.entities.offers.Offer;
import lib.abstractions.PlatformAdapter;
import lib.commands.abstractions.IEntityCommands;
import lib.gui.GUIFactory;

import java.math.BigDecimal;

public class OfferListener implements BlockDynasty.Economy.aplication.listeners.OfferListener {
    PlatformAdapter platformAdapter;

    public  OfferListener(PlatformAdapter platformAdapter){
        this.platformAdapter=platformAdapter;
    }

    @Override
    public void onOfferExpired(Offer offer) {
        IEntityCommands receiver = platformAdapter.getPlayerByUUID(offer.getComprador());
        IEntityCommands sender = platformAdapter.getPlayerByUUID(offer.getVendedor());

        if (receiver != null && sender != null) {
            receiver.sendMessage(" Your offer from " + sender.getName() + " has expired.");
            sender.sendMessage(" The offer to " + receiver.getName() + " has expired.");
        }
        GUIFactory.getGuiService().refresh(offer.getComprador());
    }

    @Override
    public void onOfferCreated(Offer offer) {
        IEntityCommands receiver = platformAdapter.getPlayerByUUID(offer.getComprador());
        IEntityCommands sender = platformAdapter.getPlayerByUUID(offer.getVendedor());
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

            receiver.playNotificationSound();
        }
        GUIFactory.getGuiService().refresh(offer.getComprador());
    }

    @Override
    public void onOfferCanceled(Offer offer) {
        GUIFactory.getGuiService().refresh(offer.getComprador());
        GUIFactory.getGuiService().refresh(offer.getVendedor());

        IEntityCommands receiver = platformAdapter.getPlayerByUUID(offer.getComprador());
        IEntityCommands sender = platformAdapter.getPlayerByUUID(offer.getVendedor());
        if (receiver != null && sender != null) {
            receiver.sendMessage(" Your offer from " + sender.getName() + " has been canceled.");
            sender.sendMessage(" The offer to " + receiver.getName() + " has been canceled.");
        }
    }
}
