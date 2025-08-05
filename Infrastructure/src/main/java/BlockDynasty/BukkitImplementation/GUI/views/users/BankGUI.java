package BlockDynasty.BukkitImplementation.GUI.views.users;

import BlockDynasty.BukkitImplementation.GUI.GUIFactory;
import BlockDynasty.BukkitImplementation.GUI.MaterialAdapter;
import BlockDynasty.BukkitImplementation.GUI.components.AbstractGUI;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class BankGUI extends AbstractGUI {
    private final Player player;

    public BankGUI( Player player) {
        super("Banco", 3,player);
        this.player = player;
        setupGUI();
    }

    private void setupGUI() {
        // Balance option
        setItem(11, createItem(Material.BOOK, "§6See Balance",
                "§7Click to see your balance"), unused -> {
            GUIFactory.balancePanel(player,this).open();
        });

        setItem(15, createItem(MaterialAdapter.getPlayerHead(), "§aPay a Player",
                "§7Click to pay another player"), unused -> {
            GUIFactory.payPanel(player,this).open();
        });

        setItem(22, createItem(Material.BARRIER, "§cExit",
                "§7Click to exit"), unused -> player.closeInventory());
    }
}