package net.blockdynasty.economy.gui.gui.components.factory;

import net.blockdynasty.economy.gui.gui.components.PlatformGUI;
import net.blockdynasty.economy.gui.gui.components.IItemStack;
import net.blockdynasty.economy.libs.abstractions.platform.recipes.RecipeItem;

public class Item {
    private static PlatformGUI platformAdapter;

    public static void init(PlatformGUI adapter) {
        platformAdapter = adapter;
    }

    public static IItemStack of(RecipeItem recipeItem){
        return platformAdapter.createItemStack(recipeItem);
    }
}
