package BlockDynasty.BukkitImplementation.GUI.views.users.userPanels;

import BlockDynasty.BukkitImplementation.GUI.components.IGUI;
import BlockDynasty.Economy.domain.entities.balance.Money;
import BlockDynasty.Economy.domain.result.Result;
import BlockDynasty.BukkitImplementation.GUI.components.AbstractGUI;
import BlockDynasty.Economy.aplication.useCase.balance.GetBalanceUseCase;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import BlockDynasty.Economy.domain.entities.currency.Currency;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public class BalanceGUI extends AbstractGUI {
    private final GetBalanceUseCase getBalanceUseCase;
    private final Player player;
    private int currentPage = 0;
    private final int CURRENCIES_PER_PAGE = 7; // 7 items in a single row (9 slots minus edge slots)
    private List<Money> monies;
    private UUID targetUUID;

    //CONSULTA SALDO
    public BalanceGUI(Player player, GetBalanceUseCase getBalanceUseCase, IGUI parent) {
        super("Balance de cuenta", 3, player, parent);
        this.getBalanceUseCase = getBalanceUseCase;
        this.player = player;
        this.targetUUID = player.getUniqueId();

        loadBalances();
    }

    public BalanceGUI(Player sender, UUID target, GetBalanceUseCase getBalanceUseCase, IGUI parent){
        super("Balance de cuenta", 3, sender, parent);
        this.getBalanceUseCase = getBalanceUseCase;
        this.player = sender;
        this.targetUUID = target;

        loadBalances();
    }

    private void loadBalances() {
        Result<List<Money>> result = getBalanceUseCase.getBalances(targetUUID);

        if (result.isSuccess() && result.getValue() != null) {
            monies = result.getValue();
            showBalancePage();
        } else {
            // Show error message if balances couldn't be retrieved
            clearGui();
            setItem(13, createItem(Material.BARRIER, "§cError", "§7No se pudieron obtener los balances"), null);
        }
    }
    private void showBalancePage() {
        clearGui();

        // Add title item
        setItem(4, createItem(Material.BOOK, "§6Balance de cuenta",
                "§7Saldos disponibles"), null);

        if (monies.isEmpty()) {
            setItem(13, createItem(Material.BARRIER, "§cSin monedas",
                    "§7No hay monedas en la cuenta"), null);

            // Back button
            setItem(22, createItem(Material.BARRIER, "§cAtrás", "§7Click para volver"), f -> {
                this.openParent();
            });

            return;
        }

        // Calculate pagination
        int startIndex = currentPage * CURRENCIES_PER_PAGE;
        int endIndex = Math.min(startIndex + CURRENCIES_PER_PAGE, monies.size());

        // Display each balance in a single row
        int slot = 10;
        for (int i = startIndex; i < endIndex; i++) {
            Money money = monies.get(i);
            String currencyName = money.getCurrency().getSingular();
            BigDecimal amount = money.getAmount();
            Currency currency = money.getCurrency();

            setItem(slot, createItem(Material.GOLD_INGOT,
                    ChatColor.valueOf(currency.getColor()) + currencyName,
                    "Balance: " + ChatColor.valueOf(currency.getColor()) + currency.format(amount)), null);

            slot++;
        }

        // Navigation buttons
        if (currentPage > 0) {
            setItem(21, createItem(Material.ARROW, "§aPágina Anterior",
                    "§7Click para ver monedas anteriores"), unused -> {
                currentPage--;
                showBalancePage();
            });
        }

        // Back button
        setItem(22, createItem(Material.BARRIER, "§cAtrás", "§7Click para volver"), f -> {
            this.openParent();
        });

        if (endIndex < monies.size()) {
            setItem(23, createItem(Material.ARROW, "§aPágina Siguiente",
                    "§7Click para ver más monedas"), unused -> {
                currentPage++;
                showBalancePage();
            });
        }
    }
}