package net.blockdynasty.economy.hytale.adapters.Materials;

import net.blockdynasty.economy.gui.gui.components.IItemStack;
import net.blockdynasty.economy.hardcash.domain.entity.currency.ItemStackCurrency;
import net.blockdynasty.economy.hardcash.domain.entity.currency.RecipeItemCurrency;
import net.blockdynasty.economy.hytale.adapters.ItemStackAdapter;
import net.blockdynasty.economy.libs.abstractions.platform.recipes.RecipeItem;


public class MaterialAdapter {


    //icono personalizado para lagui
    public static IItemStack createItemStack(RecipeItem recipeItem) {
        /*recipeItem.getMaterial();
        recipeItem.getName();
        recipeItem.getLore();
        recipeItem.getTexture();*/
        //todo la textura puede ser la referencia a algun asset en memoria

        return new ItemStackAdapter(recipeItem);
    }
    public static ItemStackCurrency createItemStack(RecipeItemCurrency recipeItemCurrency){
        return null;
    }


}
