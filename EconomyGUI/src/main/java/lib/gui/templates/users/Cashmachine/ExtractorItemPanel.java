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
package lib.gui.templates.users.Cashmachine;

import BlockDynasty.Economy.aplication.useCase.currency.SearchCurrencyUseCase;
import BlockDynasty.Economy.domain.entities.currency.ICurrency;
import aplication.useCase.HardCashUseCaseFactory;
import aplication.useCase.items.withdraw.IExtractItemUseCase;
import lib.gui.components.IEntityGUI;
import lib.gui.components.IGUI;
import lib.gui.components.ITextInput;
import lib.gui.components.generics.CurrencySelectorAndAmount;

import java.math.BigDecimal;

public class ExtractorItemPanel extends CurrencySelectorAndAmount {
    private IExtractItemUseCase extractItemUseCase;

    public ExtractorItemPanel(IEntityGUI player, SearchCurrencyUseCase searchCurrencyUseCase, IGUI parentGUI, ITextInput textInput) {
        super(player, searchCurrencyUseCase, parentGUI, textInput);
        this.extractItemUseCase = HardCashUseCaseFactory.getExtractItemUseCase();
    }

    @Override
    public String execute(IEntityGUI sender, ICurrency currency, BigDecimal amount){
        extractItemUseCase.execute(sender.asEntityHardCash(),amount, currency.getSingular());
        return "";
    }
}
