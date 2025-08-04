package BlockDynasty.BukkitImplementation.GUI;

import BlockDynasty.BukkitImplementation.GUI.commands.AdminGUICommand;
import BlockDynasty.BukkitImplementation.GUI.commands.BankGUICommand;
import BlockDynasty.BukkitImplementation.GUI.listeners.GUIListener;
import BlockDynasty.BukkitImplementation.GUI.services.GUIService;
import BlockDynasty.BukkitImplementation.services.MessageService;
import BlockDynasty.BukkitImplementation.utils.UtilServer;
import BlockDynasty.Economy.aplication.useCase.AccountsUseCase;
import BlockDynasty.Economy.aplication.useCase.CurrencyUseCase;
import BlockDynasty.Economy.aplication.useCase.TransactionsUseCase;
import org.bukkit.plugin.java.JavaPlugin;

public  class RegisterGuiModule {
    private static final GUIService guiService= new GUIService();

    public static void register(JavaPlugin plugin,
                                TransactionsUseCase transactionsUseCase,
                                AccountsUseCase accountsUseCase,
                                CurrencyUseCase currencyUseCase,
                                MessageService messageService) {

        GUIFactory.init(currencyUseCase, accountsUseCase, transactionsUseCase,messageService);

        // Register GUI event listener
        plugin.getServer().getPluginManager().registerEvents(new GUIListener(guiService), plugin);
        UtilServer.consoleLog("GUI events registered successfully.");
    }

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
