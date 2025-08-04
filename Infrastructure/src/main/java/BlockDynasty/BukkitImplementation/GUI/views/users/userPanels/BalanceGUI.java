package BlockDynasty.BukkitImplementation.GUI.views.users.userPanels;

import BlockDynasty.BukkitImplementation.GUI.components.IGUI;
import BlockDynasty.BukkitImplementation.GUI.components.PaginatedGUI;
import BlockDynasty.Economy.domain.entities.balance.Money;
import BlockDynasty.Economy.domain.result.Result;
import BlockDynasty.Economy.aplication.useCase.balance.GetBalanceUseCase;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import BlockDynasty.Economy.domain.entities.currency.Currency;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.UUID;

public class BalanceGUI extends PaginatedGUI<Money> {
    private final GetBalanceUseCase getBalanceUseCase;
    private final UUID targetUUID;

    public BalanceGUI(Player player, GetBalanceUseCase getBalanceUseCase, IGUI parent) {
        super("Balance de cuenta", 3, player, parent, 7); // 7 currencies per page
        this.getBalanceUseCase = getBalanceUseCase;
        this.targetUUID = player.getUniqueId();

        loadBalances();
    }

    public BalanceGUI(Player sender, UUID target, GetBalanceUseCase getBalanceUseCase, IGUI parent) {
        super("Balance de cuenta", 3, sender, parent, 7);
        this.getBalanceUseCase = getBalanceUseCase;
        this.targetUUID = target;

        loadBalances();
    }

    private void loadBalances() {
        Result<List<Money>> result = getBalanceUseCase.getBalances(targetUUID);

        //TEST
        //for (int i=0; i < 45; i++) {
       //     result.getValue().add(new Money(new Currency(UUID.randomUUID(),"test","test")));
        //}

        if (result.isSuccess() && result.getValue() != null) {
            showItemsPage(result.getValue());
        }
    }

    @Override
    protected ItemStack createItemFor(Money money) {
        Currency currency = money.getCurrency();
        return createItem(Material.GOLD_INGOT,
                "§6" + currency.getSingular(),
                "§eBalance: §f" + ChatColor.valueOf(currency.getColor()) + currency.format(money.getAmount()));
    }

    @Override
    protected ItemStack createEmptyMessage() {
        return createItem(Material.BARRIER, "§cSin monedas", "§7No hay monedas en la cuenta");
    }

    @Override
    protected void addCustomButtons() {
        setItem(4, createItem(Material.BOOK, "§6Balance de cuenta", "§7Saldos disponibles"), null);
    }
}