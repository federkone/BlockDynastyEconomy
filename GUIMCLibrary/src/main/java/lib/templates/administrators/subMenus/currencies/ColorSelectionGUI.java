package lib.templates.administrators.subMenus.currencies;

import BlockDynasty.Economy.aplication.useCase.currency.EditCurrencyUseCase;
import BlockDynasty.Economy.domain.entities.currency.Currency;
import lib.GUIFactory;
import lib.components.IGUI;
import lib.components.IItemStack;
import lib.components.IPlayer;
import lib.components.Materials;
import lib.templates.abstractions.AbstractGUI;
import lib.templates.abstractions.ChatColor;

public class ColorSelectionGUI extends AbstractGUI {
    private final IPlayer player;
    private final Currency currency;
    private final EditCurrencyUseCase editCurrencyUseCase;

    public ColorSelectionGUI(IPlayer player, Currency currency, EditCurrencyUseCase editCurrencyUseCase, IGUI parent) {
        super("Select color", 4,player,parent);
        this.player = player;
        this.currency = currency;
        this.editCurrencyUseCase = editCurrencyUseCase;
        setupColorGUI();
    }

    private void setupColorGUI() {
        setItem(10, createColorItem(Materials.WHITE_WOOL, ChatColor.stringValueOf("WHITE"), "WHITE"),
                unused -> handleColorSelection( "WHITE"));
        setItem(11, createColorItem(Materials.YELLOW_WOOL, ChatColor.stringValueOf("YELLOW"), "YELLOW"),
                unused -> handleColorSelection( "YELLOW"));
        setItem(12, createColorItem(Materials.RED_WOOL, ChatColor.stringValueOf("RED"), "RED"),
                unused -> handleColorSelection( "RED"));
        setItem(13, createColorItem(Materials.PINK_WOOL, ChatColor.stringValueOf("LIGHT_PURPLE"), "LIGHT_PURPLE"),
                unused -> handleColorSelection( "LIGHT_PURPLE"));
        setItem(14, createColorItem(Materials.PURPLE_WOOL, ChatColor.stringValueOf("DARK_PURPLE"), "DARK_PURPLE"),
                unused -> handleColorSelection( "DARK_PURPLE"));
        setItem(15, createColorItem(Materials.ORANGE_WOOL, ChatColor.stringValueOf("GOLD"), "GOLD"),
                unused -> handleColorSelection( "GOLD"));
        setItem(16, createColorItem(Materials.LIME_WOOL, ChatColor.stringValueOf("GREEN"), "GREEN"),
                unused -> handleColorSelection( "GREEN"));
        setItem(19, createColorItem(Materials.GRAY_WOOL, ChatColor.stringValueOf("GRAY"), "GRAY"),
                unused -> handleColorSelection( "GRAY"));
        setItem(20, createColorItem(Materials.LIGHT_GRAY_WOOL, ChatColor.stringValueOf("DARK_GRAY"), "DARK_GRAY"),
                unused -> handleColorSelection( "DARK_GRAY"));
        setItem(21, createColorItem(Materials.CYAN_WOOL, ChatColor.stringValueOf("AQUA"), "AQUA"),
                unused -> handleColorSelection("AQUA"));
        setItem(22, createColorItem(Materials.LIGHT_BLUE_WOOL, ChatColor.stringValueOf("BLUE"), "BLUE"),
                unused -> handleColorSelection("BLUE"));
        setItem(23, createColorItem(Materials.BLUE_WOOL, ChatColor.stringValueOf("DARK_BLUE"), "DARK_BLUE"),
                unused -> handleColorSelection( "DARK_BLUE"));
        setItem(24, createColorItem(Materials.BROWN_WOOL, ChatColor.stringValueOf("DARK_RED"), "DARK_RED"),
                unused -> handleColorSelection( "DARK_RED"));
        setItem(25, createColorItem(Materials.GREEN_WOOL, ChatColor.stringValueOf("DARK_GREEN"), "DARK_GREEN"),
                unused -> handleColorSelection( "DARK_GREEN"));

        setItem(31, createItem(Materials.BARRIER, "§cBack",
                "§7Click to go back"), unused -> {
            this.openParent();
        });
    }

    private void handleColorSelection(String colorName) {
        try {
            editCurrencyUseCase.editColor(currency.getSingular(), colorName);
            player.sendMessage("§a[Banco] §7Color updated successfully to " + colorName + ".");
            GUIFactory.editCurrencyPanel(player,currency,getParent().getParent()).open();
        } catch (Exception e) {
            player.sendMessage("§a[Banco] §cError: §e" + e.getMessage());
            this.openParent();
        }
    }

    private IItemStack createColorItem(Materials material, String chatColor, String colorName) {
        return createItem(material, chatColor + colorName,
                "§7Click to select this color",
                chatColor + "Example: " + currency.getSingular());
    }
}