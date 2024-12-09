package me.BlockDynasty.Economy.aplication.bungee;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;

import me.BlockDynasty.Economy.BlockDynastyEconomy;
import me.BlockDynasty.Economy.aplication.useCase.account.GetAccountsUseCase;
import me.BlockDynasty.Economy.domain.account.Account;
import me.BlockDynasty.Economy.domain.currency.Currency;
import me.BlockDynasty.Economy.domain.repository.Criteria.Criteria;
import me.BlockDynasty.Economy.utils.SchedulerUtils;
import me.BlockDynasty.Economy.utils.UtilServer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
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
    private final String channelName = "GemsEconomy Data Channel";
    private  final GetAccountsUseCase getAccountUseCase;

    public UpdateForwarder(BlockDynastyEconomy plugin ,GetAccountsUseCase getAccountUseCase) {
        this.plugin = plugin;
        this.getAccountUseCase = getAccountUseCase;
    }

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
                if (plugin.isDebug()) {
                    UtilServer.consoleLog(channelName + " - Received: " + type + " = " + name);
                }

                UUID uuid = UUID.fromString(name);
                switch (type) {
                    case "account":
                        Player player = Bukkit.getPlayer(uuid);
                        if (player == null || !player.isOnline()) {
                            if (plugin.isDebug()) {
                                UtilServer.consoleLog(channelName + " - User is not online. Skipping update.");
                            }
                            return;
                        }

                        SchedulerUtils.runAsync(() -> {
                            getAccountUseCase.updateAccountCache(uuid);
                            player.sendMessage("§a¡Cuenta bancaria actualizada, se ha realizado un depostio o extraccion en tu cuenta!");
                        });

                        if (plugin.isDebug()) {
                            UtilServer.consoleLog(channelName + " - Account " + name + " updated.");
                        }
                        break;
                    case "currency":
                        Currency currency = plugin.getCurrencyManager().getCurrency(uuid);
                        if (currency != null) {
                            Criteria criteria = Criteria.create().filter("uuid", uuid.toString()).limit(1);
                            currency = plugin.getDataStore().loadCurrencies(criteria).get(0);
                            if (plugin.isDebug()) {
                                UtilServer.consoleLog(channelName + " - Currency " + name + " updated.");
                            }
                        }
                        break;
                    default:
                        if (plugin.isDebug()) {
                            UtilServer.consoleLog(channelName + " - Unknown type: " + type);
                        }
                        break;
                }
            }
        }catch (IOException exception){
            exception.printStackTrace();
        }
    }


    public void sendUpdateMessage(String type, String name) {
        if (Bukkit.getOnlinePlayers().isEmpty()) {
            if (plugin.isDebug()) {
                UtilServer.consoleLog(channelName + " - Player is Null. Cancelled.");
            }
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
