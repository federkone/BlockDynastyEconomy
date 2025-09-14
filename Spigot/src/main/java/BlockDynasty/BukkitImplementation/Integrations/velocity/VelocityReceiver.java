package BlockDynasty.BukkitImplementation.Integrations.velocity;

import BlockDynasty.BukkitImplementation.BlockDynastyEconomy;
import BlockDynasty.BukkitImplementation.scheduler.ContextualTask;
import BlockDynasty.BukkitImplementation.scheduler.Scheduler;
import BlockDynasty.BukkitImplementation.utils.Console;
import BlockDynasty.Economy.domain.services.IAccountService;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.UUID;

public class VelocityReceiver implements PluginMessageListener {
    private final BlockDynastyEconomy plugin;
    private final IAccountService accountService;

    public VelocityReceiver(BlockDynastyEconomy plugin , IAccountService accountService) {
        this.plugin = plugin;
        this.accountService = accountService;
    }

    @Override
    public void onPluginMessageReceived(@NotNull String channel, @NotNull Player notInUse, @NotNull byte[] message) {
        Console.log("Mensaje recibido en canal: " + channel);
        if (!channel.equals("velocity:economy")) {
            return;
        }

        try (DataInputStream in = new DataInputStream(new ByteArrayInputStream(message))) {
                String[] info = in.readUTF().split(",");

                String type = info[0];
                String name = info[1];

                UUID uuid = UUID.fromString(name);
                switch (type) {
                    case "account":
                        Player player = Bukkit.getPlayer(uuid);
                        if (player == null || !player.isOnline()) {
                            return;
                        }

                        Scheduler.runAsync(ContextualTask.build(() -> {
                            accountService.syncOnlineAccount(uuid);
                            player.sendMessage("§a¡Cuenta bancaria actualizada, se ha realizado un depostio o extraccion en tu cuenta!");
                        }));

                        break;
                    //si es currency, traerlas de db y actualizar el cache/service
                    case "currency":
                        /*Currency currency = plugin.getCurrencyManager().getCurrency(uuid);
                        if (currency != null) {
                             currency = plugin.getDataStore().loadCurrencyByUuid(uuid.toString()).getValue();
                            UtilServer.consoleLog(channelName + " - Currency " + name + " updated.");
                        }*/
                        break;
                    default:
                }

        }catch (IOException exception){
            Console.logError(exception.getMessage());
        }
    }
}
