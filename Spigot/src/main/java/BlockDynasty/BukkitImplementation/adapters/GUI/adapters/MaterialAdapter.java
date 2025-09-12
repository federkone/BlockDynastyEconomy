package BlockDynasty.BukkitImplementation.adapters.GUI.adapters;

import BlockDynasty.BukkitImplementation.utils.Version;
import lib.gui.abstractions.Materials;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings( "deprecation")
public class MaterialAdapter {
    private static final Map<Materials, Material> MATERIAL_MAP = new HashMap<>();
    private static final Material FALLBACK = Material.STONE;

    static {
        // Initialize automatically
        for (Materials material : Materials.values()) {
            try {
                // In Bukkit, materials are direct enum constants, not suppliers
                Material bukkitMaterial = Material.valueOf(material.name());
                MATERIAL_MAP.put(material, bukkitMaterial);
            } catch (IllegalArgumentException e) {
                //System.out.println("No mapping found for " + material.name() + ": " + e.getMessage());

                if (Version.isLegacy()) {
                    if (material == Materials.PLAYER_HEAD) {
                        MATERIAL_MAP.put(material, Material.valueOf("SKULL_ITEM"));
                    }
                    if (material.name().contains("WOOL")) {
                        MATERIAL_MAP.put(material, Material.valueOf("WOOL"));
                    }
                    if (material == Materials.LIME_DYE) {
                        MATERIAL_MAP.put(material, Material.SLIME_BALL);
                    }
                    if (material == Materials.LIME_CONCRETE) {
                        MATERIAL_MAP.put(material, Material.EMERALD_BLOCK);
                    }
                    if (material == Materials.RED_CONCRETE) {
                        MATERIAL_MAP.put(material, Material.REDSTONE_BLOCK);
                    }
                    if (material == Materials.WRITABLE_BOOK) {
                        MATERIAL_MAP.put(material, Material.valueOf("BOOK_AND_QUILL"));
                    }
                    if (material == Materials.GLASS_PANE) {
                        MATERIAL_MAP.put(material, Material.valueOf("THIN_GLASS"));
                    }
                    if (material == Materials.BLUE_STAINED_GLASS_PANE) {
                        MATERIAL_MAP.put(material, Material.valueOf("STAINED_GLASS_PANE"));
                    }
                    continue;
                }
                MATERIAL_MAP.put(material, FALLBACK);
            }
        }
    }

    public static Material toBukkitMaterial(Materials material) {
        //aqui podemos tratar materiales segun version si es necesario y entregamos el material correcto
        //con los metodos de abajo
        return MATERIAL_MAP.getOrDefault(material, FALLBACK);
    }

    public static ItemStack toBukkitItemStack(Materials materials) {
        if (Version.isLegacy()) {
            switch (materials) {
                case PLAYER_HEAD:
                    return new ItemStack(Material.valueOf("SKULL_ITEM"), 1, (short) 3);
                case BLUE_STAINED_GLASS_PANE:
                    return new ItemStack(Material.valueOf("STAINED_GLASS_PANE"), 1, (short) 11);
                default:
                    if (materials.name().contains("WOOL")) {
                        return new ItemStack(Material.valueOf("WOOL"), 1, getLegacyDataValue(materials.name()));
                    }
            }
        }
        return new ItemStack(toBukkitMaterial(materials));
    }

    public static void applyItemMeta(ItemStack item, String displayName, List<String> lore) {
        ItemMeta meta;

        if (isPlayerHead(item.getType())) {
            SkullMeta skullMeta = (SkullMeta) item.getItemMeta();
            skullMeta.setOwner(displayName);
            meta = skullMeta;
        } else {
            meta = item.getItemMeta();
        }

        if (displayName != null) {
            meta.setDisplayName(displayName);
        }

        if (lore != null) {
            meta.setLore(lore);
        }

        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        item.setItemMeta(meta);
    }

    private static boolean isPlayerHead(Material material) {
        if (Version.isLegacy()) {
            return material == Material.valueOf("SKULL_ITEM");
        } else {
            return material == Material.PLAYER_HEAD;
        }

    }

    public static Sound getClickSound() {
        if (Version.match("1.8", "1.9", "1.10","1.11")) {
            return Sound.valueOf("CLICK");
        } else {
            return Sound.UI_BUTTON_CLICK;
        }
    }

    public static Sound getPickupSound() {
        if (Version.match("1.8", "1.9", "1.10","1.11")) {
            return Sound.valueOf("ORB_PICKUP");
        } else {
            return Sound.ENTITY_EXPERIENCE_ORB_PICKUP;
        }
    }

    /**
     * Obtiene el valor de data (durabilidad) para wool en versiones legacy.
     */
    private static short getLegacyDataValue(String material) {
        switch (material) {
            case "WHITE_WOOL":
                return 0;
            case "ORANGE_WOOL":
                return 1;
            case "MAGENTA_WOOL":
                return 2;
            case "LIGHT_BLUE_WOOL":
                return 3;
            case "YELLOW_WOOL":
                return 4;
            case "LIME_WOOL":
                return 5;
            case "PINK_WOOL":
                return 6;
            case "GRAY_WOOL":
                return 7;
            case "LIGHT_GRAY_WOOL":
                return 8;
            case "CYAN_WOOL":
                return 9;
            case "PURPLE_WOOL":
                return 10;
            case "BLUE_WOOL":
                return 11;
            case "BROWN_WOOL":
                return 12;
            case "GREEN_WOOL":
                return 13;
            case "RED_WOOL":
                return 14;
            case "BLACK_WOOL":
                return 15;
            default:
                return 0; // Default a white wool
        }
    }


}