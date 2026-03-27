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
import BlockDynasty.Economy.domain.services.log.Log;
import com.BlockDynasty.api.DynastyEconomy;
import net.blockdynasty.providers.services.ServiceProvider;

import java.util.UUID;

public class ApiFactory {
    private final ApiDynamicSupplier apiDynamicSupplier;
    private final ApiDefaultSupplier apiDefaultSupplier;
    private final ApiCustomSupplier apiCustomSupplier;

    public ApiFactory(){
        this.apiDynamicSupplier = new ApiDynamicSupplier();
        this.apiCustomSupplier = new ApiCustomSupplier();
        this.apiDefaultSupplier = new ApiDefaultSupplier();
        ServiceProvider.register(DynastyEconomy.class,apiDynamicSupplier);
        ServiceProvider.register(DynastyEconomy.class,apiCustomSupplier);
        ServiceProvider.register(DynastyEconomy.class,apiDefaultSupplier);
    }

    public void updateDependencies(UseCaseFactory useCaseFactory, Log logger) {
        apiCustomSupplier.updateDependencies(useCaseFactory, logger);
        apiDynamicSupplier.updateDependencies(useCaseFactory, logger);
        apiDefaultSupplier.updateDependencies(useCaseFactory);
    }

    public void unregister() {
        ServiceProvider.unregister(DynastyEconomy.class, apiDynamicSupplier);
        ServiceProvider.unregister(DynastyEconomy.class, apiCustomSupplier);
        ServiceProvider.unregister(DynastyEconomy.class, apiDefaultSupplier);
    }

    public void disableService() {
        apiCustomSupplier.disable();
        apiDefaultSupplier.disable();
        apiDynamicSupplier.disable();
    }

    public UUID getIDApiCustomSupplier() {
        return apiCustomSupplier.getId();
    }

    public UUID getIDApiDefaultSupplier() {
        return apiDefaultSupplier.getId();
    }

    public UUID getIDApiDynamicSupplier() {
        return apiDynamicSupplier.getId();
    }
}
