package BlockDynasty.adapters.listeners;

import BlockDynasty.adapters.platformAdapter.EntityPlayerAdapter;
import platform.listeners.IPlayerJoin;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.network.ServerSideConnectionEvent;

public class PlayerJoinListenerOnline {
    private final IPlayerJoin playerJoin;

    public PlayerJoinListenerOnline(IPlayerJoin playerJoin) {
        this.playerJoin = playerJoin;
    }

    @Listener
    public void onPlayerJoin(final ServerSideConnectionEvent.Join event) {
        playerJoin.loadOnlinePlayerAccount(EntityPlayerAdapter.of(event.player()));
    }
}
