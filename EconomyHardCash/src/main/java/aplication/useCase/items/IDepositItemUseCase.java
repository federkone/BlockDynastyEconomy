package aplication.useCase.items;

import domain.entity.player.IEntityHardCash;

public interface IDepositItemUseCase {
    void execute(IEntityHardCash player);
}
