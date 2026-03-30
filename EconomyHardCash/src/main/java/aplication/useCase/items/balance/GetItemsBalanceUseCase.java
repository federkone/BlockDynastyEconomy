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
import BlockDynasty.Economy.domain.result.Result;
import aplication.useCase.items.service.ItemBase64Creator;
import domain.entity.currency.ItemStackCurrency;
import domain.entity.platform.HardCashCreator;
import domain.entity.player.IEntityHardCash;
import aplication.useCase.items.service.CacheCurrencyItems;
import domain.service.ItemCreator;

import java.util.UUID;

public class GetItemsBalanceUseCase implements IGetItemsBalanceUseCase {
    private SearchCurrencyUseCase searchCurrencyUseCase;
    private HardCashCreator platform;
    private ItemCreator itemCreator;
    private CacheCurrencyItems cacheCurrencyItems;

    public GetItemsBalanceUseCase(HardCashCreator platform, SearchCurrencyUseCase searchCurrencyUseCase, CacheCurrencyItems cacheCurrencyItems) {
        this.platform = platform;
        this.searchCurrencyUseCase = searchCurrencyUseCase;
        this.cacheCurrencyItems = cacheCurrencyItems;
        this.itemCreator = new ItemBase64Creator(platform);
    }

    @Override
    public int execute(IEntityHardCash player, ICurrency currency) {
        if (!currency.isPhysicalItemSupported()){
            return -1;
        }

        if (currency.getBase64Item() == null || currency.getBase64Item().isEmpty()) {
            return -1;
        }
        CacheCurrencyItems.Currencywrapper wrapper = cacheCurrencyItems.getItem(currency.getUuid());
        if (wrapper == null) {
            return -1;
        }
        ItemStackCurrency itemCurrency = wrapper.getItem();
        if (itemCurrency.isNull()) return -1;
        return player.countItems(itemCurrency);
    }

    @Override
    public int execute(String playerName, String currencyName) {
        IEntityHardCash player = this.platform.getPlayer(playerName);
        if (player == null) {
            return -1;
        }

        Result<ICurrency> currencyResult = searchCurrencyUseCase.getCurrency(currencyName);
        if (!currencyResult.isSuccess()) {
            return -1;
        }


        return execute(player, currencyResult.getValue());
    }

    @Override
    public int execute(UUID playerUuid, String currencyName) {
        IEntityHardCash player = this.platform.getPlayerByUUID(playerUuid);
        if (player == null) {
            return -1;
        }

        Result<ICurrency> currencyResult = searchCurrencyUseCase.getCurrency(currencyName);
        if (!currencyResult.isSuccess()) {
            return -1;
        }

        return execute(player, currencyResult.getValue());
    }
}
