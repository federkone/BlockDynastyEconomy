package BlockDynasty.BukkitImplementation.GUI;

import BlockDynasty.Economy.domain.entities.account.Player;
import org.bukkit.Material;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.Collections;

@SuppressWarnings("deprecation")
/**
 * MaterialAdapter is a utility class that provides methods to adapt materials
 * for different Minecraft versions, particularly for legacy versions (1.8-1.12)
 * and modern versions (1.13+).
 * at the moment translate: LimeDye, LimeConcrete, RedConcrete, PlayerHead, adaptWool, createPlayerHead
 */
public class MaterialAdapter {

    public static Material getLimeDye() {
        if (isLegacy()) return Material.SLIME_BALL;
        return Material.LIME_DYE;
    }

    public static Material getLimeConcrete() {
        if (isLegacy()) return Material.EMERALD_BLOCK;
        return Material.LIME_CONCRETE;
    }

    public static Material getRedConcrete() {
        if (isLegacy()) return Material.REDSTONE_BLOCK;
        return Material.RED_CONCRETE;
    }

    public static Material getPlayerHead() {
        if (isLegacy()) return Material.valueOf("SKULL_ITEM"); // 1.8-1.12
        return Material.PLAYER_HEAD; // 1.13+
    }

    public static ItemStack getPanelGlass(){
        if (isLegacy()) {
            return new ItemStack(Material.valueOf("THIN_GLASS"));
        }else {
            return new ItemStack(Material.GLASS_PANE);
        }
    }

    public static ItemStack createPlayerHead(String playerName) {
        if(isLegacy()){
            ItemStack skull = new ItemStack(Material.valueOf("SKULL_ITEM"), 1, (short) 3);
            SkullMeta meta = (SkullMeta) skull.getItemMeta();
            meta.setOwner(playerName);
            meta.setDisplayName(playerName);
            skull.setItemMeta(meta);
            return skull;
        }else{
            ItemStack head = new ItemStack(Material.PLAYER_HEAD);
            SkullMeta meta = (SkullMeta) head.getItemMeta();
            meta.setOwner(playerName);
            meta.setDisplayName("§e" + playerName);
            meta.setLore(Collections.singletonList("§7Click para pagar a este jugador"));
            head.setItemMeta(meta);
            return head;
        }
    }

    public static ItemStack adaptWool(String material) {
        if (!isLegacy()) {
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


    public static boolean isLegacy() {
        String version = org.bukkit.Bukkit.getBukkitVersion(); // "1.8.8-R0.1-SNAPSHOT"
        return version.startsWith("1.8") || version.startsWith("1.9") || version.startsWith("1.10") || version.startsWith("1.11") || version.startsWith("1.12");
    }
}