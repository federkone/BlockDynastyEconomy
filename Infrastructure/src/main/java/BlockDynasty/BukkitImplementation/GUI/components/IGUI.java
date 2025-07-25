package BlockDynasty.BukkitImplementation.GUI.components;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public interface IGUI {
    void open(Player player);
    void handleClick(int slot, Player player);
    Inventory getInventory();
    String getTitle();
}