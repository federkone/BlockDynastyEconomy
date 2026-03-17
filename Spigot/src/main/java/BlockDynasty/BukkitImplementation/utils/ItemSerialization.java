package BlockDynasty.BukkitImplementation.utils;

import com.BlockDynasty.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ItemSerialization {
    private static final Map<String, ItemStack> cache = new ConcurrentHashMap<>();

    // Convierte un ItemStack (con todo su NBT) a un String Base64
    public static String toBase64(ItemStack item) {
        try {
            // Hacemos una copia para no modificar el ítem real en el inventario del admin
            ItemStack clone = item.clone();
            clone.setAmount(1); // Normalizamos a 1 para la DB

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream dataStream = new BukkitObjectOutputStream(outputStream);
            dataStream.writeObject(clone);
            dataStream.close();
            String base64 = Base64Coder.encodeLines(outputStream.toByteArray());
            cache.put(base64, clone);
            return base64;
        } catch (Exception e) {
            throw new IllegalStateException("No se pudo serializar el ítem.", e);
        }
    }

    // Convierte el String Base64 de vuelta a un ItemStack funcional
    public static ItemStack fromBase64(String data) {
        ItemStack cacheItem=  cache.computeIfAbsent(data, key ->{
            try {
                ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
                BukkitObjectInputStream dataStream = new BukkitObjectInputStream(inputStream);
                ItemStack item = (ItemStack) dataStream.readObject();
                dataStream.close();
                return item;
            } catch (Exception e) {
                return null;
            }
        });
        if(cacheItem!=null){
            return cacheItem.clone();
        }
        return null;
    }
}