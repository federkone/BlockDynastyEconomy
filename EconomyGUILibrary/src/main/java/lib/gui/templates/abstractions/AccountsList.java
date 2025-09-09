package lib.gui.templates.abstractions;

import BlockDynasty.Economy.domain.entities.account.Player;
import lib.gui.abstractions.*;

import java.util.List;

public abstract class AccountsList extends PaginatedGUI<Player>{
    private final ITextInput textInput;
    public AccountsList(String title, int rows, IPlayer sender, IGUI parent, ITextInput textInput) {
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
        setItem(39, createItem(Materials.NAME_TAG, "§6Search Player",
                        "§7Click to search a player by name","§7CaseSensitive"),
                unused -> openAnvilSearch(unused));
    }

    protected void openAnvilSearch(IPlayer sender) {
        textInput.open(this, sender, "Search Player", "Name..", s -> {
            Player foundPlayer = findPlayerByName(s);

            if (foundPlayer != null) {
                //openNextSection(foundPlayer);
                this.open();
                showPlayers(List.of(foundPlayer));
            } else {
                sender.sendMessage("§cPlayer not found!");
                this.open();
            }
            return null;
        });
    }

    public abstract Player findPlayerByName(String playerName);
    public abstract void openNextSection(Player target);
}