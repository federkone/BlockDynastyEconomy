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

package BlockDynasty.Economy.aplication.services;

import BlockDynasty.Economy.aplication.events.EventManager;
import BlockDynasty.Economy.domain.persistence.entities.IRepository;
import BlockDynasty.Economy.domain.services.IAccountService;
import BlockDynasty.Economy.domain.services.ICurrencyService;
import BlockDynasty.Economy.domain.services.IOfferService;
import BlockDynasty.Economy.domain.services.courier.Courier;

public class ServicesManager {
    private final ICurrencyService currencyService;
    private final IAccountService accountService;
    private final IOfferService offerService;
    private final EventManager eventManager;

    public ServicesManager(IRepository repository, int cacheTopMinutes, Courier courier) {
        this.currencyService = new CurrencyService(repository);
        this.accountService = new AccountService(cacheTopMinutes,repository,currencyService);
        this.eventManager = new EventManager();
        this.offerService = new OfferService(courier,eventManager);
    }

    public ICurrencyService getCurrencyService() {
        return currencyService;
    }
    public IAccountService getAccountService() {
        return accountService;
    }
    public IOfferService getOfferService() {
        return offerService;
    }
    public EventManager getEventManager() {
        return eventManager;
    }
}
