package BlockDynasty.BukkitImplementation.adapters.GUI.listener;

import BlockDynasty.BukkitImplementation.adapters.GUI.adapters.PlayerAdapter;
import lib.gui.GUIFactory;
import lib.gui.abstractions.ClickType;
import lib.gui.abstractions.IPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class ClickListener implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        IPlayer sender = new PlayerAdapter((Player) event.getWhoClicked());
        int slot = event.getRawSlot();
        if (!GUIFactory.getGuiService().hasOpenedGUI(sender)) return;
        event.setCancelled(true);
        if (event.isLeftClick()) {
            GUIFactory.getGuiService().handleClick(sender, ClickType.LEFT, slot);
        } else if (event.isRightClick()) {
            GUIFactory.getGuiService().handleClick(sender, ClickType.RIGHT, slot);
        }
    }
}