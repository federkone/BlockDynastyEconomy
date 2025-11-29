package minestom;

import lib.gui.components.recipes.RecipeInventory;
import net.kyori.adventure.text.Component;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;

public class MinestomInventory extends Inventory {

    public MinestomInventory(RecipeInventory recipe) {
        super(MinestomInventory.getInventoryType(recipe.getRows()), Component.text(recipe.getTitle()));
    }

    public static InventoryType getInventoryType(int type) {
        switch (type) {
            case 1:
                return InventoryType.CHEST_1_ROW;
            case 2:
                return InventoryType.CHEST_2_ROW;
            case 3:
                return InventoryType.CHEST_3_ROW;
            case 4:
                return InventoryType.CHEST_4_ROW;
            case 5:
                return InventoryType.CHEST_5_ROW;
            case 6:
                return InventoryType.CHEST_6_ROW;
            default:
                return InventoryType.CHEST_6_ROW;
        }
    }
}
