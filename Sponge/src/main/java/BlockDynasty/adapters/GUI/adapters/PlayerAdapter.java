package BlockDynasty.adapters.GUI.adapters;

import BlockDynasty.adapters.GUI.listener.ClickListener;
import BlockDynasty.adapters.GUI.listener.CloseListener;
import lib.gui.abstractions.IInventory;
import lib.gui.abstractions.IPlayer;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
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
        Component component = Component.text()
                .append(LegacyComponentSerializer.legacyAmpersand().deserialize(message))
                .build();
        player.sendMessage(component);
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
    public void playSuccessSound() {
        Sound sound = Sound.sound(
                org.spongepowered.api.ResourceKey.resolve("minecraft:ui.button.click"),
                Sound.Source.PLAYER,
                0.3f,
                1.0f
        );
        player.playSound(sound);
    }

    @Override
    public void playFailureSound() {
        Sound sound = Sound.sound(
                org.spongepowered.api.ResourceKey.resolve("minecraft:block.note_block.pling"),
                Sound.Source.PLAYER,
                0.3f,
                0.5f
        );
        player.playSound(sound);
    }

    @Override
    public Object getHandle() {
        return this.player;
    }

}
