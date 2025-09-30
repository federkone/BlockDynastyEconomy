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
import BlockDynasty.Economy.aplication.useCase.AccountsUseCase;
import BlockDynasty.Economy.aplication.useCase.CurrencyUseCase;
import BlockDynasty.Economy.aplication.useCase.OfferUseCase;
import BlockDynasty.Economy.aplication.useCase.TransactionsUseCase;
import BlockDynasty.Economy.domain.persistence.entities.IRepository;
import BlockDynasty.Economy.domain.services.courier.Courier;
import BlockDynasty.Economy.domain.services.log.Log;

public class Core {
    private final IRepository repository;
    private final Courier courier;

    private final ServicesManager services;
    private final AccountsUseCase accountsUseCase;
    private final CurrencyUseCase currencyUseCase;
    private final TransactionsUseCase transactionsUseCase;
    private final OfferUseCase offerUseCase;

    public Core(IRepository repository, int cacheTopMinutes, Courier courier, Log log) {
        this.repository = repository;
        this.courier = courier;

        this.services = new ServicesManager( repository, cacheTopMinutes, courier);
        this.accountsUseCase = new AccountsUseCase(services, repository,courier);
        this.currencyUseCase = new CurrencyUseCase(services, repository, accountsUseCase, courier);
        this.transactionsUseCase = new TransactionsUseCase(currencyUseCase,accountsUseCase, services.getAccountService(), repository, courier, log, services.getEventManager());
        this.offerUseCase = new OfferUseCase(services.getOfferService(),courier, services.getEventManager(), this.currencyUseCase.getGetCurrencyUseCase(),this.accountsUseCase.getGetAccountsUseCase(),this.transactionsUseCase.getTradeCurrenciesUseCase());
    }

    public ServicesManager getServicesManager() {
        return this.services;
    }
    public AccountsUseCase getAccountsUseCase() {
        return this.accountsUseCase;
    }
    public CurrencyUseCase getCurrencyUseCase() {
        return this.currencyUseCase;
    }
    public TransactionsUseCase getTransactionsUseCase() {
        return this.transactionsUseCase;
    }
    public TransactionsUseCase getTransactionsUseCase(Log log) {
        return new TransactionsUseCase(getCurrencyUseCase(), getAccountsUseCase(), services.getAccountService(),this.repository, this.courier, log, this.services.getEventManager());
    }
    public OfferUseCase getOfferUseCase() {
        return this.offerUseCase;
    }
}
