package net.blockdynasty.economy.gui.placeholder.components;

import net.blockdynasty.economy.core.aplication.useCase.account.getAccountUseCase.GetAccountByUUIDUseCase;
import net.blockdynasty.economy.core.aplication.useCase.transaction.balance.GetBalanceUseCase;
import net.blockdynasty.economy.core.domain.entities.balance.Money;
import net.blockdynasty.economy.core.domain.result.Result;
import net.blockdynasty.economy.hardcash.aplication.useCase.HardCashUseCaseFactory;
import net.blockdynasty.economy.hardcash.aplication.useCase.items.balance.IGetItemsBalanceUseCase;

import java.util.UUID;


//todo: muestra la suma de balance inventario+ balance virtual
public class BalanceItemsAndVirtualPlaceHolder {
    private final GetBalanceUseCase getBalanceUseCase;

    public BalanceItemsAndVirtualPlaceHolder(GetBalanceUseCase getBalanceUseCase) {
        this.getBalanceUseCase = getBalanceUseCase;
    }

    //la suma de los dos
    public String handle(UUID playerId, String placeholder){
        String[] parts = placeholder.split("_");
        if (parts.length < 2) return "Invalid placeholder format";

        String currencyName = parts[1];

        try {
            Result<Money> result = getBalanceUseCase.execute(playerId, currencyName);
            if (!result.isSuccess()) {
                return "Unknown";
            }
            int itemsBalance = HardCashUseCaseFactory.getItemsBalanceUseCase().execute(playerId, currencyName);
            if (itemsBalance == -1){
                return "unknown";
            }
            return String.valueOf(itemsBalance + result.getValue().getAmount().doubleValue());
        } catch (Exception e) {
            return "Unknown";
        }
    }
}
