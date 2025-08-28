package BlockDynasty.GUI.views.users;


import BlockDynasty.Economy.domain.entities.account.Player;
import BlockDynasty.GUI.GUIFactory;
import BlockDynasty.GUI.components.AccountsList;
import BlockDynasty.GUI.components.IGUI;
import BlockDynasty.SpongePlugin;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.item.ItemTypes;


import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PayGUI extends AccountsList {
    private final ServerPlayer sender;

    public PayGUI( ServerPlayer sender, IGUI parent) {
        super("Select Player", 5,sender,parent);
        this.sender = sender;

        List<Player> players = Sponge.server().onlinePlayers().stream()
            .map(p -> new Player(p.uniqueId().toString(), p.name()))
            .sorted((a, b) -> a.getNickname().compareToIgnoreCase(b.getNickname()))
            .collect(Collectors.toList());

        showPlayers(players);
    }

    @Override
    public Player findPlayerByName(String playerName) {
        Player player;
        ServerPlayer target = Sponge.server().onlinePlayers().stream()
                    .filter(p -> p.name().equalsIgnoreCase(playerName))
                    .findFirst().orElse(null); //online players only
        if (target != null) {
            return new Player(target.uniqueId().toString(), target.name());
        } else {
            return null;
        }
    }

    @Override
    public void openNextSection(Player target) {
        GUIFactory.currencyListToPayPanel(sender,target,this.getParent()).open();
    }
    @Override
    public void addCustomButtons(){
        super.addCustomButtons(); // Call the parent method to add the default buttons accountList
        setItem(4, createItem(ItemTypes.PAPER.get(), "§aSelect Player to Pay", "§7Click to select the player you want to pay","#Ordered by name"), null);
    }
}