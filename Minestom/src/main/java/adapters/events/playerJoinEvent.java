package adapters.events;

import adapters.PlayerAdapter;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.player.PlayerLoadedEvent;
import platform.listeners.IPlayerJoin;

public class playerJoinEvent {

    public static void register(IPlayerJoin playerJoin){
        GlobalEventHandler handler = MinecraftServer.getGlobalEventHandler();

        handler.addListener(PlayerLoadedEvent.class, event -> {
            final Player player = event.getPlayer();
            playerJoin.loadOnlinePlayerAccount(new PlayerAdapter(player));
        });
    }
}
