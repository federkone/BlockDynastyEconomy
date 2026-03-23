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

package com.blockdynasty.economyProxy;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.Server;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.event.EventHandler;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Logger;

public final class BlockDynastyEconomy extends Plugin implements Listener {
    private static final String CHANNEL_NAME = "proxy:blockdynasty";
    private static ProxyServer proxyServer;
    private static Logger logger;
    //file proxyServer.getPluginsFolder

    @Override
    public void onEnable() {
        proxyServer = getProxy();
        logger = getLogger();
        proxyServer.registerChannel(CHANNEL_NAME);
        proxyServer.getPluginManager().registerListener(this, this);
        logger.info("Plugin BlockDynastyEconomy enabled!");
    }

    @Override
    public void onDisable() {
        proxyServer.unregisterChannel(CHANNEL_NAME);
        logger.info("Plugin BlockDynastyEconomy disabled!");
    }

    @EventHandler
    public void onPluginMessage(PluginMessageEvent event) {
        if (!event.getTag().equalsIgnoreCase(CHANNEL_NAME)) {
            return;
        }
        if(event.getSender() instanceof Server){
            String serverSourceName = ((Server) event.getSender()).getInfo().getName();
            event.setCancelled(true);
            try {
                DataInputStream dataInputStream = new DataInputStream(new ByteArrayInputStream(event.getData()));
                String jsonMessage = dataInputStream.readUTF();
                Gson gson = new Gson();
                Map<String, String> messageData = gson.fromJson(jsonMessage, new TypeToken<Map<String, String>>(){}.getType());

                String type = messageData.get("type");
                switch (type) {
                    case "event":
                    case "account":
                        processEvent(messageData, serverSourceName, event);
                        break;
                    case "currency":
                        processCurrency(serverSourceName, event);
                        break;
                    case "syncData":
                        processSyncData();
                        break;
                    default:
                        logger.warning("Unknown message type received: " + type);
                }
            } catch (IOException e) {
                logger.warning("Error reading message channel: " + e.getMessage());
            }
        }
    }
    private void processSyncData(){
        //todo implement.
    }
    private void processEvent(Map<String, String> messageData, String serverSourceName, PluginMessageEvent event){
        String target = messageData.get("target");
        UUID uuid = UUID.fromString(target);

        Optional<ServerInfo> playerServer = Optional.ofNullable(proxyServer.getPlayer(uuid))
                .filter(player -> !player.getServer().getInfo().getName().equals(serverSourceName))
                .map(player -> player.getServer().getInfo());

        playerServer.ifPresent(targetServer -> targetServer.sendData(CHANNEL_NAME, event.getData()));
    }

    private void processCurrency(String serverSourceName, PluginMessageEvent event){
        proxyServer.getServers().values().stream()
                .filter(targetServer -> !targetServer.getName().equals(serverSourceName))
                .forEach(targetServer -> targetServer.sendData(CHANNEL_NAME, event.getData()));
    }
}
