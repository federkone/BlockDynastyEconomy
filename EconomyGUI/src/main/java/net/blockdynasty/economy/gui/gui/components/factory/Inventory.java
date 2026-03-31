package net.blockdynasty.economy.gui.gui.components.factory;

import net.blockdynasty.economy.gui.gui.components.PlatformGUI;
import net.blockdynasty.economy.gui.gui.components.IInventory;
import net.blockdynasty.economy.libs.abstractions.platform.recipes.RecipeInventory;

public class Inventory {
    private static PlatformGUI platformAdapter;

    public static void init(PlatformGUI adapter) {
        platformAdapter = adapter;
    }

    public static IInventory of(RecipeInventory recipeInventory){
        return platformAdapter.createInventory(recipeInventory);
    }
}
