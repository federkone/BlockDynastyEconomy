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

package lib.gui.templates.users.Exchange;

import BlockDynasty.Economy.aplication.useCase.currency.SearchCurrencyUseCase;
import BlockDynasty.Economy.aplication.useCase.transaction.ExchangeUseCase;
import BlockDynasty.Economy.domain.entities.currency.Currency;
import lib.gui.components.IGUI;
import lib.gui.components.IEntityGUI;
import lib.gui.components.ITextInput;
import lib.gui.components.Materials;
import lib.gui.components.abstractions.CurrencySelectorAndAmount;
import lib.util.colors.ChatColor;
import lib.util.colors.Colors;

public class ExchangeFirstPanel extends CurrencySelectorAndAmount {
    private final IEntityGUI player;
    private final SearchCurrencyUseCase searchCurrencyUseCase;
    private final ExchangeUseCase exchangeUseCase;
    private final ITextInput textInput;

    public ExchangeFirstPanel(IEntityGUI player, SearchCurrencyUseCase searchCurrencyUseCase, ExchangeUseCase exchangeUseCase, IGUI parentGUI , ITextInput textInput) {
        super(player, searchCurrencyUseCase, parentGUI,textInput);
        this.player = player;
        this.textInput = textInput;
        this.searchCurrencyUseCase = searchCurrencyUseCase;
        this.exchangeUseCase = exchangeUseCase;
    }

    @Override
    protected void functionLeftItemClick(Currency currency) {
        new ExchangeSecondPanel(player, searchCurrencyUseCase, exchangeUseCase, currency, this,textInput).open();
    }

    @Override
    public void addCustomButtons() {
        setItem(4, createItem(Materials.PAPER, ChatColor.stringValueOf(Colors.GREEN)+"Select Currency you want to give",
                        ChatColor.stringValueOf(Colors.WHITE)+"Click to select the currency you want to give"),
                null);

    }
}