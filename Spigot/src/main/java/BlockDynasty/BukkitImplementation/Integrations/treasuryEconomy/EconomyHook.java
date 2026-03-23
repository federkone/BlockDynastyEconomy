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
package BlockDynasty.BukkitImplementation.Integrations.treasuryEconomy;

import BlockDynasty.BukkitImplementation.Integrations.treasuryEconomy.accounts.AccountTreasury;
import BlockDynasty.BukkitImplementation.Integrations.treasuryEconomy.currency.CurrencyTreasury;
import com.BlockDynasty.api.DynastyEconomy;
import com.BlockDynasty.api.EconomyResponse;
import com.BlockDynasty.api.entity.Account;
import me.lokka30.treasury.api.common.NamespacedKey;
import me.lokka30.treasury.api.common.misc.TriState;
import me.lokka30.treasury.api.economy.EconomyProvider;
import me.lokka30.treasury.api.economy.account.AccountData;
import me.lokka30.treasury.api.economy.account.AccountPermission;
import me.lokka30.treasury.api.economy.account.NonPlayerAccount;
import me.lokka30.treasury.api.economy.account.accessor.AccountAccessor;
import me.lokka30.treasury.api.economy.currency.Currency;
import net.blockdynasty.providers.services.ServiceProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.CompletableFuture;

public class EconomyHook implements EconomyProvider {

    public EconomyHook() {

    }
    private Optional<DynastyEconomy> getApi(){
        return ServiceProvider.get(DynastyEconomy.class, service -> service.getId().equals(com.blockdynasty.economy.Economy.getApiWithVaultLoggerId()));
    }


    @Override
    public @NotNull AccountAccessor accountAccessor() {
        return new AccountTreasury();
    }

    @Override
    public @NotNull CompletableFuture<Boolean> hasAccount(@NotNull AccountData accountData) {
        Optional<DynastyEconomy> optionalApi = getApi();
        if (optionalApi.isEmpty()) {
            return CompletableFuture.completedFuture(false);
        }
        DynastyEconomy api = optionalApi.get();
        Optional<UUID> optional =accountData.getPlayerIdentifier();
        if (optional.isPresent()) {
            UUID playerId = optional.get();
            Account a = api.getAccount(playerId);
            if (a != null) {
                return CompletableFuture.completedFuture(true);
            }
        }
        return CompletableFuture.completedFuture(false);
    }

    @Override
    public @NotNull CompletableFuture<Collection<UUID>> retrievePlayerAccountIds() {
        Optional<DynastyEconomy> optionalApi = getApi();
        if (optionalApi.isEmpty()) {
            return CompletableFuture.completedFuture(Collections.emptyList());
        }
        DynastyEconomy api = optionalApi.get();
        List<Account> accounts = api.getAccountsOffline();
        return CompletableFuture.completedFuture(accounts.stream().map(Account::getUuid).toList());
    }

    @Override
    public @NotNull CompletableFuture<Collection<NamespacedKey>> retrieveNonPlayerAccountIds() {
        Optional<DynastyEconomy> optionalApi = getApi();
        if (optionalApi.isEmpty()) {
            return CompletableFuture.completedFuture(Collections.emptyList());
        }
        DynastyEconomy api = optionalApi.get();
        List<Account> accounts = api.getAccountsOffline();
        List<String> list = accounts.stream().map(Account::getName).toList();
        return CompletableFuture.completedFuture(list.stream().map(name -> NamespacedKey.of("blockdynasty",name)).toList());
    }

    @Override
    public @NotNull CompletableFuture<Collection<NonPlayerAccount>> retrieveAllAccountsPlayerIsMemberOf(@NotNull UUID playerId) {
        return EconomyProvider.super.retrieveAllAccountsPlayerIsMemberOf(playerId);
    }

    @Override
    public @NotNull CompletableFuture<Collection<NonPlayerAccount>> retrieveAllAccountsPlayerHasPermissions(@NotNull UUID playerId, @NotNull AccountPermission @NotNull ... permissions) {
        return EconomyProvider.super.retrieveAllAccountsPlayerHasPermissions(playerId, permissions);
    }

    @Override
    public @NotNull Currency getPrimaryCurrency() {
        Optional<DynastyEconomy> optionalApi = getApi();
        if (optionalApi.isEmpty()) {
            return new CurrencyTreasury(null);
        }
        return new CurrencyTreasury(optionalApi.get().getDefaultCurrency());
    }

    @Override
    public @NotNull Optional<Currency> findCurrency(@NotNull String identifier) {
        Optional<DynastyEconomy> optionalApi = getApi();
        if (optionalApi.isEmpty()) {
            return Optional.empty();
        }
        DynastyEconomy api = optionalApi.get();
        com.BlockDynasty.api.entity.Currency c = api.getCurrency(identifier);
        if (c == null) {
            return Optional.empty();
        }
        return Optional.of(new CurrencyTreasury(api.getCurrency(identifier)));
    }

    @Override
    public @NotNull Optional<Currency> findCurrencyByDisplayName(@NotNull String displayName, @NotNull BigDecimal value, @Nullable Locale locale) {
        return EconomyProvider.super.findCurrencyByDisplayName(displayName, value, locale);
    }

    @Override
    public @NotNull Set<Currency> getCurrencies() {
        Optional<DynastyEconomy> optionalApi = getApi();
        if (optionalApi.isEmpty()) {
            return Set.of();
        }
        DynastyEconomy api = optionalApi.get();
        List< com.BlockDynasty.api.entity.Currency> c= api.getCurrencies();
        if (c != null) {
            Set<Currency> currencies = new HashSet<>();
            for ( com.BlockDynasty.api.entity.Currency currency : c) {
                currencies.add(new CurrencyTreasury(currency));
            }
            return currencies;
        }
        return Set.of();
    }

    @Override
    public @NotNull String getPrimaryCurrencyId() {
        return EconomyProvider.super.getPrimaryCurrencyId();
    }

    @Override
    public @NotNull CompletableFuture<TriState> registerCurrency(@NotNull Currency currency) {
        Optional<DynastyEconomy> optionalApi = getApi();
        if (optionalApi.isEmpty()) {
            return CompletableFuture.completedFuture(TriState.FALSE);
        }
        DynastyEconomy api = optionalApi.get();
        EconomyResponse response = api.createCurrency(currency.getDisplayName(BigDecimal.ZERO,Locale.getDefault()),currency.getDisplayName(BigDecimal.ZERO,Locale.getDefault()));
        if (response.isSuccess()){
            return CompletableFuture.completedFuture(TriState.TRUE);
        }else {
            return CompletableFuture.completedFuture(TriState.FALSE);
        }
    }

    @Override
    public @NotNull CompletableFuture<TriState> unregisterCurrency(@NotNull Currency currency) {
        Optional<DynastyEconomy> optionalApi = getApi();
        if (optionalApi.isEmpty()) {
            return CompletableFuture.completedFuture(TriState.FALSE);
        }
        DynastyEconomy api = optionalApi.get();
        EconomyResponse response = api.deleteCurrency(currency.getDisplayName(BigDecimal.ZERO,Locale.getDefault()));
        if (response.isSuccess()){
            return CompletableFuture.completedFuture(TriState.TRUE);
        }else {
            return CompletableFuture.completedFuture(TriState.FALSE);
        }
    }
}
