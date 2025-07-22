package me.BlockDynasty.Economy.Infrastructure.BukkitImplementation.GUI.components;

import me.BlockDynasty.Economy.aplication.useCase.UsesCaseFactory;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public abstract class AbstractGUI implements IGUI {
    protected final Inventory inventory;
    protected final String title;
    protected final Map<Integer, Consumer<Player>> actions = new HashMap<>();

    public AbstractGUI(String title, int rows) {
        this.title = title;
        this.inventory = Bukkit.createInventory(null, rows * 9, title);
    }

    @Override
    public void open(Player player) {
        player.openInventory(inventory);
    }

    @Override
    public void handleClick(int slot, Player player) {
        Consumer<Player> action = actions.get(slot);
        if (action != null) {
            action.accept(player);
        }
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }

    @Override
    public String getTitle() {
        return title;
    }

    protected void setItem(int slot, ItemStack item, Consumer<Player> action) {
        inventory.setItem(slot, item);
        if (action != null) {
            actions.put(slot, action);
        }
    }

    protected ItemStack createItem(Material material, String name, String... lore) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(name);
            if (lore.length > 0) {
                meta.setLore(Arrays.asList(lore));
            }
            item.setItemMeta(meta);
        }
        return item;
    }

    public ItemStack createItem(ItemStack base, String name, String... lore) {
        ItemMeta meta = base.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(name);
            meta.setLore(Arrays.asList(lore));
            base.setItemMeta(meta);
        }
        return base;
    }
}