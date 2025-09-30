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
import BlockDynasty.Economy.aplication.useCase.transaction.ExchangeUseCase;
import BlockDynasty.Economy.domain.entities.currency.Currency;
import BlockDynasty.Economy.domain.result.Result;
import lib.gui.abstractions.IEntityGUI;
import lib.gui.abstractions.ITextInput;
import lib.gui.abstractions.Materials;
import lib.gui.templates.abstractions.CurrenciesList;
import lib.util.colors.ChatColor;
import lib.util.colors.Colors;

import java.math.BigDecimal;

public class CurrencyListToExchangeSecond extends CurrenciesList {
    private final Currency currencyFrom;
    private final ExchangeUseCase exchangeUseCase;

    public CurrencyListToExchangeSecond(IEntityGUI player, SearchCurrencyUseCase searchCurrencyUseCase, ExchangeUseCase exchangeUseCase,
                                        Currency currencyFrom, CurrencyListToExchangeFirst parentGUI, ITextInput textInput) {
        super(player, searchCurrencyUseCase, parentGUI,currencyFrom, textInput);
        this.currencyFrom = currencyFrom;
        this.exchangeUseCase = exchangeUseCase;
    }

    @Override
    protected String execute(IEntityGUI sender, Currency currencyTo, BigDecimal amountTo){
        Result<BigDecimal> result= exchangeUseCase.execute(sender.getUniqueId(),currencyFrom.getSingular(),currencyTo.getSingular(),null, amountTo);
        if(result.isSuccess()){
            return "success";
        }else {
            return result.getErrorMessage();
        }
    }

    @Override
    public void addCustomButtons() {
        setItem(4, createItem(Materials.PAPER, ChatColor.stringValueOf(Colors.GREEN)+"Select Currency you want to receive",
                        ChatColor.stringValueOf(Colors.WHITE)+"Click to select the currency you want to receive", ChatColor.stringValueOf(Colors.WHITE)+"And before that, the amount"),
                null);

    }
}