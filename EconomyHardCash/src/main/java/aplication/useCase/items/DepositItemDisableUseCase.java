package aplication.useCase.items;

import domain.entity.player.IEntityHardCash;

public class DepositItemDisableUseCase implements IDepositItemUseCase{

    @Override
    public void execute(IEntityHardCash player) {
        player.sendMessage("Economy Item Based system is disabled." );
    }
}
