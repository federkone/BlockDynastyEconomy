package BlockDynasty.BukkitImplementation.GUI.views.admins.submenus.Currencies;

import BlockDynasty.BukkitImplementation.GUI.GUIFactory;
import BlockDynasty.BukkitImplementation.GUI.MaterialAdapter;
import BlockDynasty.BukkitImplementation.GUI.components.AbstractGUI;
import BlockDynasty.BukkitImplementation.GUI.components.IGUI;
import BlockDynasty.Economy.aplication.useCase.currency.EditCurrencyUseCase;
import BlockDynasty.Economy.domain.entities.currency.Currency;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ColorSelectionGUI extends AbstractGUI {
    private final Player player;
    private final Currency currency;
    private final EditCurrencyUseCase editCurrencyUseCase;

    public ColorSelectionGUI(Player player, Currency currency, EditCurrencyUseCase editCurrencyUseCase, IGUI parent) {
        super("Select color", 4,player,parent);
        this.player = player;
        this.currency = currency;
        this.editCurrencyUseCase = editCurrencyUseCase;
        setupColorGUI();
    }

    private void setupColorGUI() {
        setItem(10, createColorItem("WHITE_WOOL", ChatColor.WHITE, "WHITE"),
                unused -> handleColorSelection(ChatColor.WHITE, "WHITE"));
        setItem(11, createColorItem("YELLOW_WOOL", ChatColor.YELLOW, "YELLOW"),
                unused -> handleColorSelection(ChatColor.YELLOW, "YELLOW"));
        setItem(12, createColorItem("RED_WOOL", ChatColor.RED, "RED"),
                unused -> handleColorSelection(ChatColor.RED, "RED"));
        setItem(13, createColorItem("PINK_WOOL", ChatColor.LIGHT_PURPLE, "LIGHT_PURPLE"),
                unused -> handleColorSelection(ChatColor.LIGHT_PURPLE, "LIGHT_PURPLE"));
        setItem(14, createColorItem("PURPLE_WOOL", ChatColor.DARK_PURPLE, "DARK_PURPLE"),
                unused -> handleColorSelection(ChatColor.DARK_PURPLE, "DARK_PURPLE"));
        setItem(15, createColorItem("ORANGE_WOOL", ChatColor.GOLD, "GOLD"),
                unused -> handleColorSelection(ChatColor.GOLD, "GOLD"));
        setItem(16, createColorItem("LIME_WOOL", ChatColor.GREEN, "GREEN"),
                unused -> handleColorSelection(ChatColor.GREEN, "GREEN"));
        setItem(19, createColorItem("GRAY_WOOL", ChatColor.GRAY, "GRAY"),
                unused -> handleColorSelection(ChatColor.GRAY, "GRAY"));
        setItem(20, createColorItem("LIGHT_GRAY_WOOL", ChatColor.DARK_GRAY, "DARK_GRAY"),
                unused -> handleColorSelection(ChatColor.DARK_GRAY, "DARK_GRAY"));
        setItem(21, createColorItem("CYAN_WOOL", ChatColor.AQUA, "AQUA"),
                unused -> handleColorSelection(ChatColor.AQUA, "AQUA"));
        setItem(22, createColorItem("LIGHT_BLUE_WOOL", ChatColor.BLUE, "BLUE"),
                unused -> handleColorSelection(ChatColor.BLUE, "BLUE"));
        setItem(23, createColorItem("BLUE_WOOL", ChatColor.DARK_BLUE, "DARK_BLUE"),
                unused -> handleColorSelection(ChatColor.DARK_BLUE, "DARK_BLUE"));
        setItem(24, createColorItem("BROWN_WOOL", ChatColor.DARK_RED, "DARK_RED"),
                unused -> handleColorSelection(ChatColor.DARK_RED, "DARK_RED"));
        setItem(25, createColorItem("GREEN_WOOL", ChatColor.DARK_GREEN, "DARK_GREEN"),
                unused -> handleColorSelection(ChatColor.DARK_GREEN, "DARK_GREEN"));

        setItem(31, createItem(Material.BARRIER, "§cBack",
                "§7Click to go back"), unused -> {
            this.openParent();
        });
    }

    private void handleColorSelection(ChatColor chatColor, String colorName) {
        try {
            editCurrencyUseCase.editColor(currency.getSingular(), chatColor.name());
            player.sendMessage("§a[Banco] §7Color updated successfully to " + colorName + ".");
            GUIFactory.editCurrencyPanel(player,currency,getParent().getParent()).open();
        } catch (Exception e) {
            player.sendMessage("§a[Banco] §cError: §e" + e.getMessage());
            this.openParent();
        }
    }

    private ItemStack createColorItem(String material, ChatColor chatColor, String colorName) {
        return createItem(MaterialAdapter.adaptWool(material), chatColor + colorName,
                "§7Click to select this color",
                chatColor + "Example: " + currency.getSingular());
    }
}