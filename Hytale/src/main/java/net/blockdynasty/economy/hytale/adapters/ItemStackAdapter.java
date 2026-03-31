package net.blockdynasty.economy.hytale.adapters;


import net.blockdynasty.economy.gui.gui.components.IItemStack;
import net.blockdynasty.economy.libs.abstractions.platform.recipes.RecipeItem;

//adaptador de icono de gui
public class ItemStackAdapter implements IItemStack {
    private RecipeItem recipeItem;

    public ItemStackAdapter(RecipeItem recipeItem) {
        this.recipeItem = recipeItem;
    }

    public RecipeItem getRecipeItem() {
        return this.recipeItem;
    }

    @Override
    public Object getHandle() {
        return this;
    }
}
