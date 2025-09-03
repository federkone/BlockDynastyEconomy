package BlockDynasty.commands;

import lib.commands.abstractions.Source;
import net.kyori.adventure.text.Component;
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
        player.sendMessage(Component.text(message));
    }

    @Override
    public boolean hasPermission(String permission) {
        return player.hasPermission(permission);
    }

    @Override
    public void kickPlayer(String message) {
        player.kick(Component.text(message));
    }
}
