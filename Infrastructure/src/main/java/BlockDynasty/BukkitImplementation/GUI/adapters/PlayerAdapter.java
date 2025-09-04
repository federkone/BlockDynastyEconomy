package BlockDynasty.BukkitImplementation.GUI.adapters;

import lib.gui.abstractions.IInventory;
import lib.gui.abstractions.IPlayer;
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
    public void playSuccessSound() {
        player.playSound(player.getLocation(),  MaterialAdapter.getClickSound(), 0.3f, 1.0f);
    }

    @Override
    public void playFailureSound() {
        player.playSound(player.getLocation(), "block.note_block.pling", 0.3f, 0.5f);
    }

    @Override
    public Object getHandle() {
        return player;
    }
}
