package BlockDynasty.BukkitImplementation.GUI.views.admins.adminPanels;

import BlockDynasty.BukkitImplementation.GUI.components.AbstractGUI;
import BlockDynasty.BukkitImplementation.GUI.services.GUIService;
import BlockDynasty.Economy.aplication.useCase.currency.GetCurrencyUseCase;
import BlockDynasty.Economy.domain.entities.currency.Currency;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;


import java.util.List;

public abstract class AbstractCurrenciesList extends AbstractGUI {
    private final GUIService guiService;
    private final Player player;
    private final GetCurrencyUseCase getCurrencyUseCase;
    private final AbstractGUI parentGUI;
    private int currentPage = 0;
    private final int CURRENCIES_PER_PAGE = 21;

    public AbstractCurrenciesList( GUIService guiService, Player player, GetCurrencyUseCase getCurrencyUseCase,AbstractGUI parentGUI) {
        super("Lista de Monedas", 5);
        this.guiService = guiService;
        this.player = player;
        this.parentGUI = parentGUI;
        this.getCurrencyUseCase = getCurrencyUseCase;

        showCurrenciesPage();
    }

    private void showCurrenciesPage() {
        // Get all currencies
        List<Currency> currencies = getCurrencyUseCase.getCurrencies();

        // Calculate pagination
        int startIndex = currentPage * CURRENCIES_PER_PAGE;
        int endIndex = Math.min(startIndex + CURRENCIES_PER_PAGE, currencies.size());

        // Clear GUI
        for (int i = 0; i < getInventory().getSize(); i++) {
            setItem(i, null, null);
        }

        if (currencies.isEmpty()) {
            setItem(22, createItem(Material.BARRIER, "§cNo hay monedas",
                    "§7No hay monedas registradas en el sistema"), null);

            // Back button
            setItem(40, createItem(Material.ARROW, "§aVolver",
                    "§7Click para volver"), unused -> {
                //player.closeInventory();
                player.openInventory(parentGUI.getInventory());
                guiService.registerGUI(player, parentGUI);
            });

            return;
        }

        // Add currencies to GUI
        int slot = 10;
        for (int i = startIndex; i < endIndex; i++) {
            Currency currency = currencies.get(i);
            ChatColor color = ChatColor.valueOf(currency.getColor());

            setItem(slot, createItem(Material.GOLD_INGOT,
                            color + currency.getSingular(),
                            "§7Singular: " + color + currency.getSingular(),
                            "§7Plural: " + color + currency.getPlural()),
                    unused -> {
                        //player.closeInventory();
                        openSubMenu(currency,player);
                    });

            // Adjust slot position
            slot++;
            if (slot % 9 == 8) slot += 2;
        }

        // Navigation buttons
        if (currentPage > 0) {
            setItem(38, createItem(Material.ARROW, "§aPágina Anterior",
                    "§7Click para ver monedas anteriores"), unused -> {
                currentPage--;
                showCurrenciesPage();
            });
        }

        if (endIndex < currencies.size()) {
            setItem(42, createItem(Material.ARROW, "§aPágina Siguiente",
                    "§7Click para ver más monedas"), unused -> {
                currentPage++;
                showCurrenciesPage();
            });
        }

        // Back button
        setItem(40, createItem(Material.BARRIER, "§cVolver",
                "§7Click para volver"), unused -> {
            //player.closeInventory();
            player.openInventory(parentGUI.getInventory());
            guiService.registerGUI(player, parentGUI);
        });
    }


    public abstract void openSubMenu(Currency currency,Player player);

}

