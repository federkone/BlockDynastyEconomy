package BlockDynasty.BukkitImplementation.GUI.adapters;

import lib.gui.abstractions.IItemStack;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class ItemStackAdapter implements IItemStack {
    ItemStack itemStack;

    public ItemStackAdapter(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    @Override
    public IItemStack setDisplayName(String name) {
        MaterialAdapter.applyItemMeta(itemStack, name, null);
        return this;
    }

    @Override
    public IItemStack setLore(List<String> lore) {
        MaterialAdapter.applyItemMeta(itemStack, null, lore);
        return this;
    }

    @Override
    public Object getHandle() {
        return itemStack;
    }
}
