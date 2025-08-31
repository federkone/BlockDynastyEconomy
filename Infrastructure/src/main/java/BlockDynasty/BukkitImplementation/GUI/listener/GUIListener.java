package BlockDynasty.BukkitImplementation.GUI.listener;

import BlockDynasty.BukkitImplementation.GUI.adapters.PlayerAdapter;
import lib.GUIFactory;
import lib.components.ClickType;
import lib.components.IPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class GUIListener implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        IPlayer sender = new PlayerAdapter((Player) event.getWhoClicked());
        int slot = event.getRawSlot();
        if (!GUIFactory.getGuiService().hasOpenedGUI(sender)) return;
        event.setCancelled(true);
        if (event.isLeftClick()){
            GUIFactory.getGuiService().handleClick(sender, ClickType.LEFT, slot);
        }else if (event.isRightClick()){
            GUIFactory.getGuiService().handleClick(sender, ClickType.RIGHT,slot);
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) { //solo cuando el usuario cierra con ESC
        GUIFactory.getGuiService().unregisterGUI(new PlayerAdapter((Player) event.getPlayer()));
    }
}
