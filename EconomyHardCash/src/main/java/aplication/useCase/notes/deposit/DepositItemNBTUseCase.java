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

package aplication.useCase.notes.deposit;

import BlockDynasty.Economy.aplication.useCase.currency.SearchCurrencyUseCase;
import BlockDynasty.Economy.aplication.useCase.transaction.interfaces.IDepositUseCase;
import BlockDynasty.Economy.domain.entities.currency.ICurrency;
import BlockDynasty.Economy.domain.events.Context;
import BlockDynasty.Economy.domain.result.Result;
import abstractions.platform.scheduler.ContextualTask;
import abstractions.platform.scheduler.IScheduler;
import domain.entity.currency.ItemStackCurrency;
import domain.entity.currency.NbtData;
import domain.entity.platform.HardCashCreator;
import domain.entity.player.IEntityHardCash;

import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class DepositItemNBTUseCase implements IDepositItemNBTUseCase {
    private HardCashCreator platformHardCash;
    private IDepositUseCase depositUseCase;
    private SearchCurrencyUseCase searchCurrencyUseCase;
    private IScheduler scheduler;
    private static final Set<UUID> activeTransactions = ConcurrentHashMap.newKeySet();

    public DepositItemNBTUseCase(HardCashCreator platformHardCash, IDepositUseCase depositUseCase, SearchCurrencyUseCase searchCurrencyUseCase) {
        this.platformHardCash = platformHardCash;
        this.depositUseCase = depositUseCase;
        this.scheduler = platformHardCash.getScheduler();
        this.searchCurrencyUseCase = searchCurrencyUseCase;
    }

    public void execute(IEntityHardCash player) {
        if (!activeTransactions.add(player.getUniqueId())) {
            player.sendMessage("You already have a transaction in progress. Please wait.");
            return;
        }

        Runnable r = ()-> {
            try {
                ItemStackCurrency item = player.takeHandItem();
                if(item == null){
                    player.sendMessage("You must hold a Note item to deposit.");
                    return;
                }
                NbtData nbtData = item.getNbtData();
                if (nbtData.getItemType() == null || nbtData.getUuidCurrency() == null) {
                    player.sendMessage("Not have a valid Note item in hand.");
                    return;
                }
                Result<ICurrency> result = searchCurrencyUseCase.getCurrency(nbtData.getItemType());
                if (!result.isSuccess()) {
                    player.sendMessage("Currency type not found: " + nbtData.getItemType());
                    return;
                }
                ICurrency currency = result.getValue();
                if(!currency.getUuid().equals(UUID.fromString(nbtData.getUuidCurrency()))) {
                    player.sendMessage("Not have a valid Note item in hand.");
                    return;
                }
                player.removeItem(item);

                scheduler.runAsync(ContextualTask.build(()->{
                    BigDecimal value = new BigDecimal(nbtData.getValue());
                    BigDecimal cant = new BigDecimal(item.getCantity());
                    value = value.multiply(cant);
                    Result<Void> depositResult = depositUseCase.execute(player.getUniqueId(),currency.getSingular(), value, Context.COMMAND);
                    if (!depositResult.isSuccess()) {
                        scheduler.run(ContextualTask.build(()->{ player.giveItem(item);},player));
                    }
                },player));

            }finally {
                activeTransactions.remove(player.getUniqueId());
            }

        };

        ContextualTask contextualTask = ContextualTask.build(r, player);
        scheduler.run(contextualTask);
    }
}

