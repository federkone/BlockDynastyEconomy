package aplication.useCase.items.deposit;

import java.math.BigDecimal;
import java.util.UUID;

public class DepositItemsCurrencyUseCaseDisable implements IDepositItemsCurrencyUseCase{
    @Override
    public void execute(String playerName, String Currency, BigDecimal amount) {

    }

    @Override
    public void execute(UUID playerUUID, String Currency, BigDecimal amount) {

    }
}
