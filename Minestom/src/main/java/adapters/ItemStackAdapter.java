package adapters;

import lib.gui.components.IItemStack;
import net.kyori.adventure.text.Component;
import net.minestom.server.item.ItemStack;

import java.util.List;
import java.util.stream.Collectors;

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
