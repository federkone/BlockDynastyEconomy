package BlockDynasty.BukkitImplementation.GUI.views.admins;

import BlockDynasty.BukkitImplementation.GUI.GUIFactory;
import BlockDynasty.BukkitImplementation.GUI.MaterialAdapter;
import BlockDynasty.BukkitImplementation.GUI.components.AbstractGUI;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class AdminPanelGUI extends AbstractGUI {
    private final Player sender;

    public AdminPanelGUI(Player sender)
    {
        super("Economy Admin Panel", 5,sender);
        this.sender = sender;

        initializeButtons();
    }

    private void initializeButtons() {
        setItem(20, createItem(Material.EMERALD, "Edit Currencies", "Click to edit currencies"), event -> {
            GUIFactory.currencyPanel( sender, this).open();
        });


        setItem(24, createItem(MaterialAdapter.getPlayerHead(), "Manage Accounts", "Click to manage accounts"), event -> {
            GUIFactory.accountPanel( sender, this).open();
        });

        setItem(40, createItem(Material.BARRIER, "Close", "Click to close this menu"), event -> {
            sender.closeInventory();
        });


    }
}
