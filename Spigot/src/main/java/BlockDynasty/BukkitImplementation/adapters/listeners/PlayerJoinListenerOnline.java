package BlockDynasty.BukkitImplementation.adapters.listeners;

import BlockDynasty.BukkitImplementation.adapters.commands.SourceAdapter;

import BlockDynasty.BukkitImplementation.scheduler.ContextualTask;
import BlockDynasty.BukkitImplementation.scheduler.Scheduler;
import listeners.IPlayerJoin;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerJoinListenerOnline implements Listener {
    private IPlayerJoin playerJoin;

    public PlayerJoinListenerOnline(IPlayerJoin playerJoin) {
        this.playerJoin = playerJoin;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onLogin(PlayerLoginEvent event) {
        Player player = event.getPlayer();
        Scheduler.run(ContextualTask.build(() -> loadPlayerAccount(player)));
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        Scheduler.run(ContextualTask.build(() -> loadPlayerAccount(player)));
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        removePlayerCache(event.getPlayer());
    }

    @EventHandler
    public void onKick(PlayerKickEvent event) {
        removePlayerCache(event.getPlayer());
    }

    private void removePlayerCache(Player player) {
        playerJoin.offLoadPlayerAccount( new SourceAdapter(player));
    }

    //si se comienza a trabajar en online se van a buscar las cuentas por uuid y se va a preguntar si cambio el nombre para actualizar en sistema.
    protected void loadPlayerAccount(Player player) {
        playerJoin.loadOnlinePlayerAccount( new SourceAdapter(player));
    }
}


