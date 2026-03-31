package net.blockdynasty.economy.engine.configFromChannel;

import net.blockdynasty.economy.libs.abstractions.platform.PlatformAdapter;
import net.blockdynasty.economy.libs.abstractions.platform.scheduler.ContextualTask;
import net.blockdynasty.economy.engine.platform.listeners.IPlayerJoin;
import net.blockdynasty.economy.gui.commands.abstractions.IEntityCommands;

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
