package BlockDynasty.BukkitImplementation.GUI.views.users.userPanels;

import BlockDynasty.BukkitImplementation.GUI.GUIFactory;
import BlockDynasty.BukkitImplementation.GUI.components.CurrenciesList;
import BlockDynasty.BukkitImplementation.GUI.components.IGUI;
import BlockDynasty.Economy.aplication.useCase.currency.SearchCurrencyUseCase;
import BlockDynasty.Economy.aplication.useCase.offer.CreateOfferUseCase;
import BlockDynasty.Economy.domain.entities.currency.Currency;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.math.BigDecimal;
import java.util.Arrays;

public class CurrencyListToOfferFirst extends CurrenciesList {
    private final BlockDynasty.Economy.domain.entities.account.Player target;
    private final SearchCurrencyUseCase searchCurrencyUseCase;
    protected final CreateOfferUseCase createOfferUseCase;
    private Currency currency;
    private BigDecimal amount;

    public CurrencyListToOfferFirst(Player player, BlockDynasty.Economy.domain.entities.account.Player target,
                                    SearchCurrencyUseCase searchCurrencyUseCase,CreateOfferUseCase createOfferUseCase, IGUI parentGUI){
        super(player, searchCurrencyUseCase, parentGUI);
        this.target = target;
        this.searchCurrencyUseCase = searchCurrencyUseCase;
        this.createOfferUseCase = createOfferUseCase;
    }

    protected Currency getCurrency() {
        return currency;
    }

    protected BigDecimal getAmount() {
        return amount;
    }

    @Override
    protected String execute(Player sender, Currency currency, java.math.BigDecimal amount) {
            this.currency = currency;
            this.amount = amount;
            new CurrencyListToOfferSecond(sender, target, searchCurrencyUseCase,createOfferUseCase, this).open();
            return null;
    }

    @Override
    public void addCustomButtons() {
        setItem(4, createItem(Material.PAPER, "§aSelect Currency to Offer",
                        Arrays.asList("§7Click to select the currency you want to offer", "§7And before that, the amount")),
                null);

    }
}
