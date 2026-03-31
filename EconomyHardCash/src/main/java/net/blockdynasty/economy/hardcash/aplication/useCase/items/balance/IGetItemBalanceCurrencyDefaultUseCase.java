package net.blockdynasty.economy.hardcash.aplication.useCase.items.balance;

import java.util.UUID;

public interface IGetItemBalanceCurrencyDefaultUseCase {
    int execute(String playerName);
    int execute(UUID playerUUID);
}
