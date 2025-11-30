package adapters;

import lib.gui.components.recipes.RecipeItem;
import lib.util.materials.Materials;
import net.kyori.adventure.text.Component;
import net.minestom.server.component.DataComponents;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MaterialAdapter {
    //map of materials between lib and minestom
    private static Map<Materials, Material> materialMap = new HashMap<>();

    static {
        //initialize material map here
        //hacer el map a mano, donde haré Materials.X -> Material.X, tengo que hacerlo a mano uno por uno
        materialMap.put(Materials.GLASS_PANE, Material.GLASS_PANE);
        materialMap.put(Materials.BLUE_STAINED_GLASS_PANE, Material.BLUE_STAINED_GLASS_PANE);
        materialMap.put(Materials.RED_CONCRETE, Material.RED_CONCRETE);
        materialMap.put(Materials.LIME_CONCRETE, Material.LIME_CONCRETE);
        materialMap.put(Materials.EMERALD_BLOCK, Material.EMERALD_BLOCK);
        materialMap.put(Materials.GOLD_NUGGET, Material.GOLD_NUGGET);
        materialMap.put(Materials.LIME_DYE, Material.LIME_DYE);
        materialMap.put(Materials.NETHER_STAR, Material.NETHER_STAR);
        materialMap.put(Materials.STONE, Material.STONE);
        materialMap.put(Materials.AIR, Material.AIR);
        materialMap.put(Materials.PAPER, Material.PAPER);
        materialMap.put(Materials.BOOK, Material.BOOK);
        materialMap.put(Materials.WRITABLE_BOOK, Material.WRITABLE_BOOK);
        materialMap.put(Materials.CHEST, Material.CHEST);
        materialMap.put(Materials.ENDER_CHEST, Material.ENDER_CHEST);
        materialMap.put(Materials.BARRIER, Material.BARRIER);
        materialMap.put(Materials.NAME_TAG, Material.NAME_TAG);
        materialMap.put(Materials.ARROW, Material.ARROW);
        materialMap.put(Materials.EMERALD, Material.EMERALD);
        materialMap.put(Materials.DIAMOND, Material.DIAMOND);
        materialMap.put(Materials.GOLD_INGOT, Material.GOLD_INGOT);
        materialMap.put(Materials.REDSTONE, Material.REDSTONE);
        materialMap.put(Materials.PLAYER_HEAD, Material.PLAYER_HEAD);
        materialMap.put(Materials.WHITE_WOOL, Material.WHITE_WOOL);
        materialMap.put(Materials.RED_WOOL, Material.RED_WOOL);
        materialMap.put(Materials.PURPLE_WOOL, Material.PURPLE_WOOL);
        materialMap.put(Materials.ORANGE_WOOL, Material.ORANGE_WOOL);
        materialMap.put(Materials.LIME_WOOL, Material.LIME_WOOL);
        materialMap.put(Materials.GRAY_WOOL, Material.GRAY_WOOL);
        materialMap.put(Materials.LIGHT_GRAY_WOOL, Material.LIGHT_GRAY_WOOL);
        materialMap.put(Materials.CYAN_WOOL, Material.CYAN_WOOL);
        materialMap.put(Materials.LIGHT_BLUE_WOOL, Material.LIGHT_BLUE_WOOL);
        materialMap.put(Materials.BLUE_WOOL, Material.BLUE_WOOL);
        materialMap.put(Materials.BROWN_WOOL, Material.BROWN_WOOL);
        materialMap.put(Materials.GREEN_WOOL, Material.GREEN_WOOL);
        materialMap.put(Materials.PINK_WOOL, Material.PINK_WOOL);
        materialMap.put(Materials.YELLOW_WOOL, Material.YELLOW_WOOL);
    }

    public static Material convertMaterial(Materials materials) {
        return materialMap.getOrDefault(materials, Material.STONE);
    }

    public static ItemStack createItem(RecipeItem recipeItem) {
        // Creation logic here
        List<Component> loreComponents = Stream.of(recipeItem.getLore())
                .map(line -> Component.text(line))
                .collect(Collectors.toList());

        ItemStack item = ItemStack.builder(convertMaterial(recipeItem.getMaterial()))
                .set(DataComponents.CUSTOM_NAME, Component.text(recipeItem.getName()))
                .set(DataComponents.LORE, loreComponents)
                .build();

        return item;
    }
}
