package net.blockdynasty.economy.hardcash.aplication.useCase.items.payment;

import net.blockdynasty.economy.core.domain.entities.currency.ICurrency;
import net.blockdynasty.economy.hardcash.domain.entity.player.IEntityHardCash;

public class PayWithItemsDisableUseCase implements IPayWithItemsUseCase{
    @Override
    public void execute(IEntityHardCash player, String targetPlayerName, ICurrency currencyName, int cantItems) {
        player.sendMessage("This command is disabled.");
    }
}
