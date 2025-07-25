package BlockDynasty.BukkitImplementation.GUI.views.admins.adminPanels;

import BlockDynasty.BukkitImplementation.GUI.components.AbstractGUI;
import BlockDynasty.BukkitImplementation.GUI.services.GUIService;
import BlockDynasty.BukkitImplementation.config.file.F;
import BlockDynasty.Economy.domain.persistence.Exceptions.TransactionException;
import BlockDynasty.Economy.aplication.useCase.currency.DeleteCurrencyUseCase;
import BlockDynasty.Economy.aplication.useCase.currency.GetCurrencyUseCase;
import BlockDynasty.Economy.domain.entities.currency.Currency;
import BlockDynasty.Economy.domain.entities.currency.Exceptions.CurrencyNotFoundException;
import org.bukkit.entity.Player;

public class CurrencyListDelete extends AbstractCurrenciesList {
    private final DeleteCurrencyUseCase deleteCurrencyUseCase;

    public CurrencyListDelete(GUIService guiService, Player player, GetCurrencyUseCase getCurrencyUseCase,
                              DeleteCurrencyUseCase deleteCurrencyUseCase, AbstractGUI abstractGUI)  {
            super(guiService, player,getCurrencyUseCase,abstractGUI);
            this.deleteCurrencyUseCase =deleteCurrencyUseCase;
    }

    @Override
    public void openSubMenu(Currency currency,Player player) {
        try {
            deleteCurrencyUseCase.deleteCurrency(currency.getSingular());
            player.sendMessage(F.getPrefix() + "§7Deleted currency: §a" + currency.getSingular());
        } catch (CurrencyNotFoundException e) {
            player.sendMessage(F.getPrefix()+"§7"+ e.getMessage()+" asegurate de tener otra moneda por defecto antes de eliminarla");
        } catch (TransactionException e) {
            player.sendMessage(F.getPrefix() + "§cError while deleting currency: §4" + e.getMessage());
        }
    }
}