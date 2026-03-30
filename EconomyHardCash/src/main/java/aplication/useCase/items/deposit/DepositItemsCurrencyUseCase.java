package aplication.useCase.items.deposit;

import BlockDynasty.Economy.aplication.useCase.currency.SearchCurrencyUseCase;
import BlockDynasty.Economy.aplication.useCase.transaction.interfaces.IDepositUseCase;
import BlockDynasty.Economy.domain.entities.currency.ICurrency;
import BlockDynasty.Economy.domain.events.Context;
import BlockDynasty.Economy.domain.result.Result;
import abstractions.platform.scheduler.ContextualTask;
import abstractions.platform.scheduler.IScheduler;
import aplication.listener.ItemNoteValidator;
import aplication.useCase.items.service.CacheCurrencyItems;
import aplication.useCase.items.service.ItemBase64Creator;
import domain.entity.currency.ItemStackCurrency;
import domain.entity.platform.HardCashCreator;
import domain.entity.player.IEntityHardCash;
import domain.service.ItemCreator;

import java.math.BigDecimal;
import java.util.UUID;

//todo refactor
public class DepositItemsCurrencyUseCase implements IDepositItemsCurrencyUseCase{
    private HardCashCreator platformHardCash;
    private SearchCurrencyUseCase searchCurrencyUseCase;
    private CacheCurrencyItems cacheCurrencyItems;
    private IDepositUseCase depositUseCase;
    private IScheduler scheduler;
    private ItemCreator itemCreator;

    public DepositItemsCurrencyUseCase(HardCashCreator hardCashCreator, IDepositUseCase depositUseCase, SearchCurrencyUseCase searchCurrencyUseCase,
                                       CacheCurrencyItems cacheCurrencyItems) {
        this.platformHardCash = hardCashCreator;
        this.scheduler = hardCashCreator.getScheduler();
        this.searchCurrencyUseCase = searchCurrencyUseCase;
        this.depositUseCase = depositUseCase;
        this.cacheCurrencyItems = cacheCurrencyItems;
        this.itemCreator = new ItemBase64Creator(hardCashCreator);
    }

    @Override
    public void execute(String playerName, String currency, BigDecimal amount) {
        //si el monto no es mayor a 0 y no es decimal
        if (amount.compareTo(BigDecimal.ZERO) <= 0 || amount.stripTrailingZeros().scale() > 0) {
            return;
        }

        IEntityHardCash player = platformHardCash.getPlayer(playerName);
        if (player == null) {
            return;
        }

        Result<ICurrency> currencyResult = searchCurrencyUseCase.getCurrency(currency);
        if (!currencyResult.isSuccess()) {
            return;
        }

        var item = itemCreator.create(currencyResult.getValue());
        if (item.isNull()) {
            return;
        }

        this.execute(player,item, amount);
    }

    @Override
    public void execute(UUID playerUUID, String currency, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0 || amount.stripTrailingZeros().scale() > 0) {
            return;
        }

        IEntityHardCash player = platformHardCash.getPlayerByUUID(playerUUID);
        if (player == null) {
            return;
        }

        Result<ICurrency> currencyResult = searchCurrencyUseCase.getCurrency(currency);
        if (!currencyResult.isSuccess()) {
            return;
        }

        var item = itemCreator.create(currencyResult.getValue());
        if (item.isNull()) {
            return;
        }

        this.execute(player,item, amount);
    }

    private void execute(IEntityHardCash player, ItemStackCurrency item, BigDecimal amount) {
        CacheCurrencyItems.Currencywrapper currencywrapper = cacheCurrencyItems.getSimilarItem(item);
        if (currencywrapper == null) {
            return;
        }

        if (ItemNoteValidator.isANoteCurrency(item)) {
            return;
        }

        ICurrency currency = currencywrapper.getCurrency();
        if (!currency.isPhysicalItemSupported()){
            return;
        }

        if(player.takeItems(item, amount.intValue())){
            Result<Void> depositResult = depositUseCase.execute(player.getUniqueId(),currency.getSingular(), amount, Context.SYSTEM);
            if (!depositResult.isSuccess()) {
                scheduler.run(ContextualTask.build(()->{
                    item.setCantity(amount.intValue());
                    player.giveItem(item);
                }));
            }
        }
    }
}
