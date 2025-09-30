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

package lib.gui.templates.users;

import BlockDynasty.Economy.aplication.useCase.currency.SearchCurrencyUseCase;
import BlockDynasty.Economy.aplication.useCase.offer.CreateOfferUseCase;
import BlockDynasty.Economy.domain.entities.currency.Currency;
import lib.gui.abstractions.IGUI;
import lib.gui.abstractions.IEntityGUI;
import lib.gui.abstractions.ITextInput;
import lib.gui.abstractions.Materials;
import lib.gui.templates.abstractions.CurrenciesList;
import lib.util.colors.ChatColor;
import lib.util.colors.Colors;

import java.math.BigDecimal;

public class CurrencyListToOfferFirst extends CurrenciesList {
    private final BlockDynasty.Economy.domain.entities.account.Player target;
    private final SearchCurrencyUseCase searchCurrencyUseCase;
    protected final CreateOfferUseCase createOfferUseCase;
    private Currency currency;
    private BigDecimal amount;
    private final ITextInput textInput;

    public CurrencyListToOfferFirst(IEntityGUI player, BlockDynasty.Economy.domain.entities.account.Player target,
                                    SearchCurrencyUseCase searchCurrencyUseCase, CreateOfferUseCase createOfferUseCase, IGUI parentGUI, ITextInput textInput) {
        super(player, searchCurrencyUseCase, parentGUI, textInput);
        this.target = target;
        this.textInput = textInput;
        this.searchCurrencyUseCase = searchCurrencyUseCase;
        this.createOfferUseCase = createOfferUseCase;
    }

    protected Currency getCurrency() {
        return currency;
    }

    protected BigDecimal getAmount() {
        return amount;
    }

    @Override
    protected String execute(IEntityGUI sender, Currency currency, java.math.BigDecimal amount) {
        this.currency = currency;
        this.amount = amount;
        new CurrencyListToOfferSecond(sender, target, searchCurrencyUseCase,createOfferUseCase, this,textInput).open();
        return null;
    }

    @Override
    public void addCustomButtons() {
        setItem(4, createItem(Materials.PAPER, ChatColor.stringValueOf(Colors.GREEN)+"Select Currency to Offer",
                        ChatColor.stringValueOf(Colors.WHITE)+"Click to select the currency you want to offer", ChatColor.stringValueOf(Colors.WHITE)+"And before that, the amount"),
                null);

    }
}