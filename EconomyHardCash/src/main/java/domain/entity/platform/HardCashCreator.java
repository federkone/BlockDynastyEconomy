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

package domain.entity.platform;

import abstractions.platform.scheduler.IScheduler;
import domain.entity.currency.ItemStackCurrency;
import domain.entity.currency.RecipeItemCurrency;
import domain.entity.player.IEntityHardCash;

import java.util.UUID;

public interface HardCashCreator {
    IEntityHardCash getPlayer(String playerName);
    IEntityHardCash getPlayerByUUID(UUID playerUUID);
    ItemStackCurrency createItemStackNBT(RecipeItemCurrency recipe);
    ItemStackCurrency createItemBase64(RecipeItemCurrency recipe);
    IScheduler getScheduler();
    boolean hasSupportHardCash();
}
