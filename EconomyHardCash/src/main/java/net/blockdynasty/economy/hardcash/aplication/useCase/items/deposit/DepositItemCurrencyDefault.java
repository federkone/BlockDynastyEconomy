package net.blockdynasty.economy.hardcash.aplication.useCase.items.deposit;

import net.blockdynasty.economy.core.aplication.useCase.currency.SearchCurrencyUseCase;
import net.blockdynasty.economy.core.aplication.useCase.transaction.interfaces.IDepositUseCase;
import net.blockdynasty.economy.core.domain.entities.currency.ICurrency;
import net.blockdynasty.economy.core.domain.events.Context;
import net.blockdynasty.economy.core.domain.result.Result;
import net.blockdynasty.economy.libs.abstractions.platform.scheduler.ContextualTask;
import net.blockdynasty.economy.libs.abstractions.platform.scheduler.IScheduler;
import net.blockdynasty.economy.hardcash.aplication.listener.ItemNoteValidator;
import net.blockdynasty.economy.hardcash.aplication.useCase.items.service.CacheCurrencyItems;
import net.blockdynasty.economy.hardcash.aplication.useCase.items.service.ItemBase64Creator;
import net.blockdynasty.economy.hardcash.domain.entity.currency.ItemStackCurrency;
import net.blockdynasty.economy.hardcash.domain.entity.platform.HardCashCreator;
import net.blockdynasty.economy.hardcash.domain.entity.player.IEntityHardCash;
import net.blockdynasty.economy.hardcash.domain.service.ItemCreator;

import java.math.BigDecimal;
import java.util.UUID;

public class DepositItemCurrencyDefault implements DepositItemsDefaultUseCase {
    private HardCashCreator platformHardCash;
    private IDepositUseCase depositUseCase;
    private SearchCurrencyUseCase searchCurrencyUseCase;
    private CacheCurrencyItems cacheCurrencyItems;
    private IScheduler scheduler;
    private ItemCreator itemCreator;
    //private static final Set<UUID> activeTransactions = ConcurrentHashMap.newKeySet();

    //todo refactor
    public DepositItemCurrencyDefault(HardCashCreator platformHardCash,
                                      IDepositUseCase depositUseCase, SearchCurrencyUseCase searchCurrencyUseCase,
                                      CacheCurrencyItems cacheCurrencyItems) {
        this.platformHardCash = platformHardCash;
        this.scheduler = platformHardCash.getScheduler();
        this.itemCreator = new ItemBase64Creator(platformHardCash);
        this.depositUseCase = depositUseCase;
        this.searchCurrencyUseCase = searchCurrencyUseCase;
        this.cacheCurrencyItems = cacheCurrencyItems;
    }

    @Override
    public void execute(String playerName, BigDecimal amount) {
        //si el monto no es mayor a 0 y no es decimal
        if (amount.compareTo(BigDecimal.ZERO) <= 0 || amount.stripTrailingZeros().scale() > 0) {
            return;
        }

        IEntityHardCash player = platformHardCash.getPlayer(playerName);
        if (player == null) {
            return;
        }

        Result<ICurrency> currencyResult = searchCurrencyUseCase.getDefaultCurrency();
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
    public void execute(UUID playerUUID, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0 || amount.stripTrailingZeros().scale() > 0) {
            return;
        }

        IEntityHardCash player = platformHardCash.getPlayerByUUID(playerUUID);
        if (player == null) {
            return;
        }

        Result<ICurrency> currencyResult = searchCurrencyUseCase.getDefaultCurrency();
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
        };
}
