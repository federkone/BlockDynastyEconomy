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

package net.blockdynasty.economy.spigot.BukkitImplementation.adapters.GUI.adapters;

import net.blockdynasty.economy.gui.gui.components.IItemStack;
import net.blockdynasty.economy.hardcash.domain.entity.currency.ItemStackCurrency;
import net.blockdynasty.economy.hardcash.domain.entity.currency.NbtData;
import org.bukkit.inventory.ItemStack;

public class ItemStackAdapter implements IItemStack, ItemStackCurrency {
    private ItemStack itemStack;

    public ItemStackAdapter(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    @Override
    public NbtData getNbtData() {
        if (itemStack == null) {
            return new NbtData();
        }
        return ItemStackProvider.getNBTData(itemStack);
    }

    @Override
    public  int getCantity() {
        return itemStack.getAmount();
    }

    @Override
    public void setCantity(int amount) {
        itemStack.setAmount(amount);
    }

    @Override
    public  String getMaterial() {
        return itemStack.getType().toString();
    }

    @Override
    public String asBase64() {
        return ItemSerialization.toBase64(itemStack);
    }

    @Override
    public int maxStackSize() {
        return itemStack.getType().getMaxStackSize();
    }

    @Override
    public boolean isNull() {
        return itemStack == null || itemStack.getType() == org.bukkit.Material.AIR;
    }

    @Override
    public boolean isSimilar(ItemStackCurrency itemStackCurrency) {
        return this.itemStack.isSimilar((ItemStack) itemStackCurrency.getRoot());
    }

    @Override
    public Object getRoot() {
        return itemStack;
    }

    @Override
    public Object getHandle() {
        return itemStack;
    }
}
