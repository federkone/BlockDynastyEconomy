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

package aplication.useCase.items;

import BlockDynasty.Economy.aplication.useCase.currency.SearchCurrencyUseCase;
import BlockDynasty.Economy.aplication.useCase.transaction.interfaces.IWithdrawUseCase;
import BlockDynasty.Economy.domain.entities.currency.ICurrency;
import BlockDynasty.Economy.domain.events.Context;
import BlockDynasty.Economy.domain.result.Result;
import domain.entity.platform.HardCashCreator;
import domain.entity.player.IEntityHardCash;
import domain.service.ItemCreator;

import java.math.BigDecimal;

public class ExtractItemUseCase implements IExtractItemUseCase{
    private HardCashCreator platform;
    private IWithdrawUseCase withdrawUseCase;
    private SearchCurrencyUseCase searchCurrencyUseCase;
    private ItemCreator itemCreator;

    public ExtractItemUseCase(HardCashCreator platform, IWithdrawUseCase withdrawUseCase, SearchCurrencyUseCase searchCurrencyUseCase) {
        this.platform = platform;
        this.searchCurrencyUseCase = searchCurrencyUseCase;
        this.withdrawUseCase = withdrawUseCase;
        this.itemCreator = new ItemBaseCreator(platform);
    }

    @Override
    public void execute(IEntityHardCash player, BigDecimal amount, String currencyName) {
        if (amount.stripTrailingZeros().scale() > 0) {
            player.sendMessage("Amount must be an integer.");
            return;
        }
        Result<ICurrency> currencyResult = searchCurrencyUseCase.getCurrency(currencyName);
        if (!currencyResult.isSuccess()) {
            player.sendMessage("Currency not found.");
            return;
        }

        ICurrency currency = currencyResult.getValue();
        if (currency.getMaterial() == null || currency.getMaterial().isEmpty()) {
            player.sendMessage("Currency does not have a valid material.");
            return;
        }

        int emptySlots = player.emptySlots();
        int maxWithdrawable = emptySlots * 64;

        if (maxWithdrawable <= 0) {
            player.sendMessage("Error. Not enough space in inventory.");
            return;
        }

        BigDecimal amountToWithdraw = amount.compareTo(BigDecimal.valueOf(maxWithdrawable)) > 0
                ? BigDecimal.valueOf(maxWithdrawable)
                : amount;

        Result<Void> withdrawResult = withdrawUseCase.execute(
                player.getUniqueId(),
                currencyName,
                amountToWithdraw,
                Context.COMMAND
        );

        if (!withdrawResult.isSuccess()) {
            player.sendMessage("Error. " + withdrawResult.getErrorMessage());
            return;
        }

        var item = itemCreator.create(currency, amountToWithdraw);
        item.setCantity(amountToWithdraw.intValue());
        player.giveItem(item);

        if (amount.compareTo(amountToWithdraw) > 0) {
            BigDecimal remaining = amount.subtract(amountToWithdraw);
            player.sendMessage("You have received " + amountToWithdraw + " " + currencyName
                    + " in items. You still have " + remaining + " " + currencyName
                    + " to withdraw. Please complete the transaction later.");
        } else {
            player.sendMessage("You have received " + amountToWithdraw + " " + currencyName + " in items.");
        }
    }
}
