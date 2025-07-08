package me.BlockDynasty.Economy.Infrastructure.BukkitImplementation.Integrations.bungee;

import me.BlockDynasty.Economy.Infrastructure.BukkitImplementation.utils.UtilServer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import org.bukkit.plugin.Plugin;

public class CourierImpl implements Courier {
    private final String channelName = "BlockDynastyEconomy Data Channel";
    private  final  Plugin plugin;

    public CourierImpl(Plugin plugin) {
        this.plugin = plugin;
    }
    public void sendUpdateMessage(String type, String name) {
        if (Bukkit.getOnlinePlayers().isEmpty()) {
            UtilServer.consoleLog(channelName + " - Player is Null. Cancelled.");
            return;
        }
        try {
            // Crear mensaje del canal personalizado
            ByteArrayOutputStream msgBytes = new ByteArrayOutputStream();
            try (DataOutputStream msgOut = new DataOutputStream(msgBytes)) {
                msgOut.writeUTF(type + "," + name);
            }

            // Crear mensaje para BungeeCord
            ByteArrayOutputStream outBytes = new ByteArrayOutputStream();
            try (DataOutputStream out = new DataOutputStream(outBytes)) {
                out.writeUTF("Forward");
                out.writeUTF("ONLINE");
                out.writeUTF(channelName);
                out.write(msgBytes.toByteArray());
            }

            // Obtener el primer jugador conectado
            Player player = Bukkit.getOnlinePlayers().iterator().next();

            // Enviar mensaje
            player.sendPluginMessage(plugin, "BungeeCord", outBytes.toByteArray());

        } catch (IOException e) {
            plugin.getLogger().severe("Failed to send plugin message: " + e.getMessage());
            e.printStackTrace();
        }
    }

}
