package net.blockdynasty.economy.core.aplication.useCase.transaction.interfaces;

import net.blockdynasty.economy.core.domain.entities.account.Player;
import net.blockdynasty.economy.core.domain.events.Context;
import net.blockdynasty.economy.core.domain.result.Result;

import java.math.BigDecimal;
import java.util.UUID;

public interface IDepositUseCase {
   Result<Void> execute(UUID targetUUID, BigDecimal amount);
   Result<Void> execute(String targetName, BigDecimal amount);
   Result<Void> execute(Player targetPlayer, BigDecimal amount);
   Result<Void> execute(UUID targetUUID, String currencyName, BigDecimal amount);
   Result<Void> execute(String targetName, String currencyName, BigDecimal amount);
   Result<Void> execute(UUID targetUUID, String currencyName, BigDecimal amount,Context context);
   Result<Void> execute(String targetName, String currencyName, BigDecimal amount,Context context);
   Result<Void> execute(Player targetPlayer, String currencyName, BigDecimal amount,Context context);
}
