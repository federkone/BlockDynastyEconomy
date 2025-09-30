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

package lib.gui.templates.administrators.subMenus.accounts;

import BlockDynasty.Economy.aplication.useCase.currency.SearchCurrencyUseCase;
import BlockDynasty.Economy.aplication.useCase.transaction.WithdrawUseCase;
import BlockDynasty.Economy.domain.entities.currency.Currency;
import BlockDynasty.Economy.domain.events.Context;
import BlockDynasty.Economy.domain.result.Result;
import lib.gui.abstractions.IGUI;
import lib.gui.abstractions.IEntityGUI;
import lib.gui.abstractions.ITextInput;
import lib.gui.templates.abstractions.CurrenciesList;
import lib.util.colors.ChatColor;

import java.math.BigDecimal;

public class CurrencyListToWithdraw extends CurrenciesList {
    private final WithdrawUseCase withdrawUseCase;
    private final BlockDynasty.Economy.domain.entities.account.Player targetPlayer;

    public CurrencyListToWithdraw(IEntityGUI player, BlockDynasty.Economy.domain.entities.account.Player targetPlayer,
                                  SearchCurrencyUseCase searchCurrencyUseCase, WithdrawUseCase withdrawUseCase,
                                  IGUI parentGUI, ITextInput textInput) {
        super( player, searchCurrencyUseCase, parentGUI,textInput);
        this.targetPlayer = targetPlayer;
        this.withdrawUseCase = withdrawUseCase;
        //this.messageService = BlockDynastyEconomy.getInstance().getMessageService();
    }

    @Override
    public String execute(IEntityGUI sender, Currency currency, BigDecimal amount) {
        Result<Void> result = withdrawUseCase.execute(targetPlayer.getUuid(), currency.getSingular(), amount, Context.COMMAND);
        if (result.isSuccess()) {
            sender.sendMessage("Success withdraw " + ChatColor.stringValueOf(currency.getColor()) + currency.format(amount) + " from " + targetPlayer.getNickname() + "'s account.");
            this.openParent();
            return null;
        } else {
            return result.getErrorMessage();
        }
    }
}