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
package net.blockdynasty.economy.hardcash.aplication.useCase;

import net.blockdynasty.economy.core.aplication.useCase.currency.SearchCurrencyUseCase;
import net.blockdynasty.economy.core.aplication.useCase.transaction.interfaces.IDepositUseCase;
import net.blockdynasty.economy.core.aplication.useCase.transaction.interfaces.IPayUseCase;
import net.blockdynasty.economy.core.aplication.useCase.transaction.interfaces.IWithdrawUseCase;
import net.blockdynasty.economy.hardcash.aplication.HardCashService;
import net.blockdynasty.economy.hardcash.aplication.useCase.items.balance.*;
import net.blockdynasty.economy.hardcash.aplication.useCase.items.deposit.*;
import net.blockdynasty.economy.hardcash.aplication.useCase.items.payment.IPayWithItemsUseCase;
import net.blockdynasty.economy.hardcash.aplication.useCase.items.payment.PayWithItemsDisableUseCase;
import net.blockdynasty.economy.hardcash.aplication.useCase.items.payment.PayWithItemsUseCase;
import net.blockdynasty.economy.hardcash.aplication.useCase.items.withdraw.ExtractItemDisableUseCase;
import net.blockdynasty.economy.hardcash.aplication.useCase.items.withdraw.ExtractItemUseCase;
import net.blockdynasty.economy.hardcash.aplication.useCase.items.withdraw.IExtractItemUseCase;
import net.blockdynasty.economy.hardcash.aplication.useCase.notes.deposit.DepositItemNBTUseCase;
import net.blockdynasty.economy.hardcash.aplication.useCase.notes.deposit.DepositItemNBTUseCaseDisable;
import net.blockdynasty.economy.hardcash.aplication.useCase.notes.deposit.IDepositItemNBTUseCase;
import net.blockdynasty.economy.hardcash.aplication.useCase.notes.withdraw.ExtractItemNBTUseCase;
import net.blockdynasty.economy.hardcash.aplication.useCase.notes.withdraw.ExtractItemNBTUseCaseDisable;
import net.blockdynasty.economy.hardcash.aplication.useCase.notes.withdraw.IExtractItemNBTUseCase;
import net.blockdynasty.economy.hardcash.aplication.useCase.notes.give.GiveItemNBTUseCase;
import net.blockdynasty.economy.hardcash.aplication.useCase.notes.give.GiveItemNBTUseCaseDisable;
import net.blockdynasty.economy.hardcash.aplication.useCase.notes.give.IGiveItemNBTUseCase;
import net.blockdynasty.economy.hardcash.domain.entity.platform.HardCashCreator;
import net.blockdynasty.economy.hardcash.aplication.useCase.items.service.CacheCurrencyItems;

public class HardCashUseCaseFactory {
    private static HardCashCreator hardCashCreator;
    private static IDepositUseCase depositUseCase;
    private static IWithdrawUseCase withdrawUseCase;
    private static SearchCurrencyUseCase searchCurrencyUseCase;
    private static IPayUseCase payUseCase;
    private static CacheCurrencyItems cacheCurrencyItems;

    public static void init(HardCashCreator hardCashCreator, IDepositUseCase depositUseCase, IWithdrawUseCase withdrawUseCase, IPayUseCase payUseCase,SearchCurrencyUseCase searchCurrencyUseCase) {
        HardCashUseCaseFactory.hardCashCreator = hardCashCreator;
        HardCashUseCaseFactory.depositUseCase =depositUseCase;
        HardCashUseCaseFactory.withdrawUseCase = withdrawUseCase;
        HardCashUseCaseFactory.searchCurrencyUseCase = searchCurrencyUseCase;
        HardCashUseCaseFactory.payUseCase = payUseCase;
        HardCashUseCaseFactory.cacheCurrencyItems = new CacheCurrencyItems(searchCurrencyUseCase, hardCashCreator);
    }

