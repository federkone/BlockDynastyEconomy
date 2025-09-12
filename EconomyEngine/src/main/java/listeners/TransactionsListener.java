package listeners;

import BlockDynasty.Economy.aplication.events.EventManager;
import BlockDynasty.Economy.domain.entities.currency.Currency;
import BlockDynasty.Economy.domain.events.Context;
import BlockDynasty.Economy.domain.events.transactionsEvents.*;
import lib.commands.abstractions.PlatformAdapter;
import lib.commands.abstractions.Source;
import lib.gui.templates.abstractions.ChatColor;

public class TransactionsListener {

    public static void register(EventManager eventManager, PlatformAdapter platformAdapter) {

        eventManager.subscribe(PayEvent.class, event -> {
            //Main.Console.debug("Event PayEvent emitted: " +event);
            Source player = platformAdapter.getPlayer(event.getPayer().getNickname());
            Source target = platformAdapter.getPlayer(event.getReceived().getNickname());

            Currency currency = event.getCurrency();
            String format = currency.format(event.getAmount());
            String colorCode = ChatColor.stringValueOf(currency.getColor());
            String receiverName = event.getReceived().getNickname();
            String senderName = event.getPayer().getNickname();

            if (player != null){
                player.sendMessage("&7You paid " + colorCode+format + "&7 to " + receiverName);
                player.soundNotification();
            }
            if (target != null){
                target.sendMessage("&7You received " + colorCode+format + "&7 from " +senderName);
                target.soundNotification();
            }
        });

        eventManager.subscribe(TransferEvent.class, event -> {
            //Main.Console.debug("Event TransferEvent emitted: "+event);
            Source player = platformAdapter.getPlayer(event.getFromPlayer().getNickname());
            Source target = platformAdapter.getPlayer(event.getToPlayer().getNickname());

            Currency currency = event.getCurrency();
            String format = currency.format(event.getAmount());
            String colorCode = ChatColor.stringValueOf(currency.getColor());
            String receiverName = event.getToPlayer().getNickname();
            String senderName = event.getFromPlayer().getNickname();

            if (player != null) {
                player.sendMessage("&7You transferred " + colorCode+format + "&7 to " + receiverName);
                player.soundNotification();
            }
            if (target != null) {
                target.sendMessage("&7You received " + colorCode+format + "&7 from " + senderName);
                target.soundNotification();
            }
        });

        eventManager.subscribe(ExchangeEvent.class, event -> {
            //Main.Console.debug("Event ExchangeEvent emitted: "+event);
            Source player = platformAdapter.getPlayer(event.getPlayer().getNickname());

            Currency fromCurrency = event.getFromCurrency();
            String fromFormat = fromCurrency.format(event.getAmount());
            String fromColorCode = ChatColor.stringValueOf(fromCurrency.getColor());

            Currency toCurrency = event.getToCurrency();
            String toFormat = toCurrency.format(event.getExchangedAmount());
            String toColorCode = ChatColor.stringValueOf(toCurrency.getColor());


            if (player != null) {
                player.sendMessage("&7You exchanged " + fromColorCode + fromFormat + "&7 to " + toColorCode + toFormat+"&7.");
                player.soundNotification();
            }
        });

        eventManager.subscribe(TradeEvent.class, event -> {
            //Main.Console.debug("Event Trade emitted: "+event);
            Source sender = platformAdapter.getPlayer(event.getFromPlayer().getNickname());
            Source receiver = platformAdapter.getPlayer(event.getToPlayer().getNickname());

            Currency fromCurrency = event.getCurrencyFrom();
            String fromFormat = fromCurrency.format(event.getAmountFrom());
            String fromColorCode = ChatColor.stringValueOf(fromCurrency.getColor());

            Currency toCurrency = event.getCurrencyTo();
            String toFormat = toCurrency.format(event.getAmountTo());
            String toColorCode = ChatColor.stringValueOf(toCurrency.getColor());


            if (sender != null ) {
                sender.sendMessage("You trade " +fromColorCode+fromFormat + " to " + event.getToPlayer().getNickname() + " for " + toColorCode + toFormat);
                sender.soundNotification();
            }
            if (receiver != null){
                receiver.sendMessage("You received " + fromColorCode+fromFormat + " from " + event.getFromPlayer().getNickname()+ " for " + toColorCode + toFormat);
                receiver.soundNotification();
            }

        });

        eventManager.subscribe(DepositEvent.class, event -> {
         //   Main.Console.debug("Event DepositEvent emitted: " + event);

            if (event.getContext() == Context.COMMAND){
                Source player = platformAdapter.getPlayer(event.getPlayer().getNickname());
                if (player != null) {
                    player.sendMessage( "&7Has received a deposit " +ChatColor.stringValueOf(event.getCurrency().getColor()) +event.getCurrency().format(event.getAmount()) + "&7." );
                    player.soundNotification();
                }
            }

        });

        //todo: add context to WithdrawEvent
        eventManager.subscribe(WithdrawEvent.class, event -> {
            //Main.Console.debug("Event WithdrawEvent emitted: "+event);
            if (event.getContext() == Context.COMMAND){
                Source player = platformAdapter.getPlayer(event.getPlayer().getNickname());
                if (player != null) {
                    player.sendMessage( "&7Has extracted " +ChatColor.stringValueOf(event.getCurrency().getColor()) +event.getCurrency().format(event.getAmount()) + "&7." );
                    player.soundNotification();
                }
            }
        });

        //todo: add context to SetEvent
        eventManager.subscribe(SetEvent.class, event -> {
            //Main.Console.debug("Event SetEvent emitted: "+event);
            if (event.getContext() == Context.COMMAND){
                Source player = platformAdapter.getPlayer(event.getPlayer().getNickname());
                if (player != null) {
                    player.sendMessage( "&7your balance has been established  " +ChatColor.stringValueOf(event.getCurrency().getColor()) +event.getCurrency().format(event.getAmount()) + "&7." );
                    player.soundNotification();
                }
            }
        });
    }
}
