package com.blockdynasty.economy.apiImplement;

import BlockDynasty.Economy.aplication.useCase.UseCaseFactory;
import BlockDynasty.Economy.domain.services.log.Log;
import aplication.useCase.HardCashUseCaseFactory;
import aplication.useCase.items.balance.IGetItemBalanceCurrencyDefaultUseCase;
import aplication.useCase.items.deposit.DepositAllItemsDefaultUseCase;
import com.BlockDynasty.api.EconomyResponse;

import java.math.BigDecimal;
import java.util.UUID;

class DynastyEconomyApiHardCash extends DynastyEconomyApi{
    private DepositAllItemsDefaultUseCase depositItemUseCase;
    private IGetItemBalanceCurrencyDefaultUseCase getItemsBalanceUseCase;

    public DynastyEconomyApiHardCash(UseCaseFactory factory, UUID id) {
        super(factory, id);
        this.depositItemUseCase = HardCashUseCaseFactory.getDepositAllItemsDefaultUseCase();
        this.getItemsBalanceUseCase = HardCashUseCaseFactory.getItemBalanceCurrencyDefaultUseCase();
    }

    public DynastyEconomyApiHardCash(UseCaseFactory factory, Log log, UUID id) {
        super(factory, log, id);
        this.depositItemUseCase = HardCashUseCaseFactory.getDepositAllItemsDefaultUseCase();
        this.getItemsBalanceUseCase = HardCashUseCaseFactory.getItemBalanceCurrencyDefaultUseCase();
    }

    @Override
    public EconomyResponse withdraw(UUID uuid, BigDecimal amount) {
        BigDecimal itemsInInventory = BigDecimal.valueOf(getItemsBalanceUseCase.execute(uuid));
        BigDecimal amountToMove = amount.min(itemsInInventory);

        if (amountToMove.compareTo(BigDecimal.ZERO) > 0) {
            this.depositItemUseCase.execute(uuid, amountToMove);
        }
        return super.withdraw(uuid, amount);
    }

    @Override
    public EconomyResponse withdraw(String name, BigDecimal amount) {
        BigDecimal itemsInInventory = BigDecimal.valueOf(getItemsBalanceUseCase.execute(name));
        BigDecimal amountToMove = amount.min(itemsInInventory);

        if (amountToMove.compareTo(BigDecimal.ZERO) > 0) {
            this.depositItemUseCase.execute(name, amountToMove);
        }

        return super.withdraw(name, amount);
    }

    @Override
    public BigDecimal getBalance(UUID uuid){
        int inv = getItemsBalanceUseCase.execute(uuid);
        BigDecimal balance = super.getBalance(uuid);
        if (inv != -1) return balance.add(BigDecimal.valueOf(inv));
        return balance;
    }

    @Override
    public BigDecimal getBalance(String name){
        int inv = getItemsBalanceUseCase.execute(name);
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
