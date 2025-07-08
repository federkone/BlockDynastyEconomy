package me.BlockDynasty.Economy.Infrastructure.BukkitImplementation.GUI;

import me.BlockDynasty.Economy.Infrastructure.BukkitImplementation.GUI.components.IGUI;
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
            gui.handleClick(event.getRawSlot(), player);
        }
    }
}