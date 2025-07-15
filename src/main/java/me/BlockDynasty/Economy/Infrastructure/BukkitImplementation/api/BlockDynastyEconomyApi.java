package me.BlockDynasty.Economy.Infrastructure.BukkitImplementation.api;

import me.BlockDynasty.Economy.aplication.api.Api;
import me.BlockDynasty.Economy.aplication.useCase.UsesCaseFactory;
import me.BlockDynasty.Economy.aplication.useCase.balance.GetBalanceUseCase;
import me.BlockDynasty.Economy.domain.result.Result;

import me.BlockDynasty.Economy.aplication.useCase.currency.GetCurrencyUseCase;
import me.BlockDynasty.Economy.aplication.useCase.transaction.*;
import me.BlockDynasty.Economy.domain.entities.balance.Balance;
import me.BlockDynasty.Economy.domain.entities.currency.Currency;

import java.math.BigDecimal;
import java.util.UUID;

//todo : la api deberia proporcionar/exponer, todos los casos de usos existentes
//todo:ademas, se deberia retornar siempre un EconomyResponse, para que desde afuera se sepa el resultado de la operacion. puede ser simplemente un boolean, o un objeto que contenga el resultado de la operacion.
public class BlockDynastyEconomyApi implements Api {
    private final GetCurrencyUseCase getCurrencyUseCase;
    private final DepositUseCase depositUseCase;
    private final WithdrawUseCase withdrawUseCase;
    private final ExchangeUseCase exchangeUseCase;
    private final TransferFundsUseCase transferFundsUseCase;
    private final TradeCurrenciesUseCase tradeCurrenciesUseCase;
    private final GetBalanceUseCase getBalanceUseCase;

    public BlockDynastyEconomyApi(UsesCaseFactory usesCaseFactory) {
        this.depositUseCase = usesCaseFactory.getDepositUseCase();
        this.withdrawUseCase = usesCaseFactory.getWithdrawUseCase();
        this.exchangeUseCase = usesCaseFactory.getExchangeUseCase();
        this.transferFundsUseCase = usesCaseFactory.getTransferFundsUseCase();
        this.getCurrencyUseCase = usesCaseFactory.getCurrencyUseCase();
        this.tradeCurrenciesUseCase = usesCaseFactory.getTradeCurrenciesUseCase();
        this.getBalanceUseCase = usesCaseFactory.getGetBalanceUseCase();
    }

    public void deposit(UUID uuid, double amount){
        //SchedulerUtils.runAsync(() -> {
            this.depositUseCase.execute(uuid, BigDecimal.valueOf(amount));
        //});
    }

    public void deposit(UUID uuid, double amount, String currency){
        //SchedulerUtils.runAsync(() -> {
            this.depositUseCase.execute(uuid,currency,  BigDecimal.valueOf(amount));
        //});
    }

    public void withdraw(UUID uuid, double amount){
        //SchedulerUtils.runAsync(() -> {
            this.withdrawUseCase.execute(uuid,  BigDecimal.valueOf(amount));
        //});
    }

    public void withdraw(UUID uuid, double amount, String currency){
        //SchedulerUtils.runAsync(() -> {
            this.withdrawUseCase.execute(uuid,currency, BigDecimal.valueOf(amount));
        //});
    }

    public double getBalance(UUID uuid){
        Result<Balance> balanceResult =  this.getBalanceUseCase.getBalance(uuid);
        if (!balanceResult.isSuccess()) {
            throw new IllegalStateException("Failed to retrieve default balance: " + balanceResult.getErrorMessage());
        }
        return balanceResult.getValue().getAmount().doubleValue();
    }

    public double getBalance(UUID uuid, String currency) {
        Result<Balance> balanceResult =  this.getBalanceUseCase.getBalance(uuid, currency);
        if (!balanceResult.isSuccess()) {
            throw new IllegalStateException("Failed to retrieve balance for currency '" + currency + "': " + balanceResult.getErrorMessage());
        }
        return balanceResult.getValue().getAmount().doubleValue();
    }

    public void exchange(UUID uuid, String currencyFrom, String currencyTo, double amountFrom,double ammountTo){
       // SchedulerUtils.runAsync(() -> {
            this.exchangeUseCase.execute(uuid,currencyFrom,currencyTo,BigDecimal.valueOf(amountFrom), BigDecimal.valueOf(ammountTo));
        //});
    }

    public void transfer(UUID userFrom, UUID userTo, String currency, double amount){
        //SchedulerUtils.runAsync(() -> {
            this.transferFundsUseCase.execute(userFrom,userTo,currency,BigDecimal.valueOf(amount));
        //});
    }

    public void trade(UUID userFrom, UUID userTo, String currencyFrom, String currencyTo, double amountFrom, double amountTo){
        //SchedulerUtils.runAsync(() -> {
            this.tradeCurrenciesUseCase.execute(userFrom,userTo,currencyFrom,currencyTo,BigDecimal.valueOf(amountFrom),BigDecimal.valueOf(amountTo));
        //});

    }

    public Currency getCurrency(String name){
            return  this.getCurrencyUseCase.getCurrency(name).getValue();
    }

}
