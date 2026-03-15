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
package aplication.useCase;

import BlockDynasty.Economy.aplication.useCase.currency.SearchCurrencyUseCase;
import BlockDynasty.Economy.aplication.useCase.transaction.interfaces.IDepositUseCase;
import BlockDynasty.Economy.aplication.useCase.transaction.interfaces.IPayUseCase;
import BlockDynasty.Economy.aplication.useCase.transaction.interfaces.IWithdrawUseCase;
import aplication.HardCashService;
import aplication.useCase.items.*;
import aplication.useCase.nbtItems.*;
import domain.entity.platform.HardCashCreator;

public class HardCashUseCaseFactory {
    private static HardCashCreator hardCashCreator;
    private static IDepositUseCase depositUseCase;
    private static IWithdrawUseCase withdrawUseCase;
    private static SearchCurrencyUseCase searchCurrencyUseCase;
    private static IPayUseCase payUseCase;

    public static void init(HardCashCreator hardCashCreator, IDepositUseCase depositUseCase, IWithdrawUseCase withdrawUseCase, IPayUseCase payUseCase,SearchCurrencyUseCase searchCurrencyUseCase) {
        HardCashUseCaseFactory.hardCashCreator = hardCashCreator;
        HardCashUseCaseFactory.depositUseCase =depositUseCase;
        HardCashUseCaseFactory.withdrawUseCase = withdrawUseCase;
        HardCashUseCaseFactory.searchCurrencyUseCase = searchCurrencyUseCase;
        HardCashUseCaseFactory.payUseCase = payUseCase;
    }

    public static IDepositItemNBTUseCase getDepositItemNBTUseCase() {
        if(HardCashService.isEnabled()){
            return new DepositItemNBTUseCase(hardCashCreator, depositUseCase,searchCurrencyUseCase);
        }
        return new DepositItemNBTUseCaseDisable();
    }

    public static IExtractItemNBTUseCase getExtractItemNBTUseCase() {
        if (HardCashService.isEnabled()) {
            return new ExtractItemNBTUseCase( hardCashCreator, withdrawUseCase,searchCurrencyUseCase);
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
            return new DepositItemUseCase(hardCashCreator, depositUseCase,searchCurrencyUseCase);
        }
        return new DepositItemDisableUseCase();
    }

    public static IExtractItemUseCase getExtractItemUseCase() {
        if (HardCashService.isItemBasedEconomyEnabled()) {
            return new ExtractItemUseCase(hardCashCreator, withdrawUseCase,searchCurrencyUseCase);
        }
        return new ExtractItemDisableUseCase();
    }

    public static IDepositItemUseCase getDepositAllItemUseCase() {
        if (HardCashService.isItemBasedEconomyEnabled()) {
            return new DepositAllItemUseCase(hardCashCreator, depositUseCase,searchCurrencyUseCase);
        }
        return new DepositItemDisableUseCase();
    }

    public static IPayWithItemsUseCase getPayWithItemsUseCase() {
        if (HardCashService.isItemBasedEconomyEnabled()) {
            return new PayWithItemsUseCase(hardCashCreator, searchCurrencyUseCase,payUseCase,depositUseCase,getItemsBalanceUseCase());
        }
        return new PayWithItemsDisableUseCase();
    }

    public static IGetItemsBalanceUseCase getItemsBalanceUseCase() {
        if (HardCashService.isItemBasedEconomyEnabled()) {
            return new GetItemsBalanceUseCase(hardCashCreator, searchCurrencyUseCase);
        }
        return new GetItemsBalanceDisableUseCase();
    }
}
