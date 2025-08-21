package BlockDynasty.BukkitImplementation.GUI.views.admins;

import BlockDynasty.BukkitImplementation.GUI.GUIFactory;
import BlockDynasty.BukkitImplementation.GUI.components.AbstractGUI;
import BlockDynasty.BukkitImplementation.GUI.components.IGUI;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class CurrencyPanelGUI extends AbstractGUI {
    private final Player player;

    public CurrencyPanelGUI( Player player, IGUI parent) {
        super("Currency Manager", 3,player,parent);
        this.player = player;
        setupGUI();
    }

    private void setupGUI() {
        // Create Currency button
        setItem(10, createItem(Material.EMERALD, "§aCreate Currency",
                "§7Click to create new currency"), unused -> {
            GUIFactory.createCurrencyPanel(player,this);

        });

        // Delete Currency button
        setItem(12, createItem(Material.REDSTONE, "§cDelete Currency",
                "§7Click to delete currency"), unused -> {
            GUIFactory.currencyListToDeletePanel(player, this).open();
        });

        // Edit Currency button
        setItem(14, createItem(Material.BOOK, "§eEdit Currency",
                "§7Click to edit Currency"), unused -> {
            GUIFactory.currencyListToEditPanel(player, this).open();
        });

        // Toggle Features button
        setItem(16, createItem(Material.PAPER, "§bConfig Features",
                "§7Click"), unused -> {
            player.sendMessage("§a[Bank] §7This feature is not implemented yet.");
        });

        // Exit button
        setItem(22, createItem(Material.BARRIER, "§cBack",
                "§7Click to go back"), unused -> {
            this.openParent();
        });
    }

}