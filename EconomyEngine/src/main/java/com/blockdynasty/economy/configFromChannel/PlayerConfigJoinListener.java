package com.blockdynasty.economy.configFromChannel;

import abstractions.platform.PlatformAdapter;
import abstractions.platform.scheduler.ContextualTask;
import com.blockdynasty.economy.platform.listeners.IPlayerJoin;
import lib.commands.abstractions.IEntityCommands;

public class PlayerConfigJoinListener implements IPlayerJoin {
    private final PlatformAdapter platformAdapter;

    public PlayerConfigJoinListener(PlatformAdapter platformAdapter) {
        this.platformAdapter = platformAdapter;
    }

    @Override
    public void loadPlayerAccount(IEntityCommands player) {
        this.platformAdapter.getScheduler().runLater(5, ContextualTask.build(() ->{
            ProxyConfigRequest.request(this.platformAdapter,player.getUniqueId());
        },player));
    }

    @Override
    public void offLoadPlayerAccount(IEntityCommands player) {

    }
}
