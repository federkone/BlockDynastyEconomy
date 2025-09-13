package BlockDynasty.BukkitImplementation.adapters.abstractions;

import BlockDynasty.BukkitImplementation.adapters.GUI.adapters.MaterialAdapter;
import lib.commands.abstractions.IEntityCommands;
import lib.gui.abstractions.IEntityGUI;
import lib.abstractions.IPlayer;
import lib.gui.abstractions.IInventory;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.UUID;

public class EntityPlayerAdapter implements IPlayer {
    Player player;

    private EntityPlayerAdapter(Player player) {
        this.player = player;
    }

    public static EntityPlayerAdapter of(Player player) {
        return new EntityPlayerAdapter(player);
    }

    @Override
    public String getName() {
        return player.getName();
    }

    @Override
    public boolean isOnline() {
        return player.isOnline();
    }

    @Override
    public UUID getUniqueId() {
        return player.getUniqueId();
    }

    @Override
    public void sendMessage(String message) {
        player.sendMessage(translateColorCodes(message));
    }

    private String translateColorCodes(String message) {
        return message.replaceAll("&([0-9a-fk-or])", "§$1");
    }

    @Override
    public boolean hasPermission(String permission) {
        return player.hasPermission(permission);
    }

    @Override
    public void kickPlayer(String message) {
        player.kickPlayer(message);
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
    public void playNotificationSound() {
        player.playSound(player.getLocation(), MaterialAdapter.getPickupSound(), 1.0f, 1.0f);
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

    @Override
    public IEntityGUI asEntityGUI() {
        return this;
    }

    @Override
    public IEntityCommands asEntityCommands() {
        return this;
    }

}
