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

package net.blockdynasty.economy.gui.gui.templates.users.Offers;

import net.blockdynasty.economy.core.aplication.useCase.currency.SearchCurrencyUseCase;
import net.blockdynasty.economy.core.aplication.useCase.offer.CreateOfferUseCase;
import net.blockdynasty.economy.core.domain.entities.account.Player;
import net.blockdynasty.economy.core.domain.entities.currency.ICurrency;
import net.blockdynasty.economy.core.domain.result.Result;
import net.blockdynasty.economy.gui.gui.GUIFactory;
import net.blockdynasty.economy.gui.gui.components.IEntityGUI;
import net.blockdynasty.economy.gui.gui.components.ITextInput;
import net.blockdynasty.economy.gui.gui.components.factory.Item;
import net.blockdynasty.economy.gui.gui.components.generics.Button;
import net.blockdynasty.economy.libs.abstractions.platform.recipes.RecipeItem;
import net.blockdynasty.economy.libs.abstractions.platform.materials.Materials;
import net.blockdynasty.economy.gui.gui.components.generics.CurrencySelectorAndAmount;
import net.blockdynasty.economy.libs.util.colors.ChatColor;
import net.blockdynasty.economy.libs.util.colors.Colors;
import net.blockdynasty.economy.libs.services.messages.Message;

import java.util.Map;

public class CreateOfferSecondPanel extends CurrencySelectorAndAmount {
    private final Player target;
    private final CreateOfferUseCase createOfferUseCase;
    private final CreateOfferFirstPanel parentGUI;

    public CreateOfferSecondPanel(IEntityGUI player, Player target, SearchCurrencyUseCase searchCurrencyUseCase,
                                  CreateOfferUseCase createOfferUseCase, CreateOfferFirstPanel parentGUI, ITextInput textInput) {
        super(player, searchCurrencyUseCase, parentGUI,parentGUI.getCurrency(), textInput);
        this.createOfferUseCase = createOfferUseCase;
        this.target = target;
        this.parentGUI = parentGUI;
    }

    @Override
    protected String  execute(IEntityGUI sender, ICurrency currency, java.math.BigDecimal amount){
        //can be Player
        Result<Void> result= createOfferUseCase.execute(
                sender.getUniqueId(),
                target.getUuid(),
                parentGUI.getCurrency().getPlural(),
                parentGUI.getAmount(),
                currency.getSingular(),
                amount
        );

        if (result.isSuccess()){
            GUIFactory.myActiveOffers(sender).open();

        }else{
            sender.sendMessage(ChatColor.stringValueOf(Colors.RED)+"Error: " + result.getErrorMessage()+ "."+result.getErrorCode());
        }

        return null;
    }

    @Override
    public void addCustomButtons() {
        setButton(4, Button.builder()
                .setItemStack(Item.of(RecipeItem.builder()
                        .setMaterial(Materials.PAPER)
                        .setName(Message.process(Map.of("color",ChatColor.stringValueOf(Colors.GREEN)),"OfferListPlayer.button3.nameItem"))
                        .setLore(Message.processLines(Map.of("color",ChatColor.stringValueOf(Colors.WHITE)),"OfferListPlayer.button3.lore"))
                        .build()))
                .build());
    }
}
