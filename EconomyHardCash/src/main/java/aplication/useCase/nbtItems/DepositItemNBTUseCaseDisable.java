package aplication.useCase.nbtItems;

import domain.entity.player.IEntityHardCash;

public class DepositItemNBTUseCaseDisable implements IDepositItemNBTUseCase {

    @Override
    public void execute(IEntityHardCash player) {
        player.sendMessage("HardCash system is disabled." );
    }
}
