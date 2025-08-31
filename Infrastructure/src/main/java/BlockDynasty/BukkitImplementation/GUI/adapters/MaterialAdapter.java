package BlockDynasty.BukkitImplementation.GUI.adapters;

import BlockDynasty.BukkitImplementation.utils.Version;
import lib.components.Materials;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
                System.out.println("No mapping found for " + material.name() + ": " + e.getMessage());
                MATERIAL_MAP.put(material, FALLBACK);
            }
        }
    }

    public static Material toBukkitMaterial(Materials material) {
        //aqui podemos tratar materiales segun version si es necesario y entregamos el material correcto
        //con los metodos de abajo
        return MATERIAL_MAP.getOrDefault(material, FALLBACK);
    }

    public static Material getLimeDye() {
        if (Version.isLegacy()) return Material.SLIME_BALL;
        return Material.LIME_DYE;
    }

    public static Material getLimeConcrete() {
        if (Version.isLegacy()) return Material.EMERALD_BLOCK;
        return Material.LIME_CONCRETE;
    }

    public static Material getRedConcrete() {
        if (Version.isLegacy()) return Material.REDSTONE_BLOCK;
        return Material.RED_CONCRETE;
    }

    public static Material getPlayerHead() {
        if (Version.isLegacy()) return Material.valueOf("SKULL_ITEM"); // 1.8-1.12
        return Material.PLAYER_HEAD; // 1.13+
    }

    public static Material getWritableBook() {
        if (Version.isLegacy()) return Material.valueOf("BOOK_AND_QUILL"); // 1.8-1.12
        return Material.WRITABLE_BOOK; // 1.13+
    }

    public static ItemStack getPanelGlass(){
        if (Version.isLegacy()) {
            return new ItemStack(Material.valueOf("THIN_GLASS"));
        }else {
            return new ItemStack(Material.GLASS_PANE);
        }
    }
    public static ItemStack getBluePanelGlass(){
        if (Version.isLegacy()) {
            return new ItemStack(Material.valueOf("STAINED_GLASS_PANE"),1,(short)11);
        }else {
            return new ItemStack(Material.BLUE_STAINED_GLASS_PANE);
        }
    }

    public static ItemStack createPlayerHead(String playerName) {
        ItemStack skull;
        if(Version.isLegacy()){
            skull = new ItemStack(Material.valueOf("SKULL_ITEM"), 1, (short) 3);
        }else{
            skull = new ItemStack(Material.PLAYER_HEAD);
        }
        SkullMeta meta = (SkullMeta) skull.getItemMeta();
        meta.setOwner(playerName);
        meta.setDisplayName(playerName);
        skull.setItemMeta(meta);
        return skull;
    }

    public static ItemStack createPlayerHead(String playerName, List<String> lore) {
        ItemStack skull;
        if(Version.isLegacy()){
            skull = new ItemStack(Material.valueOf("SKULL_ITEM"), 1, (short) 3);
        }else{
            skull = new ItemStack(Material.PLAYER_HEAD);
        }
        SkullMeta meta = (SkullMeta) skull.getItemMeta();
        meta.setOwner(playerName);
        meta.setDisplayName(playerName);
        meta.setLore(lore);
        skull.setItemMeta(meta);
        return skull;
    }

    public static ItemStack adaptWool(String material) {
        if (!Version.isLegacy()) {
            return new ItemStack(Material.getMaterial(material));
        }

        Material legacyWool = null;
        try {
            legacyWool = Material.valueOf("WOOL");
            short data = getLegacyDataValue(material);
            return new ItemStack(legacyWool, 1, data);
        } catch (IllegalArgumentException ignored) {
            return new ItemStack(Material.PAPER);
        }
    }

    public static Sound getClickSound() {
        if (Version.match("1.8", "1.9", "1.10","1.11")) {
            return Sound.valueOf("CLICK");
        } else {
            return Sound.UI_BUTTON_CLICK;
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