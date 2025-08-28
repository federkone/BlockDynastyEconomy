package BlockDynasty.GUI.components;


import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.item.inventory.Inventory;

public interface IGUI {
    //method close??
    void close();
    void open();
    void openParent();
    boolean hasParent();
    IGUI getParent();
    void handleRightClick(int slot, ServerPlayer player);
    void handleLeftClick(int slot, ServerPlayer player);
    Inventory getInventory();
    String getTitle();
    void refresh();
}