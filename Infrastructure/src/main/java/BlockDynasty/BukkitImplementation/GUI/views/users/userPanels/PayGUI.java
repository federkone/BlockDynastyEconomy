package BlockDynasty.BukkitImplementation.GUI.views.users.userPanels;

import BlockDynasty.BukkitImplementation.GUI.MaterialAdapter;
import BlockDynasty.BukkitImplementation.GUI.components.AbstractGUI;
import BlockDynasty.BukkitImplementation.GUI.services.GUIService;
import BlockDynasty.BukkitImplementation.services.MessageService;
import BlockDynasty.Economy.aplication.useCase.currency.SearchCurrencyUseCase;
import BlockDynasty.Economy.aplication.useCase.transaction.PayUseCase;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;


//la ui para pagar, va a mostrar primero: las personas Online indexadas, luego elegir la moneda "monedas disponibles del sistema" indexada, luego abrir AnvilGUI para escribir monto.Fin
public class PayGUI extends AbstractGUI {
    private final JavaPlugin plugin;
    private final Player sender;
    private final GUIService guiService;
    private final PayUseCase payUseCase;
    private final SearchCurrencyUseCase searchCurrencyUseCase;
    private MessageService messageService;
    private int currentPage = 0;
    private final int PLAYERS_PER_PAGE = 21;

    public PayGUI(JavaPlugin plugin, PayUseCase payUseCase, Player sender, GUIService guiService, SearchCurrencyUseCase searchCurrencyUseCase, MessageService messageService) {
        super("Seleccionar Jugador", 5);
        this.plugin = plugin;
        this.guiService = guiService;
        this.sender = sender;
        this.payUseCase = payUseCase;
        this.searchCurrencyUseCase = searchCurrencyUseCase;
        this.messageService = messageService;

        showPlayersPage();
    }

    private void showPlayersPage() {
        // Get online players
        List<Player> onlinePlayers = new ArrayList<>(Bukkit.getOnlinePlayers());
        onlinePlayers.remove(sender); // Remove sender from list

        // Calculate pagination
        int startIndex = currentPage * PLAYERS_PER_PAGE;
        int endIndex = Math.min(startIndex + PLAYERS_PER_PAGE, onlinePlayers.size());

        // Clear GUI
        for (int i = 0; i < getInventory().getSize(); i++) {
            setItem(i, null, null);
        }

        // Add players to GUI
        int slot = 10;
        for (int i = startIndex; i < endIndex; i++) {
            Player target = onlinePlayers.get(i);
            ItemStack playerHead = MaterialAdapter.createPlayerHead(target);

            setItem(slot, playerHead, unused -> {
                //sender.closeInventory();
                openCurrencySelectionGUI(target);
            });

            // Adjust slot position
            slot++;
            if (slot % 9 == 8) slot += 2;
        }

        // Navigation buttons
        if (currentPage > 0) {
            setItem(38, createItem(Material.ARROW, "§aPágina Anterior",
                    "§7Click para ver jugadores anteriores"), unused -> {
                currentPage--;
                showPlayersPage();
            });
        }

        if (endIndex < onlinePlayers.size()) {
            setItem(42, createItem(Material.ARROW, "§aPágina Siguiente",
                    "§7Click para ver más jugadores"), unused -> {
                currentPage++;
                showPlayersPage();
            });
        }

        // Cancel button
        setItem(40, createItem(Material.BARRIER, "§cCancelar",
                "§7Click para cancelar"), unused -> sender.closeInventory());
    }

    private void openCurrencySelectionGUI(Player targetPlayer) {
        CurrencyListPay gui = new CurrencyListPay(plugin,guiService, sender, targetPlayer, searchCurrencyUseCase,payUseCase,messageService,this);
        sender.openInventory(gui.getInventory());

        // Register the GUI with the GUIService
        guiService.registerGUI(sender, gui);
    }

}