package BlockDynasty.BukkitImplementation.GUI.views.admins.submenus.Currencies;

import BlockDynasty.BukkitImplementation.GUI.components.AbstractGUI;
import BlockDynasty.BukkitImplementation.GUI.components.CurrenciesList;
import BlockDynasty.BukkitImplementation.config.file.Message;
import BlockDynasty.Economy.domain.persistence.Exceptions.TransactionException;
import BlockDynasty.Economy.aplication.useCase.currency.DeleteCurrencyUseCase;
import BlockDynasty.Economy.aplication.useCase.currency.SearchCurrencyUseCase;
import BlockDynasty.Economy.domain.entities.currency.Currency;
import BlockDynasty.Economy.domain.entities.currency.Exceptions.CurrencyNotFoundException;
import org.bukkit.entity.Player;

public class CurrencyListDelete extends CurrenciesList {
    private final DeleteCurrencyUseCase deleteCurrencyUseCase;

    public CurrencyListDelete( Player player, SearchCurrencyUseCase searchCurrencyUseCase,
                              DeleteCurrencyUseCase deleteCurrencyUseCase, AbstractGUI abstractGUI)  {
            super( player, searchCurrencyUseCase,abstractGUI);
            this.deleteCurrencyUseCase =deleteCurrencyUseCase;
    }

    @Override
    public void openSubMenu(Currency currency,Player player) {
        try {
            deleteCurrencyUseCase.deleteCurrency(currency.getSingular());
            player.sendMessage(Message.getPrefix() + "§7Deleted currency: §a" + currency.getSingular());
        } catch (CurrencyNotFoundException e) {
            player.sendMessage(Message.getPrefix()+"§7"+ e.getMessage()+" asegurate de tener otra moneda por defecto antes de eliminarla");
        } catch (TransactionException e) {
            player.sendMessage(Message.getPrefix() + "§cError while deleting currency: §4" + e.getMessage());
        }
    }
}