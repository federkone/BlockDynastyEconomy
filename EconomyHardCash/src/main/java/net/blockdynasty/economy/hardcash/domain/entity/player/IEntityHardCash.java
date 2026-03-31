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

package net.blockdynasty.economy.hardcash.domain.entity.player;

import net.blockdynasty.economy.libs.abstractions.platform.entity.IPlayer;
import net.blockdynasty.economy.hardcash.domain.entity.currency.ItemStackCurrency;

public interface IEntityHardCash extends IPlayer {
    void giveItem(ItemStackCurrency item);
    ItemStackCurrency takeHandItem();
    boolean hasItem(ItemStackCurrency itemCurrency);
    boolean hasEmptySlot();
    int emptySlots();
    void removeItem(ItemStackCurrency itemCurrency);
    int takeAllItems(ItemStackCurrency itemCurrency);
    boolean takeItems(ItemStackCurrency itemCurrency, int amount);
    int countItems(ItemStackCurrency itemCurrency);
}
