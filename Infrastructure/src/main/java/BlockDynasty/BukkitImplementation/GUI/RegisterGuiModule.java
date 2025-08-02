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
    //need to be admin subcommand for eco
    public static void register(JavaPlugin plugin,
                                TransactionsUseCase transactionsUseCase,
                                AccountsUseCase accountsUseCase,
                                CurrencyUseCase currencyUseCase,
                                MessageService messageService) {


        //bank is a command end user like a pay command
        //plugin.getCommand("bank").setExecutor(new BankGUICommand(guiService,transactionsUseCase.getPayUseCase(), currencyUseCase.getGetCurrencyUseCase(), accountsUseCase.getGetBalanceUseCase(),messageService));
        //plugin.getCommand("admin").setExecutor(new AdminGUICommand(guiService,accountsUseCase,currencyUseCase,transactionsUseCase));

        // Register GUI event listener
        plugin.getServer().getPluginManager().registerEvents(new GUIListener(guiService), plugin);
        UtilServer.consoleLog("GUI modules registered successfully.");
    }

    public static GUIService getGuiService() {
        return guiService;
    }

    public static AdminGUICommand AdminCommand(JavaPlugin plugin,
                                     TransactionsUseCase transactionsUseCase,
                                     AccountsUseCase accountsUseCase,
                                     CurrencyUseCase currencyUseCase){
        return new AdminGUICommand(accountsUseCase,currencyUseCase,transactionsUseCase);
    }

    public static BankGUICommand BankCommand(JavaPlugin plugin,
                                     TransactionsUseCase transactionsUseCase,
                                     AccountsUseCase accountsUseCase,
                                     CurrencyUseCase currencyUseCase,
                                     MessageService messageService){
        return new BankGUICommand(transactionsUseCase.getPayUseCase(), currencyUseCase.getGetCurrencyUseCase(), accountsUseCase.getGetBalanceUseCase(),messageService);
    }

}
