/**
 * Copyright 2025 Federico Barrionuevo "@federkone"
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package proxy;

import BlockDynasty.Economy.aplication.events.EventManager;
import BlockDynasty.Economy.aplication.events.EventRegistry;
import BlockDynasty.Economy.domain.events.Event;
import BlockDynasty.Economy.domain.events.offersEvents.OfferEvent;
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
                String eventJson = messageData.get("data");
                offerService.processNetworkEvent(eventJson);

                if(shouldSkipProcessing(target)){
                    return;
                }
                platformAdapter.getScheduler().runAsync(ContextualTask.build(() -> accountService.syncOnlineAccount(uuid)));
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
