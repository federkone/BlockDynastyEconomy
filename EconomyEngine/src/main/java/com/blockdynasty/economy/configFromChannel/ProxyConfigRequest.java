package com.blockdynasty.economy.configFromChannel;

import BlockDynasty.Economy.domain.services.courier.Message;
import abstractions.platform.PlatformAdapter;
import abstractions.platform.scheduler.ContextualTask;
import abstractions.platform.scheduler.IEntity;
import com.blockdynasty.economy.MessageChannel.proxy.ProxyData;
import com.blockdynasty.economy.platform.IPlatform;
import services.Console;

import java.io.IOException;
import java.util.UUID;

public class ProxyConfigRequest {

    public static void request(PlatformAdapter platformAdapter, IEntity entity, UUID target) {
        platformAdapter.getScheduler().runLater(5, ContextualTask.build(() ->{
            Message syncDataMessage = Message.builder()
                    .setType(Message.Type.SYNC_DATA)
                    .setTarget(target)
                    .build();
            try{
                platformAdapter.sendPluginMessage(ProxyData.getChannelName(), syncDataMessage.toJsonBytes());
            } catch (IOException e) {
                Console.logError("Proxy channel error :" + e.getMessage());
            }
        }, entity));
    }
}
