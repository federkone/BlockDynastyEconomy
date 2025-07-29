package BlockDynasty.BukkitImplementation.GUI.views.users;

import BlockDynasty.BukkitImplementation.GUI.MaterialAdapter;
import BlockDynasty.BukkitImplementation.GUI.services.GUIService;
import BlockDynasty.BukkitImplementation.GUI.views.users.userPanels.BalanceGUI;
import BlockDynasty.BukkitImplementation.GUI.views.users.userPanels.PayGUI;
import BlockDynasty.BukkitImplementation.GUI.components.AbstractGUI;
import BlockDynasty.BukkitImplementation.services.MessageService;
import BlockDynasty.Economy.aplication.useCase.balance.GetBalanceUseCase;
import BlockDynasty.Economy.aplication.useCase.currency.SearchCurrencyUseCase;
import BlockDynasty.Economy.aplication.useCase.transaction.PayUseCase;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class BankGUI extends AbstractGUI {
    private final JavaPlugin plugin;
    private final GUIService guiService;
    private final MessageService messageService;
    private final GetBalanceUseCase getBalanceUseCase;
    private final Player player;
    private final PayUseCase payUseCase;
    private final SearchCurrencyUseCase searchCurrencyUseCase;

    public BankGUI(JavaPlugin plugin, Player player, GUIService guiService, PayUseCase payUseCase, SearchCurrencyUseCase searchCurrencyUseCase, GetBalanceUseCase getBalanceUseCase, MessageService messageService) {
        super("Banco", 3);
        this.plugin = plugin;
        this.guiService = guiService;
        this.messageService = messageService;
        this.player = player;
        this.payUseCase = payUseCase;
        this.searchCurrencyUseCase = searchCurrencyUseCase;
        this.getBalanceUseCase = getBalanceUseCase;

        setupGUI();
    }

    private void setupGUI() {
        // Balance option
        setItem(11, createItem(Material.GOLD_INGOT, "§6Ver Balance",
                "§7Click para ver tu balance"), unused -> {
            //player.closeInventory();
            openBalanceGUI();
        });

        // Pay option
        setItem(15, createItem(MaterialAdapter.getPlayerHead(), "§aPagar a un Jugador",
                "§7Click para pagar a otro jugador"), unused -> {
            //player.closeInventory();
            openPayGUI();
        });

        // Exit button
        setItem(22, createItem(Material.BARRIER, "§cSalir",
                "§7Click para salir"), unused -> player.closeInventory());
    }

    private void openBalanceGUI() {
        BalanceGUI balanceGUI = new BalanceGUI(plugin, player,getBalanceUseCase);
        player.openInventory(balanceGUI.getInventory());

        // Register the GUI with the GUIService
        guiService.registerGUI(player, balanceGUI);
    }

    private void openPayGUI() {
        PayGUI payGUI = new PayGUI(plugin, payUseCase, player, guiService, searchCurrencyUseCase,messageService);
        player.openInventory(payGUI.getInventory());

        // Register the GUI with the GUIService
        guiService.registerGUI(player, payGUI);
    }
}