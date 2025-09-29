package lib.gui.templates.abstractions;

import BlockDynasty.Economy.domain.entities.account.Player;
import lib.gui.abstractions.IEntityGUI;
import lib.gui.abstractions.*;
import lib.util.colors.ChatColor;
import lib.util.colors.Colors;

import java.util.List;

public abstract class AccountsList extends PaginatedGUI<Player>{
    private final ITextInput textInput;
    public AccountsList(String title, int rows, IEntityGUI sender, IGUI parent, ITextInput textInput) {
        super(title, rows, sender, parent, 21); // 21 players per page
        this.textInput = textInput;
    }

    public void showPlayers(List<Player> players) {
        showItemsPage(players);
    }

    @Override
    protected IItemStack createItemFor(Player player) {
        return createItem(Materials.PLAYER_HEAD, player.getNickname());
    }

    @Override
    protected void functionLeftItemClick(Player player) {
        openNextSection(player);
    }

    @Override
    protected void addCustomButtons() {
        setItem(39, createItem(Materials.NAME_TAG, ChatColor.stringValueOf(Colors.GOLD)+"Search Player",
                        ChatColor.stringValueOf(Colors.WHITE)+"Click to search a player by name",ChatColor.stringValueOf(Colors.WHITE)+"CaseSensitive"),
                unused -> openAnvilSearch(unused));
    }

    protected void openAnvilSearch(IEntityGUI sender) {
        textInput.open(this, sender, "Search Player", "Name..", s -> {
            Player foundPlayer = findPlayerByName(s);

            if (foundPlayer != null) {
                //openNextSection(foundPlayer);
                showPlayers(List.of(foundPlayer));
                this.open();
            } else {
                sender.sendMessage(ChatColor.stringValueOf(Colors.RED)+"Player not found!");
                this.open();
            }
            return null;
        });
    }

    public abstract Player findPlayerByName(String playerName);
    public abstract void openNextSection(Player target);
}