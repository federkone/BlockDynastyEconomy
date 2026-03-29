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
package BlockDynasty.BukkitImplementation.Integrations.treasuryEconomy.accounts;

import com.BlockDynasty.api.EconomyResponse;
import com.BlockDynasty.api.entity.Account;
import com.blockdynasty.economy.Economy;
import me.lokka30.treasury.api.economy.account.PlayerAccount;
import me.lokka30.treasury.api.economy.currency.Currency;
import me.lokka30.treasury.api.economy.transaction.EconomyTransaction;
import me.lokka30.treasury.api.economy.transaction.EconomyTransactionType;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.time.temporal.Temporal;
import java.util.*;
import java.util.concurrent.CompletableFuture;

public class PlayerAccountTreasury implements PlayerAccount {
    private UUID accountUUID;
    private DynastyEconomy api;

    public PlayerAccountTreasury( UUID accountUUID) {
        this.accountUUID = accountUUID;
        Optional<DynastyEconomy> apiOptional =  Economy.getApi();
        if (apiOptional.isEmpty()) {
            throw new IllegalStateException("DynastyEconomy API not found. Make sure it is registered correctly.");
        }
        this.api = apiOptional.get();
    }
    @Override
    public @NotNull UUID identifier() {
        return this.accountUUID;
    }

    @Override
    public @NotNull Optional<String> getName() {
        return Optional.of(api.getAccount(accountUUID).getName());
    }

    @Override
    public @NotNull CompletableFuture<BigDecimal> retrieveBalance(@NotNull Currency currency) {
        return CompletableFuture.completedFuture(api.getBalance(accountUUID, currency.getIdentifier()));
    }

    @Override
    public @NotNull CompletableFuture<BigDecimal> doTransaction(@NotNull EconomyTransaction economyTransaction) {
        EconomyTransactionType type = economyTransaction.getType();
        //deposit
        switch (type) {
            case DEPOSIT -> {
                api.deposit(accountUUID, economyTransaction.getAmount() , economyTransaction.getCurrencyId());
            }
            case WITHDRAWAL -> {
                api.withdraw(accountUUID, economyTransaction.getAmount() , economyTransaction.getCurrencyId());
            }
            case SET -> {
                api.setBalance(accountUUID, economyTransaction.getAmount() , economyTransaction.getCurrencyId());
            }
        }
        return CompletableFuture.completedFuture(BigDecimal.ZERO);
    }

    @Override
    public @NotNull CompletableFuture<Boolean> deleteAccount() {
        EconomyResponse response = api.deleteAccount(accountUUID);
        if (response.isSuccess()) {
            return CompletableFuture.completedFuture(true);
        }else {
            return CompletableFuture.completedFuture(false);
        }
    }

    @Override
    public @NotNull CompletableFuture<Collection<String>> retrieveHeldCurrencies() { //monedas que tiene la cuenta
        Account account = api.getAccount(accountUUID);
        List<String> currencies= account.getBalances().stream().map( money -> money.getCurrency().getSingular()).toList();
        return CompletableFuture.completedFuture(currencies);
    }

    @Override
    public @NotNull CompletableFuture<Collection<EconomyTransaction>> retrieveTransactionHistory(int transactionCount, @NotNull Temporal from, @NotNull Temporal to) {
        return CompletableFuture.completedFuture(null);
    }
}
