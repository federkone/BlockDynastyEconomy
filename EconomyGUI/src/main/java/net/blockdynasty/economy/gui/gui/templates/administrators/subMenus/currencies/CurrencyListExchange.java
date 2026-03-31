package net.blockdynasty.economy.gui.gui.templates.administrators.subMenus.currencies;

import net.blockdynasty.economy.core.aplication.useCase.currency.EditCurrencyUseCase;
import net.blockdynasty.economy.core.domain.entities.currency.ICurrency;
import net.blockdynasty.economy.gui.gui.GUIFactory;
import net.blockdynasty.economy.gui.gui.components.IEntityGUI;
import net.blockdynasty.economy.gui.gui.components.IGUI;
import net.blockdynasty.economy.gui.gui.components.IItemStack;
import net.blockdynasty.economy.gui.gui.components.factory.Item;
import net.blockdynasty.economy.gui.gui.components.generics.Button;
import net.blockdynasty.economy.libs.abstractions.platform.recipes.RecipeItem;
import net.blockdynasty.economy.gui.gui.components.generics.PaginatedPanel;
import net.blockdynasty.economy.libs.util.colors.ChatColor;
import net.blockdynasty.economy.libs.services.messages.Message;
import net.blockdynasty.economy.libs.abstractions.platform.materials.Materials;

import java.util.Map;

public class CurrencyListExchange extends PaginatedPanel<ICurrency> {
    private final IEntityGUI player;
    private final EditCurrencyUseCase editCurrencyUseCase;
    private final ICurrency currency;

    public CurrencyListExchange(IEntityGUI player, EditCurrencyUseCase editCurrencyUseCase, IGUI abstractGUI, ICurrency currency) {
        super("Whitelist Exchange", 5, player, abstractGUI, 21);
        this.currency = currency;
        this.editCurrencyUseCase = editCurrencyUseCase;
        this.player = player;

        showItemsPage(currency.getInterchangeableCurrencies());
    }

    @Override
    protected IItemStack createItemFor(ICurrency currency) {
        String color = ChatColor.stringValueOf(currency.getColor());
        return Item.of(RecipeItem.builder().setMaterial(Materials.match(currency.getMaterial()))
                .setName(Message.process(Map.of("currency",color+currency.getSingular()),"CurrencySelector.button1.nameItem"))
                .setLore("Click to remove")
                .setTexture(currency.getTexture())
                .build());
    }

    @Override
    protected void functionLeftItemClick(ICurrency currency) {
        try {
            editCurrencyUseCase.removeInterchangeableCurrency(this.currency.getSingular(),currency.getSingular());
            player.sendMessage("Successfully removed exchange currency.");
            GUIFactory.currencyListExchange(player,this.currency,this.parent).open();
        }catch (Exception e){
            player.sendMessage("Error");
        }

    }

    @Override
    public void addCustomButtons(){
        setButton(39, Button.builder()
                .setItemStack(Item.of(RecipeItem.builder().setMaterial(Materials.NAME_TAG).setName("Add currency").build()))
                .setLeftClickAction(unused -> {GUIFactory.currencyListToAddExchange(player,currency,this).open();})
                .build());
    }
}
