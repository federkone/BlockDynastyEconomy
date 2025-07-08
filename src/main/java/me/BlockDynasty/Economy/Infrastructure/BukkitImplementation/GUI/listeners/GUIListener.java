package me.BlockDynasty.Economy.Infrastructure.BukkitImplementation.GUI.listeners;

import me.BlockDynasty.Economy.Infrastructure.BukkitImplementation.GUI.GUIService;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class GUIListener implements Listener {
    private final GUIService guiService;

    public GUIListener(GUIService guiService) {
        this.guiService = guiService;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        guiService.handleClick(event);
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        guiService.unregisterGUI((org.bukkit.entity.Player) event.getPlayer());
    }
}