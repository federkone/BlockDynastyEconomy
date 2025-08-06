package BlockDynasty.BukkitImplementation.GUI;

import BlockDynasty.BukkitImplementation.BlockDynastyEconomy;
import BlockDynasty.BukkitImplementation.GUI.commands.AdminGUICommand;
import BlockDynasty.BukkitImplementation.GUI.commands.BankGUICommand;
import BlockDynasty.BukkitImplementation.GUI.listeners.GUIListener;
import BlockDynasty.BukkitImplementation.GUI.services.GUIService;
import BlockDynasty.BukkitImplementation.services.MessageService;
import BlockDynasty.BukkitImplementation.utils.UtilServer;
import BlockDynasty.Economy.aplication.useCase.AccountsUseCase;
import BlockDynasty.Economy.aplication.useCase.CurrencyUseCase;
import BlockDynasty.Economy.aplication.useCase.OfferUseCase;
import BlockDynasty.Economy.aplication.useCase.TransactionsUseCase;
import org.bukkit.plugin.java.JavaPlugin;

public  class RegisterGuiModule {
    private static final GUIService guiService= new GUIService();

    public static void register(
            TransactionsUseCase transactionsUseCase,
            AccountsUseCase accountsUseCase,
            CurrencyUseCase currencyUseCase,
            OfferUseCase offerUseCase,
            MessageService messageService
            ) {

        GUIFactory.init(currencyUseCase, accountsUseCase, transactionsUseCase,offerUseCase,messageService);

        UtilServer.consoleLog("GUI registered successfully.");
    }

    public static GUIListener guiListener(){
        return new GUIListener(guiService);
    };
    public static GUIService getGuiService() {
        return guiService;
    }

    public static AdminGUICommand AdminCommand(){
        return new AdminGUICommand();
    }

    public static BankGUICommand BankCommand(){
        return new BankGUICommand();
    }

}
