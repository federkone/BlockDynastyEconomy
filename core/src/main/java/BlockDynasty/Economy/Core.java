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

package BlockDynasty.Economy;

import BlockDynasty.Economy.aplication.services.ServicesManager;
import BlockDynasty.Economy.aplication.useCase.UseCaseFactory;
import BlockDynasty.Economy.domain.persistence.entities.IRepository;
import BlockDynasty.Economy.domain.services.courier.Courier;
import BlockDynasty.Economy.domain.services.log.Log;

public class Core {
    private final IRepository repository;
    private final Courier courier;
    private final UseCaseFactory useCaseFactory;

    private final ServicesManager services;

    public Core(IRepository repository, int cacheTopMinutes, Courier courier, Log log) {
        this.repository = repository;
        this.courier = courier;
        this.services = new ServicesManager( repository, cacheTopMinutes, courier);
        this.useCaseFactory = new UseCaseFactory(services, repository, courier, log);
    }

    public ServicesManager getServicesManager() {
        return this.services;
    }
    public UseCaseFactory getUseCaseFactory() {
        return this.useCaseFactory;
    }
}
