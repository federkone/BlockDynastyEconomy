package BlockDynasty.BukkitImplementation.GUI;

import BlockDynasty.BukkitImplementation.GUI.commands.BankGUICommand;
import BlockDynasty.BukkitImplementation.GUI.commands.CurrencyPanelCommand;
import BlockDynasty.BukkitImplementation.GUI.listeners.GUIListener;
import BlockDynasty.BukkitImplementation.GUI.services.GUIService;
import BlockDynasty.BukkitImplementation.config.file.MessageService;
import BlockDynasty.BukkitImplementation.utils.UtilServer;
import BlockDynasty.Economy.aplication.useCase.balance.GetBalanceUseCase;
import BlockDynasty.Economy.aplication.useCase.currency.CreateCurrencyUseCase;
import BlockDynasty.Economy.aplication.useCase.currency.DeleteCurrencyUseCase;
import BlockDynasty.Economy.aplication.useCase.currency.EditCurrencyUseCase;
import BlockDynasty.Economy.aplication.useCase.currency.GetCurrencyUseCase;
import BlockDynasty.Economy.aplication.useCase.transaction.PayUseCase;
import org.bukkit.plugin.java.JavaPlugin;

public  class RegisterModule {

    public static void register(JavaPlugin plugin,
                                PayUseCase payUseCase,
                                CreateCurrencyUseCase createCurrencyUseCase,
                                GetBalanceUseCase getBalanceUseCase,
                                GetCurrencyUseCase getCurrencyUseCase,
                                EditCurrencyUseCase editCurrencyUseCase,
                                DeleteCurrencyUseCase deleteCurrencyUseCase, MessageService messageService) {

        GUIService guiService= new GUIService();

        plugin.getCommand("bank").setExecutor(new BankGUICommand(plugin,guiService,payUseCase, getCurrencyUseCase, getBalanceUseCase,messageService));
        plugin.getCommand("currencyPannel").setExecutor(new CurrencyPanelCommand(plugin,guiService,getCurrencyUseCase,editCurrencyUseCase,createCurrencyUseCase,deleteCurrencyUseCase));

        // Register GUI event listener
        plugin.getServer().getPluginManager().registerEvents(new GUIListener(guiService), plugin);
        UtilServer.consoleLog("GUI modules registered successfully.");
    }
}
