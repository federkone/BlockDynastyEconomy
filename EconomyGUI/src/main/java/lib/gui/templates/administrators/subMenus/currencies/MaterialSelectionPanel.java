package lib.gui.templates.administrators.subMenus.currencies;

import BlockDynasty.Economy.aplication.useCase.currency.EditCurrencyUseCase;
import BlockDynasty.Economy.domain.entities.currency.ICurrency;
import abstractions.platform.materials.Materials;
import abstractions.platform.recipes.RecipeItem;
import lib.gui.GUIFactory;
import lib.gui.components.IEntityGUI;
import lib.gui.components.IGUI;
import lib.gui.components.IItemStack;
import lib.gui.components.factory.Item;
import lib.gui.components.generics.PaginatedPanel;

import java.util.List;

public class MaterialSelectionPanel extends PaginatedPanel<Materials> {
    private EditCurrencyUseCase editCurrencyUseCase;
    private ICurrency currency;
    private IEntityGUI entityGUI;

    public MaterialSelectionPanel(IEntityGUI owner, IGUI parent,ICurrency currency, EditCurrencyUseCase editCurrencyUseCase) {
        super("Materials", 5, owner, parent,21);
        this.currency = currency;
        this.editCurrencyUseCase = editCurrencyUseCase;
        this.entityGUI = owner;

        showItemsPage(List.of(Materials.values()));
    }

    @Override
    protected IItemStack createItemFor(Materials item) {
        RecipeItem recipe = RecipeItem.builder()
                .setMaterial(item)
                .setName(item.name())
                .setLore("Click to select")
                .build();
        return Item.of(recipe);
    }

    @Override
    public void functionLeftItemClick(Materials material){
        try {
            editCurrencyUseCase.editMaterial(currency.getSingular(), material.name());
            entityGUI.sendMessage("Material updated successfully.");
            GUIFactory.editCurrencyPanel(entityGUI,currency, parent.getParent()).open();
        }catch (Exception e){
            entityGUI.sendMessage("Error");
            this.parent.open();
        }
    }
}
