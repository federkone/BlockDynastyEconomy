package BlockDynasty.BukkitImplementation.GUI.views.users.userPanels;

import BlockDynasty.BukkitImplementation.GUI.GUIFactory;
import BlockDynasty.BukkitImplementation.GUI.components.AccountsList;
import BlockDynasty.BukkitImplementation.GUI.components.IGUI;
import BlockDynasty.Economy.domain.entities.account.Player;
import org.bukkit.Bukkit;

import java.util.List;

public class PayGUI extends AccountsList {
    private final org.bukkit.entity.Player sender;

    public PayGUI( org.bukkit.entity.Player sender, IGUI parent) {
        super("Seleccionar Jugador", 5,parent,sender);
        this.sender = sender;

        List<Player> players = Bukkit.getOnlinePlayers().stream().map(p-> new Player(p.getUniqueId().toString(),p.getName())).toList();
        showPlayersPage(players,sender);
    }

    @Override
    public Player findPlayerByName(String playerName) {
        Player player;
        org.bukkit.entity.Player target = Bukkit.getPlayer(playerName);
        if (target != null) {
            return new Player(target.getUniqueId().toString(), target.getName());
        } else {
            return null;
        }
    }

    @Override
    public void openNextSection(Player target) {
        GUIFactory.currencyListToPayPanel(sender,target,this.getParent()).open();
    }
}