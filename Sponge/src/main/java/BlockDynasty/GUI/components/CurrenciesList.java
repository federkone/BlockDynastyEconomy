package BlockDynasty.GUI.components;

import BlockDynasty.Economy.aplication.useCase.currency.SearchCurrencyUseCase;
import BlockDynasty.Economy.domain.entities.currency.Currency;
import BlockDynasty.utils.ChatColor;
import net.kyori.adventure.text.format.NamedTextColor;
import org.spongepowered.api.Server;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public abstract class CurrenciesList extends PaginatedGUI<Currency> {
    private final SearchCurrencyUseCase searchCurrencyUseCase;
    private final ServerPlayer player;

    public CurrenciesList(ServerPlayer player, SearchCurrencyUseCase searchCurrencyUseCase, IGUI parentGUI) {
        super("Currency List", 5,player,parentGUI,21);
        this.searchCurrencyUseCase = searchCurrencyUseCase;
        this.player = player;
        showCurrencies();
    }

    public CurrenciesList(ServerPlayer player, SearchCurrencyUseCase searchCurrencyUseCase, IGUI parentGUI,Currency exceptCurrency) {
        this(player, searchCurrencyUseCase, parentGUI);
        showCurrenciesExcluding(exceptCurrency);
    }

    private void showCurrenciesExcluding(Currency exceptCurrency) {
        List<Currency> currencies = searchCurrencyUseCase.getCurrencies().stream()
                .filter(c -> !c.equals(exceptCurrency))
                .collect(Collectors.toList());

        //testing purposes
        for (int i = 0; i < 45; i++) {
            currencies.add(new Currency(UUID.randomUUID(),"test","test"));
        }

        showItemsPage(currencies);
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
        String color = ChatColor.stringValueOf(currency.getColor());
        //NamedTextColor color =NamedTextColor.
        return createItem(ItemTypes.GOLD_INGOT.get(),
                color + currency.getSingular(),
                "§7Singular: " + color + currency.getSingular(),
                "§7Plural: " + color + currency.getPlural(),
                "§7Transferable: " + (currency.isTransferable() ? "§aYes" : "§cNo"),
                "§7Exchange Rate: "+ color+ currency.getExchangeRate()
        );
    }

    @Override
    protected void handleLeftItemClick(Currency currency) {
        /*AnvilMenu.open(this,player,"Amount to "+currency.getSingular(),"0", s->{
            try {
                BigDecimal amount = new BigDecimal(s);
                return execute(player, currency, amount);
            } catch (NumberFormatException e) {
                return "Invalid Format";
            }
        });*/
    }

    protected String execute(ServerPlayer sender,Currency currency, BigDecimal amount){return "execute not implement";};
}
