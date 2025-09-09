package listeners;

import BlockDynasty.Economy.aplication.events.EventManager;
import BlockDynasty.Economy.domain.events.transactionsEvents.*;
import lib.commands.abstractions.PlatformAdapter;
import lib.commands.abstractions.Source;

public class TransactionsListener {
    private static PlatformAdapter platformAdapter;

    //ejemplos:
    public static void register(EventManager eventManager, PlatformAdapter platformAdapter) {
        TransactionsListener.platformAdapter = platformAdapter;

        eventManager.subscribe(PayEvent.class, event -> {
            Main.Console.debug("Event PayEvent emitted: " +event);
            //informar a ambos jugadores que se ha realizado la transaccion
            Source player = platformAdapter.getPlayer(event.getPayer().getNickname());
            Source target = platformAdapter.getPlayer(event.getReceived().getNickname());

            if (player == null || target == null) {
                Main.Console.debug("Player not found for PayEvent: " + event);
                return;
            }

            //player.sendMessage(messageService.getSuccessMessage(player.getName(), target.getName(), event.getCurrency().getSingular(), event.getAmount()));
            player.sendMessage("You have paid " + event.getAmount() + " " + event.getCurrency().getSingular() + " to " + target.getName());
            target.sendMessage("You have received " + event.getAmount() + " " + event.getCurrency().getSingular() + " from " + player.getName());
        });

        eventManager.subscribe(ExchangeEvent.class, event -> {
            /*Main.Console.debug("Event ExchangeEvent emitted: "+event);
            messageService.getExchangeSuccess(event.getFromCurrency().getSingular(), event.getAmount(), event.getToCurrency().getSingular());*/
            Source player = platformAdapter.getPlayer(event.getPlayer().getNickname());
            if (player != null) {
                player.sendMessage("You have exchanged " + event.getAmount() + " " + event.getFromCurrency().getSingular() +
                        " for " + event.getExchangedAmount() + " " + event.getToCurrency().getSingular());
            }
        });

        //este evento se esta siendo ejecutado tambien por acceptOffer Use Case, lo cual puede servir como informe unificado para todos los casos de trade
        eventManager.subscribe(TradeEvent.class, event -> {
            Main.Console.debug("Event Trade emitted: "+event);

            Source sender = platformAdapter.getPlayer(event.getFromPlayer().getNickname());
            Source receiver = platformAdapter.getPlayer(event.getToPlayer().getNickname());

            if (sender != null ) {
                sender.sendMessage("You have trade " + event.getAmountFrom() + " " + event.getCurrencyFrom().getSingular() +
                        " to " + receiver.getName() + " for " + event.getAmountTo() + " " + event.getCurrencyTo().getSingular());
                //sender.playSound(receiver.getLocation(), "entity.villager.yes", 1.0f, 1.0f);
            }
            if (receiver != null){
                receiver.sendMessage("You have received " + event.getAmountFrom() + " " + event.getCurrencyFrom().getSingular() +
                        " from " + sender.getName() + " for " + event.getAmountTo() + " " + event.getCurrencyTo().getSingular());
                //receiver.playSound(receiver.getLocation(), "entity.villager.yes" , 1.0f, 1.0f);
            }

        });

        eventManager.subscribe(TransferEvent.class, event -> {
            Main.Console.debug("Event TransferEvent emitted: "+event);

            Source player = platformAdapter.getPlayer(event.getFromPlayer().getNickname());
            if (player != null) {
                player.sendMessage("You have transferred " + event.getAmount() + " " + event.getCurrency().getSingular() +
                        " to " + event.getToPlayer().getNickname());
            }
        });


        //no puedo informar aqui, ya que estas transacciones son invasivas y no se deben informar siempre al jugador
        //eventManager.subscribe(DepositEvent.class, event -> {
         //   Main.Console.debug("Event DepositEvent emitted: " + event);

            //Source player = platformAdapter.getPlayer(event.getPlayer().getNickname());
            //if (player != null) {
             //   player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);
            //} //not work ENTITY_EXPERIENCE_ORB_PICKUP in 1.8

            //mandar aviso al canal de bungee con courier??? desacoplar los casos de uso de CourierImpl
        //});

        /*eventManager.subscribe(WithdrawEvent.class, event -> {
            Main.Console.debug("Event WithdrawEvent emitted: "+event);
        });

        eventManager.subscribe(SetEvent.class, event -> {
            Main.Console.debug("Event SetEvent emitted: "+event);
        });*/
    }
}
