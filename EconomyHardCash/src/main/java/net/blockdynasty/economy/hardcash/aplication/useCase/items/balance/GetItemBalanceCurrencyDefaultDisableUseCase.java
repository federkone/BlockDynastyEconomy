package net.blockdynasty.economy.hardcash.aplication.useCase.items.balance;

import java.util.UUID;

public class GetItemBalanceCurrencyDefaultDisableUseCase implements IGetItemBalanceCurrencyDefaultUseCase{
    @Override
    public int execute(String playerName) {
        return -1;
    }

    @Override
    public int execute(UUID playerUUID) {
        return -1;
    }
}
