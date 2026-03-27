package aplication.useCase.items.balance;

import BlockDynasty.Economy.aplication.useCase.currency.SearchCurrencyUseCase;
import BlockDynasty.Economy.domain.entities.currency.ICurrency;
import BlockDynasty.Economy.domain.result.Result;
import aplication.useCase.items.service.CacheCurrencyItems;
import domain.entity.platform.HardCashCreator;
import domain.entity.player.IEntityHardCash;
import services.Console;

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
