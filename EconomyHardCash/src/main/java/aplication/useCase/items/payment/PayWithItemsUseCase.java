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

package aplication.useCase.items.payment;

import BlockDynasty.Economy.aplication.useCase.currency.SearchCurrencyUseCase;
import BlockDynasty.Economy.aplication.useCase.transaction.interfaces.IDepositUseCase;
import BlockDynasty.Economy.aplication.useCase.transaction.interfaces.IPayUseCase;
import BlockDynasty.Economy.domain.entities.currency.ICurrency;
import BlockDynasty.Economy.domain.events.Context;
import BlockDynasty.Economy.domain.result.Result;
import aplication.useCase.items.service.CacheCurrencyItems;
import aplication.useCase.items.service.ItemBase64Creator;
import aplication.useCase.items.balance.IGetItemsBalanceUseCase;
import domain.entity.currency.ItemStackCurrency;
import domain.entity.platform.HardCashCreator;
import domain.entity.player.IEntityHardCash;
import domain.service.ItemCreator;

import java.math.BigDecimal;

public class PayWithItemsUseCase implements IPayWithItemsUseCase{
    private IDepositUseCase depositUseCase;
    private IGetItemsBalanceUseCase getItemsBalanceUseCase;
    private SearchCurrencyUseCase searchCurrencyUseCase;
    private CacheCurrencyItems cacheCurrencyItems;
    private ItemCreator itemCreator;
    private HardCashCreator platform;
    private IPayUseCase payUseCase;

    public PayWithItemsUseCase(HardCashCreator platform, SearchCurrencyUseCase searchCurrencyUseCase,
                               IPayUseCase payUseCase, IDepositUseCase depositUseCase, IGetItemsBalanceUseCase getItemsBalanceUseCase, CacheCurrencyItems cacheCurrencyItems) {
        this.depositUseCase = depositUseCase;
        this.getItemsBalanceUseCase = getItemsBalanceUseCase;
        this.cacheCurrencyItems = cacheCurrencyItems;
        this.searchCurrencyUseCase = searchCurrencyUseCase;
        this.payUseCase = payUseCase;
        this.itemCreator = new ItemBase64Creator(platform);
    }


    @Override
    public void execute(IEntityHardCash player, String targetPlayerName, ICurrency currency, int cantItems) {
        if(cantItems <= 0){
            player.sendMessage("The amount of items must be greater than zero.");
            return;
        }
        int playerItemsBalance = getItemsBalanceUseCase.execute(player, currency);
        if(playerItemsBalance == -1)return;
        if (playerItemsBalance < cantItems) {
            player.sendMessage("You don't have enough items to make the payment.");
            return;
        }

        if(!currency.isTransferable()){
            player.sendMessage("This currency is not transferable.");
            return;
        }
        if (!currency.isPhysicalItemSupported()){
            player.sendMessage("This currency does not support physical item transactions.");
            return;
        }

        CacheCurrencyItems.Currencywrapper wrapper = cacheCurrencyItems.getItem(currency.getUuid());
        if (wrapper == null){
            player.sendMessage("Currency does not have a valid item representation.");
            return;
        }

        ItemStackCurrency itemCurrency = wrapper.getItem();
        if (itemCurrency.isNull()){
            player.sendMessage("Currency does not have a valid item representation.");
            return;
        }

        if(player.takeItems(itemCurrency,cantItems)){
            Result<Void> resultDeposit= depositUseCase.execute(player.getUniqueId(),currency.getSingular(), BigDecimal.valueOf(cantItems), Context.SYSTEM);
            if(resultDeposit.isSuccess()){
                Result<Void> resultPay=payUseCase.execute(player.getName(),targetPlayerName,currency.getSingular(), BigDecimal.valueOf(cantItems));
                if (!resultPay.isSuccess()) {
                    player.sendMessage("Payment failed.");
                }
            }else{
                itemCurrency.setCantity(cantItems);
                player.giveItem(itemCurrency);
                player.sendMessage("Payment failed.");
            }
        }
    }
}
