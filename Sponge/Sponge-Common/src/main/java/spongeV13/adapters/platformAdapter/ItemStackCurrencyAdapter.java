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

package spongeV13.adapters.platformAdapter;

import domain.entity.currency.ItemStackCurrency;
import domain.entity.currency.NbtData;
import org.apache.logging.log4j.util.Base64Util;
import org.spongepowered.api.data.persistence.DataContainer;
import org.spongepowered.api.data.persistence.DataFormats;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.ItemStackSnapshot;
import spongeV13.adapters.platformAdapter.NBTApi.CustomKeys;

import javax.xml.crypto.Data;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Optional;

public class ItemStackCurrencyAdapter implements ItemStackCurrency {
    private ItemStack item;

    public ItemStackCurrencyAdapter(ItemStack item) {
        this.item = item;
    }

    @Override
    public NbtData getNbtData() {
        String nameCurrency = null;
        Optional<String> object = item.get(CustomKeys.NAME_CURRENCY);
        if (object.isPresent()) {
            nameCurrency = object.get();
        }

        String valueCurrency = null;
        Optional<String> valueObject = item.get(CustomKeys.VALUE);
        if (valueObject.isPresent()) {
            valueCurrency = valueObject.get();
        }

        String uuidCurrency = null;
        Optional<String> uuidObject = item.get(CustomKeys.UUID_CURRENCY);
        if (uuidObject.isPresent()) {
            uuidCurrency = uuidObject.get();
        }

        return new NbtData(nameCurrency,uuidCurrency,valueCurrency);
    }

    @Override
    public int getCantity() {
        return item.quantity();
    }

    @Override
    public void setCantity(int i) {
        this.item.setQuantity(i);
    }

    @Override
    public  String getMaterial() {
        return item.type().toString();
    }

    @Override
    public String asBase64() {
        ItemStack copy = item.copy();
        copy.setQuantity(1);
        DataContainer datacontainer = copy.toContainer();
        try {
            String jsonItem= DataFormats.SNBT.get().write(datacontainer); //escribo el datacontainer a json
            return Base64Util.encode(jsonItem);
        } catch (IOException e) {
            return null;
        }
    }

    @Override
    public int maxStackSize() {
        return item.type().maxStackQuantity();
    }

    @Override
    public boolean isNull() {
        return item == null || item.isEmpty();
    }

    @Override
    public boolean isSimilar(ItemStackCurrency itemStackCurrency) {
        ItemStack toCompare = ((ItemStack) itemStackCurrency.getRoot()).copy();
        toCompare.setQuantity(1);
        return this.item.equalTo(toCompare);
    }

    @Override
    public Object getRoot() {
        return item;
    }
}
