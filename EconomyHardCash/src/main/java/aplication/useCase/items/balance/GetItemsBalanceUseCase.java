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

package aplication.useCase.items.balance;

import BlockDynasty.Economy.aplication.useCase.currency.SearchCurrencyUseCase;
import BlockDynasty.Economy.domain.entities.currency.ICurrency;
import aplication.useCase.items.ItemBaseCreator;
import domain.entity.currency.ItemStackCurrency;
import domain.entity.platform.HardCashCreator;
import domain.entity.player.IEntityHardCash;
import domain.service.ItemCreator;

import java.math.BigDecimal;

public class GetItemsBalanceUseCase implements IGetItemsBalanceUseCase {
    private SearchCurrencyUseCase searchCurrencyUseCase;
    private HardCashCreator platform;
    private ItemCreator itemCreator;

    public GetItemsBalanceUseCase(HardCashCreator platform, SearchCurrencyUseCase searchCurrencyUseCase) {
        this.platform = platform;
        this.searchCurrencyUseCase = searchCurrencyUseCase;
        this.itemCreator = new ItemBaseCreator(platform);
    }

    @Override
    public int execute(IEntityHardCash player, String currencyName) {
        var currencyResult = searchCurrencyUseCase.getCurrency(currencyName);
        if (!currencyResult.isSuccess()) {
            //player.sendMessage("Invalid currency.");
            return -1;
        }
        ICurrency currency = currencyResult.getValue();
        if (!currency.isPhysicalItemSupported()){
            //player.sendMessage("Currency does not support physical items.");
            return -1;
        }

        if (currency.getBase64Item() == null || currency.getBase64Item().isEmpty()) {
            //player.sendMessage("Currency does not have a valid material.");
            return -1;
        }
        ItemStackCurrency itemCurrency = itemCreator.create(currency, BigDecimal.ONE);
        return player.countItems(itemCurrency);
    }
}
