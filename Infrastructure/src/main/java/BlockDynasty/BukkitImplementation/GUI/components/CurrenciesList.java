package BlockDynasty.BukkitImplementation.GUI.components;

import BlockDynasty.BukkitImplementation.BlockDynastyEconomy;
import BlockDynasty.BukkitImplementation.GUI.services.GUIService;
import BlockDynasty.Economy.aplication.useCase.currency.SearchCurrencyUseCase;
import BlockDynasty.Economy.domain.entities.currency.Currency;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

public abstract class CurrenciesList extends AbstractGUI {
    private final BlockDynastyEconomy plugin = BlockDynastyEconomy.getInstance();
    private final GUIService guiService;
    private final Player player;
    private final SearchCurrencyUseCase searchCurrencyUseCase;
    private final AbstractGUI parentGUI;
    private int currentPage = 0;
    private final int CURRENCIES_PER_PAGE = 21;

    public CurrenciesList(GUIService guiService, Player player, SearchCurrencyUseCase searchCurrencyUseCase, AbstractGUI parentGUI) {
        super("Lista de Monedas", 5);
        this.guiService = guiService;
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

//test anoter implementation:
/*  private void setupGUItest() {
       /* setItem(11, createItem(Material.EMERALD, "§aDepositar", "§7Click para depositar"), unused -> {
            player.closeInventory();
            openAmountInputGUI(
                    player,
                    "Depositar monto",
                    amount -> usesCase.getDepositUseCase().execute(player.getUniqueId(), amount),
                    "§aHas depositado: §f%amount%"
            );
        });

        setItem(15, createItem(Material.REDSTONE, "§cExtraer", "§7Click para extraer"), unused -> {
            player.closeInventory();
            openAmountInputGUI(
                    player,
                    "Extraer monto",
                    amount -> usesCase.getWithdrawUseCase().execute(player.getUniqueId(), amount),
                    "§cHas extraído: §f%amount%"
            );
        });
    }

private void openAmountInputGUI(Player player, String title, Function<BigDecimal, Result<Void>> operation, String successMessage) {
    new AnvilGUI.Builder()
            .onClick((slot, stateSnapshot) -> {
                if (slot != AnvilGUI.Slot.OUTPUT) {
                    return Collections.emptyList();
                }
                try {
                    BigDecimal amount = new BigDecimal(stateSnapshot.getText());
                    Result<Void> result = operation.apply(amount);

                    if (result.isSuccess()) {
                        player.sendMessage(successMessage.replace("%amount%", amount.toString()));
                        return List.of(AnvilGUI.ResponseAction.close());
                    } else {
                        return List.of( AnvilGUI.ResponseAction.replaceInputText("§c" + result.getErrorMessage()) );
                    }
                } catch (NumberFormatException e) {
                    return List.of( AnvilGUI.ResponseAction.replaceInputText("§cFormato inválido"));
                }
            })
            .text("0")
            .title(title)
            .plugin(plugin)
            .open(player);
} */