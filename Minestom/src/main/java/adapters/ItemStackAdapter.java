package adapters;

import lib.gui.components.IItemStack;
import net.minestom.server.item.ItemStack;

public class ItemStackAdapter implements IItemStack {
    private ItemStack itemStack;

    public ItemStackAdapter(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    @Override
    public Object getHandle() {
        return itemStack;
    }
}
