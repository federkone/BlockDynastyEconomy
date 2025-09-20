package proxy;

import BlockDynasty.Economy.aplication.events.EventManager;
import BlockDynasty.Economy.domain.services.IAccountService;
import BlockDynasty.Economy.domain.services.ICurrencyService;
import BlockDynasty.Economy.domain.services.IOfferService;
import Main.Console;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lib.abstractions.IPlayer;
import lib.abstractions.PlatformAdapter;
import lib.scheduler.ContextualTask;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;

public abstract class ProxyReceiver {
    private static IAccountService accountService;
    private static ICurrencyService currencyService;
    private static EventManager eventManager;
    private static PlatformAdapter platformAdapter;
    private static IOfferService offerService;

    public static void init(IAccountService accountService, ICurrencyService currencyService, EventManager eventManager, IOfferService offerService, PlatformAdapter platformAdapter) {
        ProxyReceiver.platformAdapter = platformAdapter;
        ProxyReceiver.accountService = accountService;
        ProxyReceiver.eventManager = eventManager;
        ProxyReceiver.offerService = offerService;
        ProxyReceiver.currencyService = currencyService;
    }

    public void onPluginMessageReceived(String channel, byte[] message) {
        if (!channel.equals(ProxyData.getChannelName())) {
            return;
        }
        try (DataInputStream in = new DataInputStream(new ByteArrayInputStream(message))) {
            String jsonMessage = in.readUTF();
            Gson gson = new Gson();
            Map<String, String> messageData = gson.fromJson(jsonMessage, new TypeToken<Map<String, String>>(){}.getType());

            String type = messageData.get("type");
            String target = messageData.get("target");
            UUID uuid = UUID.fromString(target);

            if (type.equals("event")) {
                if(shouldSkipProcessing(target)){
                    return;
                }
                platformAdapter.getScheduler().runAsync(ContextualTask.build(() -> accountService.syncOnlineAccount(uuid)));

                String eventJson = messageData.get("data");
                offerService.processNetworkEvent(eventJson);
                eventManager.processNetworkEvent(eventJson);
                return;
            }
            if (type.equals("account")){
                if(shouldSkipProcessing(target)){
                    return;
                }
                platformAdapter.getScheduler().runAsync(ContextualTask.build(() -> accountService.syncOnlineAccount(uuid)));
                return;
            }
            if (type.equals("currency")) {
                currencyService.syncCurrency(uuid);
            }

        }catch (IOException exception){
            Console.logError(exception.getMessage());
        }
    }

    private boolean shouldSkipProcessing(String target) {
        IPlayer player = platformAdapter.getPlayerByUUID(UUID.fromString(target));
        return player == null || !player.isOnline();
    }
}
