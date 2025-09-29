package lib.gui.templates.users;

import BlockDynasty.Economy.domain.entities.account.Player;
import lib.gui.GUIFactory;
import lib.gui.abstractions.IGUI;
import lib.gui.abstractions.IEntityGUI;
import lib.gui.abstractions.ITextInput;
import lib.gui.abstractions.Materials;
import lib.gui.templates.abstractions.AccountsList;
import lib.util.colors.ChatColor;
import lib.util.colors.Colors;

import java.util.List;
import java.util.stream.Collectors;

public class PayGUI extends AccountsList {
    private final IEntityGUI sender;

    public PayGUI(IEntityGUI sender, IGUI parent , ITextInput textInput ) {
        super("Select Player", 5,sender,parent, textInput );
        this.sender = sender;

        List<Player> players = platformAdapter.getOnlinePlayers().stream()
                .map(p -> new Player(p.getUniqueId(), p.getName()))
                .sorted((a, b) -> a.getNickname().compareToIgnoreCase(b.getNickname()))
                .collect(Collectors.toList());

        showPlayers(players);
    }

    @Override
    public Player findPlayerByName(String playerName) {
        IEntityGUI player;
        IEntityGUI target = platformAdapter.getOnlinePlayers().stream()
                .filter(p -> p.getName().equalsIgnoreCase(playerName))
                .findFirst().orElse(null); //online players only
        if (target != null) {
            return new Player(target.getUniqueId(), target.getName());
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
        setItem(4, createItem(Materials.PAPER, ChatColor.stringValueOf(Colors.GREEN)+"Select Player to Pay", ChatColor.stringValueOf(Colors.WHITE)+"Click to select the player you want to pay","#Ordered by name"), null);
    }
}