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

package net.blockdynasty.economy.core.aplication.services;

import net.blockdynasty.economy.core.domain.entities.currency.Currency;
import net.blockdynasty.economy.core.domain.entities.currency.ICurrency;
import net.blockdynasty.economy.core.domain.persistence.entities.IRepository;
import net.blockdynasty.economy.core.domain.result.Result;
import net.blockdynasty.economy.core.domain.services.ICurrencyService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

//todo make private
public class CurrencyService implements ICurrencyService {
    private List<ICurrency> currencies ;
    private IRepository repository;
    public ICurrency defaultCurrency;

    public CurrencyService(IRepository repository) {
        this.repository = repository;
        this.currencies = new ArrayList<>();
        this.currencies = repository.loadCurrencies();

        if(currencies.isEmpty()){
            ICurrency defaultCurrency = Currency.builder()
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
        Result<ICurrency> result = repository.loadCurrencyByUuid(uuid.toString());
        if(!result.isSuccess()){
            return;
        }
        currencies.removeIf(currency -> currency.getUuid().equals(uuid));
        currencies.add(result.getValue());
        //updateDefaultCurrency();
    }

    public void add(ICurrency currency) {
        if(currencies.contains(currency))return;

        currencies.add(currency);
    }


    public void add(List<ICurrency> currencyList) {
        currencies.addAll(currencyList);
    }

    public void remove(ICurrency currency){
        currencies.remove(currency);
    }

    public List<ICurrency> getCurrencies() {
        return currencies;
    }

    public ICurrency getCurrency(String name) {
        return currencies.stream()
                .filter(currency -> currency.getSingular().equalsIgnoreCase(name) || currency.getPlural().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }

    public ICurrency getCurrency(UUID uuid) {
        return currencies.stream()
                .filter(currency -> currency.getUuid().equals(uuid))
                .findFirst()
                .orElse(null);
    }

    @Override
    public ICurrency getCurrencyByMaterial(String material) {
        return currencies.stream()
                .filter(currency -> currency.getMaterial() != null && currency.getMaterial().equals(material))
                .findFirst()
                .orElse(null);
    }

    @Override
    public ICurrency getCurrencyByBase64Item(String base64Item) {
        return currencies.stream()
                .filter(currency -> currency.getBase64Item() != null && currency.getBase64Item().equals(base64Item))
                .findFirst()
                .orElse(null);
    }

    public ICurrency getDefaultCurrency() {
        return this.defaultCurrency;
    }

    public boolean currencyExist(String name) {
        return currencies.stream()
                .anyMatch(currency -> currency.getSingular().equals(name) || currency.getPlural().equals(name));
    }

    public void updateDefaultCurrency() {
        defaultCurrency = currencies.stream()
                .filter(ICurrency::isDefaultCurrency)
                .findFirst()
                .orElse(defaultCurrency);
    }

    @Override
    public boolean existsDefaultCurrency() {
        return this.currencies.stream().anyMatch(ICurrency::isDefaultCurrency);
    }

}
