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

package com.blockdynasty.velocity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.inject.Inject;
import com.velocitypowered.api.event.connection.PluginMessageEvent;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.ServerConnection;
import com.velocitypowered.api.proxy.messages.MinecraftChannelIdentifier;

import org.slf4j.Logger;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Plugin(
    id = "blockdynastyeconomy",
    name = "BlockDynastyEconomy",
    version = "1.0"
)
public class Velocity {
    private static final String CHANNEL_NAME = "proxy:blockdynasty";
    @Inject private ProxyServer proxyServer;
    @Inject private Logger logger;

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        proxyServer.getChannelRegistrar().register(MinecraftChannelIdentifier.from(CHANNEL_NAME));
        logger.info("Velocity BlockDynastyEconomy Channel Registered....");
    }

    @Subscribe
    public void onShutdown(ProxyShutdownEvent event) {
        proxyServer.getChannelRegistrar().unregister(MinecraftChannelIdentifier.from(CHANNEL_NAME));
        logger.info("Velocity BlockDynastyEconomy Channel Unregistered....");
    }

    @Subscribe
    public void onPluginMessage(PluginMessageEvent event) {
        if (!event.getIdentifier().getId().equals(CHANNEL_NAME)) {
            return;
        }
        if(event.getSource() instanceof  ServerConnection backend){
            String serverSourceName = backend.getServerInfo().getName();
            event.setResult(PluginMessageEvent.ForwardResult.handled());
            try {
                DataInputStream dataInputStream = new DataInputStream(new ByteArrayInputStream(event.getData()));
                String jsonMessage = dataInputStream.readUTF();
                Gson gson = new Gson();
                Map<String, String> messageData = gson.fromJson(jsonMessage, new TypeToken<Map<String, String>>(){}.getType());

                String type = messageData.get("type");
                if (type.equals("event")|| type.equals("account")) {
                    String target = messageData.get("target");
                    UUID uuid = UUID.fromString(target);

                    // Get the player's current server
                    Optional<Player> optional = proxyServer.getPlayer(uuid);
                    optional.flatMap(Player::getCurrentServer).ifPresent(targetServer -> {
                        if (!targetServer.getServerInfo().getName().equals(serverSourceName)) {
                            targetServer.sendPluginMessage(MinecraftChannelIdentifier.from(CHANNEL_NAME), event.getData());
                        }
                    });
                }else if (type.equals("currency")) {
                    //broadcast to all servers
                    proxyServer.getAllServers().stream()
                            .filter(targetServer -> !targetServer.getServerInfo().getName().equals(serverSourceName))
                            .forEach(targetServer -> targetServer.sendPluginMessage(MinecraftChannelIdentifier.from(CHANNEL_NAME), event.getData()));
                }
            }catch (Exception e) {
                logger.warn("->> Error reading message: {}", e.getMessage());
            }
        }
    }
}
