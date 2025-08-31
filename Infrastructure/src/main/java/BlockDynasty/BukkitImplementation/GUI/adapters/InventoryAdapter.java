package BlockDynasty.BukkitImplementation.GUI.adapters;

import lib.components.IInventory;
import lib.components.IItemStack;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class InventoryAdapter implements IInventory {
    Inventory inventory;

    public InventoryAdapter(Inventory inventory) {
        this.inventory = inventory;
    }

    @Override
    public void set(int slot, IItemStack item) {
        this.inventory.setItem(slot,(ItemStack) item.getHandle());
    }

    @Override
    public int getSize() {
        return this.inventory.getMaxStackSize();
    }

    @Override
    public Object getHandle() {
        return inventory;
    }
}
