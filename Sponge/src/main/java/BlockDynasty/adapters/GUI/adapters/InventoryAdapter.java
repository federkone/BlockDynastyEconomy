package BlockDynasty.adapters.GUI.adapters;

import lib.gui.abstractions.IInventory;
import lib.gui.abstractions.IItemStack;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.type.ViewableInventory;

public class InventoryAdapter implements IInventory {
    ViewableInventory inventory;
    private int rows;
    private String title;

    public InventoryAdapter(ViewableInventory inventory) {
        this.inventory = inventory;
    }

    @Override
    public void set(int slot, IItemStack item) {
        this.inventory.set(slot, (ItemStack) item.getHandle());
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
        return this.inventory.capacity();
    }

    @Override
    public Object getHandle() {
        return inventory;
    }
}
