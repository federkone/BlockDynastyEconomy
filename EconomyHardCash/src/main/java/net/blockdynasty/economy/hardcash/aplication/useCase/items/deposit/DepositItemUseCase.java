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

package net.blockdynasty.economy.hardcash.aplication.useCase.items.deposit;

import net.blockdynasty.economy.core.aplication.useCase.currency.SearchCurrencyUseCase;
import net.blockdynasty.economy.core.aplication.useCase.transaction.interfaces.IDepositUseCase;
import net.blockdynasty.economy.core.domain.entities.currency.ICurrency;
import net.blockdynasty.economy.core.domain.events.Context;
import net.blockdynasty.economy.core.domain.result.Result;
import net.blockdynasty.economy.libs.abstractions.platform.scheduler.ContextualTask;
import net.blockdynasty.economy.libs.abstractions.platform.scheduler.IScheduler;
import net.blockdynasty.economy.hardcash.aplication.listener.ItemNoteValidator;
import net.blockdynasty.economy.hardcash.domain.entity.currency.ItemStackCurrency;
import net.blockdynasty.economy.hardcash.domain.entity.platform.HardCashCreator;
import net.blockdynasty.economy.hardcash.domain.entity.player.IEntityHardCash;
import net.blockdynasty.economy.hardcash.aplication.useCase.items.service.CacheCurrencyItems;

import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class DepositItemUseCase implements IDepositItemUseCase {
    private HardCashCreator platformHardCash;
    private IDepositUseCase depositUseCase;
    private SearchCurrencyUseCase searchCurrencyUseCase;
    private CacheCurrencyItems cacheCurrencyItems;
    private IScheduler scheduler;

    private static final Set<UUID> activeTransactions = ConcurrentHashMap.newKeySet();

    public DepositItemUseCase(HardCashCreator platformHardCash, IDepositUseCase depositUseCase, SearchCurrencyUseCase searchCurrencyUseCase,CacheCurrencyItems cacheCurrencyItems) {
        this.platformHardCash = platformHardCash;
        this.scheduler = platformHardCash.getScheduler();
        this.depositUseCase = depositUseCase;
        this.searchCurrencyUseCase = searchCurrencyUseCase;
        this.cacheCurrencyItems = cacheCurrencyItems;
    }

    @Override
    public void execute(IEntityHardCash player) {
        if (!activeTransactions.add(player.getUniqueId())) {
            player.sendMessage("You already have a transaction in progress. Please wait.");
            return;
        }

        Runnable task = () -> {
            try{
                ItemStackCurrency item = player.takeHandItem();
                if(item.isNull()){
                    player.sendMessage("You must hold a currency item to deposit.");
                    return;
                }

                CacheCurrencyItems.Currencywrapper currencywrapper = cacheCurrencyItems.getSimilarItem(item);
                if (currencywrapper == null) {
                    player.sendMessage("Not have a valid currency item in hand.");
                    return;
                }

                if (ItemNoteValidator.isANoteCurrency(item)) {
                    player.sendMessage("Not have a valid currency item in hand.");
                    return;
                }

                ICurrency currency = currencywrapper.getCurrency();
                if(!currency.isPhysicalItemSupported()){
                    player.sendMessage("This currency does not support physical items.");
                    return;
                }

                BigDecimal cant = new BigDecimal(item.getCantity());
                player.removeItem(item);

                scheduler.runAsync(ContextualTask.build(()->{
                    Result<Void> depositResult = depositUseCase.execute(player.getUniqueId(),currency.getSingular(), cant, Context.COMMAND);
                    if (!depositResult.isSuccess()) {
                        scheduler.run(ContextualTask.build(() -> {
                            player.giveItem(item);
                        },player));
                    }
                },player));
            }finally {
                activeTransactions.remove(player.getUniqueId());
            }
        };
        ContextualTask contextualTask = ContextualTask.build(task,player);
        scheduler.run(contextualTask);
    }
}
