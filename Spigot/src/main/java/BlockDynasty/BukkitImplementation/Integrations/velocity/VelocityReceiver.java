package BlockDynasty.BukkitImplementation.Integrations.velocity;

import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.jetbrains.annotations.NotNull;
import proxy.ProxyReceiver;


public class VelocityReceiver extends ProxyReceiver implements PluginMessageListener {

    @Override
    public void onPluginMessageReceived(@NotNull String channel, @NotNull Player player, @NotNull byte[] message) {
        super.onPluginMessageReceived(channel, message);
    }
}
