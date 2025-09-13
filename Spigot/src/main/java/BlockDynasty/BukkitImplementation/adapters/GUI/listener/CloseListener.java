package BlockDynasty.BukkitImplementation.adapters.GUI.listener;

import BlockDynasty.BukkitImplementation.adapters.abstractions.EntityPlayerAdapter;
import lib.gui.GUIFactory;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class CloseListener implements Listener {
    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) { //solo cuando el usuario cierra con ESC
        GUIFactory.getGuiService().unregisterGUI(EntityPlayerAdapter.of((Player) event.getPlayer()));
    }
}
