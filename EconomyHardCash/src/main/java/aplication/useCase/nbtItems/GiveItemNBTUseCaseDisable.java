package aplication.useCase.nbtItems;

import java.math.BigDecimal;

public class GiveItemNBTUseCaseDisable implements IGiveItemNBTUseCase {


    @Override
    public boolean execute(String playerName, BigDecimal amount, String currency) {
        return false;
    }
}
