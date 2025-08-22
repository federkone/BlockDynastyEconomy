package BlockDynasty.BukkitImplementation.GUI.views.admins.submenus.Currencies;

import BlockDynasty.BukkitImplementation.GUI.GUIFactory;
import BlockDynasty.BukkitImplementation.GUI.components.AnvilMenu;
import BlockDynasty.BukkitImplementation.GUI.components.CurrenciesList;
import BlockDynasty.BukkitImplementation.GUI.components.IGUI;
import BlockDynasty.BukkitImplementation.config.file.Message;
import BlockDynasty.Economy.domain.persistence.Exceptions.TransactionException;
import BlockDynasty.Economy.aplication.useCase.currency.DeleteCurrencyUseCase;
import BlockDynasty.Economy.aplication.useCase.currency.SearchCurrencyUseCase;
import BlockDynasty.Economy.domain.entities.currency.Currency;
import BlockDynasty.Economy.domain.entities.currency.Exceptions.CurrencyNotFoundException;
import org.bukkit.entity.Player;

public class CurrencyListDelete extends CurrenciesList {
    private final DeleteCurrencyUseCase deleteCurrencyUseCase;
    private final Player player;

    public CurrencyListDelete( Player player, SearchCurrencyUseCase searchCurrencyUseCase,
                              DeleteCurrencyUseCase deleteCurrencyUseCase, IGUI parent)  {
            super( player, searchCurrencyUseCase,parent);
            this.deleteCurrencyUseCase =deleteCurrencyUseCase;
            this.player = player;
    }

    @Override
    public void handleLeftItemClick(Currency currency) {
        openAnvilConfirmation(currency);
    }

    public void openAnvilConfirmation(Currency currency) {
        AnvilMenu.open(this, player, "Confirm Deletion", "Type 'delete' to confirm", input -> {
            if ("delete".equalsIgnoreCase(input)) {
                return execute(currency);
            } else {
                player.sendMessage(Message.getPrefix() + "§cDeletion cancelled. Type 'delete' to confirm.");
                this.open();
                return "Deletion cancelled.";
            }
        });
    }

    private String execute(Currency currency){
        try {
            deleteCurrencyUseCase.deleteCurrency(currency.getSingular());
            player.sendMessage(Message.getPrefix() + "§7Deleted currency: §a" + currency.getSingular());
            this.openParent();
            return "Currency deleted successfully.";
        } catch (CurrencyNotFoundException e) {
            player.sendMessage(Message.getPrefix()+"§7"+ e.getMessage()+" Make sure you have another default currency before deleting it.");
            return e.getMessage();
        } catch (TransactionException e) {
            player.sendMessage(Message.getPrefix() + "§cError while deleting currency: §4" + e.getMessage());
            return e.getMessage();
        }
    }
}