/**
 * Copyright 2025 Federico Barrionuevo "@federkone"
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package BlockDynasty.BukkitImplementation.adapters.GUI.adapters;

import com.BlockDynasty.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;
import org.bukkit.Material;
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
        if (item == null) return null;
        try {
            ItemStack clone = item.clone();
            clone.setAmount(1);

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

    public static ItemStack fromBase64(String data) {
        if (data == null || data.isEmpty()) return new ItemStack(Material.AIR);
        return cache.computeIfAbsent(data, key ->{
            try {
                ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
                BukkitObjectInputStream dataStream = new BukkitObjectInputStream(inputStream);
                ItemStack item = (ItemStack) dataStream.readObject();
                dataStream.close();
                return item;
            } catch (Exception e) {
                return new ItemStack(Material.AIR);
            }
        }).clone();
    }
}