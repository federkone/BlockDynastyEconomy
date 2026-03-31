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

import net.blockdynasty.economy.core.aplication.useCase.currency.CreateCurrencyUseCase;
import net.blockdynasty.economy.core.aplication.useCase.currency.SearchCurrencyUseCase;
import net.blockdynasty.economy.core.domain.entities.currency.Exceptions.CurrencyException;
import net.blockdynasty.economy.core.domain.entities.currency.ICurrency;
import net.blockdynasty.economy.core.domain.result.Result;
import net.blockdynasty.economy.gui.gui.GUIFactory;
import net.blockdynasty.economy.gui.gui.components.IGUI;
import net.blockdynasty.economy.gui.gui.components.IEntityGUI;
import net.blockdynasty.economy.gui.gui.components.ITextInput;
import net.blockdynasty.economy.libs.util.colors.ChatColor;
import net.blockdynasty.economy.libs.util.colors.Colors;

public class CreateCurrencyGUI {
    private final IEntityGUI player;
    private final CreateCurrencyUseCase createCurrencyUseCase;
    private final SearchCurrencyUseCase searchCurrencyUseCase;
    private String singularName;
    private final IGUI parent;
    private final ITextInput textInput;
    public CreateCurrencyGUI(IEntityGUI player, CreateCurrencyUseCase createCurrencyUseCase, SearchCurrencyUseCase searchCurrencyUseCase, IGUI parent, ITextInput textInput) {
        this.parent = parent;
        this.player = player;
        this.textInput = textInput;
        this.createCurrencyUseCase = createCurrencyUseCase;
        this.searchCurrencyUseCase = searchCurrencyUseCase;
        openSingularNameInput();
    }

    private void openSingularNameInput() {
        textInput.open(parent,player,"Singular Name","Name..", s -> {
            singularName = s.trim();
            openPluralNameInput();
            return null;
        });
    }

    private void openPluralNameInput() {
        textInput.open(player,"Plural Name", "Name..", s -> {
            createCurrency(singularName, s.trim());
            return null;
        });
    }

    private void createCurrency(String singular, String plural) {
        try {
            createCurrencyUseCase.execute(singular, plural);
            Result<ICurrency> searchResult = searchCurrencyUseCase.getCurrency(singular);
            if (searchResult.isSuccess()) {
                player.sendMessage(ChatColor.stringValueOf(Colors.GREEN)+"[Bank] "+ChatColor.stringValueOf(Colors.GRAY)+"The currency " + singular + ChatColor.stringValueOf(Colors.GREEN)+" has been created successfully.");
                GUIFactory.editCurrencyPanel(player, searchResult.getValue(),parent).open();
            }
        }
        catch (CurrencyException e) {
            player.sendMessage(ChatColor.stringValueOf(Colors.GREEN)+"[Bank] "+ChatColor.stringValueOf(Colors.RED)+"Error creating currency: "+ChatColor.stringValueOf(Colors.YELLOW) + e.getMessage());
            GUIFactory.currencyPanel(player, parent).open();
        }
    }
}
