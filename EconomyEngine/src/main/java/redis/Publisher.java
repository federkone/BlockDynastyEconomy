package redis;

import BlockDynasty.Economy.domain.services.courier.Courier;
import Main.Console;
import com.google.gson.Gson;
import lib.abstractions.IPlayer;
import lib.abstractions.PlatformAdapter;
import redis.clients.jedis.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Publisher implements Courier {
    private final Gson gson = new Gson();
    private final String channelName;
    private final PlatformAdapter platformAdapter;
    private final HostAndPort hostAndPort;
    private final JedisClientConfig config;
    private final String INSTANCE_ID;

    public Publisher(RedisData redisData, PlatformAdapter platformAdapter) {
        this.channelName = redisData.getChannelName();
        this.platformAdapter = platformAdapter;

        this.hostAndPort = new HostAndPort(redisData.getHost(), redisData.getPort());
        this.config = DefaultJedisClientConfig.builder()
                .user(redisData.getUsername())
                .password(redisData.getPassword())
                .build();
        this.INSTANCE_ID = redisData.getInstanceID();
    }

    @Override
    public void sendUpdateMessage(String type, String target) {
        sendUpdateMessage(type, null, target);
    }

    @Override
    public void sendUpdateMessage(String type, String data, String target) {
        if (shouldSkipProcessing(type, target)) {
            return;
        }
        try (UnifiedJedis jedis = new UnifiedJedis(hostAndPort, config)) {
            Map<String, String> messageData = new HashMap<>();
            messageData.put("type", type);
            messageData.put("target", target);
            messageData.put("instanceId", INSTANCE_ID);
            if (data != null) {
                messageData.put("data", data);
            }
            String jsonMessage = gson.toJson(messageData);
            jedis.publish(channelName, jsonMessage);
        } catch (Exception e) {
            Console.logError("Redis publish error: " + e.getMessage());
        }
    }


    private boolean shouldSkipProcessing(String type, String target) {
        switch (type) {
            case "account":
            case "event":
                IPlayer player = platformAdapter.getPlayerByUUID(UUID.fromString(target));
                return player != null && player.isOnline();
            default:
                return false;
        }
    }
}