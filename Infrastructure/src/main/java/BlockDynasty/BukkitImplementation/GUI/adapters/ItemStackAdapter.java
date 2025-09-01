package BlockDynasty.BukkitImplementation.GUI.adapters;

import lib.components.IItemStack;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.List;

public class ItemStackAdapter implements IItemStack {
    ItemStack itemStack;

    public ItemStackAdapter(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    @Override
    public IItemStack setDisplayName(String name) {
        if(MaterialAdapter.isPlayerHead(this.itemStack.getType())) {
            SkullMeta skullmeta = (SkullMeta) itemStack.getItemMeta();
            skullmeta.setOwner(name);
            skullmeta.setDisplayName(name);
            this.itemStack.setItemMeta(skullmeta);
        }else{
            ItemMeta meta = itemStack.getItemMeta();
            meta.setDisplayName(name);
            meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
            itemStack.setItemMeta(meta);
        }

        return this;
    }

    @Override
    public IItemStack setLore(List<String> lore) {
        ItemMeta meta = itemStack.getItemMeta();
        meta.setLore(lore);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        itemStack.setItemMeta(meta);
        return this;
    }

    @Override
    public Object getHandle() {
        return itemStack;
    }
}
