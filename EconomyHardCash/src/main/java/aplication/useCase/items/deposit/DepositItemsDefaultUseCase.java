package aplication.useCase.items.deposit;

import java.math.BigDecimal;
import java.util.UUID;

public interface DepositItemsDefaultUseCase {

    void execute(String playerName, BigDecimal amount);
    void execute(UUID playerUUID, BigDecimal amount);
}
