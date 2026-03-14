package aplication.useCase.items;

import BlockDynasty.Economy.domain.entities.currency.ICurrency;
import abstractions.platform.materials.Materials;
import domain.entity.currency.ItemStackCurrency;
import domain.entity.currency.RecipeItemCurrency;
import domain.entity.platform.HardCashCreator;
import domain.service.ItemCreator;

import java.math.BigDecimal;

public class ItemBaseCreator implements ItemCreator {
    private HardCashCreator platform;
    public ItemBaseCreator(HardCashCreator platform) {
        this.platform = platform;
    }

    @Override
    public ItemStackCurrency create(ICurrency currency, BigDecimal amount) {
        return platform.createItemStackCurrency(RecipeItemCurrency.builder()
                .setMaterial(Materials.match(currency.getMaterial()))
                .setNbtData(null)
                .setName(null)
                .setLore(null)
                .setTexture(null)
                .build()
        );
    }
}
