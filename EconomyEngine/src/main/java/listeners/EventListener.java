package listeners;

import BlockDynasty.Economy.aplication.events.EventManager;
import BlockDynasty.Economy.domain.entities.currency.Currency;
import BlockDynasty.Economy.domain.entities.offers.Offer;
import BlockDynasty.Economy.domain.events.Context;
import BlockDynasty.Economy.domain.events.offersEvents.OfferCanceled;
import BlockDynasty.Economy.domain.events.offersEvents.OfferCreated;
import BlockDynasty.Economy.domain.events.offersEvents.OfferExpired;
import BlockDynasty.Economy.domain.events.transactionsEvents.*;
import lib.abstractions.PlatformAdapter;
import lib.commands.abstractions.IEntityCommands;
import lib.gui.GUIFactory;
import lib.gui.templates.abstractions.ChatColor;
import lib.scheduler.ContextualTask;

import java.math.BigDecimal;

public class EventListener {

    public static void register(EventManager eventManager, PlatformAdapter platformAdapter) {

        eventManager.subscribe(PayEvent.class, event -> {
            //Main.Console.debug("Event PayEvent emitted: " +event);
            IEntityCommands player = platformAdapter.getPlayer(event.getPayer().getNickname());
            IEntityCommands target = platformAdapter.getPlayer(event.getReceived().getNickname());

            Currency currency = event.getCurrency();
            String format = currency.format(event.getAmount());
            String colorCode = ChatColor.stringValueOf(currency.getColor());
            String receiverName = event.getReceived().getNickname();
            String senderName = event.getPayer().getNickname();

            if (player != null){
                player.sendMessage("&7You paid " + colorCode+format + "&7 to " + receiverName);
                player.playNotificationSound();
            }
            if (target != null){
                target.sendMessage("&7You received " + colorCode+format + "&7 from " +senderName);
                target.playNotificationSound();
            }
        });

        eventManager.subscribe(TransferEvent.class, event -> {
            //Main.Console.debug("Event TransferEvent emitted: "+event);
            IEntityCommands player = platformAdapter.getPlayer(event.getFromPlayer().getNickname());
            IEntityCommands target = platformAdapter.getPlayer(event.getToPlayer().getNickname());

            Currency currency = event.getCurrency();
            String format = currency.format(event.getAmount());
            String colorCode = ChatColor.stringValueOf(currency.getColor());
            String receiverName = event.getToPlayer().getNickname();
            String senderName = event.getFromPlayer().getNickname();


            if (player != null) {
                player.sendMessage("&7You transferred " + colorCode+format + "&7 to " + receiverName);
                player.playNotificationSound();

                Runnable task=()->{
                    GUIFactory.getGuiService().refresh(player.getUniqueId());
                };
                platformAdapter.getScheduler().run(ContextualTask.build(task , player));
            }

            if (target != null) {
                target.sendMessage("&7You received " + colorCode+format + "&7 from " + senderName);
                target.playNotificationSound();

                Runnable task=()->{
                    GUIFactory.getGuiService().refresh(target.getUniqueId());
                };
                platformAdapter.getScheduler().run(ContextualTask.build(task , target));
            }
        });

        eventManager.subscribe(ExchangeEvent.class, event -> {
            //Main.Console.debug("Event ExchangeEvent emitted: "+event);
            IEntityCommands player = platformAdapter.getPlayer(event.getPlayer().getNickname());

            Currency fromCurrency = event.getFromCurrency();
            String fromFormat = fromCurrency.format(event.getAmount());
            String fromColorCode = ChatColor.stringValueOf(fromCurrency.getColor());

            Currency toCurrency = event.getToCurrency();
            String toFormat = toCurrency.format(event.getExchangedAmount());
            String toColorCode = ChatColor.stringValueOf(toCurrency.getColor());


            if (player != null) {
                player.sendMessage("&7You exchanged " + fromColorCode + fromFormat + "&7 to " + toColorCode + toFormat+"&7.");
                player.playNotificationSound();
            }
        });

        eventManager.subscribe(TradeEvent.class, event -> {
            //Main.Console.debug("Event Trade emitted: "+event);
            IEntityCommands sender = platformAdapter.getPlayer(event.getFromPlayer().getNickname());
            IEntityCommands receiver = platformAdapter.getPlayer(event.getToPlayer().getNickname());

            Currency fromCurrency = event.getCurrencyFrom();
            String fromFormat = fromCurrency.format(event.getAmountFrom());
            String fromColorCode = ChatColor.stringValueOf(fromCurrency.getColor());

            Currency toCurrency = event.getCurrencyTo();
            String toFormat = toCurrency.format(event.getAmountTo());
            String toColorCode = ChatColor.stringValueOf(toCurrency.getColor());


            if (sender != null ) {
                sender.sendMessage("&7You trade " +fromColorCode+fromFormat + " &7to " + event.getToPlayer().getNickname() + " &7for " + toColorCode + toFormat+"&7.");
                sender.playNotificationSound();

                Runnable task=()->{
                    GUIFactory.getGuiService().refresh(sender.getUniqueId());
                };
                platformAdapter.getScheduler().run(ContextualTask.build(task , sender));
            }
            if (receiver != null){
                receiver.sendMessage("&7You received " + fromColorCode+fromFormat + " &7from " + event.getFromPlayer().getNickname()+ " &7for " + toColorCode + toFormat+"&7.");
                receiver.playNotificationSound();

                Runnable task=()->{
                    GUIFactory.getGuiService().refresh(receiver.getUniqueId());
                };
                platformAdapter.getScheduler().run(ContextualTask.build(task , receiver));
            }

        });

        eventManager.subscribe(DepositEvent.class, event -> {
         //   Main.Console.debug("Event DepositEvent emitted: " + event);

            if (event.getContext() == Context.COMMAND){
                IEntityCommands player = platformAdapter.getPlayer(event.getPlayer().getNickname());
                if (player != null) {
                    player.sendMessage( "&7Has received a deposit " +ChatColor.stringValueOf(event.getCurrency().getColor()) +event.getCurrency().format(event.getAmount()) + "&7." );
                    player.playNotificationSound();
                }
            }

        });

        //todo: add context to WithdrawEvent
        eventManager.subscribe(WithdrawEvent.class, event -> {
            //Main.Console.debug("Event WithdrawEvent emitted: "+event);
            if (event.getContext() == Context.COMMAND){
                IEntityCommands player = platformAdapter.getPlayer(event.getPlayer().getNickname());
                if (player != null) {
                    player.sendMessage( "&7Has extracted " +ChatColor.stringValueOf(event.getCurrency().getColor()) +event.getCurrency().format(event.getAmount()) + "&7." );
                    player.playNotificationSound();
                }
            }
        });

        //todo: add context to SetEvent
        eventManager.subscribe(SetEvent.class, event -> {
            //Main.Console.debug("Event SetEvent emitted: "+event);
            if (event.getContext() == Context.COMMAND){
                IEntityCommands player = platformAdapter.getPlayer(event.getPlayer().getNickname());
                if (player != null) {
                    player.sendMessage( "&7your balance has been established  " +ChatColor.stringValueOf(event.getCurrency().getColor()) +event.getCurrency().format(event.getAmount()) + "&7." );
                    player.playNotificationSound();
                }
            }
        });

        eventManager.subscribe(OfferCreated.class, event -> {
            Offer offer = event.getOffer();
            IEntityCommands receiver = platformAdapter.getPlayerByUUID(offer.getComprador().getUuid());
            IEntityCommands sender = platformAdapter.getPlayerByUUID(offer.getVendedor().getUuid());

            Currency currencyOffered = offer.getTipoCantidad();
            BigDecimal amountOffered = offer.getCantidad();
            Currency currencyValue = offer.getTipoMonto();
            BigDecimal amountValue = offer.getMonto();

            if (sender != null) {
                sender.sendMessage("You have sent an offer to " + offer.getComprador().getNickname() +
                        " offering " + amountOffered + " " + currencyOffered.getSingular() +
                        " in exchange for " + amountValue + " " + currencyValue.getSingular());
            }
            if (receiver != null){
                receiver.sendMessage("You have received an offer from " + offer.getVendedor().getNickname() +
                        " offering " + amountOffered + " " + currencyOffered.getSingular() +
                        " in exchange for " + amountValue + " " + currencyValue.getSingular());
                receiver.playNotificationSound();


                Runnable task1=()->{
                    GUIFactory.getGuiService().refresh(offer.getComprador().getUuid());
                };
                platformAdapter.getScheduler().run(ContextualTask.build(task1 , receiver));
            }


        });

        eventManager.subscribe(OfferCanceled.class, event -> {
            Offer offer = event.getOffer();
            IEntityCommands receiver = platformAdapter.getPlayerByUUID(offer.getComprador().getUuid());
            IEntityCommands sender = platformAdapter.getPlayerByUUID(offer.getVendedor().getUuid());


            if (receiver != null ) {
                receiver.sendMessage(" Your offer from " +offer.getVendedor().getNickname() + " has been canceled.");

                Runnable task = () -> {
                    GUIFactory.getGuiService().refresh(receiver.getUniqueId());
                };

                platformAdapter.getScheduler().run(ContextualTask.build(task , receiver));
            }


            if (sender != null){
                sender.sendMessage(" The offer to " + offer.getComprador().getNickname()  + " has been canceled.");

                Runnable task = () -> {
                    GUIFactory.getGuiService().refresh(sender.getUniqueId());
                };

                platformAdapter.getScheduler().run(ContextualTask.build(task , sender));
            }
        });

        eventManager.subscribe(OfferExpired.class, event -> {
            Offer offer = event.getOffer();
            IEntityCommands receiver = platformAdapter.getPlayerByUUID(offer.getComprador().getUuid());
            IEntityCommands sender = platformAdapter.getPlayerByUUID(offer.getVendedor().getUuid());


            if (receiver != null ) {
                receiver.sendMessage(" Your offer from " +offer.getVendedor().getNickname() + " has expired.");
                Runnable task = () -> {
                    GUIFactory.getGuiService().refresh(receiver.getUniqueId());
                };
                platformAdapter.getScheduler().run(ContextualTask.build(task , receiver));
            }

            if (sender != null){
                sender.sendMessage(" The offer to " + offer.getComprador().getNickname()  + " has expired.");

                Runnable task = () -> {
                    GUIFactory.getGuiService().refresh(sender.getUniqueId());
                };
                platformAdapter.getScheduler().run(ContextualTask.build(task , sender));
            }

        });
    }
}
