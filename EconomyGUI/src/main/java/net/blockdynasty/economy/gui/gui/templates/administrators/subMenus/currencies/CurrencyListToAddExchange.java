package net.blockdynasty.economy.gui.gui.templates.administrators.subMenus.currencies;

import net.blockdynasty.economy.core.aplication.useCase.currency.EditCurrencyUseCase;
import net.blockdynasty.economy.core.aplication.useCase.currency.SearchCurrencyUseCase;
import net.blockdynasty.economy.core.domain.entities.currency.ICurrency;
import net.blockdynasty.economy.gui.gui.GUIFactory;
import net.blockdynasty.economy.gui.gui.components.IEntityGUI;
import net.blockdynasty.economy.gui.gui.components.IGUI;
import net.blockdynasty.economy.gui.gui.components.IItemStack;
import net.blockdynasty.economy.gui.gui.components.factory.Item;
import net.blockdynasty.economy.libs.abstractions.platform.recipes.RecipeItem;
import net.blockdynasty.economy.libs.abstractions.platform.materials.Materials;
import net.blockdynasty.economy.gui.gui.components.generics.PaginatedPanel;
import net.blockdynasty.economy.libs.util.colors.ChatColor;
import net.blockdynasty.economy.libs.services.messages.Message;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CurrencyListToAddExchange extends PaginatedPanel<ICurrency> {
    private final IEntityGUI player;
    private final EditCurrencyUseCase editCurrencyUseCase;
    private final SearchCurrencyUseCase searchCurrencyUseCase;
    private final ICurrency currency;

    public CurrencyListToAddExchange(IEntityGUI player, SearchCurrencyUseCase searchCurrencyUseCase, EditCurrencyUseCase editCurrencyUseCase, IGUI abstractGUI, ICurrency currency) {
        super("Currency List", 5, player, abstractGUI, 21);
        this.currency = currency;
        this.searchCurrencyUseCase = searchCurrencyUseCase;
        this.editCurrencyUseCase = editCurrencyUseCase;
        this.player = player;

        List<ICurrency> currencies = new ArrayList<>(searchCurrencyUseCase.getCurrencies());
        currencies = currencies.stream().filter( c -> !c.getSingular().equalsIgnoreCase(currency.getSingular())).collect(Collectors.toList());
        showItemsPage(currencies);
    }

    @Override
    protected IItemStack createItemFor(ICurrency currency) {
        String color = ChatColor.stringValueOf(currency.getColor());
        return Item.of(RecipeItem.builder().setMaterial(Materials.match(currency.getMaterial()))
                .setName(Message.process(Map.of("currency",color+currency.getSingular()),"CurrencySelector.button1.nameItem"))
                .setLore("Click to add")
                .setTexture(currency.getTexture())
                .build());
    }

    @Override
    protected void functionLeftItemClick(ICurrency currency) {
        try {
            editCurrencyUseCase.addInterchangeableCurrency(this.currency.getSingular(),currency.getSingular());
            player.sendMessage("Successfully added exchange currency.");
            GUIFactory.currencyListExchange(player, this.currency,this.getParent().getParent()).open();
        }catch (Exception e){
            player.sendMessage("Error");
        }
    }
}