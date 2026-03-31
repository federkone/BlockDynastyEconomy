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

package net.blockdynasty.economy.hardcash.aplication;

import net.blockdynasty.economy.core.aplication.useCase.currency.SearchCurrencyUseCase;
import net.blockdynasty.economy.core.aplication.useCase.transaction.interfaces.IDepositUseCase;
import net.blockdynasty.economy.core.aplication.useCase.transaction.interfaces.IPayUseCase;
import net.blockdynasty.economy.core.aplication.useCase.transaction.interfaces.IWithdrawUseCase;
import net.blockdynasty.economy.hardcash.aplication.useCase.HardCashUseCaseFactory;
import net.blockdynasty.economy.hardcash.domain.entity.platform.HardCashCreator;
import net.blockdynasty.economy.libs.services.Console;
import net.blockdynasty.economy.libs.services.configuration.IConfiguration;

public class HardCashService {
    private static boolean itemBasedEconomyEnabled = false;
    private static boolean enabled = false;
    private static boolean enableCustomHead = false;

    public static void init(IConfiguration configuration, HardCashCreator hardCashCreator,
                            IDepositUseCase depositUseCase, IWithdrawUseCase withdrawUseCase,
                            IPayUseCase payUseCase,SearchCurrencyUseCase searchCurrencyUseCase){
        enabled = configuration.getBoolean("HardCash.enable");
        enableCustomHead = configuration.getBoolean("HardCash.customHeadTexture");
        itemBasedEconomyEnabled = configuration.getBoolean("ItemsBasedEconomy.enable");
        if(!hardCashCreator.hasSupportHardCash()){
            Console.log("Hard Cash/item based economy not supported by the platform, available in 1.8.8+");
            enabled = false;
            itemBasedEconomyEnabled = false;
        }
        if (enabled){
            Console.log("Hard Cash/item based economy System Enabled");
        }else { Console.log("Hard Cash/item based economy System Disabled");}
        HardCashUseCaseFactory.init(hardCashCreator,depositUseCase,withdrawUseCase,payUseCase,searchCurrencyUseCase);
    }

    public static boolean isEnableCustomHead() {
        return enableCustomHead;
    }

    public static boolean isEnabled() {
        return enabled;
    }
    public static boolean isItemBasedEconomyEnabled() {
        return itemBasedEconomyEnabled;
    }

}