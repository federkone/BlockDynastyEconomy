package com.blockdynasty.economy.configFromChannel;

import abstractions.platform.PlatformAdapter;
import com.blockdynasty.economy.platform.listeners.IPlayerJoin;
import lib.commands.abstractions.IEntityCommands;

public class PlayerConfigJoinListener implements IPlayerJoin {
    private final PlatformAdapter platformAdapter;

    public PlayerConfigJoinListener(PlatformAdapter platformAdapter) {
        this.platformAdapter = platformAdapter;
    }

    @Override
    public void loadPlayerAccount(IEntityCommands player) {
        ProxyConfigRequest.request(this.platformAdapter,player,player.getUniqueId());
    }

    @Override
    public void offLoadPlayerAccount(IEntityCommands player) {

    }
}
