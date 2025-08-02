package BlockDynasty.BukkitImplementation.GUI.views.users.userPanels;

import BlockDynasty.BukkitImplementation.GUI.services.GUIService;
import BlockDynasty.Economy.domain.entities.balance.Money;
import BlockDynasty.Economy.domain.result.Result;
import BlockDynasty.BukkitImplementation.GUI.components.AbstractGUI;
import BlockDynasty.Economy.aplication.useCase.balance.GetBalanceUseCase;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import BlockDynasty.Economy.domain.entities.currency.Currency;
import org.bukkit.plugin.java.JavaPlugin;

import java.math.BigDecimal;
import java.util.Collections;

import java.util.List;
import java.util.UUID;
import java.util.function.Function;

public class BalanceGUI extends AbstractGUI {
    private final GetBalanceUseCase getBalanceUseCase;
    private final Player player;
    private final AbstractGUI parent;

    //CONSULTA SALDO
    public BalanceGUI(Player player,GetBalanceUseCase getBalanceUseCase,AbstractGUI parent) {
        super("Balance de cuenta", 3);
        this.getBalanceUseCase = getBalanceUseCase;
        this.parent = parent;
        this.player = player;

        setupGUI(player.getUniqueId());
    }

    public BalanceGUI(Player sender, UUID target, GetBalanceUseCase getBalanceUseCase, AbstractGUI parent){
        super("Balance de cuenta", 3);
        this.getBalanceUseCase = getBalanceUseCase;
        this.parent = parent;
        this.player = sender;

        setupGUI(target);
    }

    private void setupGUI(UUID target) {
        Result<List<Money>> result = getBalanceUseCase.getBalances(target);

        if (result.isSuccess() && result.getValue() != null) {
            List<Money> monies = result.getValue();

            // Add title item
            setItem(4, createItem(Material.BOOK, "§6Balance de cuenta",
                    "§7Saldos disponibles"), null);

            // Display each balance in separate slots
            int slot = 10;
            for (Money money : monies) {
                String currencyName = money.getCurrency().getSingular();
                BigDecimal amount = money.getAmount();
                Currency currency = money.getCurrency();

                setItem(slot, createItem(Material.GOLD_INGOT,
                        "§6" + currencyName,
                        "§eBalance: §f" + ChatColor.valueOf(currency.getColor())+  currency.format(amount)), null);

                // Adjust slot position
                slot++;
                if (slot % 9 == 8) slot += 2;
            }

            // Add a close button
            setItem(22, createItem(Material.BARRIER, "§cAtrás", "§7Click para atrás"), f -> { parent.open(player);});
        } else {
            // Show error message if balances couldn't be retrieved
            setItem(13, createItem(Material.BARRIER, "§cError", "§7No se pudieron obtener los balances"), null);
        }
    }
}