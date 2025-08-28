package BlockDynasty.GUI.components;

import BlockDynasty.Economy.domain.entities.account.Player;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;

import java.util.List;

public abstract class AccountsList extends PaginatedGUI<Player>{
    public AccountsList(String title, int rows, ServerPlayer sender, IGUI parent) {
        super(title, rows, sender, parent, 21); // 21 players per page
    }

    public void showPlayers(List<Player> players) {
        showItemsPage(players);
    }

    @Override
    protected ItemStack createItemFor(Player player) {
        return createItem(ItemTypes.PLAYER_HEAD.get(), player.getNickname());
    }

    @Override
    protected void handleLeftItemClick(Player player) {
        openNextSection(player);
    }


    @Override
    protected void addCustomButtons() {
        setItem(39, createItem(ItemTypes.NAME_TAG.get(), "§aSearch Player",
                        "§7Click to search for a player by name, CaseSensitive"),
                unused -> openAnvilSearch(unused));
    }

    protected void openAnvilSearch(ServerPlayer sender) {
       /* AnvilMenu.open(this, sender, "Search Player", "Name..", s -> {
            Player foundPlayer = findPlayerByName(s);

            if (foundPlayer != null) {
                //openNextSection(foundPlayer);
                this.open();
                showPlayers(List.of(foundPlayer));
            } else {
                sender.sendMessage("§cPlayer not found.");
                this.open();
            }
            return null;
        });*/
    }

    public abstract Player findPlayerByName(String playerName);
    public abstract void openNextSection(Player target);
}