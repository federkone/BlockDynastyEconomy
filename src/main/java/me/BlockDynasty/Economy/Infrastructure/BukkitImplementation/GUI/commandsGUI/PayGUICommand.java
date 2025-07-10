package me.BlockDynasty.Economy.Infrastructure.BukkitImplementation.GUI.commandsGUI;

import me.BlockDynasty.Economy.Infrastructure.BukkitImplementation.BlockDynastyEconomy;
import me.BlockDynasty.Economy.Infrastructure.BukkitImplementation.GUI.GUIService;
import me.BlockDynasty.Economy.Infrastructure.BukkitImplementation.GUI.components.IGUI;
import me.BlockDynasty.Economy.Infrastructure.BukkitImplementation.GUI.views.TransactionsView.BalanceGUI;
import me.BlockDynasty.Economy.Infrastructure.BukkitImplementation.GUI.views.TransactionsView.PayGUI;
import me.BlockDynasty.Economy.aplication.useCase.transaction.PayUseCase;
import me.BlockDynasty.Economy.domain.services.ICurrencyService;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PayGUICommand  implements CommandExecutor {
    private final BlockDynastyEconomy plugin;
    private GUIService guiService;
    private PayUseCase payUseCase;
    private  ICurrencyService currencyService;

    public PayGUICommand(BlockDynastyEconomy plugin, PayUseCase payUseCase, ICurrencyService currencyService) {
        this.plugin = plugin;
        this.guiService = plugin.getGuiManager();
        this.payUseCase = payUseCase;
        this.currencyService = currencyService;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cOnly players can use this command.");
            return true;
        }

        Player player = (Player) sender;
        IGUI gui = new PayGUI(plugin, payUseCase, player, currencyService);
        this.guiService.registerGUI(player, gui);
        gui.open(player);
        return true;
    }
}
