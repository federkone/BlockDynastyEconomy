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

    public static void register(JavaPlugin plugin,
                                TransactionsUseCase transactionsUseCase,
                                AccountsUseCase accountsUseCase,
                                CurrencyUseCase currencyUseCase,
                                MessageService messageService) {

        GUIService guiService= new GUIService();

        plugin.getCommand("bank").setExecutor(new BankGUICommand(plugin,guiService,transactionsUseCase.getPayUseCase(), currencyUseCase.getGetCurrencyUseCase(), accountsUseCase.getGetBalanceUseCase(),messageService));
        //plugin.getCommand("currencyPanel").setExecutor(new CurrencyPanelCommand(plugin,guiService,currencyUseCase.getGetCurrencyUseCase(),currencyUseCase.getEditCurrencyUseCase(),currencyUseCase.getCreateCurrencyUseCase(),currencyUseCase.getDeleteCurrencyUseCase()));
        //plugin.getCommand("deleteAccount").setExecutor(new DeletePlayerCommand(accountsUseCase.getGetAccountsUseCase(),accountsUseCase.getDeleteAccountUseCase(),guiService));
        plugin.getCommand("admin").setExecutor(new AdminGUICommand(plugin, guiService,accountsUseCase,currencyUseCase,transactionsUseCase));

        // Register GUI event listener
        plugin.getServer().getPluginManager().registerEvents(new GUIListener(guiService), plugin);
        UtilServer.consoleLog("GUI modules registered successfully.");
    }
}
