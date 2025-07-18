package me.BlockDynasty.Economy.Infrastructure.BukkitImplementation.GUI.commands;

import me.BlockDynasty.Economy.Infrastructure.BukkitImplementation.GUI.services.GUIService;
import me.BlockDynasty.Economy.Infrastructure.BukkitImplementation.GUI.components.IGUI;
import me.BlockDynasty.Economy.Infrastructure.BukkitImplementation.GUI.views.admins.CurrencyPanelGUI;
import me.BlockDynasty.Economy.aplication.useCase.currency.CreateCurrencyUseCase;
import me.BlockDynasty.Economy.aplication.useCase.currency.DeleteCurrencyUseCase;
import me.BlockDynasty.Economy.aplication.useCase.currency.EditCurrencyUseCase;
import me.BlockDynasty.Economy.aplication.useCase.currency.GetCurrencyUseCase;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class CurrencyPanelCommand implements CommandExecutor {
    private final JavaPlugin plugin;
    private GUIService guiService;
    private CreateCurrencyUseCase createCurrencyUseCase;
    private EditCurrencyUseCase editCurrencyUseCase;
    private GetCurrencyUseCase getCurrencyUseCase;
    private DeleteCurrencyUseCase deleteCurrencyUseCase;

    public CurrencyPanelCommand(JavaPlugin plugin, GUIService guiService,
                                GetCurrencyUseCase getCurrencyUseCase,
                                EditCurrencyUseCase editCurrencyUseCase,
                                CreateCurrencyUseCase createCurrencyUseCase, DeleteCurrencyUseCase deleteCurrencyUseCase) {
        this.plugin = plugin;
        this.createCurrencyUseCase = createCurrencyUseCase;
        this.editCurrencyUseCase = editCurrencyUseCase;
        this.getCurrencyUseCase = getCurrencyUseCase;
        this.deleteCurrencyUseCase = deleteCurrencyUseCase;
        this.guiService = guiService;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("blockdynasty.economy.currencypanel")) {
            sender.sendMessage("§cYou do not have permission to use this command.");
            return true;
        }
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cOnly players can use this command.");
            return true;
        }

        Player player = (Player) sender;
        IGUI gui = new CurrencyPanelGUI(guiService,plugin, player,getCurrencyUseCase,editCurrencyUseCase,createCurrencyUseCase,deleteCurrencyUseCase);
        this.guiService.registerGUI(player, gui);
        gui.open(player);
        return true;
    }
}