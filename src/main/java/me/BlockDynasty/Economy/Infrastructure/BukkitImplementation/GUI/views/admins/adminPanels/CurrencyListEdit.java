package me.BlockDynasty.Economy.Infrastructure.BukkitImplementation.GUI.views.admins.adminPanels;

import me.BlockDynasty.Economy.Infrastructure.BukkitImplementation.GUI.components.AbstractGUI;
import me.BlockDynasty.Economy.Infrastructure.BukkitImplementation.GUI.services.GUIService;
import me.BlockDynasty.Economy.aplication.useCase.currency.EditCurrencyUseCase;
import me.BlockDynasty.Economy.aplication.useCase.currency.GetCurrencyUseCase;
import me.BlockDynasty.Economy.domain.entities.currency.Currency;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class CurrencyListEdit extends AbstractCurrenciesList {
    private final JavaPlugin plugin;
    private final GUIService guiService;
    private final GetCurrencyUseCase getCurrencyUseCase;
    private final EditCurrencyUseCase editCurrencyUseCase;

    public CurrencyListEdit(GUIService guiService , JavaPlugin plugin, Player player, GetCurrencyUseCase getCurrencyUseCase, EditCurrencyUseCase editCurrencyUseCase, AbstractGUI abstractGUI) {
        super(guiService, player, getCurrencyUseCase,abstractGUI);
        this.editCurrencyUseCase = editCurrencyUseCase;
        this.guiService = guiService;
        this.plugin = plugin;
        this.getCurrencyUseCase = getCurrencyUseCase;
    }

    @Override
    public void openSubMenu(Currency currency, Player player) {
        EditCurrencyGUI editCurrencyGUI = new EditCurrencyGUI(plugin,guiService,player,currency,editCurrencyUseCase,getCurrencyUseCase,this);
        player.openInventory(editCurrencyGUI.getInventory());
        guiService.registerGUI(player, editCurrencyGUI);
    }

}