package com.blockdynasty.economy.apiImplement;

import BlockDynasty.Economy.aplication.useCase.UseCaseFactory;
import BlockDynasty.Economy.domain.services.log.Log;
import aplication.useCase.HardCashUseCaseFactory;
import aplication.useCase.items.balance.IGetItemsBalanceUseCase;
import aplication.useCase.items.deposit.IDepositItemsCurrencyUseCase;
import com.BlockDynasty.api.EconomyResponse;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.UUID;

public class DynastyEconomyApiHardCashHardcoded extends DynastyEconomyApiHardCoded{
    private String currencyName;
    private IDepositItemsCurrencyUseCase depositItemUseCase;
    private IGetItemsBalanceUseCase getItemsBalanceUseCase;


    public DynastyEconomyApiHardCashHardcoded(UseCaseFactory factory, UUID id, String currencyName) {
        super(factory, id, currencyName);
        this.depositItemUseCase = HardCashUseCaseFactory.getDepositItemsCurrencyUseCase();
        this.getItemsBalanceUseCase = HardCashUseCaseFactory.getItemsBalanceUseCase();
        this.currencyName = currencyName;
    }

    public DynastyEconomyApiHardCashHardcoded(UseCaseFactory factory, Log log, UUID id, String currencyName) {
        super(factory, log, id, currencyName);
        this.depositItemUseCase = HardCashUseCaseFactory.getDepositItemsCurrencyUseCase();
        this.getItemsBalanceUseCase = HardCashUseCaseFactory.getItemsBalanceUseCase();
        this.currencyName = currencyName;
    }

    @Override
    public EconomyResponse withdraw(UUID uuid, BigDecimal amount){
        BigDecimal filteredAmount = amount.setScale(2, RoundingMode.HALF_UP);

        int invInt = getItemsBalanceUseCase.execute(uuid,this.currencyName);
        BigDecimal itemsInInventory = BigDecimal.valueOf(invInt);

        BigDecimal amountToMove = (filteredAmount.compareTo(BigDecimal.ONE) >= 0)
                ? filteredAmount.min(itemsInInventory).setScale(0, RoundingMode.FLOOR)
                : BigDecimal.ZERO;

        if (amountToMove.compareTo(BigDecimal.ZERO) > 0) {
            this.depositItemUseCase.execute(uuid, this.currencyName,amountToMove);
        }

        return super.withdraw(uuid, filteredAmount);
    }

    @Override
    public EconomyResponse withdraw(String name, BigDecimal amount) {
        BigDecimal filteredAmount = amount.setScale(2, RoundingMode.HALF_UP);

        int invInt = getItemsBalanceUseCase.execute(name,this.currencyName);
        BigDecimal itemsInInventory = BigDecimal.valueOf(invInt);

        BigDecimal amountToMove = (filteredAmount.compareTo(BigDecimal.ONE) >= 0)
                ? filteredAmount.min(itemsInInventory).setScale(0, RoundingMode.FLOOR)
                : BigDecimal.ZERO;

        if (amountToMove.compareTo(BigDecimal.ZERO) > 0) {
            this.depositItemUseCase.execute(name, this.currencyName,amountToMove);
        }

        return super.withdraw(name, filteredAmount);
    }

    @Override
    public BigDecimal getBalance(UUID uuid){
        int inv = getItemsBalanceUseCase.execute(uuid,this.currencyName);
        BigDecimal balance = super.getBalance(uuid);
        if (inv != -1) return balance.add(BigDecimal.valueOf(inv));
        return balance;
    }

    @Override
    public BigDecimal getBalance(String name){
        int inv = getItemsBalanceUseCase.execute(name,this.currencyName);
        BigDecimal balance = super.getBalance(name);
        if (inv != -1) return balance.add(BigDecimal.valueOf(inv));
        return balance;
    }


    @Override
    public boolean hasAmount(UUID uuid, BigDecimal amount){
        return getBalance(uuid).compareTo(amount) >= 0;
    }

    @Override
    public boolean hasAmount(String name, BigDecimal amount) {
        return getBalance(name).compareTo(amount) >= 0;
    }
}
