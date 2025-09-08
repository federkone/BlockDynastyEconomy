package BlockDynasty.BukkitImplementation.adapters.listeners;

import BlockDynasty.BukkitImplementation.adapters.commands.SourceAdapter;
import listeners.IPlayerJoin;
import org.bukkit.entity.Player;

public class PlayerJoinListenerOffline extends PlayerJoinListenerOnline {

    private IPlayerJoin playerJoin;
    public PlayerJoinListenerOffline(IPlayerJoin playerJoin) {
        super(playerJoin);
        this.playerJoin = playerJoin;
    }

    //si se comienza a trabajar en offline se van a buscar las cuentas por nombre y se va a preguntar si cambio el uuid para actualizar en sistema.
    @Override
    protected void loadPlayerAccount(Player player){
       this.playerJoin.loadOfflinePlayerAccount(new SourceAdapter(player));
    }
}
