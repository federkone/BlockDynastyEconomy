package lib.gui.templates.users;

import lib.gui.GUIFactory;
import lib.gui.abstractions.IGUI;
import lib.gui.abstractions.IPlayer;
import lib.gui.abstractions.ITextInput;
import lib.gui.abstractions.Materials;

public class OfferGUI extends PayGUI{
    private final IPlayer sender;

    public OfferGUI(IPlayer sender, IGUI parent, ITextInput textInput) {
        super(sender,parent ,textInput);
        this.sender = sender;
    }

    @Override
    public void openNextSection(BlockDynasty.Economy.domain.entities.account.Player target) {
        GUIFactory.currencyListToOffer(sender, target, this.getParent()).open();
    }

    @Override
    public void addCustomButtons() {
        super.addCustomButtons();
        setItem(4, createItem(Materials.PAPER, "§aSelect Player to Offer",
                        "§7Click to select the player you want to offer", "§7And before that, the Currencies"),
                null);
    }
}
