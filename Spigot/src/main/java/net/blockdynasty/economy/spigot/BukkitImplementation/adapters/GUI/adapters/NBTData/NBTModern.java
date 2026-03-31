package net.blockdynasty.economy.spigot.BukkitImplementation.adapters.GUI.adapters.NBTData;

import net.blockdynasty.economy.hardcash.domain.entity.currency.NbtData;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

//with bukkit implementation for modern versions
public class NBTModern implements NBTService{
    private JavaPlugin plugin;
    public NBTModern(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void applyNBTData(ItemStack itemStack, NbtData nbtData) {
        Map<String ,String> nbtMap = nbtData.getNbtMap();
        ItemMeta meta = itemStack.getItemMeta();
        for (Map.Entry<String, String> entry : nbtMap.entrySet()) {
            NamespacedKey key = new NamespacedKey(plugin, entry.getKey());
            meta.getPersistentDataContainer().set(
                    key,
                    PersistentDataType.STRING,
                    entry.getValue()
            );
        }
        itemStack.setItemMeta(meta);
    }

    @Override
    public NbtData getNBTData(ItemStack itemStack) {
        ItemMeta meta = itemStack.getItemMeta();
        if (meta == null) {
            return new NbtData();
        }
        NbtData nbtData = new NbtData();
        PersistentDataContainer pdc = meta.getPersistentDataContainer();
        Map<String ,String> nbtMap = new HashMap<>();
        for (String key : nbtData.getNbtMap().keySet()) {
            String value = pdc.get(new NamespacedKey(plugin, key), PersistentDataType.STRING);
            nbtMap.put(key, value);
        }
        return new NbtData(nbtMap);
    }
}
