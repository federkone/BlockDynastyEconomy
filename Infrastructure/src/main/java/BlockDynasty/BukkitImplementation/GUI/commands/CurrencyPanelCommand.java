package BlockDynasty.BukkitImplementation.GUI.commands;

import BlockDynasty.BukkitImplementation.GUI.services.GUIService;
import BlockDynasty.BukkitImplementation.GUI.components.IGUI;
import BlockDynasty.BukkitImplementation.GUI.views.admins.CurrencyPanelGUI;
import BlockDynasty.Economy.aplication.useCase.currency.CreateCurrencyUseCase;
import BlockDynasty.Economy.aplication.useCase.currency.DeleteCurrencyUseCase;
import BlockDynasty.Economy.aplication.useCase.currency.EditCurrencyUseCase;
import BlockDynasty.Economy.aplication.useCase.currency.SearchCurrencyUseCase;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class CurrencyPanelCommand implements CommandExecutor {
    private final JavaPlugin plugin;
    private final GUIService guiService;
    private final CreateCurrencyUseCase createCurrencyUseCase;
    private final EditCurrencyUseCase editCurrencyUseCase;
    private final SearchCurrencyUseCase searchCurrencyUseCase;
    private final DeleteCurrencyUseCase deleteCurrencyUseCase;

    public CurrencyPanelCommand(JavaPlugin plugin, GUIService guiService,
                                SearchCurrencyUseCase searchCurrencyUseCase,
                                EditCurrencyUseCase editCurrencyUseCase,
                                CreateCurrencyUseCase createCurrencyUseCase, DeleteCurrencyUseCase deleteCurrencyUseCase) {
        this.plugin = plugin;
        this.createCurrencyUseCase = createCurrencyUseCase;
        this.editCurrencyUseCase = editCurrencyUseCase;
        this.searchCurrencyUseCase = searchCurrencyUseCase;
        this.deleteCurrencyUseCase = deleteCurrencyUseCase;
        this.guiService = guiService;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("BlockDynastyEconomy.economy.currencypanel")) {
            sender.sendMessage("§cYou do not have permission to use this command.");
            return true;
        }
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cOnly players can use this command.");
            return true;
        }

        Player player = (Player) sender;
        IGUI gui = new CurrencyPanelGUI(guiService,plugin, player, searchCurrencyUseCase,editCurrencyUseCase,createCurrencyUseCase,deleteCurrencyUseCase);
        this.guiService.registerGUI(player, gui);
        gui.open(player);
        return true;
    }
}