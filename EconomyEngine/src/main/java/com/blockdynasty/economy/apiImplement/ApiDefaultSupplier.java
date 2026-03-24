/**
 * Copyright 2026 Federico Barrionuevo "@federkone"
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

package com.blockdynasty.economy.apiImplement;

import BlockDynasty.Economy.aplication.useCase.UseCaseFactory;
import BlockDynasty.Economy.domain.services.IAccountService;
import com.BlockDynasty.api.DynastyEconomy;

import java.util.UUID;
import java.util.function.Supplier;

public class ApiDefaultSupplier implements Supplier<DynastyEconomy> {
    private UseCaseFactory useCaseFactory;
    private IAccountService accountService;
    private final UUID id= UUID.randomUUID();
    private volatile DynastyEconomy economy;

    public ApiDefaultSupplier(){
        this.economy = new DynastyEconomyApiNull();
    }

    public void updateDependencies(UseCaseFactory useCaseFactory, IAccountService accountService) {
        this.useCaseFactory = useCaseFactory;
        this.accountService = accountService;
        this.economy = null;
    }

    @Override
    public DynastyEconomy get() {
        if (economy == null) {
            synchronized (this) {
                if (economy == null) {
                    economy = new DynastyEconomyApi(useCaseFactory, accountService, id);
                }
            }
        }
        return economy;
    }

    public UUID getId() {
        return id;
    }
}
