package lib.gui.templates.administrators.subMenus.currencies;

import BlockDynasty.Economy.aplication.useCase.currency.DeleteCurrencyUseCase;
import BlockDynasty.Economy.aplication.useCase.currency.SearchCurrencyUseCase;
import BlockDynasty.Economy.domain.entities.currency.Currency;
import BlockDynasty.Economy.domain.entities.currency.Exceptions.CurrencyNotFoundException;
import BlockDynasty.Economy.domain.persistence.Exceptions.TransactionException;
import lib.gui.abstractions.IGUI;
import lib.gui.abstractions.IPlayer;
import lib.gui.abstractions.ITextInput;
import lib.gui.templates.abstractions.CurrenciesList;

public class CurrencyListDelete extends CurrenciesList {
    private final DeleteCurrencyUseCase deleteCurrencyUseCase;
    private final IPlayer player;
    private final ITextInput textInput;

    public CurrencyListDelete(IPlayer player, SearchCurrencyUseCase searchCurrencyUseCase,
                              DeleteCurrencyUseCase deleteCurrencyUseCase, IGUI parent, ITextInput textInput)  {
        super( player, searchCurrencyUseCase,parent,textInput);
        this.deleteCurrencyUseCase =deleteCurrencyUseCase;
        this.player = player;
        this.textInput = textInput;

    }

    @Override
    public void functionLeftItemClick(Currency currency) {
        openAnvilConfirmation(currency);
    }

    public void openAnvilConfirmation(Currency currency) {
        textInput.open(this, player, "Confirm Deletion", "Type 'delete' to confirm", input -> {
            if ("delete".equalsIgnoreCase(input)) {
                return execute(currency);
            } else {
                //player.sendMessage(Message.getPrefix() + "§cDeletion cancelled. Type 'delete' to confirm.");
                this.open();
                return "Deletion cancelled.";
            }
        });
    }

    private String execute(Currency currency){
        try {
            deleteCurrencyUseCase.deleteCurrency(currency.getSingular());
            //player.sendMessage(Message.getPrefix() + "§7Deleted currency: §a" + currency.getSingular());
            this.openParent();
            return "Currency deleted successfully.";
        } catch (CurrencyNotFoundException e) {
            //player.sendMessage(Message.getPrefix()+"§7"+ e.getMessage()+" Make sure you have another default currency before deleting it.");
            return e.getMessage();
        } catch (TransactionException e) {
            //player.sendMessage(Message.getPrefix() + "§cError while deleting currency: §4" + e.getMessage());
            return e.getMessage();
        }
    }
}