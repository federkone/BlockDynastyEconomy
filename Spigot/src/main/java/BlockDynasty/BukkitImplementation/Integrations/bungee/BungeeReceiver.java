package BlockDynasty.BukkitImplementation.Integrations.bungee;

import java.io.DataInputStream;

import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;
import proxy.ProxyReceiver;

import java.io.IOException;


/**
 * BlockDynasty Bungee-Spigot Messaging Listener
 *
 * This listener is used to update currencies and balance for players
 * on different servers. This is important to sustain synced balances
 * and currencies on all of the servers.
 */

public class BungeeReceiver extends ProxyReceiver implements PluginMessageListener {
    private final BungeeData data = new BungeeData();

    @Override
    public boolean isValidChannel(DataInputStream in) throws IOException {
        String subchannel = in.readUTF();
        return subchannel.equals(data.getSubChannelName());
    }

    @Override
    public void onPluginMessageReceived(String channel, Player notInUse, byte[] message) {
      super.onPluginMessageReceived(channel, message);
    }
}

