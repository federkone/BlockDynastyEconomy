package BlockDynasty.BukkitImplementation.listeners;

import BlockDynasty.BukkitImplementation.utils.UtilServer;
import BlockDynasty.Economy.aplication.events.EventManager;
import BlockDynasty.Economy.domain.events.transactionsEvents.*;
import jdk.jshell.execution.Util;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class TransactionsListener {

    //todo: por ejemplo aqui informar a los jugadores sobre el estado de la transaccion
    //ejemplos:
    public static void register(EventManager eventManager) {
        eventManager.subscribe(PayEvent.class, event -> {
            UtilServer.consoleLog("Event PayEvent emitted: " +event);
        });

        eventManager.subscribe(DepositEvent.class, event -> {
            UtilServer.consoleLog("Event DepositEvent emitted: " + event);
            event.getCurrency().setColor("WHITE");
            //Player player = Bukkit.getPlayer(event.getPlayer().getNickname());
            //player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);

            //mandar aviso al canal de bungee con courier??? desacoplar los casos de uso de CourierImpl
        });

        eventManager.subscribe(WithdrawEvent.class, event -> {
            UtilServer.consoleLog("Event WithdrawEvent emitted: "+event);
        });

        eventManager.subscribe(SetEvent.class, event -> {
            UtilServer.consoleLog("Event SetEvent emitted: "+event);
        });

        eventManager.subscribe(ExchangeEvent.class, event -> {
            UtilServer.consoleLog("Event ExchangeEvent emitted: "+event);
        });
        eventManager.subscribe(TradeEvent.class, event -> {
            UtilServer.consoleLog("Event Trade emitted: "+event);
        });
        eventManager.subscribe(TransferEvent.class, event -> {
            UtilServer.consoleLog("Event TransferEvent emitted: "+event);
        });
    }
}
