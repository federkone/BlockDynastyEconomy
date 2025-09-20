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

public final class EconomyProxy extends Plugin implements Listener {
    private static final String CHANNEL_NAME = "proxy:blockdynastyeconomy";
    private static ProxyServer proxyServer;
    private static Logger logger;

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
                if (type.equals("event")|| type.equals("account")) {
                    String target = messageData.get("target");
                    UUID uuid = UUID.fromString(target);

                    // Get the player's current server except origin server and send message
                    Optional<ServerInfo> playerServer = Optional.ofNullable(proxyServer.getPlayer(uuid))
                            .filter(player -> !player.getServer().getInfo().getName().equals(serverSourceName))
                            .map(player -> player.getServer().getInfo());

                    playerServer.ifPresent(targetServer -> targetServer.sendData(CHANNEL_NAME, event.getData()));
                }else if (type.equals("currency")) {
                    //broadcast to all servers except origin server
                    proxyServer.getServers().values().stream()
                            .filter(targetServer -> !targetServer.getName().equals(serverSourceName))
                            .forEach(targetServer -> targetServer.sendData(CHANNEL_NAME, event.getData()));
                }
            } catch (IOException e) {
                logger.warning("Error reading message channel: " + e.getMessage());
            }
        }
    }
}
