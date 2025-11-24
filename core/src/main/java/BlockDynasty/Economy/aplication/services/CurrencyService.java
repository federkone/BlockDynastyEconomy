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

import BlockDynasty.Economy.domain.entities.currency.Currency;
import BlockDynasty.Economy.domain.persistence.entities.IRepository;
import BlockDynasty.Economy.domain.result.Result;
import BlockDynasty.Economy.domain.services.ICurrencyService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CurrencyService implements ICurrencyService {
    private List<Currency> currencies ;
    private IRepository repository;
    public Currency defaultCurrency;

    public CurrencyService(IRepository repository) {
        this.repository = repository;
        this.currencies = new ArrayList<>();
        this.currencies = repository.loadCurrencies();

        if(currencies.isEmpty()){
            Currency defaultCurrency = Currency.builder()
                    .setSingular("Money")
                    .setPlural("Money")
                    .setColor("GREEN")
                    .setDefaultCurrency(true)
                    .build();
            repository.saveCurrency(defaultCurrency);
            currencies.add(defaultCurrency);
        }

        updateDefaultCurrency();
    }

    public void syncCurrency(UUID uuid){
        Result<Currency> result = repository.loadCurrencyByUuid(uuid.toString());
        if(!result.isSuccess()){
            return;
        }
        currencies.removeIf(currency -> currency.getUuid().equals(uuid));
        currencies.add(result.getValue());
        //updateDefaultCurrency();
    }

    public void add(Currency currency) {
        if(currencies.contains(currency))return;

        currencies.add(currency);
    }

    public void add(List<Currency> currencyList) {
        currencies.addAll(currencyList);
    }

    public void remove(Currency currency){
        currencies.remove(currency);
    }

    public List<Currency> getCurrencies() {
        return currencies;
    }

    public Currency getCurrency(String name) {
        return currencies.stream()
                .filter(currency -> currency.getSingular().equalsIgnoreCase(name) || currency.getPlural().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }

    public Currency getCurrency(UUID uuid) {
        return currencies.stream()
                .filter(currency -> currency.getUuid().equals(uuid))
                .findFirst()
                .orElse(null);
    }

    public Currency getDefaultCurrency() {
        return this.defaultCurrency;
    }

    public boolean currencyExist(String name) {
        return currencies.stream()
                .anyMatch(currency -> currency.getSingular().equalsIgnoreCase(name) || currency.getPlural().equalsIgnoreCase(name));
    }

    public void updateDefaultCurrency() {
        defaultCurrency = currencies.stream()
                .filter(Currency::isDefaultCurrency)
                .findFirst()
                .orElse(defaultCurrency);
    }

    @Override
    public boolean existsDefaultCurrency() {
        return this.currencies.stream().anyMatch(Currency::isDefaultCurrency);
    }

}
