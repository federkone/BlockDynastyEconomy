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

package net.blockdynasty.economy.api;

import net.blockdynasty.economy.api.entity.Account;
import net.blockdynasty.economy.api.entity.Currency;
import net.blockdynasty.providers.services.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public interface DynastyEconomy extends Service<UUID> {

    /**
     *
     * @param uuid - The users unique ID.
     * @param amount - An amount of the default currency.
     */
    EconomyResponse deposit(UUID uuid, BigDecimal amount);
    EconomyResponse deposit(String name, BigDecimal amount);

    Currency getDefaultCurrency();
    List<Account> getAccountsOffline();

    /**
     *
     * @param uuid - The users unique ID.
     * @param amount - An amount of the default currency.
     */
    EconomyResponse withdraw(UUID uuid, BigDecimal amount);
    EconomyResponse withdraw(String name, BigDecimal amount);

    EconomyResponse setBalance(UUID uuid, BigDecimal amount);
    EconomyResponse setBalance(String name, BigDecimal amount, String currency);

    EconomyResponse setBalance(UUID uuid, BigDecimal amount, String currency);
    EconomyResponse setBalance(String name, BigDecimal amount);
    /**
     *
     * @param uuid - The users unique ID.
     * @param amount - An amount of a currency
     * @param currency - A specified currency.
     */
    EconomyResponse deposit(UUID uuid, BigDecimal amount, String currency);
    EconomyResponse deposit(String name, BigDecimal amount, String currency);
    /**
     *
     * @param uuid - The users unique ID.
     * @param amount - An amount of the currency.
     * @param currency - The currency you withdraw from.
     */
    EconomyResponse withdraw(UUID uuid, BigDecimal amount, String currency);
    EconomyResponse withdraw(String name, BigDecimal amount, String currency);

    /**
     *
     * @param uuid - The users unique ID.
     * @return - The default currency balance of the user.
     */
    BigDecimal getBalance(UUID uuid);
    BigDecimal getBalance(String name);

    /**
     *
     * @param uuid - The users unique ID.
     * @param currency - An amount of the default currency.
     * @return - The balance of the specified currency.
     */
    BigDecimal getBalance(UUID uuid, String currency);
    BigDecimal getBalance(String name, String currency);

    /**
     *
     * @param uuid - The users unique ID.
     * @param currencyFrom - String name currencyFrom.
     * @param currencyTo - String name currencyTo.
     * @param amountFrom - double mount amountFrom.
     * @param amountTo - double mount ammountTo.
     */
    EconomyResponse exchange(UUID uuid, String currencyFrom, String currencyTo, BigDecimal amountFrom,BigDecimal amountTo);

    /**
     *
     * @param userFrom - The userFrom unique ID.
     * @param userTo - The userTO unique ID.
     * @param currency- String name currency.
     * @param amount - double mount amount.
     */
    EconomyResponse transfer(UUID userFrom, UUID userTo, String currency, BigDecimal amount);
    EconomyResponse transfer(String userFrom, String userTo, String currency, BigDecimal amount);

    /**
     *
     * @param userFrom - The userFrom unique ID.
     * @param userTo - The userTO unique ID.
     * @param currencyFrom- String name currency from.
     * @param amountFrom - double mount amount from.
     * @param currencyTo - String name currency to.
     * @param amountTo - double mount amount to.
     */
    EconomyResponse trade(UUID userFrom, UUID userTo, String currencyFrom, String currencyTo, BigDecimal amountFrom, BigDecimal amountTo);

    /**
     *
     * @param uuid - The users unique ID.
     * @param amount - An amount of a currency
     */
    boolean hasAmount(UUID uuid, BigDecimal amount);

    /**
     *
     * @param name - The users name.
     * @param amount - An amount of a currency
     */
    boolean hasAmount(String name , BigDecimal amount);

    /**
     *
     * @param uuid - The users unique ID.
     * @param amount - An amount of a currency
     * @param currency - A specified currency.
     */
    boolean hasAmount(UUID uuid, BigDecimal amount, String currency);

    /**
     *
     * @param name - The users name.
     * @param amount - An amount of a currency
     * @param currency - A specified currency.
     */
    boolean hasAmount(String name , BigDecimal amount, String currency);

    /**
     *
     * @param nameCurrency - The name of the currency.
     * @return - true if the currency exist, false if not.
     */
    boolean existCurrency(String nameCurrency);

    /**
     *
     * @param uuid - The users unique ID.
     * @return - true if the account exist, false if not.
     */
    boolean existAccount(UUID uuid);

    /**
     *
     * @param name - The users name.
     * @return - true if the account exist, false if not.
     */
    boolean existAccount(String name);

    /**
     *
     * @return - The plural name of the default currency.
     */
    String getDefaultCurrencyNamePlural();

    /**
     *
     * @param nameCurrency - The name of the currency.
     * @return - The plural name of the currency.
     */
    String getNameCurrencyPlural(String nameCurrency);

    /**
     *
     * @param nameCurrency - The name of the currency.
     * @return - The singular name of the currency.
     */
    String getNameCurrencySingular(String nameCurrency);

    /**
     *
     * @return - The singular name of the default currency.
     */
    String getDefaultCurrencyNameSingular();

    /**
     *
     * @param amount - An amount of a currency.
     * @return - A formatted string of the amount with the default currency symbol.
     */
    String format(BigDecimal amount);

    /**
     *
     * @param amount - An amount of a currency.
     * @param currency - The name of the currency.
     * @return - A formatted string of the amount with the specified currency symbol.
     */
    String format(BigDecimal amount, String currency);

    /**
     *
     * @return - A list of all the currency names.
     */
    List<String> getCurrenciesNamesList();

    /**
     *
     * @return - A list of all the currencies.
     */
    List<Currency> getCurrencies();

    /**
     *
     * @param accountID - The users unique ID.
     * @param name - The users name.
     * @return - An EconomyResponse indicating the success or failure of the account creation.
     */
    EconomyResponse createAccount(final UUID accountID, final String name);

    /**
     *
     * @param accountID - The users unique ID.
     * @return - An EconomyResponse indicating the success or failure of the account deletion.
     */
    EconomyResponse deleteAccount(final UUID accountID);

    /**
     *
     * @param name - The users name.
     * @return - An EconomyResponse indicating the success or failure of the account deletion.
     */
    EconomyResponse deleteAccount(final String name);

    /**
     *
     * @param accountID - The users unique ID.
     * @return - An EconomyResponse indicating the success or failure of blocking the account transactions.
     */
    EconomyResponse blockAccountTransactions(final UUID accountID);

    /**
     *
     * @param accountID - The users unique ID.
     * @return - An EconomyResponse indicating the success or failure of unblocking the account transactions.
     */
    EconomyResponse unblockAccountTransactions(final UUID accountID);

    /**
     *
     * @param plural - The plural name of the currency.
     * @param singular - The singular name of the currency.
     * @return - An EconomyResponse indicating the success or failure of the currency creation.
     */
    EconomyResponse createCurrency(String plural,String singular);

    /**
     *
     * @param name - The name of the currency.
     * @return - An EconomyResponse indicating the success or failure of the currency deletion.
     */
    EconomyResponse deleteCurrency(String name);

    /**
     *
     * @param uuid - The users unique ID.
     * @return - The account associated with the unique ID.
     */
    Account getAccount(UUID uuid);

    /**
     *
     * @param name - The users name.
     * @return - The account associated with the name.
     */
    Account getAccount(String name);

    /**
     *
     * @param amount - The amount of top accounts to retrieve.
     * @param currency - The name of the currency to sort by.
     * @return - A list of the top accounts sorted by balance in the specified currency.
     */
    List<Account> getTopAccounts(int amount, String currency);

    /**
     *
     * @param name - The name of the currency.
     * @param startBal - The starting balance for the currency.
     * @return - An EconomyResponse indicating the success or failure of setting the currency starting balance.
     */
    EconomyResponse setCurrencyStartBalance(String name, BigDecimal startBal);

    /**
     *
     * @param currencyName - The name of the currency.
     * @param colorString - The color string to set for the currency.
     * @return - An EconomyResponse indicating the success or failure of setting the currency color.
     */
    EconomyResponse setCurrencyColor(String currencyName, String colorString);

    /**
     *
     * @param currencyName - The name of the currency.
     * @param rate - The exchange rate to set for the currency.
     * @return - An EconomyResponse indicating the success or failure of setting the currency exchange rate.
     */
    EconomyResponse setCurrencyRate(String currencyName, BigDecimal rate);

    /**
     *
     * @param currencyName - The name of the currency.
     * @param supportDecimals - A boolean indicating whether the currency should support decimals.
     * @return - An EconomyResponse indicating the success or failure of setting the currency decimal support.
     */
    EconomyResponse setCurrencyDecimalSupport(String currencyName, boolean supportDecimals);

    /**
     *
     * @param currencyName - The name of the currency.
     * @param symbol - The symbol to set for the currency.
     * @return - An EconomyResponse indicating the success or failure of setting the currency symbol.
     */
    EconomyResponse setCurrencySymbol(String currencyName, String symbol);

    /**
     *
     * @param currencyName - The name of the currency.
     * @return - An EconomyResponse indicating the success or failure of setting the currency payable status.
     */
    EconomyResponse setDefaultCurrency(String currencyName);

    /**
     *
     * @param currentName - The current name of the currency.
     * @param newName - The new singular name to set for the currency.
     * @return - An EconomyResponse indicating the success or failure of setting the currency singular name.
     */
    EconomyResponse setSingularName(String currentName, String newName);

    /**
     *
     * @param currentName - The current name of the currency.
     * @param newName - The new plural name to set for the currency.
     * @return - An EconomyResponse indicating the success or failure of setting the currency plural name.
     */
    EconomyResponse setPluralName(String currentName, String newName);

    /**
     *
     * @param currencyName - The name of the currency.
     * @param isPayable - A boolean indicating whether the currency should be payable.
     * @return - An EconomyResponse indicating the success or failure of setting the currency payable status.
     */
    EconomyResponse setPayable(String currencyName, boolean isPayable);

    /**
     *
     * @param currency - The currency to save.
     * @return - An EconomyResponse indicating the success or failure of saving the currency.
     */
    EconomyResponse saveCurrency(Currency currency);

    /**
     *
     * @param name - The name of the currency.
     * @return - The currency associated with the name.
     */
    Currency getCurrency(String name );
}
