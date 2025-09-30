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

package BlockDynasty.Economy.domain.entities.cheque;

public class ChequeStorage {
    /*private String issuer;
    private String currency;
    private double value;
    public static BlockDynastyEconomy plugin;

    public ChequeStorage(String issuer, String currency, double value,BlockDynastyEconomy plugin) {
        this.issuer = issuer;
        this.currency = currency;
        this.value = value;
        ChequeStorage.plugin = plugin;
    }
    public String getIssuer() {
        return issuer;
    }

    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChequeStorage that = (ChequeStorage) o;
        return Objects.equals(issuer, that.issuer) && Objects.equals(currency, that.currency) && Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(issuer, currency, value);
    }
    public static final NamespacedKey key = new NamespacedKey(plugin, "cheque");
    public static ChequeStorage read(ItemStack itemStack){
       ChequeStorage storage = itemStack.getItemMeta().getPersistentDataContainer().get(key,ChequeStorageType.INSTANCE);
       if(storage == null)
           storage = ChequeUpdater.tryUpdate(itemStack);
       return storage;
    }*/

}
