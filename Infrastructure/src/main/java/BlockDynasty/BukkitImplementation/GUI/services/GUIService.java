package BlockDynasty.BukkitImplementation.GUI.services;

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
        System.out.println("cantidad de guis abiertas: "+ openGUIs.size());
    }

    public void unregisterGUI(Player player) {
        openGUIs.remove(player.getUniqueId());
        System.out.println("cantidad de guis abiertas: "+ openGUIs.size());
    }

    public void handleClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        IGUI gui = openGUIs.get(player.getUniqueId());

        if (gui != null && event.getView().getTitle().equals(gui.getTitle())) {
            event.setCancelled(true);
            gui.handleClick(event.getRawSlot(), player);
        }
    }
}