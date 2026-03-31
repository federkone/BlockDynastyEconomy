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

package net.blockdynasty.economy.proxy.bungeecord;

import net.blockdynasty.economy.proxy.bungeecord.adapters.BungeeCordAdapter;
import net.blockdynasty.economy.proxy.bungeecord.adapters.BungeeLoggerAdapter;
import net.blockdynasty.economy.proxy.bungeecord.adapters.MessageAdapter;
import net.blockdynasty.economy.proxy.common.MessageProcessor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.event.EventHandler;

import java.util.logging.Logger;

public final class BlockDynastyEconomy extends Plugin implements Listener {
    private static ProxyServer proxyServer;
    private static Logger logger;
    private MessageProcessor messageProcessor;

    @Override
    public void onEnable() {
        proxyServer = getProxy();
        logger = getLogger();
        this.messageProcessor = new BungeeCordAdapter(new BungeeLoggerAdapter(logger),proxyServer.getPluginsFolder().toPath().resolve("BlockDynastyEconomy"), proxyServer);
        proxyServer.registerChannel(MessageProcessor.CHANNEL_NAME);
        proxyServer.getPluginManager().registerListener(this, this);
        proxyServer.getPluginManager().registerCommand(this, new CommandReload(messageProcessor, logger));
        logger.info("Plugin BlockDynastyEconomy enabled!");
    }

    @Override
    public void onDisable() {
        proxyServer.unregisterChannel(MessageProcessor.CHANNEL_NAME);
        logger.info("Plugin BlockDynastyEconomy disabled!");
    }

    @EventHandler
    public void onPluginMessage(PluginMessageEvent event) {
        this.messageProcessor.processMessage(new MessageAdapter(event));
    }
}
