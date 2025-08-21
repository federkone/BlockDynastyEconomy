package BlockDynasty.BukkitImplementation.GUI.components;

import BlockDynasty.BukkitImplementation.GUI.MaterialAdapter;
import BlockDynasty.Economy.domain.entities.account.Player;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public abstract class AccountsList extends PaginatedGUI<Player>{
    public AccountsList(String title, int rows, org.bukkit.entity.Player sender, IGUI parent) {
        super(title, rows, sender, parent, 21); // 21 players per page
    }

    public void showPlayers(List<Player> players) {
        showItemsPage(players);
    }

    @Override
    protected ItemStack createItemFor(Player player) {
        return MaterialAdapter.createPlayerHead(player.getNickname());
    }

    @Override
    protected void handleItemClick(Player player) {
        openNextSection(player);
    }


    @Override
    protected void addCustomButtons() {
        setItem(39, createItem(Material.NAME_TAG, "§aSearch Player",
                        "§7Click to search for a player by name, CaseSensitive"),
                unused -> openAnvilSearch(unused));
    }

    protected void openAnvilSearch(org.bukkit.entity.Player sender) {
        AnvilMenu.open(this, sender, "Search Player", "Name..", s -> {
            Player foundPlayer = findPlayerByName(s);

            if (foundPlayer != null) {
                openNextSection(foundPlayer);
            } else {
                sender.sendMessage("§cPlayer not found.");
                this.open();
            }
            return null;
        });
    }

    public abstract Player findPlayerByName(String playerName);
    public abstract void openNextSection(Player target);
}
