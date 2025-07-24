package BlockDynasty.BukkitImplementation.Integrations.bungee;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;

import BlockDynasty.BukkitImplementation.BlockDynastyEconomy;
import BlockDynasty.BukkitImplementation.scheduler.ContextualTask;
import BlockDynasty.BukkitImplementation.scheduler.Scheduler;
import BlockDynasty.BukkitImplementation.scheduler.SchedulerFactory;
import BlockDynasty.Economy.aplication.useCase.account.GetAccountsUseCase;
import BlockDynasty.Economy.domain.entities.currency.Currency;
import BlockDynasty.BukkitImplementation.utils.UtilServer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.io.IOException;
import java.util.UUID;

public class UpdateForwarder implements PluginMessageListener {

    /**
     * BlockDynasty Bungee-Spigot Messaging Listener
     *
     * This listener is used to update currencies and balance for players
     * on different servers. This is important to sustain synced balances
     * and currencies on all of the servers.
     */

    private BlockDynastyEconomy plugin;
    private final String channelName = "BlockDynastyEconomy Data Channel";
    private  GetAccountsUseCase getAccountUseCase;

    public UpdateForwarder(BlockDynastyEconomy plugin ) {
        this.plugin = plugin;
        this.getAccountUseCase = plugin.getUsesCase().getAccountsUseCase();
    }

//testear en todos los servidores. esto funciona a modo de broadcast. lo cual puede generar trafico innecesario
    @Override
    public void onPluginMessageReceived(String channel, Player notInUse, byte[] message) {
        if (!channel.equals("BungeeCord")) {
            return;
        }

        try (DataInputStream in = new DataInputStream(new ByteArrayInputStream(message))) {
            String subchannel = in.readUTF();
            if (subchannel.equals(channelName)) {
                String[] info = in.readUTF().split(",");
                String type = info[0];
                String name = info[1];
                UtilServer.consoleLog(channelName + " - Received: " + type + " = " + name);

                UUID uuid = UUID.fromString(name);
                switch (type) {
                    case "account":
                        Player player = Bukkit.getPlayer(uuid);
                        if (player == null || !player.isOnline()) {
                            UtilServer.consoleLog(channelName + " - User is not online. Skipping update.");
                            return;
                        }

                        Scheduler.runAsync(ContextualTask.build(() -> {
                            getAccountUseCase.syncCache(uuid);
                            player.sendMessage("§a¡Cuenta bancaria actualizada, se ha realizado un depostio o extraccion en tu cuenta!");
                        }));

                        UtilServer.consoleLog(channelName + " - Account " + name + " updated.");
                        break;
                        //si es currency, traerlas de db y actualizar el cache/service
                    case "currency":
                        Currency currency = plugin.getCurrencyManager().getCurrency(uuid);
                        if (currency != null) {
                             currency = plugin.getDataStore().loadCurrencyByUuid(uuid.toString()).getValue();
                            UtilServer.consoleLog(channelName + " - Currency " + name + " updated.");
                        }
                        break;
                    default:
                        UtilServer.consoleLog(channelName + " - Unknown type: " + type);
                        break;
                }
            }
        }catch (IOException exception){
            exception.printStackTrace();
        }
    }
}
