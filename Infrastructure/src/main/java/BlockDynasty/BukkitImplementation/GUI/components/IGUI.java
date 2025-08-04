package BlockDynasty.BukkitImplementation.GUI.components;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public interface IGUI {
    void open();
    void openParent();
    boolean hasParent();
    IGUI getParent();
    void handleClick(int slot, Player player);
    Inventory getInventory();
    String getTitle();
}