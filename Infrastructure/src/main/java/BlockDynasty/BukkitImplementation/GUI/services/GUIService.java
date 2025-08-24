package BlockDynasty.BukkitImplementation.GUI.services;

import BlockDynasty.BukkitImplementation.GUI.MaterialAdapter;
import BlockDynasty.BukkitImplementation.GUI.components.IGUI;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class GUIService {
    private final Map<UUID, IGUI> openGUIs = new HashMap<>();

    public void registerGUI(Player player, IGUI gui) {
        openGUIs.put(player.getUniqueId(), gui);
    }

    public void unregisterGUI(Player player) {
        openGUIs.remove(player.getUniqueId());
    }

    public void handleClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        IGUI gui = openGUIs.get(player.getUniqueId());

        if (gui != null && event.getView().getTitle().equals(gui.getTitle())) {
            event.setCancelled(true);
            if(event.isRightClick()){
                gui.handleRightClick(event.getRawSlot(), player);
            }else if (event.isLeftClick()) {
                gui.handleLeftClick(event.getRawSlot(), player);
            }

            player.playSound(player.getLocation(), MaterialAdapter.getClickSound(), 0.3f, 1.0f);
        }
    }

    public void refresh(UUID playerId) {
        IGUI gui = openGUIs.get(playerId);
        if (gui != null) {
            gui.refresh();
        }
    }
}