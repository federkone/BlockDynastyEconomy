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

package redis;

import BlockDynasty.Economy.aplication.events.EventManager;
import BlockDynasty.Economy.domain.services.IAccountService;
import BlockDynasty.Economy.domain.services.ICurrencyService;
import BlockDynasty.Economy.domain.services.IOfferService;
import utils.Console;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lib.abstractions.IPlayer;
import lib.abstractions.PlatformAdapter;
import lib.scheduler.ContextualTask;
import redis.clients.jedis.*;

import java.util.Map;
import java.util.UUID;

public class Subscriber {
    private final Gson gson = new Gson();
    private final PlatformAdapter platformAdapter;
    private final IOfferService offerService;
    private final EventManager eventManager;
    private final ICurrencyService currencyService;
    private final IAccountService accountService;
    private final HostAndPort hostAndPort;
    private final JedisClientConfig config;
    private Thread listenerThread;
    private boolean running = false;
    private final String channelName;
    private final String INSTANCE_ID;


    public Subscriber(RedisData redisData,PlatformAdapter platformAdapter, IOfferService offerService, ICurrencyService currencyService, IAccountService accountService, EventManager eventManager) {
        this.platformAdapter = platformAdapter;
        this.offerService = offerService;
        this.eventManager = eventManager;
        this.currencyService = currencyService;
        this.accountService = accountService;

        this.hostAndPort = new HostAndPort(redisData.getHost(), redisData.getPort());
        this.config = DefaultJedisClientConfig.builder()
                .user(redisData.getUsername())
                .password(redisData.getPassword())
                .build();

        this.channelName = redisData.getChannelName();
        this.INSTANCE_ID = redisData.getInstanceID();
    }

    public void startListening() {
        if (running) return;
        running = true;

        listenerThread = new Thread(() -> {
            try {
                UnifiedJedis jedis = new UnifiedJedis(hostAndPort, config);
                jedis.subscribe(new JedisPubSub() {
                    @Override
                    public void onMessage(String channel, String message) {
                        if (!channel.equals(channelName)) {
                            return;
                        }

                        try {
                            Map<String, String> messageData = gson.fromJson(message, new TypeToken<Map<String, String>>(){}.getType());

                            if(messageData.get("instanceId").equals(INSTANCE_ID)){
                                return;
                            }

                            String type = messageData.get("type");
                            String target = messageData.get("target");
                            UUID uuid = UUID.fromString(target);

                            if (type.equals("event")) {
                                String eventJson = messageData.get("data");
                                offerService.processNetworkEvent(eventJson);

                                if (shouldSkipProcessing(target)) {
                                    return;
                                }
                                platformAdapter.getScheduler().runAsync(ContextualTask.build(() -> accountService.syncOnlineAccount(uuid)));
                                eventManager.processNetworkEvent(eventJson);
                                return;
                            }
                            if (type.equals("account")) {
                                if (shouldSkipProcessing(target)) {
                                    return;
                                }
                                platformAdapter.getScheduler().runAsync(ContextualTask.build(() -> accountService.syncOnlineAccount(uuid)));
                                return;
                            }
                            if (type.equals("currency")) {
                                currencyService.syncCurrency(uuid);
                            }
                        } catch (Exception e) {
                            Console.logError("Redis message processing error: " + e.getMessage());
                        }
                    }
                }, channelName);
            } catch (Exception e) {
                Console.logError("Redis subscription error: " + e.getMessage());
                running = false;
            }
        });

        listenerThread.setDaemon(true);
        listenerThread.start();
    }

    public void stopListening() {
        running = false;
        if (listenerThread != null) {
            listenerThread.interrupt();
        }
    }

    private boolean shouldSkipProcessing(String target) {
        IPlayer player = platformAdapter.getPlayerByUUID(UUID.fromString(target));
        return player == null || !player.isOnline();
    }
}