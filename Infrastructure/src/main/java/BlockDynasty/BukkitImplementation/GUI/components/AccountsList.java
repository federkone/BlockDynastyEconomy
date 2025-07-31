package BlockDynasty.BukkitImplementation.GUI.components;

import BlockDynasty.BukkitImplementation.BlockDynastyEconomy;
import BlockDynasty.BukkitImplementation.GUI.MaterialAdapter;
import BlockDynasty.BukkitImplementation.GUI.services.GUIService;
import BlockDynasty.Economy.domain.entities.account.Player;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Collections;
import java.util.List;

public abstract class AccountsList extends AbstractGUI {
    private int currentPage = 0;
    private final int PLAYERS_PER_PAGE = 21;
    private final JavaPlugin plugin = BlockDynastyEconomy.getInstance();
    private final GUIService guiService;
    private final AbstractGUI parent;

    public AccountsList(String title, int rows, GUIService guiService,AbstractGUI parent) {
        super(title, rows);
        this.parent = parent;
        this.guiService  = guiService;
    }


    protected void showPlayersPage(List<Player> players, org.bukkit.entity.Player sender) {
        // Calculate pagination
        int startIndex = currentPage * PLAYERS_PER_PAGE;
        int endIndex = Math.min(startIndex + PLAYERS_PER_PAGE, players.size());

        // Clear GUI
        for (int i = 0; i < getInventory().getSize(); i++) {
            setItem(i, null, null);
        }

        // Add players to GUI
        int slot = 10;
        for (int i = startIndex; i < endIndex; i++) {
            Player target = players.get(i);
            ItemStack playerHead = MaterialAdapter.createPlayerHead(target.getNickname());

            setItem(slot, playerHead, unused -> {
                openNextSection(target);
            });

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
            setItem(42, createItem(Material.ARROW, "§aPágina Siguiente",
                    "§7Click para ver más jugadores"), unused -> {
                currentPage++;
                showPlayersPage(players,sender);
            });
        }

        setItem(39, createItem(Material.NAME_TAG, "§aBuscar Jugador",
                "§7Click para buscar un jugador por nombre"), unused -> {
            openAnvilSearch(sender);
        });

        // Cancel button
        setItem(41, createItem(Material.BARRIER, "§7Atrás",
                "§7Click para Atrás"), unused -> {
            sender.openInventory(parent.getInventory());
            guiService.registerGUI(sender, parent);
        });
    }

    protected void openAnvilSearch(org.bukkit.entity.Player sender) {
        new AnvilGUI.Builder()
                .onClick((slot, stateSnapshot) -> {
                    if (slot != AnvilGUI.Slot.OUTPUT) {
                        return Collections.emptyList();
                    }

                    String playerName = stateSnapshot.getText().trim();
                    if (playerName.isEmpty()) {
                        return List.of(AnvilGUI.ResponseAction.replaceInputText("§cNombre no válido"));
                    }

                    return List.of(
                            AnvilGUI.ResponseAction.close(),
                            AnvilGUI.ResponseAction.run(() -> searchPlayerByName(playerName, sender))
                    );
                })
                .text("Nombre del jugador")
                .title("Buscar Jugador")
                .plugin(plugin)
                .open(sender);
    }
    protected void searchPlayerByName(String playerName, org.bukkit.entity.Player searcher) {
        Player foundPlayer = findPlayerByName(playerName);

        if (foundPlayer != null) {
            openNextSection(foundPlayer);
        } else {
            searcher.sendMessage("§cJugador no encontrado");
            searcher.openInventory(this.getInventory());
            guiService.registerGUI(searcher, this);
        }
    }
    public abstract Player findPlayerByName(String playerName);
    public abstract void openNextSection(Player target);
}
