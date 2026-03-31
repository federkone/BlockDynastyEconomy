package net.blockdynasty.economy.hardcash.aplication.useCase.items.balance;

import net.blockdynasty.economy.core.aplication.useCase.currency.SearchCurrencyUseCase;
import net.blockdynasty.economy.core.domain.entities.currency.ICurrency;
import net.blockdynasty.economy.core.domain.result.Result;
import net.blockdynasty.economy.hardcash.aplication.useCase.items.service.CacheCurrencyItems;
import net.blockdynasty.economy.hardcash.domain.entity.platform.HardCashCreator;
import net.blockdynasty.economy.hardcash.domain.entity.player.IEntityHardCash;

import java.util.UUID;

public class GetItemBalanceCurrencyDefaultUseCase extends GetItemsBalanceUseCase implements IGetItemBalanceCurrencyDefaultUseCase{
    private SearchCurrencyUseCase searchCurrencyUseCase;
    private HardCashCreator platform;

    public GetItemBalanceCurrencyDefaultUseCase(HardCashCreator platform, SearchCurrencyUseCase searchCurrencyUseCase,CacheCurrencyItems cacheCurrencyItems) {
        super( platform, searchCurrencyUseCase, cacheCurrencyItems);
        this.platform = platform;
        this.searchCurrencyUseCase = searchCurrencyUseCase;
    }

    @Override
    public int execute(String playerName) {
        Result<ICurrency> currency  = searchCurrencyUseCase.getDefaultCurrency();
        if (!currency.isSuccess()) {
            return -1;
        }

        IEntityHardCash player = platform.getPlayer(playerName);
        if (player == null) {
            return -1;
        }

        return super.execute(player, currency.getValue());
    }

    @Override
    public int execute(UUID playerUUID) {
        Result<ICurrency> currency  = searchCurrencyUseCase.getDefaultCurrency();
        if (!currency.isSuccess()) {
            return -1;
        }

        IEntityHardCash player = platform.getPlayerByUUID(playerUUID);
        if (player == null) {
            return -1;
        }

        return super.execute(player, currency.getValue());
    }
}
