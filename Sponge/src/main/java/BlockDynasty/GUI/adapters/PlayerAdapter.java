package BlockDynasty.GUI.adapters;

import BlockDynasty.GUI.listener.ClickListener;
import BlockDynasty.GUI.listener.CloseListener;
import lib.components.IInventory;
import lib.components.IPlayer;
import net.kyori.adventure.text.Component;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.item.inventory.menu.InventoryMenu;
import org.spongepowered.api.item.inventory.type.ViewableInventory;

import java.util.UUID;

public class PlayerAdapter implements IPlayer {
    ServerPlayer player;

    public PlayerAdapter(ServerPlayer player) {
        this.player = player;
    }

    @Override
    public UUID getUniqueId() {
        return player.uniqueId();
    }

    @Override
    public String getName() {
        return player.name();
    }

    @Override
    public void sendMessage(String message) {
        player.sendMessage(Component.text(message));
    }

    @Override
    public void closeInventory() {
        player.closeInventory();
    }

    @Override
    public void openInventory(IInventory inventory) {
        if (inventory.getHandle() instanceof ViewableInventory) {
            ViewableInventory spongeInventory = (ViewableInventory) inventory.getHandle();
            InventoryMenu menu = spongeInventory.asMenu();

            menu.setReadOnly(true).setTitle(Component.text(inventory.getTitle()));
            menu.registerSlotClick(new ClickListener());
            menu.registerClose(new CloseListener());

            menu.open(player);
        } else {
            throw new IllegalArgumentException("Invalid inventory type provided");
        }
    }

    @Override
    public Object getHandle() {
        return this.player;
    }

}
