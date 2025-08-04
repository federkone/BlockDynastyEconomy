package BlockDynasty.BukkitImplementation.GUI.components;

import BlockDynasty.BukkitImplementation.GUI.MaterialAdapter;
import BlockDynasty.Economy.domain.entities.account.Player;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public abstract class AccountsList extends AbstractGUI {
    private int currentPage = 0;
    private final int PLAYERS_PER_PAGE = 21;

    public AccountsList(String title, int rows,IGUI parent,org.bukkit.entity.Player sender) {
        super(title, rows,sender,parent);
    }

    protected void showPlayersPage(List<Player> players, org.bukkit.entity.Player sender) {
        // Calculate pagination
        int startIndex = currentPage * PLAYERS_PER_PAGE;
        int endIndex = Math.min(startIndex + PLAYERS_PER_PAGE, players.size());

        // Clear GUI
        clearGui();

        // Add players to GUI
        int slot = 10;
        for (int i = startIndex; i < endIndex; i++) {
            Player target = players.get(i);
            ItemStack playerHead = MaterialAdapter.createPlayerHead(target.getNickname());

            setItem(slot, playerHead, unused -> {openNextSection(target);});

            // Adjust slot position
            slot++;
            if (slot % 9 == 8) slot += 2;
        }

        // Navigation buttons
        if (currentPage > 0) {
            setItem(38, createItem(Material.ARROW, "§aPágina Anterior",
                    "§7Click para ver jugadores anteriores"), unused -> {
                currentPage--;
                showPlayersPage(players,sender);
            });
        }

        if (endIndex < players.size()) {
            setItem(42, createItem(Material.ARROW, "§aPágina Siguiente", "§7Click para ver más jugadores"), unused -> {
                currentPage++;
                showPlayersPage(players,sender);
            });
        }

        setItem(39, createItem(Material.NAME_TAG, "§aBuscar Jugador", "§7Click para buscar un jugador por nombre"), unused -> {openAnvilSearch(sender);});

        // Cancel button
        setItem(41, createItem(Material.BARRIER, "§7Atrás", "§7Click para Atrás"),unused -> {this.openParent();});
    }

    protected void openAnvilSearch(org.bukkit.entity.Player sender) {
        AnvilMenu.open(sender,"Buscar Jugador","Name..", s ->{
            Player foundPlayer = findPlayerByName(s);

            if (foundPlayer != null) {
                openNextSection(foundPlayer);
            } else {
                sender.sendMessage("§cJugador no encontrado");
                this.open();
            }
            return null;
        });
    }
    public abstract Player findPlayerByName(String playerName);
    public abstract void openNextSection(Player target);
}
