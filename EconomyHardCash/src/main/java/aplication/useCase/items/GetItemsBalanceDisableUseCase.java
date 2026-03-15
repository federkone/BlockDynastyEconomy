package aplication.useCase.items;

import domain.entity.player.IEntityHardCash;

public class GetItemsBalanceDisableUseCase implements  IGetItemsBalanceUseCase {
    @Override
    public int execute(IEntityHardCash player, String currencyName) {
        return -1;
    }
}
