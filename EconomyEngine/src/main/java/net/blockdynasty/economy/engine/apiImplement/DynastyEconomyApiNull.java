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

package net.blockdynasty.economy.engine.apiImplement;

import net.blockdynasty.economy.api.DynastyEconomy;
import net.blockdynasty.economy.api.EconomyResponse;
import net.blockdynasty.economy.api.entity.Account;
import net.blockdynasty.economy.api.entity.Currency;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

class DynastyEconomyApiNull implements DynastyEconomy {
    private UUID id;

    public  DynastyEconomyApiNull(UUID id) {
        this.id = id;
    }
    @Override
    public EconomyResponse deposit(UUID uuid, BigDecimal amount) {
        return EconomyResponse.failure("API not available");
    }

    @Override
    public EconomyResponse deposit(String name, BigDecimal amount) {
        return EconomyResponse.failure("API not available");
    }

    @Override
    public Currency getDefaultCurrency() {
        return null;
    }

    @Override
    public List<Account> getAccountsOffline() {
        return List.of();
    }

    @Override
    public EconomyResponse withdraw(UUID uuid, BigDecimal amount) {
        return EconomyResponse.failure("API not available");
    }

    @Override
    public EconomyResponse withdraw(String name, BigDecimal amount) {
        return EconomyResponse.failure("API not available");
    }

    @Override
    public EconomyResponse setBalance(UUID uuid, BigDecimal amount) {
        return EconomyResponse.failure("API not available");
    }

    @Override
    public EconomyResponse setBalance(String name, BigDecimal amount, String currency) {
        return EconomyResponse.failure("API not available");
    }

    @Override
    public EconomyResponse setBalance(UUID uuid, BigDecimal amount, String currency) {
        return EconomyResponse.failure("API not available");
    }

    @Override
    public EconomyResponse setBalance(String name, BigDecimal amount) {
        return EconomyResponse.failure("API not available");
    }

    @Override
    public EconomyResponse deposit(UUID uuid, BigDecimal amount, String currency) {
        return EconomyResponse.failure("API not available");
    }

    @Override
    public EconomyResponse deposit(String name, BigDecimal amount, String currency) {
        return EconomyResponse.failure("API not available");
    }

    @Override
    public EconomyResponse withdraw(UUID uuid, BigDecimal amount, String currency) {
        return EconomyResponse.failure("API not available");
    }

    @Override
    public EconomyResponse withdraw(String name, BigDecimal amount, String currency) {
        return EconomyResponse.failure("API not available");
    }

    @Override
    public BigDecimal getBalance(UUID uuid) {
        return BigDecimal.ZERO;
    }

    @Override
    public BigDecimal getBalance(String name) {
        return BigDecimal.ZERO;
    }

    @Override
    public BigDecimal getBalance(UUID uuid, String currency) {
        return BigDecimal.ZERO;
    }

    @Override
    public BigDecimal getBalance(String name, String currency) {
        return BigDecimal.ZERO;
    }

    @Override
    public EconomyResponse exchange(UUID uuid, String currencyFrom, String currencyTo, BigDecimal amountFrom, BigDecimal amountTo) {
        return EconomyResponse.failure("API not available");
    }

    @Override
    public EconomyResponse transfer(UUID userFrom, UUID userTo, String currency, BigDecimal amount) {
        return EconomyResponse.failure("API not available");
    }

    @Override
    public EconomyResponse transfer(String userFrom, String userTo, String currency, BigDecimal amount) {
        return EconomyResponse.failure("API not available");
    }

    @Override
    public EconomyResponse trade(UUID userFrom, UUID userTo, String currencyFrom, String currencyTo, BigDecimal amountFrom, BigDecimal amountTo) {
        return EconomyResponse.failure("API not available");
    }

    @Override
    public boolean hasAmount(UUID uuid, BigDecimal amount) {
        return false;
    }

    @Override
    public boolean hasAmount(String name, BigDecimal amount) {
        return false;
    }

