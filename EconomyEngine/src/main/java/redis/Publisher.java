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