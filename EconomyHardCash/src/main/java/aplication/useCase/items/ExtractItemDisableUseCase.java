package aplication.useCase.items;

import domain.entity.player.IEntityHardCash;

import java.math.BigDecimal;

public class ExtractItemDisableUseCase implements IExtractItemUseCase{
    @Override
    public void execute(IEntityHardCash player, BigDecimal amount, String currency) {
        player.sendMessage("Economy Item Based system is disabled." );
    }
}
