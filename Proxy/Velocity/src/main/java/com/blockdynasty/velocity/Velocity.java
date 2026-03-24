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

import com.blockdynasty.proxy.common.MessageProcessor;
import com.blockdynasty.velocity.adapters.MessageAdapter;
import com.blockdynasty.velocity.adapters.VelocityAdapter;
import com.blockdynasty.velocity.adapters.VelocityLogger;
import com.google.inject.Inject;
import com.velocitypowered.api.command.BrigadierCommand;
import com.velocitypowered.api.command.CommandManager;
import com.velocitypowered.api.event.connection.PluginMessageEvent;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.messages.MinecraftChannelIdentifier;

import org.slf4j.Logger;

import java.nio.file.Path;

@Plugin(
    id = "blockdynastyeconomy",
    name = "BlockDynastyEconomy",
    version = "1.0",
    authors = {"federkone"}
)
public class Velocity {
    @Inject private ProxyServer proxyServer;
    @Inject private Logger logger;
    @Inject private @DataDirectory Path dataDirectory;
    private MessageProcessor messageProcessor;

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        this.messageProcessor = new VelocityAdapter(new VelocityLogger(logger),dataDirectory,proxyServer);

        CommandManager commandManager = proxyServer.getCommandManager();
        BrigadierCommand brigadierCommand = CommandReload.createBrigadierCommand(this.messageProcessor);
        commandManager.register(
                commandManager.metaBuilder("dynasty").build(),
                brigadierCommand
        );

        proxyServer.getChannelRegistrar().register(MinecraftChannelIdentifier.from(MessageProcessor.CHANNEL_NAME));
        logger.info("Velocity BlockDynastyEconomy Channel Registered....");
    }

    @Subscribe
    public void onShutdown(ProxyShutdownEvent event) {
        proxyServer.getChannelRegistrar().unregister(MinecraftChannelIdentifier.from(MessageProcessor.CHANNEL_NAME));
        logger.info("Velocity BlockDynastyEconomy Channel Unregistered....");
    }

    @Subscribe
    public void onPluginMessage(PluginMessageEvent event) {
        messageProcessor.processMessage(new MessageAdapter(event));
    }
}
