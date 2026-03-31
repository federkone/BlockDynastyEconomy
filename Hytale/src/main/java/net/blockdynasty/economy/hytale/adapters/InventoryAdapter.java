package net.blockdynasty.economy.hytale.adapters;


import net.blockdynasty.economy.gui.gui.components.IInventory;
import net.blockdynasty.economy.gui.gui.components.IItemStack;
import net.blockdynasty.economy.libs.abstractions.platform.recipes.RecipeInventory;

//gui representation of an inventory...
public class InventoryAdapter implements IInventory {
    private ItemStackAdapter[] items;
    private RecipeInventory recipeInventory;

    public InventoryAdapter(RecipeInventory recipeInventory) {
        this.recipeInventory = recipeInventory;
        this.items = new ItemStackAdapter[recipeInventory.getRows()*9];
    }

    @Override
    public void set(int i, IItemStack iItemStack) {
        //this.items.set(i, (ItemStackAdapter) iItemStack.getHandle());
        this.items[i] = (ItemStackAdapter) iItemStack;
    }

    public RecipeInventory getRecipeInventory() {
        return this.recipeInventory;
    }

    public ItemStackAdapter[] getItems() {
        return this.items;
    }

    @Override
    public Object getHandle() {
        return this;
    }
}
