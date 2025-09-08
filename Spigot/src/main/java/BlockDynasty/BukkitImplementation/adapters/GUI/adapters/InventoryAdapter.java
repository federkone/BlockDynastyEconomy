package BlockDynasty.BukkitImplementation.adapters.GUI.adapters;

import lib.gui.abstractions.IInventory;
import lib.gui.abstractions.IItemStack;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class InventoryAdapter implements IInventory {
    Inventory inventory;
    int rows;
    String title;

    public InventoryAdapter(Inventory inventory) {
        this.inventory = inventory;
    }

    @Override
    public void set(int slot, IItemStack item) {
        this.inventory.setItem(slot,(ItemStack) item.getHandle());
    }

    @Override
    public void setRows(int rows) {
        this.rows = rows;
    }

    @Override
    public int getRows() {
        return this.rows;
    }

    @Override
    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String getTitle() {
        return this.title;
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
