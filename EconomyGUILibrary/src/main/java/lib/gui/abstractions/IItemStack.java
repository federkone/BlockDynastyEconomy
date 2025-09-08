package lib.gui.abstractions;

import java.util.List;

public interface IItemStack {
    IItemStack setDisplayName(String name);
    IItemStack setLore(List<String> lore);
    Object getHandle(); // Returns native ItemStack
}