package BlockDynasty.BukkitImplementation.GUI.components;

import BlockDynasty.BukkitImplementation.BlockDynastyEconomy;
import BlockDynasty.Economy.aplication.useCase.currency.SearchCurrencyUseCase;
import BlockDynasty.Economy.domain.entities.currency.Currency;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.math.BigDecimal;
import java.util.List;

public abstract class CurrenciesList extends AbstractGUI {
    private final BlockDynastyEconomy plugin = BlockDynastyEconomy.getInstance();
    private final Player player;
    private final SearchCurrencyUseCase searchCurrencyUseCase;
    private final AbstractGUI parentGUI;
    private int currentPage = 0;
    private final int CURRENCIES_PER_PAGE = 21;

    public CurrenciesList(Player player, SearchCurrencyUseCase searchCurrencyUseCase, AbstractGUI parentGUI) {
        super("Lista de Monedas", 5,player);
        this.player = player;
        this.parentGUI = parentGUI;
        this.searchCurrencyUseCase = searchCurrencyUseCase;

        showCurrenciesPage();
    }

    private void showCurrenciesPage() {
        // Get all currencies
        List<Currency> currencies = searchCurrencyUseCase.getCurrencies();

        // Calculate pagination
        int startIndex = currentPage * CURRENCIES_PER_PAGE;
        int endIndex = Math.min(startIndex + CURRENCIES_PER_PAGE, currencies.size());

        // Clear GUI
        clearGui();

        if (currencies.isEmpty()) {
            setItem(22, createItem(Material.BARRIER, "§cNo hay monedas",
                    "§7No hay monedas registradas en el sistema"), null);

            // Back button
            setItem(40, createItem(Material.ARROW, "§aVolver",
                    "§7Click para volver"), unused -> {
                //player.closeInventory();
                parentGUI.open();
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
            parentGUI.open();
        });
    }

    private void openAnvilInput(Player sender,Currency currency) {
        AnvilMenu.open(sender,"Ingresar Monto","0", s->{
            try {
                BigDecimal amount = new BigDecimal(s);
                return execute(sender, currency, amount);
            } catch (NumberFormatException e) {
                return "Formato inválido";
            }
        });
    }
    public String execute(Player sender,Currency currency, BigDecimal amount){return "execute not implement";};
    public void openSubMenu(Currency currency,Player player){
        openAnvilInput(player,currency);
    };

}
