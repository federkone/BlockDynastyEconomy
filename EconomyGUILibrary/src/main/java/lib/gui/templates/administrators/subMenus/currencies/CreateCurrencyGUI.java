package lib.gui.templates.administrators.subMenus.currencies;

import BlockDynasty.Economy.aplication.useCase.currency.CreateCurrencyUseCase;
import BlockDynasty.Economy.aplication.useCase.currency.SearchCurrencyUseCase;
import BlockDynasty.Economy.domain.entities.currency.Exceptions.CurrencyException;
import lib.gui.GUIFactory;
import lib.gui.abstractions.IGUI;
import lib.gui.abstractions.IEntityGUI;
import lib.gui.abstractions.ITextInput;
import lib.util.colors.ChatColor;
import lib.util.colors.Colors;

public class CreateCurrencyGUI {
    private final IEntityGUI player;
    private final CreateCurrencyUseCase createCurrencyUseCase;
    //private final SearchCurrencyUseCase searchCurrencyUseCase;
    private String singularName;
    private final IGUI parent;
    private final ITextInput textInput;
    public CreateCurrencyGUI(IEntityGUI player, CreateCurrencyUseCase createCurrencyUseCase, SearchCurrencyUseCase searchCurrencyUseCase, IGUI parent, ITextInput textInput) {
        this.parent = parent;
        this.player = player;
        this.textInput = textInput;
        this.createCurrencyUseCase = createCurrencyUseCase;
        //this.searchCurrencyUseCase = searchCurrencyUseCase;
        openSingularNameInput();
    }

    private void openSingularNameInput() {
        textInput.open(parent,player,"Singular Name","Name..", s -> {
            singularName = s.trim();
            openPluralNameInput();
            return null;
        });
    }

    private void openPluralNameInput() {
        textInput.open(player,"Plural Name", "Name..", s -> {
            createCurrency(singularName, s.trim());
            return null;
        });
    }

    private void createCurrency(String singular, String plural) {
        try {
            createCurrencyUseCase.createCurrency(singular, plural);
            player.sendMessage(ChatColor.stringValueOf(Colors.GREEN)+"[Bank] "+ChatColor.stringValueOf(Colors.GRAY)+"The currency " + singular + ChatColor.stringValueOf(Colors.GREEN)+" has been created successfully.");

            //Result<Currency> result = searchCurrencyUseCase.getCurrency(singular);
            //if (result.isSuccess()) {
            //    GUIFactory.editCurrencyPanel(player, result.getValue(), parent).open();
            //}
            GUIFactory.currencyPanel(player, parent).open();
        }
        catch (CurrencyException e) {
            player.sendMessage(ChatColor.stringValueOf(Colors.GREEN)+"[Bank] "+ChatColor.stringValueOf(Colors.RED)+"Error creating currency: "+ChatColor.stringValueOf(Colors.YELLOW) + e.getMessage());
            player.closeInventory();
        }
    }
}
