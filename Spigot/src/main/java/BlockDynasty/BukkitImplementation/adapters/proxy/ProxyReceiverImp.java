package BlockDynasty.BukkitImplementation.adapters.proxy;

import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;
import proxy.ProxyReceiver;


/**
 * BlockDynasty Bungee-Spigot Messaging Listener
 *
 * This listener is used to update currencies and balance for players
 * on different servers. This is important to sustain synced balances
 * and currencies on all of the servers.
 */

public class ProxyReceiverImp extends ProxyReceiver implements PluginMessageListener {

    @Override
    public void onPluginMessageReceived(String channel, Player notInUse, byte[] message) {
      super.onPluginMessageReceived(channel, message);
    }
}

