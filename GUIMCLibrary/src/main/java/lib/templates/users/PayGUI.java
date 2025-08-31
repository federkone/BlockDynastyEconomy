package lib.templates.users;

import BlockDynasty.Economy.domain.entities.account.Player;
import lib.GUIFactory;
import lib.components.*;
import lib.templates.abstractions.AccountsList;

import java.util.List;
import java.util.stream.Collectors;

public class PayGUI extends AccountsList {
    private final IPlayer sender;

    public PayGUI(IPlayer sender, IGUI parent , ITextInput textInput ) {
        super("Select Player", 5,sender,parent, textInput );
        this.sender = sender;

        List<Player> players = platformAdapter.getOnlinePlayers().stream()
                .map(p -> new Player(p.getUniqueId().toString(), p.getName()))
                .sorted((a, b) -> a.getNickname().compareToIgnoreCase(b.getNickname()))
                .collect(Collectors.toList());

        showPlayers(players);
    }

    @Override
    public Player findPlayerByName(String playerName) {
        IPlayer player;
        IPlayer target = platformAdapter.getOnlinePlayers().stream()
                .filter(p -> p.getName().equalsIgnoreCase(playerName))
                .findFirst().orElse(null); //online players only
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
    @Override
    public void addCustomButtons(){
        super.addCustomButtons(); // Call the parent method to add the default buttons accountList
        setItem(4, createItem(Materials.PAPER, "§aSelect Player to Pay", "§7Click to select the player you want to pay","#Ordered by name"), null);
    }
}