package BlockDynasty.BukkitImplementation.GUI.views.admins;

import BlockDynasty.BukkitImplementation.GUI.GUIFactory;
import BlockDynasty.BukkitImplementation.GUI.components.AbstractGUI;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class AdminPanelGUI extends AbstractGUI {
    private final Player sender;

    public AdminPanelGUI(Player sender)
    {
        super("Economy Admin Panel", 3,sender);
        this.sender = sender;

        initializeButtons();
    }

    private  void initializeButtons() {
        setItem(11, createItem(Material.PAPER, "Edit Currencies", "Click to edit currencies"), event -> {
            GUIFactory.currencyPanel( sender, this).open();
        });


        setItem(15, createItem(Material.DIAMOND_SWORD, "Manage Accounts", "Click to manage accounts"), event -> {
            GUIFactory.accountPanel( sender, this).open();
        });

        setItem(22, createItem(Material.BARRIER, "Close", "Click to close this menu"), event -> {
            sender.closeInventory();
        });


    }
}
