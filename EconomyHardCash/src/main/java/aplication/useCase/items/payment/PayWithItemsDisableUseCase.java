package aplication.useCase.items.payment;

import BlockDynasty.Economy.domain.entities.currency.ICurrency;
import domain.entity.player.IEntityHardCash;

public class PayWithItemsDisableUseCase implements IPayWithItemsUseCase{
    @Override
    public void execute(IEntityHardCash player, String targetPlayerName, ICurrency currencyName, int cantItems) {
        player.sendMessage("This command is disabled.");
    }
}
