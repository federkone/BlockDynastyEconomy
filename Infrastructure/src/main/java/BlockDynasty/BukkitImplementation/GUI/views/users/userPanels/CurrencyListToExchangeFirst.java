package BlockDynasty.BukkitImplementation.GUI.views.users.userPanels;

import BlockDynasty.BukkitImplementation.GUI.components.CurrenciesList;
import BlockDynasty.BukkitImplementation.GUI.components.IGUI;
import BlockDynasty.Economy.aplication.useCase.currency.SearchCurrencyUseCase;
import BlockDynasty.Economy.aplication.useCase.transaction.ExchangeUseCase;
import BlockDynasty.Economy.domain.entities.currency.Currency;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class CurrencyListToExchangeFirst extends CurrenciesList {
    private final Player player;
    private final SearchCurrencyUseCase searchCurrencyUseCase;
    private final ExchangeUseCase exchangeUseCase;

    public CurrencyListToExchangeFirst(Player player, SearchCurrencyUseCase searchCurrencyUseCase,ExchangeUseCase exchangeUseCase, IGUI parentGUI) {
        super(player, searchCurrencyUseCase, parentGUI);
        this.player = player;
        this.searchCurrencyUseCase = searchCurrencyUseCase;
        this.exchangeUseCase = exchangeUseCase;
    }

    //en este punto ya selecciono la primer moneda a dar.
    @Override
    protected void handleItemClick(Currency currency) {
        //manejaremos la logica abriendo otro menu de seleccion de moneda, y ahi si, el valor, sobrescribiendo execute() con el caso de uso exchange()
        new CurrencyListToExchangeSecond(player, searchCurrencyUseCase, exchangeUseCase, currency, this).open();
    }

    @Override
    public void addCustomButtons() {
        setItem(4, createItem(Material.PAPER, "§aSelect Currency you want to give",
                        Arrays.asList("§7Click to select the currency you want to give")),
                unused -> {});

    }
}
