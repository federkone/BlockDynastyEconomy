package BlockDynasty.BukkitImplementation.GUI.commands;

import BlockDynasty.BukkitImplementation.GUI.services.GUIService;
import BlockDynasty.BukkitImplementation.GUI.components.IGUI;
import BlockDynasty.BukkitImplementation.GUI.views.users.userPanels.PayGUI;
import BlockDynasty.BukkitImplementation.services.MessageService;
import BlockDynasty.Economy.aplication.useCase.currency.GetCurrencyUseCase;
import BlockDynasty.Economy.aplication.useCase.transaction.PayUseCase;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class PayGUICommand  implements CommandExecutor {
    private final JavaPlugin plugin;
    private GUIService guiService;
    private MessageService messageService;
    private PayUseCase payUseCase;
    private GetCurrencyUseCase getCurrencyUseCase;

    public PayGUICommand(JavaPlugin plugin, GUIService guiService,PayUseCase payUseCase, GetCurrencyUseCase getCurrencyUseCase, MessageService messageService) {
        this.plugin = plugin;
        this.guiService = guiService;
        this.messageService = messageService;
        this.payUseCase = payUseCase;
        this.getCurrencyUseCase = getCurrencyUseCase;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cOnly players can use this command.");
            return true;
        }

        Player player = (Player) sender;
        IGUI gui = new PayGUI(plugin, payUseCase, player, guiService, getCurrencyUseCase,messageService);
        this.guiService.registerGUI(player, gui);
        gui.open(player);
        return true;
    }
}
