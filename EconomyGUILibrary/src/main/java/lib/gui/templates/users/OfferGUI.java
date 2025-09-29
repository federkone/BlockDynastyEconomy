package lib.gui.templates.users;

import lib.gui.GUIFactory;
import lib.gui.abstractions.IGUI;
import lib.gui.abstractions.IEntityGUI;
import lib.gui.abstractions.ITextInput;
import lib.gui.abstractions.Materials;
import lib.util.colors.ChatColor;
import lib.util.colors.Colors;

public class OfferGUI extends PayGUI{
    private final IEntityGUI sender;

    public OfferGUI(IEntityGUI sender, IGUI parent, ITextInput textInput) {
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
        setItem(4, createItem(Materials.PAPER, ChatColor.stringValueOf(Colors.GREEN)+"Select Player to Offer",
                        ChatColor.stringValueOf(Colors.WHITE)+"Click to select the player you want to offer",ChatColor.stringValueOf(Colors.WHITE)+"And before that, the Currencies"),
                null);
    }
}
