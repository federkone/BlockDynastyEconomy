package me.BlockDynasty.Economy.Infrastructure.BukkitImplementation.GUI;

import me.BlockDynasty.Economy.Infrastructure.BukkitImplementation.GUI.commands.BankGUICommand;
import me.BlockDynasty.Economy.Infrastructure.BukkitImplementation.GUI.commands.CurrencyPanelCommand;
import me.BlockDynasty.Economy.Infrastructure.BukkitImplementation.GUI.listeners.GUIListener;
import me.BlockDynasty.Economy.Infrastructure.BukkitImplementation.GUI.services.GUIService;
import me.BlockDynasty.Economy.Infrastructure.BukkitImplementation.config.file.MessageService;
import me.BlockDynasty.Economy.aplication.useCase.balance.GetBalanceUseCase;
import me.BlockDynasty.Economy.aplication.useCase.currency.CreateCurrencyUseCase;
import me.BlockDynasty.Economy.aplication.useCase.currency.DeleteCurrencyUseCase;
import me.BlockDynasty.Economy.aplication.useCase.currency.EditCurrencyUseCase;
import me.BlockDynasty.Economy.aplication.useCase.currency.GetCurrencyUseCase;
import me.BlockDynasty.Economy.aplication.useCase.transaction.PayUseCase;
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
        System.out.println("GUI modules registered successfully.");
    }
}
