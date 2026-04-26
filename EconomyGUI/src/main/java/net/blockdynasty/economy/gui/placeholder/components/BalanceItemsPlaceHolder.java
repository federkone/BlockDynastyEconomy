package net.blockdynasty.economy.gui.placeholder.components;
import net.blockdynasty.economy.hardcash.aplication.useCase.HardCashUseCaseFactory;
import net.blockdynasty.economy.hardcash.aplication.useCase.items.balance.IGetItemsBalanceUseCase;

import java.util.UUID;

//todo: muestra el balance basado en items solamente de su inventario
public class BalanceItemsPlaceHolder {

    public BalanceItemsPlaceHolder(){
    }
    //%blockdynastyeconomy_balanceItems_currencyName%
    public String handle(UUID playerId, String placeholder) {
        String[] parts = placeholder.split("_");
        if (parts.length < 2) return "Invalid placeholder format";

        String currencyName = parts[1];

        try {
            int balance = HardCashUseCaseFactory.getItemsBalanceUseCase().execute(playerId, currencyName);
            return String.valueOf(balance);
        } catch (Exception e) {
            return "Unknown";
        }
    }
}


