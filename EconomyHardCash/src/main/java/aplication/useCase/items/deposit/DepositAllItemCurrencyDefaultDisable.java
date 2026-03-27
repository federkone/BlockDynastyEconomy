package aplication.useCase.items.deposit;

import services.Console;

import java.math.BigDecimal;
import java.util.UUID;

public class DepositAllItemCurrencyDefaultDisable implements DepositAllItemsDefaultUseCase{
    @Override
    public void execute(String playerName, BigDecimal amount) {
    }

    @Override
    public void execute(UUID playerUUID, BigDecimal amount) {
    }
}
