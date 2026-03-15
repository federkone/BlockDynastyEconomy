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
