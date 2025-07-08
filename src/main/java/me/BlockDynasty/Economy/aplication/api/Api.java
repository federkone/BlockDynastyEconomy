package me.BlockDynasty.Economy.aplication.api;

import me.BlockDynasty.Economy.domain.currency.Currency;

import java.util.UUID;

public interface Api {
    /**
     *
     * @param uuid - The users unique ID.
     * @param amount - An amount of the default currency.
     */
    void deposit(UUID uuid, double amount);

    /**
     *
     * @param uuid - The users unique ID.
     * @param amount - An amount of a currency, if the currency is null, the default will be used.
     * @param currency - A specified currency.
     */
    void deposit(UUID uuid, double amount, String currency);

    /**
     *
     * @param uuid - The users unique ID.
     * @param amount - An amount of the default currency.
     */
    void withdraw(UUID uuid, double amount);

    /**
     *
     * @param uuid - The users unique ID.
     * @param amount - An amount of the currency.
     * @param currency - The currency you withdraw from.
     */
    void withdraw(UUID uuid, double amount, String currency);

    /**
     *
     * @param uuid - The users unique ID.
     * @return - The default currency balance of the user.
     */
    double getBalance(UUID uuid);

    /**
     *
     * @param uuid - The users unique ID.
     * @param currency - An amount of the default currency.
     * @return - The balance of the specified currency.
     */
    double getBalance(UUID uuid, String currency);

    /**
     *
     * @param uuid - The users unique ID.
     * @param currencyFrom - String name currencyFrom.
     * @param currencyTo - String name currencyTo.
     * @param amountFrom - double mount amountFrom.
     * @param ammountTo - double mount ammountTo.
     */
    void exchange(UUID uuid, String currencyFrom, String currencyTo, double amountFrom,double ammountTo);

    /**
     *
     * @param userFrom - The userFrom unique ID.
     * @param userTo - The userTO unique ID.
     * @param currency- String name currency.
     * @param amount - double mount amount.
     */
    void transfer(UUID userFrom, UUID userTo, String currency, double amount);

    /**
     *
     * @param userFrom - The userFrom unique ID.
     * @param userTo - The userTO unique ID.
     * @param currencyFrom- String name currency from.
     * @param amountFrom - double mount amount from.
     * @param currencyTo - String name currency to.
     * @param amountTo - double mount amount to.
     */
    void trade(UUID userFrom, UUID userTo, String currencyFrom, String currencyTo, double amountFrom, double amountTo);

    /**
     *
     * @param name - Currency singular or plural.
     * @return - Currency Object.
     */
    Currency getCurrency(String name);
}
