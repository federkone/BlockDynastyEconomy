package BlockDynasty.BukkitImplementation.GUI.commands;

import BlockDynasty.BukkitImplementation.GUI.services.GUIService;
import BlockDynasty.BukkitImplementation.GUI.components.IGUI;
import BlockDynasty.BukkitImplementation.GUI.views.users.BankGUI;
import BlockDynasty.BukkitImplementation.services.MessageService;
import BlockDynasty.Economy.aplication.useCase.balance.GetBalanceUseCase;
import BlockDynasty.Economy.aplication.useCase.currency.GetCurrencyUseCase;
import BlockDynasty.Economy.aplication.useCase.transaction.PayUseCase;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class BankGUICommand implements CommandExecutor {
    private final JavaPlugin plugin;
    private GUIService guiService;
    private PayUseCase payUseCase;
    private GetCurrencyUseCase getCurrencyUseCase;
    private GetBalanceUseCase getBalanceUseCase;
    private MessageService messageService;

    public BankGUICommand(JavaPlugin plugin, GUIService guiService,
                          PayUseCase payUseCase,
                          GetCurrencyUseCase getCurrencyUseCase,
                          GetBalanceUseCase getBalanceUseCase,
                          MessageService messageService) {
        this.plugin = plugin;
        this.payUseCase = payUseCase;
        this.getCurrencyUseCase = getCurrencyUseCase;
        this.getBalanceUseCase = getBalanceUseCase;
        this.messageService = messageService;
        this.guiService = guiService;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cOnly players can use this command.");
            return true;
        }

        Player player = (Player) sender;
        IGUI gui = new BankGUI(plugin, player,guiService,payUseCase,getCurrencyUseCase,getBalanceUseCase,messageService);
        this.guiService.registerGUI(player, gui);
        gui.open(player);
        return true;
    }
}
