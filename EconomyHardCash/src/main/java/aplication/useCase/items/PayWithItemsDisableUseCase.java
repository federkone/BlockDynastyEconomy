package aplication.useCase.items;

import domain.entity.player.IEntityHardCash;

public class PayWithItemsDisableUseCase implements IPayWithItemsUseCase{
    @Override
    public void execute(IEntityHardCash player, String targetPlayerName, String currencyName, int cantItems) {
        player.sendMessage("This command is disabled.");
    }
}
