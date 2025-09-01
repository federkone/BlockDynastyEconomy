package BlockDynasty.BukkitImplementation.GUI.adapters;

import lib.components.IGUI;
import lib.components.IInventory;
import lib.components.IPlayer;
import lib.templates.abstractions.AbstractGUI;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.UUID;

public class PlayerAdapter implements IPlayer {
    Player player;

    public PlayerAdapter(Player player) {
        this.player = player;
    }
    @Override
    public UUID getUniqueId() {
        return player.getUniqueId();
    }

    @Override
    public String getName() {
        return player.getName();
    }

    @Override
    public void sendMessage(String message) {
        player.sendMessage(message);
    }

    @Override
    public void closeInventory() {
        player.closeInventory();
    }

    @Override
    public void openInventory(IInventory inventory) {
        Inventory inventoryBukkit= (Inventory) inventory.getHandle();
        player.openInventory(inventoryBukkit);
    }

    @Override
    public Object getHandle() {
        return player;
    }
}
