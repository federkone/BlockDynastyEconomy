package BlockDynasty.GUI.adapters;

import BlockDynasty.GUI.listener.ClickListener;
import lib.components.IInventory;
import lib.components.IPlayer;
import lib.templates.abstractions.AbstractGUI;
import net.kyori.adventure.text.Component;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.event.Cause;
import org.spongepowered.api.item.inventory.Container;
import org.spongepowered.api.item.inventory.Slot;
import org.spongepowered.api.item.inventory.menu.ClickType;
import org.spongepowered.api.item.inventory.menu.ClickTypes;
import org.spongepowered.api.item.inventory.menu.InventoryMenu;
import org.spongepowered.api.item.inventory.menu.handler.SlotClickHandler;
import org.spongepowered.api.item.inventory.type.ViewableInventory;

import java.util.UUID;

public class SpongePlayer implements IPlayer {
    ServerPlayer player;

    public SpongePlayer(ServerPlayer player) {
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
    public void openInventory(AbstractGUI gui) {
        IInventory inventory = gui.getInventory();
        if (inventory.getHandle() instanceof ViewableInventory) {
            ViewableInventory spongeInventory = (ViewableInventory) inventory.getHandle();
            InventoryMenu menu = spongeInventory.asMenu();

            menu.setReadOnly(true).setTitle( Component.text( gui.getTitle()));
            menu.registerSlotClick(new ClickListener(gui));

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
