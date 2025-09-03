package BlockDynasty.GUI.adapters;

import lib.components.IItemStack;
import net.kyori.adventure.text.Component;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.Keys;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


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
