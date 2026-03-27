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

package aplication.useCase.notes.withdraw;

import BlockDynasty.Economy.aplication.useCase.currency.SearchCurrencyUseCase;
import BlockDynasty.Economy.aplication.useCase.transaction.interfaces.IWithdrawUseCase;
import BlockDynasty.Economy.domain.entities.currency.ICurrency;
import BlockDynasty.Economy.domain.events.Context;
import BlockDynasty.Economy.domain.result.Result;
import abstractions.platform.scheduler.ContextualTask;
import abstractions.platform.scheduler.IScheduler;
import domain.entity.currency.ItemStackCurrency;
import domain.entity.platform.HardCashCreator;
import domain.entity.player.IEntityHardCash;
import domain.service.ItemCreator;
import aplication.useCase.notes.service.ItemCreatorFactory;

import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class ExtractItemNBTUseCase implements IExtractItemNBTUseCase {
    private HardCashCreator platform;
    private IWithdrawUseCase withdrawUseCase;
    private SearchCurrencyUseCase searchCurrencyUseCase;
    private ItemCreator itemCreator;
    private IScheduler scheduler;
    private static final Set<UUID> activeTransactions = ConcurrentHashMap.newKeySet();

    public ExtractItemNBTUseCase(HardCashCreator platform, IWithdrawUseCase withdrawUseCase, SearchCurrencyUseCase searchCurrencyUseCase) {
        this.platform = platform;
        this.searchCurrencyUseCase = searchCurrencyUseCase;
        this.withdrawUseCase = withdrawUseCase;
        this.scheduler = platform.getScheduler();
        this.itemCreator = ItemCreatorFactory.getItemCreator(platform);
    }

    public void execute(IEntityHardCash player, BigDecimal amount, String currency){
        if (!activeTransactions.add(player.getUniqueId())) {
            player.sendMessage("You already have a transaction in progress. Please wait.");
            return;
        }

        Runnable r =()->{
            try {
                Result<ICurrency> currencyResult = searchCurrencyUseCase.getCurrency(currency);
                if (!currencyResult.isSuccess()) {
                    player.sendMessage("Error."+ currencyResult.getErrorMessage());
                    return;
                }
                ICurrency currencyData = currencyResult.getValue();
                ItemStackCurrency item = itemCreator.create(currencyData, amount);

                int emptySlots = player.emptySlots();
                int maxWithdrawable = emptySlots * item.maxStackSize();

                if (maxWithdrawable <= 0) {
                    player.sendMessage("Error. Not enough space in inventory.");
                    return;
                }

                Result<Void> withdrawResult = withdrawUseCase.execute(
                        player.getUniqueId(),
                        currency,
                        amount,
                        Context.COMMAND);

                if (!withdrawResult.isSuccess()) {
                    player.sendMessage("Error."+ withdrawResult.getErrorMessage());
                    return;
                }

                scheduler.run(ContextualTask.build(()->{player.giveItem(item);},player));
            }finally {
                activeTransactions.remove(player.getUniqueId());
            }
        };
        ContextualTask task = ContextualTask.build(r,player);
        scheduler.runAsync(task);
    }
}
