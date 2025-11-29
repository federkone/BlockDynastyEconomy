package minestom.events;

import adapters.PlayerAdapter;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.player.PlayerDisconnectEvent;
import platform.listeners.IPlayerJoin;

public class playerExitEvent {

    public static void register(IPlayerJoin playerJoin) {
        // No implementation yet
        GlobalEventHandler handler = MinecraftServer.getGlobalEventHandler();

        handler.addListener(PlayerDisconnectEvent.class, event -> {
            final Player player = event.getPlayer();
            playerJoin.offLoadPlayerAccount(new PlayerAdapter(player));
        });
    }
}
