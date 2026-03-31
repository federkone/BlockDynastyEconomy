package net.blockdynasty.economy.hardcash.aplication.useCase.items.deposit;

import java.math.BigDecimal;
import java.util.UUID;

public interface IDepositItemsCurrencyUseCase {
    void execute(String playerName, String Currency, BigDecimal amount);
    void execute(UUID playerUUID, String Currency, BigDecimal amount);
}
