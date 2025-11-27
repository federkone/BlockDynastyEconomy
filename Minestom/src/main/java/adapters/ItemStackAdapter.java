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
    public IItemStack setDisplayName(String name) {
        itemStack= itemStack.withCustomName(Component.text(name));
        return this;
    }

    @Override
    public IItemStack setLore(List<String> lore) {
        itemStack = itemStack.withLore(Component.text(""));
        return this;
    }

    @Override
    public IItemStack setTexture(String texture) {
        return this;
    }

    @Override
    public Object getHandle() {
        return itemStack;
    }
}