    public static CacheCurrencyItems getCacheCurrencyItems() {
        return cacheCurrencyItems;
    }

    public static IDepositItemNBTUseCase getDepositItemNBTUseCase() {
        if(HardCashService.isEnabled()){
            return new DepositItemNBTUseCase(hardCashCreator, depositUseCase,searchCurrencyUseCase);
        }
        return new DepositItemNBTUseCaseDisable();
    }

    public static IExtractItemNBTUseCase getExtractItemNBTUseCase() {
        if (HardCashService.isEnabled()) {
            return new ExtractItemNBTUseCase(hardCashCreator, withdrawUseCase,searchCurrencyUseCase);
        }
        return new ExtractItemNBTUseCaseDisable();
    }

    public static IGiveItemNBTUseCase getGiveItemNBTUseCase() {
        if (HardCashService.isEnabled()) {
            return new GiveItemNBTUseCase(searchCurrencyUseCase,hardCashCreator);
        }
        return new GiveItemNBTUseCaseDisable();
    }

    public static IDepositItemUseCase getDepositItemUseCase() {
        if (HardCashService.isItemBasedEconomyEnabled()) {
            return new DepositItemUseCase(hardCashCreator, depositUseCase,searchCurrencyUseCase,cacheCurrencyItems);
        }
        return new DepositItemDisableUseCase();
    }

    public static IExtractItemUseCase getExtractItemUseCase() {
        if (HardCashService.isItemBasedEconomyEnabled()) {
            return new ExtractItemUseCase(hardCashCreator, withdrawUseCase,cacheCurrencyItems);
        }
        return new ExtractItemDisableUseCase();
    }

    public static IDepositItemUseCase getDepositAllItemUseCase() {
        if (HardCashService.isItemBasedEconomyEnabled()) {
            return new DepositAllItemUseCase(hardCashCreator, depositUseCase,searchCurrencyUseCase,cacheCurrencyItems);
        }
        return new DepositItemDisableUseCase();
    }

    public static IPayWithItemsUseCase getPayWithItemsUseCase() {
        if (HardCashService.isItemBasedEconomyEnabled()) {
            return new PayWithItemsUseCase(hardCashCreator, searchCurrencyUseCase,payUseCase,depositUseCase,getItemsBalanceUseCase(), cacheCurrencyItems);
        }
        return new PayWithItemsDisableUseCase();
    }

    public static IGetItemsBalanceUseCase getItemsBalanceUseCase() {
        if (HardCashService.isItemBasedEconomyEnabled()) {
            return new GetItemsBalanceUseCase(hardCashCreator, searchCurrencyUseCase,cacheCurrencyItems);
        }
        return new GetItemsBalanceDisableUseCase();
    }

    public static DepositItemsDefaultUseCase getDepositAllItemsDefaultUseCase(){
        if (HardCashService.isItemBasedEconomyEnabled()) {
            return new DepositItemCurrencyDefault(hardCashCreator, depositUseCase,searchCurrencyUseCase,cacheCurrencyItems);
        }
        return new DepositItemCurrencyDefaultDisable();
    }

    public static IGetItemBalanceCurrencyDefaultUseCase getItemBalanceCurrencyDefaultUseCase(){
        if (HardCashService.isItemBasedEconomyEnabled()) {
            return new GetItemBalanceCurrencyDefaultUseCase(hardCashCreator, searchCurrencyUseCase,cacheCurrencyItems);
        }
        return new GetItemBalanceCurrencyDefaultDisableUseCase();
    }

    public static IDepositItemsCurrencyUseCase getDepositItemsCurrencyUseCase(){
        if (HardCashService.isItemBasedEconomyEnabled()) {
            return new DepositItemsCurrencyUseCase(hardCashCreator, depositUseCase,searchCurrencyUseCase,cacheCurrencyItems);
        }
        return new DepositItemsCurrencyUseCaseDisable();
    }
}
