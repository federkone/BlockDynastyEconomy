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

import java.util.UUID;
import java.util.function.Supplier;

class ApiCustomSupplier implements Supplier<DynastyEconomy>,InternalProvider {
    private final UUID id;
    private final DynastyEconomy proxy;

    private volatile UseCaseFactory useCaseFactory;
    private volatile Log logger;
    private volatile DynastyEconomy internalEconomy;

    public ApiCustomSupplier(){
        this.id = UUID.randomUUID();
        this.internalEconomy = new DynastyEconomyApiNull(id);
        this.proxy = new DynastyEconomyProxy(this);
    }

    public void updateDependencies(UseCaseFactory useCaseFactory,Log logger) {
        this.useCaseFactory = useCaseFactory;
        this.logger = logger;
        this.internalEconomy = new DynastyEconomyApi(useCaseFactory,logger, id);
    }

    public void disable() {
        this.internalEconomy = new DynastyEconomyApiNull(id);
    }

    @Override
    public DynastyEconomy get() {
        return proxy;
    }

    public DynastyEconomy getInternal() {
        return this.internalEconomy;
    }

    public UUID getId() {
        return id;
    }
}
