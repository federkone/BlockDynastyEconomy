package BlockDynasty.BukkitImplementation.adapters.platformAdapter;

import BlockDynasty.BukkitImplementation.BlockDynastyEconomy;
import domain.entity.currency.ItemStackCurrency;
import domain.entity.currency.NbtData;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.HashMap;
import java.util.Map;

public class ItemStackCurrencyAdapter implements ItemStackCurrency {
    private final ItemStack itemStack;

    public ItemStackCurrencyAdapter(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    @Override
    public NbtData getNbtData() {
        if (itemStack == null) {
            return new NbtData();
        }
        NbtData nbtData = new NbtData();
        ItemMeta meta = itemStack.getItemMeta();
        if (meta == null) {
            return new NbtData();
        }
        PersistentDataContainer pdc = meta.getPersistentDataContainer();

        Map<String ,String> nbtMap = new HashMap<>();
        for (String key : nbtData.getNbtMap().keySet()) {
            String value = pdc.get(new NamespacedKey(BlockDynastyEconomy.getInstance(), key), PersistentDataType.STRING);
            nbtMap.put(key, value);
        }

        //
        System.out.println(nbtMap);

        return new NbtData(nbtMap);
    }

    @Override
    public  int getCantity() {
        return itemStack.getAmount();
    }

    @Override
    public Object getRoot() {
        return itemStack;
    }
}
