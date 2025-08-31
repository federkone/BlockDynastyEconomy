package BlockDynasty.GUI.adapters;

import lib.components.Materials;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.ItemTypes;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class MaterialAdapter {
    private static final Map<Materials, ItemType> MATERIAL_MAP = new HashMap<>();
    private static final ItemType FALLBACK = ItemTypes.STONE.get();

    static {
        // Initialize automatically
        for (Materials material : Materials.values()) {
            try {
                Field field = ItemTypes.class.getField(material.name());
                Object supplier = field.get(null);
                // Call the get() method on the supplier
                ItemType itemType = (ItemType) supplier.getClass().getMethod("get").invoke(supplier);
                MATERIAL_MAP.put(material, itemType);
            } catch (Exception e) {
                System.out.println("No mapping found for " + material.name() + ": " + e.getMessage());
                MATERIAL_MAP.put(material, FALLBACK);
            }
        }
    }

    public static ItemType toItemType(Materials material) {
        return MATERIAL_MAP.getOrDefault(material, FALLBACK);
    }
}
