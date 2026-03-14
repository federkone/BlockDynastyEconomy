package aplication.useCase.nbtItems;

import java.math.BigDecimal;

public interface IGiveItemNBTUseCase {

    boolean execute(String target, BigDecimal amount, String currency);
}
