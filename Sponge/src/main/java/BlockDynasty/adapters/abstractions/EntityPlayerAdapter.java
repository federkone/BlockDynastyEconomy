package BlockDynasty.adapters.abstractions;

import BlockDynasty.adapters.GUI.listener.ClickListener;
import BlockDynasty.adapters.GUI.listener.CloseListener;
import lib.commands.abstractions.IEntityCommands;
import lib.gui.abstractions.IEntityGUI;
import lib.abstractions.IPlayer;
import lib.gui.abstractions.IInventory;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.item.inventory.menu.InventoryMenu;
import org.spongepowered.api.item.inventory.type.ViewableInventory;

import java.util.UUID;

public class EntityPlayerAdapter implements IPlayer {
    ServerPlayer player;

    private EntityPlayerAdapter(ServerPlayer player) {
        this.player = player;
    }

    public static EntityPlayerAdapter of(ServerPlayer player) {
        return new EntityPlayerAdapter(player);
    }

    @Override
    public boolean isOnline() {
        return player.isOnline();
    }

    @Override
    public boolean hasPermission(String permission) {
        return player.hasPermission(permission);
    }

    @Override
    public void kickPlayer(String message) {
        player.kick(Component.text(message));
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
        //Component component = Component.text()
        //        .append(LegacyComponentSerializer.legacyAmpersand().deserialize(message))
        //        .build();

        Component textonuevo = MiniMessage.miniMessage().deserialize(message);
        player.sendMessage(textonuevo);
    }

    @Override
    public void playNotificationSound() {
        Sound sound = Sound.sound(
                org.spongepowered.api.ResourceKey.resolve("minecraft:block.note_block.pling"),
                Sound.Source.PLAYER,
                1.0f,
                1.5f
        );
        player.playSound(sound);
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
    public IEntityCommands asEntityCommands() {
        return this;
    }
    @Override
    public IEntityGUI asEntityGUI() {
        return this;
    }

    @Override
    public Object getRoot() {
        return player;
    }
}
