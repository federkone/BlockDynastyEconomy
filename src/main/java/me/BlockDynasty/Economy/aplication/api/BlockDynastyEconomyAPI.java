package me.BlockDynasty.Economy.aplication.api;

import me.BlockDynasty.Economy.domain.result.Result;
import me.BlockDynasty.Economy.aplication.useCase.UsesCase;

import me.BlockDynasty.Economy.aplication.useCase.account.GetBalanceUseCase;
import me.BlockDynasty.Economy.aplication.useCase.currency.GetCurrencyUseCase;
import me.BlockDynasty.Economy.aplication.useCase.transaction.*;
import me.BlockDynasty.Economy.domain.balance.Balance;
import me.BlockDynasty.Economy.domain.currency.Currency;

import java.math.BigDecimal;
import java.util.UUID;

//todo : la api deberia proporcionar/exponer, todos los casos de usos existentes
//todo:ademas, se deberia retornar siempre un EconomyResponse, para que desde afuera se sepa el resultado de la operacion. puede ser simplemente un boolean, o un objeto que contenga el resultado de la operacion.
public class BlockDynastyEconomyAPI {
    //private final GetAccountsUseCase getAccountsUseCase;
    private final GetCurrencyUseCase getCurrencyUseCase;
    private final DepositUseCase depositUseCase;
    private final WithdrawUseCase withdrawUseCase;
    private final ExchangeUseCase exchangeUseCase;
    private final TransferFundsUseCase transferFundsUseCase;
    private final TradeCurrenciesUseCase tradeCurrenciesUseCase;
    private final GetBalanceUseCase getBalanceUseCase;


    public BlockDynastyEconomyAPI(UsesCase usesCase) {
       // this.getAccountsUseCase = usesCase.getAccountsUseCase();
        this.depositUseCase = usesCase.getDepositUseCase();
        this.withdrawUseCase = usesCase.getWithdrawUseCase();
        this.exchangeUseCase = usesCase.getExchangeUseCase();
        this.transferFundsUseCase = usesCase.getTransferFundsUseCase();
        this.getCurrencyUseCase = usesCase.getCurrencyUseCase();
        this.tradeCurrenciesUseCase = usesCase.getTradeCurrenciesUseCase();
        this.getBalanceUseCase = usesCase.getGetBalanceUseCase();
    }

    /**
     *
     * @param uuid - The users unique ID.
     * @param amount - An amount of the default currency.
     */
    public void deposit(UUID uuid, double amount){
        depositUseCase.execute(uuid, BigDecimal.valueOf(amount));
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
        withdrawUseCase.execute(uuid,  BigDecimal.valueOf(amount));
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
        Result<Balance> balanceResult = getBalanceUseCase.getBalance(uuid);
        if (!balanceResult.isSuccess()) {
            throw new IllegalStateException("Failed to retrieve default balance: " + balanceResult.getErrorMessage());
        }
        return balanceResult.getValue().getBalance().doubleValue();
    }

    /**
     *
     * @param uuid - The users unique ID.
     * @param currency - An amount of the default currency.
     * @return - The balance of the specified currency.
     */
    public double getBalance(UUID uuid, String currency) {
        Result<Balance> balanceResult = getBalanceUseCase.getBalance(uuid, currency);
        if (!balanceResult.isSuccess()) {
            throw new IllegalStateException("Failed to retrieve balance for currency '" + currency + "': " + balanceResult.getErrorMessage());
        }
        return balanceResult.getValue().getBalance().doubleValue();
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

    /**
     *
     * @param userFrom - The userFrom unique ID.
     * @param userTo - The userTO unique ID.
     * @param currencyFrom- String name currency from.
     * @param amountFrom - double mount amount from.
     * @param currencyTo - String name currency to.
     * @param amountTo - double mount amount to.
     */
    public void trade(UUID userFrom, UUID userTo, String currencyFrom, String currencyTo, double amountFrom, double amountTo){
        tradeCurrenciesUseCase.execute(userFrom,userTo,currencyFrom,currencyTo,BigDecimal.valueOf(amountFrom),BigDecimal.valueOf(amountTo));

    }
    /**
     *
     * @param name - Currency singular or plural.
     * @return - Currency Object.
     */
    public Currency getCurrency(String name){
            return getCurrencyUseCase.getCurrency(name).getValue();
    }

    public BlockDynastyEconomyAPI getAPI(){
        return this;
    }

}
