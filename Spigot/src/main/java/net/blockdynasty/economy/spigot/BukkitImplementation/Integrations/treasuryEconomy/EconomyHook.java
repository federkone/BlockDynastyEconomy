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
package net.blockdynasty.economy.spigot.BukkitImplementation.Integrations.treasuryEconomy;

import net.blockdynasty.economy.api.DynastyEconomy;
import net.blockdynasty.economy.api.EconomyResponse;
import net.blockdynasty.economy.api.entity.Account;
import net.blockdynasty.economy.spigot.BukkitImplementation.Integrations.treasuryEconomy.accounts.AccountTreasury;
import net.blockdynasty.economy.spigot.BukkitImplementation.Integrations.treasuryEconomy.currency.CurrencyTreasury;
import net.blockdynasty.economy.engine.Economy;
import me.lokka30.treasury.api.common.NamespacedKey;
import me.lokka30.treasury.api.common.misc.TriState;
import me.lokka30.treasury.api.economy.EconomyProvider;
import me.lokka30.treasury.api.economy.account.AccountData;
import me.lokka30.treasury.api.economy.account.AccountPermission;
import me.lokka30.treasury.api.economy.account.NonPlayerAccount;
import me.lokka30.treasury.api.economy.account.accessor.AccountAccessor;
import me.lokka30.treasury.api.economy.currency.Currency;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.CompletableFuture;

public class EconomyHook implements EconomyProvider {
    private DynastyEconomy api;
    public EconomyHook() {
        Optional<DynastyEconomy> apiOptional =  Economy.getApi();
        if (apiOptional.isEmpty()) {
            throw new IllegalStateException("DynastyEconomy API not found. Make sure it is registered correctly.");
        }
        this.api = apiOptional.get();
    }

    @Override
    public @NotNull AccountAccessor accountAccessor() {
        return new AccountTreasury();
    }

    @Override
    public @NotNull CompletableFuture<Boolean> hasAccount(@NotNull AccountData accountData) {
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
        List<Account> accounts = api.getAccountsOffline();
        return CompletableFuture.completedFuture(accounts.stream().map(Account::getUuid).toList());
    }

    @Override
    public @NotNull CompletableFuture<Collection<NamespacedKey>> retrieveNonPlayerAccountIds() {
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
        return new CurrencyTreasury(api.getDefaultCurrency());
    }

    @Override
    public @NotNull Optional<Currency> findCurrency(@NotNull String identifier) {
        net.blockdynasty.economy.api.entity.Currency c = api.getCurrency(identifier);
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
        List<net.blockdynasty.economy.api.entity.Currency> c= api.getCurrencies();
        if (c != null) {
            Set<Currency> currencies = new HashSet<>();
            for (net.blockdynasty.economy.api.entity.Currency currency : c) {
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
        EconomyResponse response = api.createCurrency(currency.getDisplayName(BigDecimal.ZERO,Locale.getDefault()),currency.getDisplayName(BigDecimal.ZERO,Locale.getDefault()));
        if (response.isSuccess()){
            return CompletableFuture.completedFuture(TriState.TRUE);
        }else {
            return CompletableFuture.completedFuture(TriState.FALSE);
        }
    }

    @Override
    public @NotNull CompletableFuture<TriState> unregisterCurrency(@NotNull Currency currency) {
        EconomyResponse response = api.deleteCurrency(currency.getDisplayName(BigDecimal.ZERO,Locale.getDefault()));
        if (response.isSuccess()){
            return CompletableFuture.completedFuture(TriState.TRUE);
        }else {
            return CompletableFuture.completedFuture(TriState.FALSE);
        }
    }
}
