package aplication.useCase.items;

import BlockDynasty.Economy.aplication.useCase.currency.SearchCurrencyUseCase;
import BlockDynasty.Economy.aplication.useCase.transaction.interfaces.IDepositUseCase;
import BlockDynasty.Economy.domain.entities.currency.ICurrency;
import BlockDynasty.Economy.domain.events.Context;
import BlockDynasty.Economy.domain.result.Result;
import domain.entity.currency.ItemStackCurrency;
import domain.entity.currency.NbtData;
import domain.entity.platform.HardCashCreator;
import domain.entity.player.IEntityHardCash;

import java.math.BigDecimal;

public class DepositItemUseCase implements IDepositItemUseCase{
    private HardCashCreator platformHardCash;
    private IDepositUseCase depositUseCase;
    private SearchCurrencyUseCase searchCurrencyUseCase;

    public DepositItemUseCase(HardCashCreator platformHardCash, IDepositUseCase depositUseCase, SearchCurrencyUseCase searchCurrencyUseCase) {
        this.platformHardCash = platformHardCash;
        this.depositUseCase = depositUseCase;
        this.searchCurrencyUseCase = searchCurrencyUseCase;
    }

    @Override
    public void execute(IEntityHardCash player) {
        ItemStackCurrency item = player.takeHandItem();
        if(item == null){
            player.sendMessage("You must hold a currency item to deposit.");
            return;
        }

        Result<ICurrency> resultC = searchCurrencyUseCase.getCurrencyByMaterial(item.getMaterial());
        if (!resultC.isSuccess()) {
            player.sendMessage("Not have a valid currency item in hand.");
            return;
        }
        NbtData nbtData = item.getNbtData();
        if (nbtData.getItemType() != null || nbtData.getUuidCurrency() != null) {
            player.sendMessage("Not have a valid currency item in hand.");
            return;
        }

        player.removeItem(item);
        BigDecimal cant = new BigDecimal(item.getCantity());
        ICurrency currency = resultC.getValue();

        Result<Void> depositResult = depositUseCase.execute(player.getUniqueId(),currency.getSingular(), cant, Context.COMMAND);
        if (!depositResult.isSuccess()) {
            player.giveItem(item);
        }
    }
}
