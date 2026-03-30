package aplication.useCase.items.balance;

import BlockDynasty.Economy.domain.entities.currency.ICurrency;
import domain.entity.player.IEntityHardCash;

import java.util.UUID;

public class GetItemsBalanceDisableUseCase implements IGetItemsBalanceUseCase {
    @Override
    public int execute(IEntityHardCash player, ICurrency currency) {
        return -1;
    }

    @Override
    public int execute(String playerName, String currencyName) {
        return -1;
    }

    @Override
    public int execute(UUID playerUuid, String currencyName) {
        return -1;
    }
}
