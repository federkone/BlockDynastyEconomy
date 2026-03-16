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

package BlockDynasty.Economy.aplication.useCase.currency;

import BlockDynasty.Economy.domain.entities.currency.ICurrency;
import BlockDynasty.Economy.domain.services.courier.Courier;
import BlockDynasty.Economy.domain.entities.currency.Exceptions.CurrencyNotFoundException;
import BlockDynasty.Economy.domain.entities.currency.Exceptions.DecimalNotSupportedException;
import BlockDynasty.Economy.domain.persistence.Exceptions.TransactionException;
import BlockDynasty.Economy.domain.persistence.entities.IRepository;
import BlockDynasty.Economy.domain.services.ICurrencyService;
import BlockDynasty.Economy.domain.services.courier.Message;

import java.math.BigDecimal;

public class EditCurrencyUseCase {
    private final ICurrencyService currencyService;
    private final IRepository dataStore;
    private final Courier updateForwarder;

    public EditCurrencyUseCase(ICurrencyService currencyService, Courier updateForwarder, IRepository dataStore) {
        this.currencyService = currencyService;
        this.dataStore = dataStore;
        this.updateForwarder = updateForwarder;
    }

    public void editStartBal(String name, double startBal){
        ICurrency currency = currencyService.getCurrency(name);
        if (currency == null){
            throw new CurrencyNotFoundException("Currency not found");
        }
        if (!currency.isDecimalSupported() && startBal % 1 != 0) {
            throw new DecimalNotSupportedException("Currency does not support decimals");
        }
        currency.setDefaultBalance(BigDecimal.valueOf(startBal));
        saveCurrency(currency);
    }

    public void setCurrencyRate(String currencyName, double rate){
        if (rate <= -1) {
            throw new IllegalArgumentException("Exchange rate must be greater than -1");
        }
        ICurrency currency = currencyService.getCurrency(currencyName);
        if (currency == null){
            throw new CurrencyNotFoundException("Currency not found");
        }
        currency.setExchangeRate(rate);
        saveCurrency(currency);
    }

    public void addInterchangeableCurrency(String currencyName, String currencyToAddName){
        if (currencyName.equalsIgnoreCase(currencyToAddName)){
            throw new IllegalArgumentException("Cannot add the same currency as interchangeable");
        }
        ICurrency currency = currencyService.getCurrency(currencyName);
        ICurrency currencyToAdd = currencyService.getCurrency(currencyToAddName);
        if (currency == null || currencyToAdd == null){
            throw new CurrencyNotFoundException("Currency not found");
        }
        currency.addInterchangeableCurrency(currencyToAdd);
        saveCurrency(currency);
    }

    public void removeInterchangeableCurrency(String currencyName, String currencyToRemoveName){
        ICurrency currency = currencyService.getCurrency(currencyName);
        ICurrency currencyToRemove = currencyService.getCurrency(currencyToRemoveName);
        if (currency == null || currencyToRemove == null){
            throw new CurrencyNotFoundException("Currency not found");
        }
        currency.removeInterchangeableCurrency(currencyToRemove);
        saveCurrency(currency);
    }

    public void editColor(String nameCurrency, String colorString){
        ICurrency currency = currencyService.getCurrency(nameCurrency);
        if (currency == null){
            throw new CurrencyNotFoundException("Currency not found");
        }

        currency.setColor(colorString);
        saveCurrency(currency);
    }

    public void editSymbol(String nameCurrency,String symbol){
        ICurrency currency = currencyService.getCurrency(nameCurrency);
        if (currency == null){
            throw new CurrencyNotFoundException("Currency not found");
        }
        currency.setSymbol(symbol);
        saveCurrency(currency);
    }

    public void editMaterial(String nameCurrency,String material){
        ICurrency currency = currencyService.getCurrency(nameCurrency);
        if (currency == null){
            throw new CurrencyNotFoundException("Currency not found");
        }
        currencyService.getCurrencies().stream()
                .filter(c -> c.getMaterial() != null && c.getMaterial().equalsIgnoreCase(material))
                .findFirst().ifPresent(c -> {
                    throw new IllegalArgumentException("Material already in use by currency: " + c.getSingular());
                });
        currency.setMaterial(material);
        saveCurrency(currency);
    }

    public void editBase64Item(String nameCurrency,String base64Item){
        ICurrency currency = currencyService.getCurrency(nameCurrency);
        if (currency == null){
            throw new CurrencyNotFoundException("Currency not found");
        }
        currencyService.getCurrencies().stream()
                .filter(c -> c.getBase64Item() != null && c.getBase64Item().equalsIgnoreCase(base64Item))
                .findFirst().ifPresent(c -> {
                    throw new IllegalArgumentException("Base64 item already in use by currency: " + c.getSingular());
                });
        currency.setBase64Item(base64Item);
        saveCurrency(currency);
    }

    public void editPhysicalItemSupported(String nameCurrency, boolean physicalItemSupported){
        ICurrency currency = currencyService.getCurrency(nameCurrency);
        if (currency == null){
            throw new CurrencyNotFoundException("Currency not found");
        }
        currency.setPhysicalItemSupported(physicalItemSupported);
        saveCurrency(currency);
    }

    public void editTexture(String nameCurrency,String texture){
        ICurrency currency = currencyService.getCurrency(nameCurrency);
        if (currency == null){
            throw new CurrencyNotFoundException("Currency not found");
        }
        currency.setTexture(texture);
        saveCurrency(currency);
    }

    public void setDefaultCurrency(String currencyName){
        ICurrency currency = currencyService.getCurrency(currencyName);
        if (currency.isDefaultCurrency()){
            return;
        }
        currencyService.getCurrencies().forEach(c -> {
            if (c.isDefaultCurrency()){
                c.setDefaultCurrency(false);
                saveCurrency(c);
            }
        });
        currency.setDefaultCurrency(true);
        currencyService.updateDefaultCurrency();
        saveCurrency(currency);
    }

    public void setSingularName(String actualName, String newName){
        //todo: cambiar nombre de la moneda, verificar si existe el actualname tanto plural como singualr, y actualizar el mismo campo plural o singular
        ICurrency currency = currencyService.getCurrency(actualName);
        if (currency == null){
            throw new CurrencyNotFoundException("Currency not found");
        }
        currency.setSingular(newName);
        saveCurrency(currency);
    }

    public void setPluralName(String actualName, String newName){
        ICurrency currency = currencyService.getCurrency(actualName);
        if (currency == null){
            throw new CurrencyNotFoundException("Currency not found");
        }
        currency.setPlural(newName);
        saveCurrency(currency);
    }

    public void togglePayable(String currencyName){
        ICurrency currency = currencyService.getCurrency(currencyName);
        if (currency == null){
            throw new CurrencyNotFoundException("Currency not found");
        }
        currency.setTransferable(!currency.isTransferable());
        saveCurrency(currency);
    }

    public void toggleDecimals(String currencyName){
        ICurrency currency = currencyService.getCurrency(currencyName);
        if (currency == null){
            throw new CurrencyNotFoundException("Currency not found");
        }
        currency.setDecimalSupported(!currency.isDecimalSupported());
        saveCurrency(currency);
    }

    public void saveCurrency(ICurrency currency){
        try {
            dataStore.saveCurrency(currency);
            updateForwarder.sendUpdateMessage(Message.builder()
                    .setType(Message.Type.CURRENCY)
                    .setTarget(currency.getUuid())
                    .build());
        }catch (TransactionException e){
            throw new TransactionException("Error saving currency");
        }
    }
}
