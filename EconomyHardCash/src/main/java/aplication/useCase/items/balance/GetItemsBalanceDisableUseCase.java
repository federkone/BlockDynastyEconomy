package aplication.useCase.items.balance;

import BlockDynasty.Economy.domain.entities.currency.ICurrency;
import domain.entity.player.IEntityHardCash;

public class GetItemsBalanceDisableUseCase implements IGetItemsBalanceUseCase {
    @Override
    public int execute(IEntityHardCash player, ICurrency currency) {
        return -1;
    }
}
