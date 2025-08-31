package lib.templates.administrators.mainMenus;

import lib.GUIFactory;
import lib.components.IPlayer;
import lib.components.Materials;
import lib.templates.abstractions.AbstractGUI;

public class AdminPanelGUI extends AbstractGUI {
    private final IPlayer sender;

    public AdminPanelGUI(IPlayer sender)
    {
        super("Economy Admin Panel", 5,sender);
        this.sender = sender;

        initializeButtons();
    }

    private void initializeButtons() {
        setItem(20, createItem(Materials.EMERALD, "Edit Currencies", "Click to edit currencies"), event -> {
            GUIFactory.currencyPanel( sender, this).open();
        });


        setItem(24, createItem(Materials.PLAYER_HEAD, "Manage Accounts", "Click to manage accounts"), event -> {
            GUIFactory.accountPanel( sender, this).open();
        });

        setItem(40, createItem(Materials.BARRIER, "Close", "Click to close this menu"), event -> {
            this.close();
        });


    }
}