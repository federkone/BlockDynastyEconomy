package me.BlockDynasty.Economy.aplication.api;

import me.BlockDynasty.Economy.aplication.useCase.account.GetAccountsUseCase;
import me.BlockDynasty.Economy.aplication.useCase.currency.GetCurrencyUseCase;
import me.BlockDynasty.Economy.aplication.useCase.transaction.*;
import me.BlockDynasty.Economy.domain.currency.Currency;

import java.math.BigDecimal;
import java.util.UUID;

public class BlockDynastyEconomyAPI {
    private final GetAccountsUseCase getAccountsUseCase;
    private final GetCurrencyUseCase getCurrencyUseCase;
    private final DepositUseCase depositUseCase;
    private final WithdrawUseCase withdrawUseCase;
    private final ExchangeUseCase exchangeUseCase;
    private final TransferFundsUseCase transferFundsUseCase;
    private final TradeCurrenciesUseCase tradeCurrenciesUseCase;



    public BlockDynastyEconomyAPI(GetAccountsUseCase getAccountsUseCase,GetCurrencyUseCase getCurrencyUseCase, DepositUseCase depositUseCase, WithdrawUseCase withdrawUseCase, ExchangeUseCase exchangeUseCase, TransferFundsUseCase transferFundsUseCase,TradeCurrenciesUseCase tradeCurrenciesUseCase) {
        this.getAccountsUseCase = getAccountsUseCase;
        this.depositUseCase = depositUseCase;
        this.withdrawUseCase = withdrawUseCase;
        this.exchangeUseCase = exchangeUseCase;
        this.transferFundsUseCase = transferFundsUseCase;
        this.getCurrencyUseCase = getCurrencyUseCase;
        this.tradeCurrenciesUseCase = tradeCurrenciesUseCase;

    }

    /**
     *
     * @param uuid - The users unique ID.
     * @param amount - An amount of the default currency.
     */
    public void deposit(UUID uuid, double amount){
        depositUseCase.execute(uuid,getCurrencyUseCase.getDefaultCurrency().getSingular(), BigDecimal.valueOf(amount));
    }

    /**
     *
     * @param uuid - The users unique ID.
     * @param amount - An amount of a currency, if the currency is null, the default will be used.
     * @param currency - A specified currency.
     */
    public void deposit(UUID uuid, double amount, String currency){
        depositUseCase.execute(uuid,currency,  BigDecimal.valueOf(amount));
    }

    /**
     *
     * @param uuid - The users unique ID.
     * @param amount - An amount of the default currency.
     */
    public void withdraw(UUID uuid, double amount){
        withdrawUseCase.execute(uuid,getCurrencyUseCase.getDefaultCurrency().getSingular(),  BigDecimal.valueOf(amount));
    }

    /**
     *
     * @param uuid - The users unique ID.
     * @param amount - An amount of the currency.
     * @param currency - The currency you withdraw from.
     */
    public void withdraw(UUID uuid, double amount, String currency){
        withdrawUseCase.execute(uuid,currency, BigDecimal.valueOf(amount));
    }

    /**
     *
     * @param uuid - The users unique ID.
     * @return - The default currency balance of the user.
     */
    public double getBalance(UUID uuid){
        return getAccountsUseCase.getAccount(uuid).getBalance(getCurrencyUseCase.getDefaultCurrency()).getBalance().doubleValue();
    }

    /**
     *
     * @param uuid - The users unique ID.
     * @param currency - An amount of the default currency.
     * @return - The balance of the specified currency.
     */
    public double getBalance(UUID uuid, String currency) {
        return getAccountsUseCase.getAccount(uuid).getBalance(getCurrencyUseCase.getCurrency(currency)).getBalance().doubleValue();
    }

    /**
     *
     * @param uuid - The users unique ID.
     * @param currencyFrom - String name currencyFrom.
     * @param currencyTo - String name currencyTo.
     * @param amountFrom - double mount amountFrom.
     * @param ammountTo - double mount ammountTo.
     */
    public void exchange(UUID uuid, String currencyFrom, String currencyTo, double amountFrom,double ammountTo){
        exchangeUseCase.execute(uuid,currencyFrom,currencyTo,BigDecimal.valueOf(amountFrom), BigDecimal.valueOf(ammountTo));
    }

    /**
     *
     * @param userFrom - The userFrom unique ID.
     * @param userTo - The userTO unique ID.
     * @param currency- String name currency.
     * @param amount - double mount amount.
     */
    public void transfer(UUID userFrom, UUID userTo, String currency, double amount){
        transferFundsUseCase.execute(userFrom,userTo,currency,BigDecimal.valueOf(amount));
    }

    public void trade(UUID userFrom, UUID userTo, String currencyFrom, String currencyTo, double amountFrom, double amountTo){
        tradeCurrenciesUseCase.execute(userFrom,userTo,currencyFrom,currencyTo,BigDecimal.valueOf(amountFrom),BigDecimal.valueOf(amountTo));

    }
    /**
     *
     * @param name - Currency singular or plural.
     * @return - Currency Object.
     */
    public Currency getCurrency(String name){
            return getCurrencyUseCase.getCurrency(name);
    }

    public BlockDynastyEconomyAPI getAPI(){
        return this;
    }

}
