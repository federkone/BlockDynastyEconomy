package lib.gui.templates.administrators.mainMenus;

import lib.gui.GUIFactory;
import lib.gui.abstractions.IGUI;
import lib.gui.abstractions.IEntityGUI;
import lib.gui.abstractions.Materials;
import lib.gui.templates.abstractions.AbstractGUI;

public class CurrencyPanelGUI extends AbstractGUI {
    private final IEntityGUI player;

    public CurrencyPanelGUI(IEntityGUI player, IGUI parent) {
        super("Currency Manager", 3,player,parent);
        this.player = player;
        setupGUI();
    }

    private void setupGUI() {
        // Create Currency button
        setItem(10, createItem(Materials.EMERALD, "Create Currency",
                "Click to create new currency"), unused -> {
            GUIFactory.createCurrencyPanel(player,this);

        });

        // Delete Currency button
        setItem(12, createItem(Materials.REDSTONE, "Delete Currency",
                "Click to delete currency"), unused -> {
            GUIFactory.currencyListToDeletePanel(player, this).open();
        });

        // Edit Currency button
        setItem(14, createItem(Materials.BOOK, "Edit Currency",
                "Click to edit Currency"), unused -> {
            GUIFactory.currencyListToEditPanel(player, this).open();
        });

        // Toggle Features button
        setItem(16, createItem(Materials.PAPER, "Config Features",
                "Click"), unused -> {
            player.sendMessage("[Bank] This feature is not implemented yet.");
        });

        // Exit button
        setItem(22, createItem(Materials.BARRIER, "Back",
                "Click to go back"), unused -> {
            this.openParent();
        });
    }

}