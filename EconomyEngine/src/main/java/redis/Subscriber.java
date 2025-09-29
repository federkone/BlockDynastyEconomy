package redis;


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