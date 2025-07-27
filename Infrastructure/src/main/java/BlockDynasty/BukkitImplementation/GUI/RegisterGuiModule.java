package BlockDynasty.BukkitImplementation.GUI;

import BlockDynasty.BukkitImplementation.GUI.commands.BankGUICommand;
import BlockDynasty.BukkitImplementation.GUI.commands.CurrencyPanelCommand;
import BlockDynasty.BukkitImplementation.GUI.listeners.GUIListener;
import BlockDynasty.BukkitImplementation.GUI.services.GUIService;
import BlockDynasty.BukkitImplementation.services.MessageService;
import BlockDynasty.BukkitImplementation.utils.UtilServer;
import BlockDynasty.Economy.aplication.useCase.AccountsUseCase;
import BlockDynasty.Economy.aplication.useCase.CurrencyUseCase;
import BlockDynasty.Economy.aplication.useCase.TransactionsUseCase;
import BlockDynasty.Economy.aplication.useCase.balance.GetBalanceUseCase;
import BlockDynasty.Economy.aplication.useCase.currency.CreateCurrencyUseCase;
import BlockDynasty.Economy.aplication.useCase.currency.DeleteCurrencyUseCase;
import BlockDynasty.Economy.aplication.useCase.currency.EditCurrencyUseCase;
import BlockDynasty.Economy.aplication.useCase.currency.GetCurrencyUseCase;
import BlockDynasty.Economy.aplication.useCase.transaction.PayUseCase;
import org.bukkit.plugin.java.JavaPlugin;

public  class RegisterGuiModule {

    public static void register(JavaPlugin plugin,
                                TransactionsUseCase transactionsUseCase,
                                AccountsUseCase accountsUseCase,
                                CurrencyUseCase currencyUseCase,
                                MessageService messageService) {

        GUIService guiService= new GUIService();

        plugin.getCommand("bank").setExecutor(new BankGUICommand(plugin,guiService,transactionsUseCase.getPayUseCase(), currencyUseCase.getGetCurrencyUseCase(), accountsUseCase.getGetBalanceUseCase(),messageService));
        plugin.getCommand("currencyPannel").setExecutor(new CurrencyPanelCommand(plugin,guiService,currencyUseCase.getGetCurrencyUseCase(),currencyUseCase.getEditCurrencyUseCase(),currencyUseCase.getCreateCurrencyUseCase(),currencyUseCase.getDeleteCurrencyUseCase()));

        // Register GUI event listener
        plugin.getServer().getPluginManager().registerEvents(new GUIListener(guiService), plugin);
        UtilServer.consoleLog("GUI modules registered successfully.");
    }
}
