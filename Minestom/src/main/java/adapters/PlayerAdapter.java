package adapters;

import lib.abstractions.IPlayer;
import lib.commands.abstractions.IEntityCommands;
import lib.gui.components.IEntityGUI;
import lib.gui.components.IInventory;
import net.minestom.server.entity.Player;

import java.util.UUID;

public class PlayerAdapter implements IPlayer {
    private Player player;

    public PlayerAdapter(Player player) {
        this.player = player;
    }

    @Override
    public void playNotificationSound() {

    }

    @Override
    public boolean isOnline() {
        return player.isOnline();
    }

    @Override
    public boolean hasPermission(String permission) {
        return true;
    }

    @Override
    public void kickPlayer(String message) {
        player.kick(message);
    }

    @Override
    public IEntityGUI asEntityGUI() {
        return this;
    }

    @Override
    public UUID getUniqueId() {
        return player.getUuid();
    }

    @Override
    public String getName() {
        return player.getUsername();
    }

    @Override
    public void sendMessage(String message) {
        player.sendMessage(message);
    }

    @Override
    public Object getRoot() {
        return this.player;
    }

    @Override
    public void playSuccessSound() {

    }

    @Override
    public void playFailureSound() {

    }

    @Override
    public void closeInventory() {
        player.closeInventory();
    }

    @Override
    public void openInventory(IInventory inventory) {
        player.openInventory( (net.minestom.server.inventory.Inventory) inventory.getHandle());
    }

    @Override
    public IEntityCommands asEntityCommands() {
        return this;
    }
}
