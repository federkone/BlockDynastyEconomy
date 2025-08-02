package BlockDynasty.BukkitImplementation.GUI.views.admins.submenus.Currencies;

import BlockDynasty.BukkitImplementation.GUI.components.AbstractGUI;
import BlockDynasty.BukkitImplementation.GUI.components.CurrenciesList;
import BlockDynasty.BukkitImplementation.GUI.services.GUIService;
import BlockDynasty.Economy.aplication.useCase.currency.EditCurrencyUseCase;
import BlockDynasty.Economy.aplication.useCase.currency.SearchCurrencyUseCase;
import BlockDynasty.Economy.domain.entities.currency.Currency;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class CurrencyListEdit extends CurrenciesList {
    private final SearchCurrencyUseCase searchCurrencyUseCase;
    private final EditCurrencyUseCase editCurrencyUseCase;

    public CurrencyListEdit( Player player, SearchCurrencyUseCase searchCurrencyUseCase, EditCurrencyUseCase editCurrencyUseCase, AbstractGUI abstractGUI) {
        super( player, searchCurrencyUseCase,abstractGUI);
        this.editCurrencyUseCase = editCurrencyUseCase;

        this.searchCurrencyUseCase = searchCurrencyUseCase;
    }

    @Override
    public void openSubMenu(Currency currency, Player player) {
        EditCurrencyGUI editCurrencyGUI = new EditCurrencyGUI(player,currency,editCurrencyUseCase, searchCurrencyUseCase,this);
        editCurrencyGUI.open(player);
    }

}