package com.blockdynasty.economy.apiImplement;

import BlockDynasty.Economy.aplication.useCase.UseCaseFactory;
import BlockDynasty.Economy.domain.services.log.Log;
import com.BlockDynasty.api.EconomyResponse;

import java.math.BigDecimal;
import java.util.UUID;

public class DynastyEconomyApiHardCoded extends DynastyEconomyApi{
    private String currencyName = "";

    public DynastyEconomyApiHardCoded(UseCaseFactory factory, UUID id, String currencyName) {
         super(factory, id);
         this.currencyName = currencyName;
    }

    public DynastyEconomyApiHardCoded(UseCaseFactory factory, Log log, UUID id, String currencyName) {
         super(factory, log, id);
         this.currencyName = currencyName;
    }

    @Override
    public EconomyResponse deposit(UUID uuid, BigDecimal amount) {
        return this.deposit(uuid, amount, this.currencyName);
    }

    @Override
    public EconomyResponse deposit(String name, BigDecimal amount) {
        return this.deposit(name, amount, this.currencyName);
    }


    @Override
    public EconomyResponse withdraw(UUID uuid, BigDecimal amount) {
        return this.withdraw(uuid, amount, this.currencyName);
    }

    @Override
    public EconomyResponse withdraw(String name, BigDecimal amount) {
        return this.withdraw(name, amount, this.currencyName);
    }


    @Override
    public BigDecimal getBalance(UUID uuid){
        return this.getBalance(uuid, this.currencyName);
    }

    @Override
    public BigDecimal getBalance(String name){
        return this.getBalance(name, this.currencyName);
    }

    @Override
    public boolean hasAmount(UUID uuid, BigDecimal amount){
        return this.hasAmount(uuid, amount, this.currencyName);
    }

    @Override
    public boolean hasAmount(String name, BigDecimal amount){
        return this.hasAmount(name, amount, this.currencyName);
    }

    @Override
    public EconomyResponse setBalance(UUID uuid, BigDecimal amount) {
        return this.setBalance(uuid, amount, this.currencyName);
    }
}