    @Override
    public boolean hasAmount(UUID uuid, BigDecimal amount, String currency) {
        return false;
    }

    @Override
    public boolean hasAmount(String name, BigDecimal amount, String currency) {
        return false;
    }

    @Override
    public boolean existCurrency(String nameCurrency) {
        return false;
    }

    @Override
    public boolean existAccount(UUID uuid) {
        return false;
    }

    @Override
    public boolean existAccount(String name) {
        return false;
    }

    @Override
    public String getDefaultCurrencyNamePlural() {
        return "";
    }

    @Override
    public String getNameCurrencyPlural(String nameCurrency) {
        return "";
    }

    @Override
    public String getNameCurrencySingular(String nameCurrency) {
        return "";
    }

    @Override
    public String getDefaultCurrencyNameSingular() {
        return "";
    }

    @Override
    public String format(BigDecimal amount) {
        return "";
    }

    @Override
    public String format(BigDecimal amount, String currency) {
        return "";
    }

    @Override
    public List<String> getCurrenciesNamesList() {
        return List.of();
    }

    @Override
    public List<Currency> getCurrencies() {
        return List.of();
    }

    @Override
    public EconomyResponse createAccount(UUID accountID, String name) {
        return EconomyResponse.failure("API not available");
    }

    @Override
    public EconomyResponse deleteAccount(UUID accountID) {
        return EconomyResponse.failure("API not available");
    }

    @Override
    public EconomyResponse deleteAccount(String name) {
        return EconomyResponse.failure("API not available");
    }

    @Override
    public EconomyResponse blockAccountTransactions(UUID accountID) {
        return EconomyResponse.failure("API not available");
    }

    @Override
    public EconomyResponse unblockAccountTransactions(UUID accountID) {
        return EconomyResponse.failure("API not available");
    }

    @Override
    public EconomyResponse createCurrency(String plural, String singular) {
        return EconomyResponse.failure("API not available");
    }

    @Override
    public EconomyResponse deleteCurrency(String name) {
        return EconomyResponse.failure("API not available");
    }

    @Override
    public Account getAccount(UUID uuid) {
        return null;
    }

    @Override
    public Account getAccount(String name) {
        return null;
    }

    @Override
    public List<Account> getTopAccounts(int amount, String currency) {
        return List.of();
    }

    @Override
    public EconomyResponse setCurrencyStartBalance(String name, BigDecimal startBal) {
        return EconomyResponse.failure("API not available");
    }

    @Override
    public EconomyResponse setCurrencyColor(String currencyName, String colorString) {
        return EconomyResponse.failure("API not available");
    }

    @Override
    public EconomyResponse setCurrencyRate(String currencyName, BigDecimal rate) {
        return EconomyResponse.failure("API not available");
    }

    @Override
    public EconomyResponse setCurrencyDecimalSupport(String currencyName, boolean supportDecimals) {
        return EconomyResponse.failure("API not available");
    }

    @Override
    public EconomyResponse setCurrencySymbol(String currencyName, String symbol) {
        return EconomyResponse.failure("API not available");
    }

    @Override
    public EconomyResponse setDefaultCurrency(String currencyName) {
        return EconomyResponse.failure("API not available");
    }

    @Override
    public EconomyResponse setSingularName(String currentName, String newName) {
        return EconomyResponse.failure("API not available");
    }

    @Override
    public EconomyResponse setPluralName(String currentName, String newName) {
        return EconomyResponse.failure("API not available");
    }

    @Override
    public EconomyResponse setPayable(String currencyName, boolean isPayable) {
        return EconomyResponse.failure("API not available");
    }

    @Override
    public EconomyResponse saveCurrency(Currency currency) {
        return EconomyResponse.failure("API not available");
    }

    @Override
    public Currency getCurrency(String name) {
        return null;
    }

    @Override
    public UUID getId() {
        return this.id;
    }
}
