package adapters;

import lib.gui.components.IInventory;
import lib.gui.components.IItemStack;
import net.minestom.server.inventory.Inventory;

public class InventoryAdapter implements IInventory {
    private Inventory inventory;

    public InventoryAdapter(Inventory inventory) {
        this.inventory = inventory;
    }

    @Override
    public void set(int slot, IItemStack item) {
        this.inventory.setItemStack(slot, (net.minestom.server.item.ItemStack) item.getHandle());
    }

    @Override
    public Object getHandle() {
        return this.inventory;
    }
}
