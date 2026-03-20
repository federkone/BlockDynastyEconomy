/**
 * Copyright 2025 Federico Barrionuevo "@federkone"
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package aplication.useCase.notes.service;

import BlockDynasty.Economy.domain.entities.currency.ICurrency;
import abstractions.platform.materials.Materials;
import domain.entity.currency.ItemStackCurrency;
import domain.entity.currency.NbtData;
import domain.entity.currency.RecipeItemCurrency;
import domain.entity.platform.HardCashCreator;
import domain.service.ItemCreator;
import util.colors.ChatColor;
import util.colors.Colors;

import java.math.BigDecimal;

public class ItemWithTexture implements ItemCreator {
    private HardCashCreator platform;
    public ItemWithTexture(HardCashCreator platform) {
        this.platform = platform;
    }

    @Override
    public ItemStackCurrency create(ICurrency currency, BigDecimal amount) {
        NbtData nbtData = new NbtData(currency.getSingular(),currency.getUuid().toString(),amount.toString());
        String color = ChatColor.stringValueOf(currency.getColor());
        RecipeItemCurrency recipe = RecipeItemCurrency.builder()
                .setNbtData(nbtData)
                .setLore("","Value: " +color+ currency.format(amount),"",
                        ChatColor.stringValueOf(Colors.GOLD)+"Consumable item.",
                        ChatColor.stringValueOf(Colors.GOLD)+"Trade it or store it safely!","",
                        ChatColor.stringValueOf(Colors.GRAY)+"[Valid for Dynasty Economy System]")
                .setName(color + currency.getSingular())
                .setTexture(currency.getTexture())
                .setMaterial(Materials.match(currency.getMaterial()))
                .setBase64ITEM(currency.getBase64Item())
                .build();

        return platform.createItemStackNBT(recipe);
    }

    @Override
    public ItemStackCurrency create(ICurrency currency) {
        return this.create(currency, BigDecimal.ONE);
    }
}
