package BlockDynasty.BukkitImplementation.listeners;

import BlockDynasty.BukkitImplementation.utils.UtilServer;
import BlockDynasty.Economy.aplication.events.EventManager;
import BlockDynasty.Economy.domain.events.transactionsEvents.DepositEvent;
import BlockDynasty.Economy.domain.events.transactionsEvents.PayEvent;
import BlockDynasty.Economy.domain.events.transactionsEvents.SetEvent;
import BlockDynasty.Economy.domain.events.transactionsEvents.WithdrawEvent;

public class TransactionsListener {

    public TransactionsListener (EventManager eventManager) {
        eventManager.subscribe(PayEvent.class,  event -> {
            UtilServer.consoleLog("EVENTO DE PAGO ESCUCHADO");
        });

        eventManager.subscribe(DepositEvent.class, event -> {
            UtilServer.consoleLog( "EVENTO DE DEPOSITO ESCUCHADO");
        });

        eventManager.subscribe(WithdrawEvent.class, event -> {
            UtilServer.consoleLog(  "EVENTO DE EXTRACCION ESCUCHADO");
        });

        eventManager.subscribe(SetEvent.class, event -> {
            UtilServer.consoleLog( "EVENTO DE SET ESCUCHADO");
        });
    }

    public static TransactionsListener register(EventManager eventManager) {
       return  new TransactionsListener(eventManager);
    }
}
