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

package aplication.useCase.notes.give;

import BlockDynasty.Economy.aplication.useCase.currency.SearchCurrencyUseCase;
import BlockDynasty.Economy.domain.entities.currency.ICurrency;
import BlockDynasty.Economy.domain.result.Result;
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

public class GiveItemNBTUseCase implements IGiveItemNBTUseCase {
    private SearchCurrencyUseCase searchCurrencyUseCase;
    private HardCashCreator hardCashCreator;
    private ItemCreator itemCreator;


    public GiveItemNBTUseCase(SearchCurrencyUseCase searchCurrencyUseCase, HardCashCreator platform) {
        this.searchCurrencyUseCase = searchCurrencyUseCase;
        this.hardCashCreator= platform;
        this.itemCreator = ItemCreatorFactory.getItemCreator(platform);
    }

    @Override
    public boolean execute(String playerName, BigDecimal amount, String currency) {
        Result<ICurrency> currencyResult = searchCurrencyUseCase.getCurrency(currency);
        if (!currencyResult.isSuccess()) {
            return false;
        }
        ICurrency currencyData = currencyResult.getValue();
        ItemStackCurrency item = itemCreator.create(currencyData, amount);

        IEntityHardCash player =hardCashCreator.getPlayer(playerName);

        if (player != null) {
            if (player.hasItem(item) || player.hasEmptySlot()){
                player.giveItem(item);
                player.sendMessage("You have received " + amount + " " + currency + " in items.");
                return true;
            }
        }
        return false;
    }
}
