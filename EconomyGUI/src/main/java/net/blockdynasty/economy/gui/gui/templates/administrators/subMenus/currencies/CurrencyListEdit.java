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

package net.blockdynasty.economy.gui.gui.templates.administrators.subMenus.currencies;

import net.blockdynasty.economy.core.aplication.useCase.currency.SearchCurrencyUseCase;
import net.blockdynasty.economy.core.domain.entities.currency.ICurrency;
import net.blockdynasty.economy.gui.gui.GUIFactory;
import net.blockdynasty.economy.gui.gui.components.IGUI;
import net.blockdynasty.economy.gui.gui.components.IEntityGUI;
import net.blockdynasty.economy.gui.gui.components.ITextInput;
import net.blockdynasty.economy.gui.gui.components.generics.CurrencySelectorAndAmount;

public class CurrencyListEdit extends CurrencySelectorAndAmount {
    private final IEntityGUI player;
    public CurrencyListEdit(IEntityGUI player, SearchCurrencyUseCase searchCurrencyUseCase, IGUI abstractGUI, ITextInput textInput) {
        super( player, searchCurrencyUseCase,abstractGUI,textInput);
        this.player = player;
    }

    @Override
    public void functionLeftItemClick(ICurrency currency) {
        GUIFactory.editCurrencyPanel(player, currency, this).open();
    }

}
