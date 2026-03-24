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

package BlockDynasty.BukkitImplementation.Integrations.vault2;

import BlockDynasty.BukkitImplementation.Integrations.vault.VaultHook;
import com.blockdynasty.economy.Economy;
import com.BlockDynasty.api.DynastyEconomy;
import net.blockdynasty.providers.services.ServiceProvider;
import com.BlockDynasty.api.entity.Currency;
import net.milkbowl.vault.economy.EconomyMultiCurrency;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.OfflinePlayer;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public class Vault2Hook extends VaultHook implements EconomyMultiCurrency {

    public Vault2Hook() {

    }
    private Optional<DynastyEconomy> getApi(){
        return ServiceProvider.get(DynastyEconomy.class, service -> service.getId().equals(Economy.getApiWithVaultLoggerId()));
    }

    @Override
    public List<String> getCurrencies() {
        Optional<DynastyEconomy> api = getApi();
        return api.map(dynastyEconomy ->
                dynastyEconomy.getCurrencies().stream().map(Currency::getSingular).toList())
                .orElseGet(List::of);
    }

    @Override
    public int fractionalDigits(String currencyName) {
        return -1;
    }

    @Override
    public String format(double amount, String currencyName) {
        Optional<DynastyEconomy> apiOptional = getApi();
        if (apiOptional.isEmpty()) {
            return String.valueOf(amount);
        }
            DynastyEconomy api = apiOptional.get();
        return api.format(new BigDecimal(amount),currencyName);
    }

    @Override
    public String currencyNamePlural(String currencyName) {
        Optional<DynastyEconomy> apiOptional = getApi();
        if (apiOptional.isEmpty()) {
            return "unknown";
        }
        DynastyEconomy api = apiOptional.get();
        return api.getNameCurrencyPlural(currencyName);
    }

    @Override
    public String currencyNameSingular(String currencyName) {
        Optional<DynastyEconomy> apiOptional = getApi();
        if (apiOptional.isEmpty()) {
            return "unknown";
        }
        DynastyEconomy api = apiOptional.get();
        return api.getNameCurrencySingular(currencyName);
    }
    @Override
    public double getBalance(OfflinePlayer offlinePlayer, String world, String currencyName) {
        Optional<DynastyEconomy> apiOptional = getApi();
        if (apiOptional.isEmpty()) {
            return 0;
        }
        DynastyEconomy api = apiOptional.get();
        return api.getBalance(offlinePlayer.getName(), currencyName).doubleValue();
    }

    @Override
    public boolean has(OfflinePlayer offlinePlayer, double amount, String currencyName) {
        Optional<DynastyEconomy> apiOptional = getApi();
        if (apiOptional.isEmpty()) {
            return false;
        }
        DynastyEconomy api = apiOptional.get();
        return api.hasAmount(offlinePlayer.getName(), BigDecimal.valueOf(amount), currencyName);
    }

    @Override
    public boolean has(OfflinePlayer offlinePlayer, String world, double amount, String currencyName) {
        return this.has(offlinePlayer, amount, currencyName);
    }

    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer offlinePlayer, double amount, String currencyName) {
        Optional<DynastyEconomy> apiOptional = getApi();
        if (apiOptional.isEmpty()) {
            return new EconomyResponse(amount, 0, EconomyResponse.ResponseType.FAILURE, "Economy API not found");
        }
        DynastyEconomy api = apiOptional.get();
        com.BlockDynasty.api.EconomyResponse resultWithdraw = api.withdraw(offlinePlayer.getName(), BigDecimal.valueOf(amount), currencyName);
        if(resultWithdraw.isSuccess()){
            return new EconomyResponse(amount, getBalance(offlinePlayer), EconomyResponse.ResponseType.SUCCESS, "withdraw success for "+ offlinePlayer.getPlayer());
        }
        return new EconomyResponse(amount, getBalance(offlinePlayer), EconomyResponse.ResponseType.FAILURE, "Transaction error"+resultWithdraw.getErrorMessage());
    }

    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer offlinePlayer, String world, double amount, String currencyName) {
        return this.withdrawPlayer(offlinePlayer, amount, currencyName);
    }

    @Override
    public EconomyResponse depositPlayer(OfflinePlayer offlinePlayer, double amount, String currencyName) {
        Optional<DynastyEconomy> apiOptional = getApi();
        if (apiOptional.isEmpty()) {
            return new EconomyResponse(amount, 0, EconomyResponse.ResponseType.FAILURE, "Economy API not found");
        }
        DynastyEconomy api = apiOptional.get();
        com.BlockDynasty.api.EconomyResponse resultDeposit = api.deposit(offlinePlayer.getName(), BigDecimal.valueOf(amount),currencyName);
        if(resultDeposit.isSuccess()){
            return new EconomyResponse(amount, getBalance(offlinePlayer), EconomyResponse.ResponseType.SUCCESS, "Deposit success for "+ offlinePlayer.getPlayer());
        }
        return new EconomyResponse(amount, getBalance(offlinePlayer), EconomyResponse.ResponseType.FAILURE, "Transaction error"+resultDeposit.getErrorMessage());

    }

    @Override
    public EconomyResponse depositPlayer(OfflinePlayer offlinePlayer, String world, double amount, String currencyName) {
        return this.depositPlayer(offlinePlayer, amount, currencyName);
    }

    @Override
    public EconomyResponse bankBalance(String name, String currencyName) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "BlockDynastyEconomy does not support bank accounts!");
    }

    @Override
    public EconomyResponse bankHas(String name, double amount, String currencyName) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "BlockDynastyEconomy does not support bank accounts!");
    }

    @Override
    public EconomyResponse bankWithdraw(String name, double amount, String currencyName) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "BlockDynastyEconomy does not support bank accounts!");
    }

    @Override
    public EconomyResponse bankDeposit(String name, double amount, String currencyName) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "BlockDynastyEconomy does not support bank accounts!");
    }
}
