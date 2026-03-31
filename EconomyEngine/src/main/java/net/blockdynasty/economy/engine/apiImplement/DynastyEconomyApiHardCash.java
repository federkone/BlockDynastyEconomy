package net.blockdynasty.economy.engine.apiImplement;

import net.blockdynasty.economy.core.aplication.useCase.UseCaseFactory;
import net.blockdynasty.economy.core.domain.services.log.Log;
import net.blockdynasty.economy.hardcash.aplication.useCase.HardCashUseCaseFactory;
import net.blockdynasty.economy.hardcash.aplication.useCase.items.balance.IGetItemBalanceCurrencyDefaultUseCase;
import net.blockdynasty.economy.hardcash.aplication.useCase.items.deposit.DepositItemsDefaultUseCase;
import net.blockdynasty.economy.api.EconomyResponse;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.UUID;

class DynastyEconomyApiHardCash extends DynastyEconomyApi{
    private DepositItemsDefaultUseCase depositItemUseCase;
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
        BigDecimal filteredAmount = amount.setScale(2, RoundingMode.HALF_UP);
        BigDecimal itemsNeeded = filteredAmount.setScale(0, RoundingMode.CEILING);

        int itemsAvailable = getItemsBalanceUseCase.execute(uuid);
        BigDecimal amountToMove = itemsNeeded.min(BigDecimal.valueOf(itemsAvailable));

        if (amountToMove.compareTo(BigDecimal.ZERO) > 0) {
            this.depositItemUseCase.execute(uuid, amountToMove);
        }
        return super.withdraw(uuid, filteredAmount);
    }

    @Override
    public EconomyResponse withdraw(String name, BigDecimal amount) {
        BigDecimal filteredAmount = amount.setScale(2, RoundingMode.HALF_UP);
        BigDecimal itemsNeeded = filteredAmount.setScale(0, RoundingMode.CEILING);

        int itemsAvailable = getItemsBalanceUseCase.execute(name);
        BigDecimal amountToMove = itemsNeeded.min(BigDecimal.valueOf(itemsAvailable));

        if (amountToMove.compareTo(BigDecimal.ZERO) > 0) {
            this.depositItemUseCase.execute(name, amountToMove);
        }
        return super.withdraw(name, filteredAmount);
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
