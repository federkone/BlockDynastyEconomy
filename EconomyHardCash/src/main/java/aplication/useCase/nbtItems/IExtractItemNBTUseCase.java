package aplication.useCase.nbtItems;

import domain.entity.player.IEntityHardCash;

import java.math.BigDecimal;

public interface IExtractItemNBTUseCase {

    void execute(IEntityHardCash player, BigDecimal amount, String currency);
}
