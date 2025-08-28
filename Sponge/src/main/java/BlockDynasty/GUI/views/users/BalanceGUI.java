package BlockDynasty.GUI.views.users;

import BlockDynasty.Economy.aplication.useCase.balance.GetBalanceUseCase;
import BlockDynasty.Economy.domain.entities.balance.Money;
import BlockDynasty.Economy.domain.entities.currency.Currency;
import BlockDynasty.Economy.domain.result.Result;
import BlockDynasty.GUI.components.IGUI;
import BlockDynasty.GUI.components.PaginatedGUI;
import BlockDynasty.utils.ChatColor;
import org.spongepowered.api.entity.Item;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BalanceGUI extends PaginatedGUI<Money> {
    private final GetBalanceUseCase getBalanceUseCase;
    private final UUID targetUUID;

    public BalanceGUI(ServerPlayer player, GetBalanceUseCase getBalanceUseCase, IGUI parent) {
        super("Account balance", 3, player, parent, 7); // 7 currencies per page
        this.getBalanceUseCase = getBalanceUseCase;
        this.targetUUID = player.uniqueId();

        loadBalances();
    }

    public BalanceGUI(ServerPlayer sender, UUID target, GetBalanceUseCase getBalanceUseCase, IGUI parent) {
        super("Account balance", 3, sender, parent, 7);
        this.getBalanceUseCase = getBalanceUseCase;
        this.targetUUID = target;

        loadBalances();
    }

    private void loadBalances() {
        Result<List<Money>> result = getBalanceUseCase.getBalances(targetUUID);

        //TEST
        //List<Money> monies = new ArrayList<>();
        //for (int i=0; i < 45; i++) {
        //    monies.add(new Money(new Currency(UUID.randomUUID(),"test","test")));
        //}

        if (result.isSuccess() && result.getValue() != null) {
            showItemsPage(result.getValue());

        }
    }

    @Override
    protected ItemStack createItemFor(Money money) {
        Currency currency = money.getCurrency();
        return createItem(ItemTypes.GOLD_INGOT.get(),
                ChatColor.stringValueOf(currency.getColor()) + currency.getSingular(),
                "§eBalance: §f" + ChatColor.stringValueOf(currency.getColor()) + money.format());
    }

    @Override
    protected ItemStack createEmptyMessage() {
        return createItem(ItemTypes.BARRIER.get(), "§cNo Currencies", "§7There are no coins in the account");
    }

    @Override
    protected void addCustomButtons() {
        setItem(4, createItem(ItemTypes.BOOK.get(), "§6Account balance", "§7Available balances"), null);
    }
}