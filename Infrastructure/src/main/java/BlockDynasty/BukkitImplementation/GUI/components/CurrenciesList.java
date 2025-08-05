package BlockDynasty.BukkitImplementation.GUI.components;

import BlockDynasty.BukkitImplementation.BlockDynastyEconomy;
import BlockDynasty.Economy.aplication.useCase.currency.SearchCurrencyUseCase;
import BlockDynasty.Economy.domain.entities.currency.Currency;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.math.BigDecimal;
import java.util.List;

public abstract class CurrenciesList extends PaginatedGUI<Currency> {
    private final BlockDynastyEconomy plugin = BlockDynastyEconomy.getInstance();
    private final SearchCurrencyUseCase searchCurrencyUseCase;
    private final Player player;

    public CurrenciesList(Player player, SearchCurrencyUseCase searchCurrencyUseCase, IGUI parentGUI) {
        super("Currency List", 5,player,parentGUI,21);
        this.searchCurrencyUseCase = searchCurrencyUseCase;
        this.player = player;
        showCurrencies();
    }

    private void showCurrencies() {
        List<Currency> currencies = searchCurrencyUseCase.getCurrencies();

        //testing purposes
        //for (int i = 0; i < 45; i++) {
        //    currencies.add(new Currency(UUID.randomUUID(),"test","test"));
        //}

        showItemsPage(currencies);
    }

    @Override
    protected ItemStack createItemFor(Currency currency) {
        ChatColor color = ChatColor.valueOf(currency.getColor());

        return createItem(Material.GOLD_INGOT,
                color + currency.getSingular(),
                "§7Singular: " + color + currency.getSingular(),
                "§7Plural: " + color + currency.getPlural());
    }

    @Override
    protected void handleItemClick(Currency currency) {
        AnvilMenu.open(this,player,"Insert amount","0", s->{
            try {
                BigDecimal amount = new BigDecimal(s);
                return execute(player, currency, amount);
            } catch (NumberFormatException e) {
                return "Invalid Format";
            }
        });
    }

    protected String execute(Player sender,Currency currency, BigDecimal amount){return "execute not implement";};
}
