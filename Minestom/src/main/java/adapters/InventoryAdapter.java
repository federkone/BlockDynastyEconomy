package adapters;

import lib.gui.components.IInventory;
import lib.gui.components.IItemStack;
import net.minestom.server.inventory.Inventory;

public class InventoryAdapter implements IInventory {
    private Inventory inventory;
    private int rows;
    private String title;

    public InventoryAdapter(Inventory inventory) {
        this.inventory = inventory;
    }

    @Override
    public void set(int slot, IItemStack item) {
        this.inventory.setItemStack(slot, (net.minestom.server.item.ItemStack) item.getHandle());
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
        return inventory.getSize();
    }

    @Override
    public Object getHandle() {
        return this.inventory;
    }
}
