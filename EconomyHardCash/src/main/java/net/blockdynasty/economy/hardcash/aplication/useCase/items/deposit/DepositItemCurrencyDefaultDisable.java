package net.blockdynasty.economy.hardcash.aplication.useCase.items.deposit;

import java.math.BigDecimal;
import java.util.UUID;

public class DepositItemCurrencyDefaultDisable implements DepositItemsDefaultUseCase {
    @Override
    public void execute(String playerName, BigDecimal amount) {
    }

    @Override
    public void execute(UUID playerUUID, BigDecimal amount) {
    }
}
