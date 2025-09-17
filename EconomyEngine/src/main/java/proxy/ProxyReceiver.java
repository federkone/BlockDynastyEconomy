package proxy;

import BlockDynasty.Economy.aplication.events.EventManager;
import BlockDynasty.Economy.domain.services.IAccountService;
import Main.Console;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lib.abstractions.IPlayer;
import lib.abstractions.PlatformAdapter;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;

public abstract class ProxyReceiver {
    private static ProxyData proxyData;
    private static IAccountService accountService;
    private static EventManager eventManager;
    private static PlatformAdapter platformAdapter;

    public static void init(ProxyData proxyData, IAccountService accountService,EventManager eventManager, PlatformAdapter platformAdapter) {
        ProxyReceiver.platformAdapter = platformAdapter;
        ProxyReceiver.accountService = accountService;
        ProxyReceiver.eventManager = eventManager;
        ProxyReceiver.proxyData = proxyData;
    }

    public void onPluginMessageReceived(String channel, byte[] message) {
        if (!channel.equals(proxyData.getChannelName())) {
            return;
        }
        try (DataInputStream in = new DataInputStream(new ByteArrayInputStream(message))) {
            if (!isValidChannel(in)) return;

            String jsonMessage = in.readUTF();
            Gson gson = new Gson();
            Map<String, String> messageData = gson.fromJson(jsonMessage, new TypeToken<Map<String, String>>(){}.getType());

            String type = messageData.get("type");

            if (type.equals("event")) {
                String target = messageData.get("target");
                String data = messageData.get("data");
                UUID uuid = UUID.fromString(target);

                IPlayer player = platformAdapter.getPlayerByUUID(uuid);
                if (player == null || !player.isOnline()) {
                    return;
                }
                platformAdapter.executeAsync(() -> accountService.syncOnlineAccount(uuid));
                eventManager.processNetworkEvent(data);
            }
            if (type.equals("account")){
                String target = messageData.get("target");
                UUID uuid = UUID.fromString(target);

                IPlayer player = platformAdapter.getPlayerByUUID(uuid);
                if (player == null || !player.isOnline()) {
                    return;
                }
                platformAdapter.executeAsync(() -> accountService.syncOnlineAccount(uuid));
            }

            if (type.equals("currency")) {
                String target = messageData.get("target");
                UUID uuid = UUID.fromString(target);
                    //sync currency system
            }

        }catch (IOException exception){
            Console.logError(exception.getMessage());
        }
    }

    public boolean isValidChannel(DataInputStream in) throws IOException {
        return true;
    };
}
