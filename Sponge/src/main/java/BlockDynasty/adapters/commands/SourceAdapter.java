package BlockDynasty.adapters.commands;

import BlockDynasty.adapters.GUI.adapters.PlayerAdapter;
import lib.commands.abstractions.Source;
import lib.gui.abstractions.IPlayer;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.spongepowered.api.effect.sound.SoundTypes;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;

import java.util.UUID;

public class SourceAdapter implements Source {
    private final ServerPlayer player;
    public SourceAdapter(ServerPlayer player) {
        this.player = player;
    }

    @Override
    public String getName() {
        return player.name();
    }

    @Override
    public boolean isOnline() {
        return player.isOnline();
    }

    @Override
    public UUID getUniqueId() {
        return player.uniqueId();
    }

    @Override
    public void sendMessage(String message) {
        Component component = Component.text()
                .append(LegacyComponentSerializer.legacyAmpersand().deserialize(message))
                .build();
        player.sendMessage(component);
    }

    @Override
    public void soundNotification() {
        Sound sound = Sound.sound(
                org.spongepowered.api.ResourceKey.resolve("minecraft:block.note_block.pling"),
                Sound.Source.PLAYER,
                1.0f,
                1.5f
        );
        player.playSound(sound);
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
    public Object getHandle() {
        return player;
    }

    @Override
    public IPlayer asIPlayer() {
        return new PlayerAdapter(player);
    }
}
