package com.blockdynasty.velocity;

import com.google.inject.Inject;
import com.velocitypowered.api.event.connection.PluginMessageEvent;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.ServerConnection;
import com.velocitypowered.api.proxy.messages.MinecraftChannelIdentifier;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import org.slf4j.Logger;

import java.nio.charset.StandardCharsets;
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
        if (event.getIdentifier().getId().equals("velocity:economy")) {
            String message = new String(event.getData(), StandardCharsets.UTF_8);
            String[] parts = message.split(",");

            String uuidPart = parts[2];
            UUID uuid = UUID.fromString(uuidPart);

            Optional<Player> optional = server.getPlayer(uuid);
            optional.ifPresent(player -> {
                player.getCurrentServer().ifPresent(targetServer -> {
                    targetServer.sendPluginMessage(MinecraftChannelIdentifier.from("velocity:economy"), event.getData());
                });
            });
            event.setResult(PluginMessageEvent.ForwardResult.handled());
        }
    }
}
