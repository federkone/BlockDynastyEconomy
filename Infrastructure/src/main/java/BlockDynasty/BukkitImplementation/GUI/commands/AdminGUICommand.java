package BlockDynasty.BukkitImplementation.GUI.commands;

import BlockDynasty.BukkitImplementation.GUI.components.IGUI;
import BlockDynasty.BukkitImplementation.GUI.services.GUIService;
import BlockDynasty.BukkitImplementation.GUI.views.admins.AdminPanelGUI;
import BlockDynasty.Economy.aplication.useCase.AccountsUseCase;
import BlockDynasty.Economy.aplication.useCase.CurrencyUseCase;
import BlockDynasty.Economy.aplication.useCase.TransactionsUseCase;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class AdminGUICommand implements CommandExecutor {
    private final JavaPlugin plugin;
    private final GUIService guiService;
    private final AccountsUseCase accountsUseCase;
    private final CurrencyUseCase currencyUseCase;
    private final TransactionsUseCase transactionsUseCase;

    public AdminGUICommand(JavaPlugin plugin, GUIService guiService, AccountsUseCase accountsUseCase
    , CurrencyUseCase currencyUseCase, TransactionsUseCase transactionsUseCase) {
        this.plugin = plugin;
        this.guiService = guiService;
        this.accountsUseCase = accountsUseCase;
        this.currencyUseCase = currencyUseCase;
        this.transactionsUseCase = transactionsUseCase;


    }
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cOnly players can use this command.");
            return true;
        }
        Player player = (Player) sender;
        IGUI gui = new AdminPanelGUI(player, guiService, plugin,
                currencyUseCase, accountsUseCase, transactionsUseCase);
        this.guiService.registerGUI(player,gui);
        gui.open(player);
        return true;
    }
}
