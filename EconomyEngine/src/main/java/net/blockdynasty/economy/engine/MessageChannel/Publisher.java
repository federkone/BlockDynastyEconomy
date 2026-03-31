package net.blockdynasty.economy.engine.MessageChannel;

import net.blockdynasty.economy.core.domain.services.courier.Courier;
import net.blockdynasty.economy.core.domain.services.courier.Message;
import net.blockdynasty.economy.libs.abstractions.platform.entity.IPlayer;
import net.blockdynasty.economy.engine.platform.IPlatform;

public abstract class Publisher implements Courier {
    private final IPlatform platformAdapter;

    public Publisher(IPlatform platformAdapter) {
        this.platformAdapter = platformAdapter;
    }

    @Override
    public void sendUpdateMessage(Message message){
        if (shouldSkipProcessing(message)){
            return;
        }
        sendMessage(message);
    };

    protected abstract void sendMessage(Message message);

    private boolean shouldSkipProcessing(Message message) {
        switch (message.getType()) {
            case ACCOUNT:
            case EVENT:
                IPlayer player = platformAdapter.getPlayerByUUID(message.getTarget());
                return player != null && player.isOnline();
            default:
                return false;
        }
    }
}