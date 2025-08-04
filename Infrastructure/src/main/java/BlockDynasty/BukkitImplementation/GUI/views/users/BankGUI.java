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
        setItem(11, createItem(Material.BOOK, "§6Ver Balance",
                "§7Click para ver tu balance"), unused -> {
            openBalanceGUI();
        });

        setItem(15, createItem(MaterialAdapter.getPlayerHead(), "§aPagar a un Jugador",
                "§7Click para pagar a otro jugador"), unused -> {
            openPayGUI();
        });

        setItem(22, createItem(Material.BARRIER, "§cSalir",
                "§7Click para salir"), unused -> player.closeInventory());
    }

    private void openBalanceGUI() {
        GUIFactory.balancePanel(player,this).open();
    }

    private void openPayGUI() {
        GUIFactory.payPanel(player,this).open();
    }
}