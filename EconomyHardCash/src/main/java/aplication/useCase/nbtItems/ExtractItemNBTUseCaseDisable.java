package aplication.useCase.nbtItems;

import domain.entity.player.IEntityHardCash;

import java.math.BigDecimal;

public class ExtractItemNBTUseCaseDisable implements IExtractItemNBTUseCase {

    @Override
    public void execute(IEntityHardCash player, BigDecimal amount, String currency) {
        player.sendMessage("HardCash system is disabled." );
    }
}
