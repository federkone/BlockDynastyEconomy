package BlockDynasty.BukkitImplementation.Integrations.velocity;

import BlockDynasty.BukkitImplementation.utils.Console;
import BlockDynasty.Economy.domain.services.courier.Courier;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.UUID;

public class VelocitySender implements Courier {
    private final Plugin plugin;

    public VelocitySender(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void sendUpdateMessage(String type, String name) {
        Player player = Bukkit.getPlayer(UUID.fromString(name));
        if(player != null && player.isOnline()) {
            return;
        }
        try {
            // Crear mensaje del canal personalizado
            ByteArrayOutputStream msgBytes = new ByteArrayOutputStream();
            try (DataOutputStream msgOut = new DataOutputStream(msgBytes)) {
                msgOut.writeUTF(type + "," + name);
            }

            Bukkit.getServer().sendPluginMessage(plugin, "velocity:economy", msgBytes.toByteArray());

        } catch (IOException e) {
            Console.logError(e.getMessage());
        }
    }
}
