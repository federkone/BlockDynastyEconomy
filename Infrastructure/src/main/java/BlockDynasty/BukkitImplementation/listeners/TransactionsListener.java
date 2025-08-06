package BlockDynasty.BukkitImplementation.listeners;

import BlockDynasty.BukkitImplementation.scheduler.ContextualTask;
import BlockDynasty.BukkitImplementation.scheduler.Scheduler;
import BlockDynasty.BukkitImplementation.services.MessageService;
import BlockDynasty.BukkitImplementation.utils.UtilServer;
import BlockDynasty.Economy.aplication.events.EventManager;
import BlockDynasty.Economy.domain.events.transactionsEvents.*;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class TransactionsListener {

    //todo: subscribe a los eventos de transacciones de la aplicacion/core
    //ejemplos:
    public static void register(EventManager eventManager, MessageService messageService) {
        eventManager.subscribe(PayEvent.class, event -> {
            UtilServer.consoleLog("Event PayEvent emitted: " +event);
            //informar a ambos jugadores que se ha realizado la transaccion
            Player player = Bukkit.getPlayer(event.getPayer().getNickname());
            Player target = Bukkit.getPlayer(event.getReceived().getNickname());

            if (player == null || target == null) {
                UtilServer.consoleLog("Player not found for PayEvent: " + event);
                return;
            }

            player.sendMessage(messageService.getSuccessMessage(player.getName(), target.getName(), event.getCurrency().getSingular(), event.getAmount()));
            target.sendMessage(messageService.getReceivedMessage(player.getName(), event.getCurrency().getSingular(), event.getAmount()));
        });

        eventManager.subscribe(ExchangeEvent.class, event -> {
            UtilServer.consoleLog("Event ExchangeEvent emitted: "+event);
            messageService.getExchangeSuccess(event.getFromCurrency().getSingular(), event.getAmount(), event.getToCurrency().getSingular());
        });

        //este evento se esta siendo ejecutado tambien por acceptOffer Use Case, lo cual puede servir como informe unificado para todos los casos de trade
        eventManager.subscribe(TradeEvent.class, event -> {
            UtilServer.consoleLog("Event Trade emitted: "+event);

            //Player player = Bukkit.getPlayer(event.getFromPlayer().getNickname());
            //Player target = Bukkit.getPlayer(event.getToPlayer().getNickname());

            //if (player == null || target == null) {
            //    UtilServer.consoleLog("Player not found for PayEvent: " + event);
            //    return;
            //}
        });

        eventManager.subscribe(TransferEvent.class, event -> {
            UtilServer.consoleLog("Event TransferEvent emitted: "+event);

        });


        //no puedo informar aqui, ya que estas transacciones son invasivas y no se deben informar siempre al jugador
        eventManager.subscribe(DepositEvent.class, event -> {
            UtilServer.consoleLog("Event DepositEvent emitted: " + event);

            //Player player = Bukkit.getPlayer(event.getPlayer().getNickname());
            //if (player != null) {
            //    player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);
            //} //not work ENTITY_EXPERIENCE_ORB_PICKUP in 1.8

            //mandar aviso al canal de bungee con courier??? desacoplar los casos de uso de CourierImpl
        });

        eventManager.subscribe(WithdrawEvent.class, event -> {
            UtilServer.consoleLog("Event WithdrawEvent emitted: "+event);
        });

        eventManager.subscribe(SetEvent.class, event -> {
            UtilServer.consoleLog("Event SetEvent emitted: "+event);
        });


    }
}
