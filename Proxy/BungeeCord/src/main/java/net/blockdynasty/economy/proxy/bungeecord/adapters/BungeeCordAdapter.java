/**
 * Copyright 2026 Federico Barrionuevo "@federkone"
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

package net.blockdynasty.economy.proxy.bungeecord.adapters;

import net.blockdynasty.economy.proxy.common.Logger;
import net.blockdynasty.economy.proxy.common.Message;
import net.blockdynasty.economy.proxy.common.MessageProcessor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class BungeeCordAdapter extends MessageProcessor {
    private ProxyServer proxyServer;
    private Logger logger;

    public BungeeCordAdapter(Logger logger, Path dataDirectory, ProxyServer proxyServer) {
        super(logger, dataDirectory);
        this.logger = logger;
        this.proxyServer = proxyServer;
    }

    @Override
    public void forwardMessageEvent(UUID playerId, Message message) {
        Optional<ServerInfo> playerServer = Optional.ofNullable(proxyServer.getPlayer(playerId))
                .filter(player -> !player.getServer().getInfo().getName().equals(message.getServerSourceName()))
                .map(player -> player.getServer().getInfo());

        playerServer.ifPresent(targetServer -> targetServer.sendData(CHANNEL_NAME, message.getData()));
    }

    @Override
    public void forwardMessageCurrency(Message message) {
        proxyServer.getServers().values().stream()
                .filter(targetServer -> !targetServer.getName().equals(message.getServerSourceName()))
                .forEach(targetServer -> targetServer.sendData(CHANNEL_NAME, message.getData()));
    }

    @Override
    public void forwardMessageSyncData(UUID playerId, boolean needCheckWhitelist, List<String> allowedServers, Message message) {
        Optional<ServerInfo> playerServer = Optional.ofNullable(proxyServer.getPlayer(playerId))
                .map(player -> player.getServer().getInfo());
        playerServer.ifPresent(targetServer -> {
            if (needCheckWhitelist){
                String serverName = targetServer.getName();
                if(allowedServers.contains(serverName)){
                    logger.log("->> Sending syncData response to server: "+ targetServer.getName());
                    targetServer.sendData(CHANNEL_NAME, message.getData());
                }else{
                    logger.logWarning("->> Server is not in the whitelist, syncData response not sent."+ serverName);
                }
            }else{
                logger.log("->> Sending syncData response to server: "+ targetServer.getName());
                targetServer.sendData(CHANNEL_NAME, message.getData());
            }
        });
    }
}
