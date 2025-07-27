package BlockDynasty.BukkitImplementation.listeners;

import BlockDynasty.BukkitImplementation.utils.UtilServer;
import BlockDynasty.Economy.aplication.events.EventManager;
import BlockDynasty.Economy.domain.events.transactionsEvents.DepositEvent;
import BlockDynasty.Economy.domain.events.transactionsEvents.PayEvent;
import BlockDynasty.Economy.domain.events.transactionsEvents.SetEvent;
import BlockDynasty.Economy.domain.events.transactionsEvents.WithdrawEvent;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class TransactionsListener {

    //todo: por ejemplo aqui informar a los jugadores sobre el estado de la transaccion
    //ejemplos:
    public static void register(EventManager eventManager) {
        eventManager.subscribe(PayEvent.class,  event -> {
            UtilServer.consoleLog("Event PayEvent emitted");
        });

        eventManager.subscribe(DepositEvent.class, event -> {
            UtilServer.consoleLog( "Event DepositEvent emitted");
            //Player player = Bukkit.getPlayer(event.getPlayer().getNickname());
            //player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);
        });

        eventManager.subscribe(WithdrawEvent.class, event -> {
            UtilServer.consoleLog(  "Event WithdrawEvent emitted");
        });

        eventManager.subscribe(SetEvent.class, event -> {
            UtilServer.consoleLog( "Event SetEvent emitted");
        });
    }
}
