package com.blockdynasty.velocity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.inject.Inject;
import com.velocitypowered.api.event.connection.PluginMessageEvent;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.ServerConnection;
import com.velocitypowered.api.proxy.messages.MinecraftChannelIdentifier;

import org.slf4j.Logger;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Plugin(
    id = "velocity",
    name = "Velocity",
    version = "1.0-SNAPSHOT"
)
public class Velocity {
    @Inject private ProxyServer server;

    @Inject private Logger logger;

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        // Registrar canal
        server.getChannelRegistrar().register(MinecraftChannelIdentifier.from("velocity:economy"));
        logger.info("Velocity Economy Channel Registered....");
    }

    @Subscribe
    public void onPluginMessage(PluginMessageEvent event) {
        logger.info("Received plugin message on channel: " + event.getIdentifier().getId());
        if (!event.getIdentifier().getId().equals("velocity:economy")) {
            return;
        }
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
                logger.info("Processing message for UUID: " + uuid.toString() + " of type: " + type);

                Optional<Player> optional = server.getPlayer(uuid);
                optional.ifPresent(player -> {
                    player.getCurrentServer().ifPresent(targetServer -> {
                        targetServer.sendPluginMessage(MinecraftChannelIdentifier.from("velocity:economy"), event.getData());
                    });
                });
            }else if (type.equals("currency")) {
                if (event.getSource() instanceof ServerConnection backend) {
                   String serverSourceName = backend.getServerInfo().getName();
                   server.getAllServers().stream()
                           .filter(s -> !s.getServerInfo().getName().equals(serverSourceName))
                           .forEach(s -> s.sendPluginMessage(MinecraftChannelIdentifier.from("velocity:economy"), event.getData()));
                }
            }
        }catch (Exception e) {
            logger.info("->> Error reading message: " + e.getMessage());
        }
    }
}
