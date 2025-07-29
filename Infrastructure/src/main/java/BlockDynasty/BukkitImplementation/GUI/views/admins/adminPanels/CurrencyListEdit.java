package BlockDynasty.BukkitImplementation.GUI.views.admins.adminPanels;

import BlockDynasty.BukkitImplementation.GUI.components.AbstractGUI;
import BlockDynasty.BukkitImplementation.GUI.services.GUIService;
import BlockDynasty.Economy.aplication.useCase.currency.EditCurrencyUseCase;
import BlockDynasty.Economy.aplication.useCase.currency.SearchCurrencyUseCase;
import BlockDynasty.Economy.domain.entities.currency.Currency;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class CurrencyListEdit extends AbstractCurrenciesList {
    private final JavaPlugin plugin;
    private final GUIService guiService;
    private final SearchCurrencyUseCase searchCurrencyUseCase;
    private final EditCurrencyUseCase editCurrencyUseCase;

    public CurrencyListEdit(GUIService guiService , JavaPlugin plugin, Player player, SearchCurrencyUseCase searchCurrencyUseCase, EditCurrencyUseCase editCurrencyUseCase, AbstractGUI abstractGUI) {
        super(guiService, player, searchCurrencyUseCase,abstractGUI);
        this.editCurrencyUseCase = editCurrencyUseCase;
        this.guiService = guiService;
        this.plugin = plugin;
        this.searchCurrencyUseCase = searchCurrencyUseCase;
    }

    @Override
    public void openSubMenu(Currency currency, Player player) {
        EditCurrencyGUI editCurrencyGUI = new EditCurrencyGUI(plugin,guiService,player,currency,editCurrencyUseCase, searchCurrencyUseCase,this);
        player.openInventory(editCurrencyGUI.getInventory());
        guiService.registerGUI(player, editCurrencyGUI);
    }

}