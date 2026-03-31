/**
 * Copyright 2025 Federico Barrionuevo "@federkone"
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.blockdynasty.economy.gui.gui.components.generics;

import net.blockdynasty.economy.core.aplication.useCase.currency.SearchCurrencyUseCase;
import net.blockdynasty.economy.core.domain.entities.currency.ICurrency;
import net.blockdynasty.economy.gui.gui.components.IGUI;
import net.blockdynasty.economy.gui.gui.components.IItemStack;
import net.blockdynasty.economy.gui.gui.components.ITextInput;
import net.blockdynasty.economy.gui.gui.components.factory.Item;
import net.blockdynasty.economy.libs.abstractions.platform.recipes.RecipeItem;
import net.blockdynasty.economy.gui.gui.components.IEntityGUI;
import net.blockdynasty.economy.libs.util.colors.ChatColor;
import net.blockdynasty.economy.libs.services.messages.Message;
import net.blockdynasty.economy.libs.abstractions.platform.materials.Materials;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public abstract class CurrencySelectorAndAmount extends PaginatedPanel<ICurrency> {
    private final SearchCurrencyUseCase searchCurrencyUseCase;
    private final IEntityGUI player;
    private final ITextInput textInput;

    public CurrencySelectorAndAmount(IEntityGUI player, SearchCurrencyUseCase searchCurrencyUseCase, IGUI parentGUI, ITextInput textInput) {
        super(Message.process("CurrencySelector.title"), 5,player,parentGUI,21);
        this.searchCurrencyUseCase = searchCurrencyUseCase;
        this.player = player;
        this.textInput = textInput;
        showCurrencies();
    }

    public CurrencySelectorAndAmount(IEntityGUI player, SearchCurrencyUseCase searchCurrencyUseCase, IGUI parentGUI, ICurrency exceptCurrency, ITextInput textInput) {
        this(player, searchCurrencyUseCase, parentGUI, textInput);
        showCurrenciesExcluding(exceptCurrency);
    }

    private void showCurrenciesExcluding(ICurrency exceptCurrency) {
        List<ICurrency> currencies = searchCurrencyUseCase.getCurrencies().stream()
                .filter(c -> !c.equals(exceptCurrency))
                .collect(Collectors.toList());
        showItemsPage(currencies);
    }

    private void showCurrencies() {
        List<ICurrency> currencies = searchCurrencyUseCase.getCurrencies();
        showItemsPage(currencies);
    }

    @Override
    protected IItemStack createItemFor(ICurrency currency) {
        String color = ChatColor.stringValueOf(currency.getColor());
        return Item.of(RecipeItem.builder()
                .setBase64Item(currency.getBase64Item())
                .setMaterial(Materials.match(currency.getMaterial()))
                .setName(Message.process(Map.of("currency",color+currency.getSingular()),"CurrencySelector.button1.nameItem"))
                .setTexture(currency.getTexture())
                .setLore(Message.processLines(Map.of(
                        "singular",color + currency.getSingular(),
                        "plural",color + currency.getPlural(),
                        "transferable",(currency.isTransferable() ? "Yes" : "No"),
                        "exchangeRate",color+ currency.getExchangeRate()),"CurrencySelector.button1.lore"))
                .build());
    }

    @Override
    protected void functionLeftItemClick(ICurrency currency) {
        textInput.open(this,player,Message.process(Map.of("currency",currency.getSingular()),"CurrencySelector.button2.nameItem"),Message.process("CurrencySelector.button2.lore"), s->{
            try {
                BigDecimal amount = new BigDecimal(s);
                return execute(player, currency, amount);
            } catch (NumberFormatException e) {
                return "Invalid Format";
            }
        });
    }

    protected String execute(IEntityGUI sender, ICurrency currency, BigDecimal amount){return "execute not implement";};
}
