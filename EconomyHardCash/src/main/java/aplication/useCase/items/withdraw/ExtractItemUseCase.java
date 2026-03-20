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

package aplication.useCase.items.withdraw;

import BlockDynasty.Economy.aplication.useCase.transaction.interfaces.IWithdrawUseCase;
import BlockDynasty.Economy.domain.entities.currency.ICurrency;
import BlockDynasty.Economy.domain.events.Context;
import BlockDynasty.Economy.domain.result.Result;
import aplication.useCase.items.service.ItemBase64Creator;
import domain.entity.platform.HardCashCreator;
import domain.entity.player.IEntityHardCash;
import aplication.useCase.items.service.CacheCurrencyItems;
import domain.service.ItemCreator;

import java.math.BigDecimal;

public class ExtractItemUseCase implements IExtractItemUseCase {
    private HardCashCreator platform;
    private IWithdrawUseCase withdrawUseCase;
    private ItemCreator itemCreator;
    private CacheCurrencyItems cacheCurrencyItems;

    public ExtractItemUseCase(HardCashCreator platform, IWithdrawUseCase withdrawUseCase, CacheCurrencyItems cacheCurrencyItems) {
        this.platform = platform;
        this.withdrawUseCase = withdrawUseCase;
        this.cacheCurrencyItems = cacheCurrencyItems;
        this.itemCreator = new ItemBase64Creator(platform);
    }

    @Override
    public void execute(IEntityHardCash player, BigDecimal amount, ICurrency currency) {
        if (amount.stripTrailingZeros().scale() > 0) {
            player.sendMessage("Amount must be an integer.");
            return;
        }

        if (!currency.isPhysicalItemSupported()){
            player.sendMessage("This currency does not support physical item withdrawal.");
            return;
        }
        if (currency.getBase64Item() == null || currency.getBase64Item().isEmpty()) {
            player.sendMessage("Currency does not have a valid material.");
            return;
        }

        var item = itemCreator.create(currency);
        if (item.isNull()){
            player.sendMessage("Currency does not have a valid item representation.");
            return;
        }

        CacheCurrencyItems.Currencywrapper currencywrapper = cacheCurrencyItems.getSimilarItem(item);
        if (currencywrapper == null) {
            player.sendMessage("Currency not have valid item.");
            return;
        }

        int emptySlots = player.emptySlots();
        int maxWithdrawable = emptySlots * item.maxStackSize();

        if (maxWithdrawable <= 0) {
            player.sendMessage("Error. Not enough space in inventory.");
            return;
        }

        BigDecimal amountToWithdraw = amount.compareTo(BigDecimal.valueOf(maxWithdrawable)) > 0
                ? BigDecimal.valueOf(maxWithdrawable)
                : amount;

        Result<Void> withdrawResult = withdrawUseCase.execute(
                player.getUniqueId(),
                currency.getSingular(),
                amountToWithdraw,
                Context.COMMAND
        );

        if (!withdrawResult.isSuccess()) {
            player.sendMessage("Error. " + withdrawResult.getErrorMessage());
            return;
        }


        item.setCantity(amountToWithdraw.intValue());
        player.giveItem(item);

        if (amount.compareTo(amountToWithdraw) > 0) {
            BigDecimal remaining = amount.subtract(amountToWithdraw);
            player.sendMessage("You have received " + amountToWithdraw + " " + currency.getSingular()
                    + " in items. You still have " + remaining + " " + currency.getSingular()
                    + " to withdraw. Please complete the transaction later.");
        } else {
            player.sendMessage("You have received " + amountToWithdraw + " " + currency.getSingular() + " in items.");
        }
    }
}
