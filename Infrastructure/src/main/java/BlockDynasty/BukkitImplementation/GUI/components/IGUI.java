package BlockDynasty.BukkitImplementation.GUI.components;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public interface IGUI {
    //method close??
    void close();
    void open();
    void openParent();
    boolean hasParent();
    IGUI getParent();
    void handleRightClick(int slot, Player player);
    void handleLeftClick(int slot, Player player);
    Inventory getInventory();
    String getTitle();
    void refresh();
}