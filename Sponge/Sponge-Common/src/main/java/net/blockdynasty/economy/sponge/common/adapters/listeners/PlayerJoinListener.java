package net.blockdynasty.economy.sponge.common.adapters.listeners;

import net.blockdynasty.economy.sponge.common.adapters.platformAdapter.EntityPlayerAdapter;
import net.blockdynasty.economy.engine.platform.listeners.IPlayerJoin;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.network.ServerSideConnectionEvent;

public class PlayerJoinListener {
    private final IPlayerJoin playerJoin;

    public PlayerJoinListener(IPlayerJoin playerJoin) {
        this.playerJoin = playerJoin;
    }

    @Listener
    public void onPlayerJoin(final ServerSideConnectionEvent.Join event) {
        playerJoin.loadPlayerAccount(EntityPlayerAdapter.of(event.player()));
    }
}
