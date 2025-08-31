package BlockDynasty.GUI.adapters;

import lib.components.IInventory;
import lib.components.IItemStack;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.type.ViewableInventory;

public class SpongeInventory implements IInventory {
    ViewableInventory inventory;

    public SpongeInventory(ViewableInventory inventory) {
        this.inventory = inventory;
    }

    @Override
    public void set(int slot, IItemStack item) {
        this.inventory.set(slot, (ItemStack) item.getHandle());
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
