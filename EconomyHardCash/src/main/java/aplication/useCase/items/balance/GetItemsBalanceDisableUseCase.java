package aplication.useCase.items.balance;

import domain.entity.player.IEntityHardCash;

public class GetItemsBalanceDisableUseCase implements IGetItemsBalanceUseCase {
    @Override
    public int execute(IEntityHardCash player, String currencyName) {
        return -1;
    }
}
