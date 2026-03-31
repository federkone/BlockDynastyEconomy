package net.blockdynasty.economy.gui.gui.templates.administrators.subMenus.currencies;

import net.blockdynasty.economy.core.aplication.useCase.currency.EditCurrencyUseCase;
import net.blockdynasty.economy.core.domain.entities.currency.ICurrency;
import net.blockdynasty.economy.libs.abstractions.platform.materials.Materials;
import net.blockdynasty.economy.libs.abstractions.platform.recipes.RecipeItem;
import net.blockdynasty.economy.gui.gui.GUIFactory;
import net.blockdynasty.economy.gui.gui.components.IEntityGUI;
import net.blockdynasty.economy.gui.gui.components.IGUI;
import net.blockdynasty.economy.gui.gui.components.IItemStack;
import net.blockdynasty.economy.gui.gui.components.factory.Item;
import net.blockdynasty.economy.gui.gui.components.generics.PaginatedPanel;

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
            editCurrencyUseCase.editMaterial(currency.getSingular(),material.name());
            entityGUI.sendMessage("Material updated successfully.");
            GUIFactory.editCurrencyPanel(entityGUI,currency, parent.getParent()).open();
        }catch (Exception e){
            entityGUI.sendMessage("Error");
            this.parent.open();
        }
    }
}
