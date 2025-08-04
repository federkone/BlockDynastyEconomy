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
        super("Seleccionar Color", 4,player,parent);
        this.player = player;
        this.currency = currency;
        this.editCurrencyUseCase = editCurrencyUseCase;
        setupColorGUI();
    }

    private void setupColorGUI() {
        setItem(10, createColorItem("WHITE_WOOL", ChatColor.WHITE, "Blanco"),
                unused -> handleColorSelection(ChatColor.WHITE, "Blanco"));
        setItem(11, createColorItem("YELLOW_WOOL", ChatColor.YELLOW, "Amarillo"),
                unused -> handleColorSelection(ChatColor.YELLOW, "Amarillo"));
        setItem(12, createColorItem("RED_WOOL", ChatColor.RED, "Rojo"),
                unused -> handleColorSelection(ChatColor.RED, "Rojo"));
        setItem(13, createColorItem("PINK_WOOL", ChatColor.LIGHT_PURPLE, "Rosa"),
                unused -> handleColorSelection(ChatColor.LIGHT_PURPLE, "Rosa"));
        setItem(14, createColorItem("PURPLE_WOOL", ChatColor.DARK_PURPLE, "Morado"),
                unused -> handleColorSelection(ChatColor.DARK_PURPLE, "Morado"));
        setItem(15, createColorItem("ORANGE_WOOL", ChatColor.GOLD, "Dorado"),
                unused -> handleColorSelection(ChatColor.GOLD, "Dorado"));
        setItem(16, createColorItem("LIME_WOOL", ChatColor.GREEN, "Verde"),
                unused -> handleColorSelection(ChatColor.GREEN, "Verde"));
        setItem(19, createColorItem("GRAY_WOOL", ChatColor.GRAY, "Gris"),
                unused -> handleColorSelection(ChatColor.GRAY, "Gris"));
        setItem(20, createColorItem("LIGHT_GRAY_WOOL", ChatColor.DARK_GRAY, "Gris Oscuro"),
                unused -> handleColorSelection(ChatColor.DARK_GRAY, "Gris Oscuro"));
        setItem(21, createColorItem("CYAN_WOOL", ChatColor.AQUA, "Agua"),
                unused -> handleColorSelection(ChatColor.AQUA, "Agua"));
        setItem(22, createColorItem("LIGHT_BLUE_WOOL", ChatColor.BLUE, "Azul"),
                unused -> handleColorSelection(ChatColor.BLUE, "Azul"));
        setItem(23, createColorItem("BLUE_WOOL", ChatColor.DARK_BLUE, "Azul Oscuro"),
                unused -> handleColorSelection(ChatColor.DARK_BLUE, "Azul Oscuro"));
        setItem(24, createColorItem("BROWN_WOOL", ChatColor.DARK_RED, "Rojo Oscuro"),
                unused -> handleColorSelection(ChatColor.DARK_RED, "Rojo Oscuro"));
        setItem(25, createColorItem("GREEN_WOOL", ChatColor.DARK_GREEN, "Verde Oscuro"),
                unused -> handleColorSelection(ChatColor.DARK_GREEN, "Verde Oscuro"));

        setItem(31, createItem(Material.BARRIER, "§cVolver",
                "§7Click para volver"), unused -> {
            this.openParent();
        });
    }

    private void handleColorSelection(ChatColor chatColor, String colorName) {
        try {
            editCurrencyUseCase.editColor(currency.getSingular(), chatColor.name());
            player.sendMessage("§a[Banco] §7Color actualizado correctamente a " + colorName + ".");
            GUIFactory.editCurrencyPanel(player,currency,getParent().getParent()).open();
        } catch (Exception e) {
            player.sendMessage("§a[Banco] §cError: §e" + e.getMessage());
            this.openParent();
        }
    }

    private ItemStack createColorItem(String material, ChatColor chatColor, String colorName) {
        return createItem(MaterialAdapter.adaptWool(material), chatColor + colorName,
                "§7Click para seleccionar este color",
                chatColor + "Ejemplo: " + currency.getSingular());
    }
}