package net.blockdynasty.economy.engine.configFromChannel;

import net.blockdynasty.economy.core.domain.services.courier.Message;
import net.blockdynasty.economy.libs.abstractions.platform.PlatformAdapter;

import net.blockdynasty.economy.engine.MessageChannel.proxy.ProxyData;
import net.blockdynasty.economy.libs.services.Console;

import java.io.IOException;
import java.util.UUID;

public class ProxyConfigRequest {

    public static void request(PlatformAdapter platformAdapter, UUID target) {
        Message syncDataMessage = Message.builder()
                .setType(Message.Type.SYNC_DATA)
                .setTarget(target)
                .build();
        try{
            platformAdapter.sendPluginMessage(ProxyData.getChannelName(), syncDataMessage.toJsonBytes());
        } catch (IOException e) {
            Console.logError("Proxy channel error :" + e.getMessage());
        }
    }
}
