package aplication.useCase;

import BlockDynasty.Economy.aplication.useCase.currency.SearchCurrencyUseCase;
import BlockDynasty.Economy.aplication.useCase.transaction.interfaces.IDepositUseCase;
import BlockDynasty.Economy.aplication.useCase.transaction.interfaces.IWithdrawUseCase;
import aplication.HardCashService;
import aplication.useCase.items.*;
import aplication.useCase.nbtItems.*;
import domain.entity.platform.HardCashCreator;

public class HardCashUseCaseFactory {
    private static HardCashCreator hardCashCreator;
    private static IDepositUseCase depositUseCase;
    private static IWithdrawUseCase withdrawUseCase;
    private static SearchCurrencyUseCase searchCurrencyUseCase;

    public static void init(HardCashCreator hardCashCreator,IDepositUseCase depositUseCase,IWithdrawUseCase withdrawUseCase,SearchCurrencyUseCase searchCurrencyUseCase) {
        HardCashUseCaseFactory.hardCashCreator = hardCashCreator;
        HardCashUseCaseFactory.depositUseCase =depositUseCase;
        HardCashUseCaseFactory.withdrawUseCase = withdrawUseCase;
        HardCashUseCaseFactory.searchCurrencyUseCase = searchCurrencyUseCase;
    }

    public static IDepositItemNBTUseCase getDepositItemNBTUseCase() {
        if(HardCashService.isEnabled()){
            return new DepositItemNBTUseCase(hardCashCreator, depositUseCase,searchCurrencyUseCase);
        }
        return new DepositItemNBTUseCaseDisable();
    }

    public static IExtractItemNBTUseCase getExtractItemNBTUseCase() {
        if (HardCashService.isEnabled()) {
            return new ExtractItemNBTUseCase( hardCashCreator, withdrawUseCase,searchCurrencyUseCase);
        }
        return new ExtractItemNBTUseCaseDisable();
    }

    public static IGiveItemNBTUseCase getGiveItemNBTUseCase() {
        if (HardCashService.isEnabled()) {
            return new GiveItemNBTUseCase(searchCurrencyUseCase,hardCashCreator);
        }
        return new GiveItemNBTUseCaseDisable();
    }

    public static IDepositItemUseCase getDepositItemUseCase() {
        if (HardCashService.isItemBasedEconomyEnabled()) {
            return new DepositItemUseCase(hardCashCreator, depositUseCase,searchCurrencyUseCase);
        }
        return new DepositItemDisableUseCase();
    }

    public static IExtractItemUseCase getExtractItemUseCase() {
        if (HardCashService.isItemBasedEconomyEnabled()) {
            return new ExtractItemUseCase(hardCashCreator, withdrawUseCase,searchCurrencyUseCase);
        }
        return new ExtractItemDisableUseCase();
    }

    public static IDepositItemUseCase getDepositAllItemUseCase() {
        if (HardCashService.isItemBasedEconomyEnabled()) {
            return new DepositAllItemUseCase(hardCashCreator, depositUseCase,searchCurrencyUseCase);
        }
        return new DepositItemDisableUseCase();
    }
}
