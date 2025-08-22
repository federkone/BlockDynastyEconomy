package BlockDynasty.BukkitImplementation.GUI.views.admins.submenus.Currencies;

import BlockDynasty.BukkitImplementation.GUI.GUIFactory;
import BlockDynasty.BukkitImplementation.GUI.components.CurrenciesList;
import BlockDynasty.BukkitImplementation.GUI.components.IGUI;
import BlockDynasty.Economy.aplication.useCase.currency.SearchCurrencyUseCase;
import BlockDynasty.Economy.domain.entities.currency.Currency;
import org.bukkit.entity.Player;

public class CurrencyListEdit extends CurrenciesList {
    private final Player player;
    public CurrencyListEdit( Player player, SearchCurrencyUseCase searchCurrencyUseCase, IGUI abstractGUI) {
        super( player, searchCurrencyUseCase,abstractGUI);
        this.player = player;
    }

    @Override
    public void handleLeftItemClick(Currency currency) {
        GUIFactory.editCurrencyPanel(player, currency, this).open();
    }

}