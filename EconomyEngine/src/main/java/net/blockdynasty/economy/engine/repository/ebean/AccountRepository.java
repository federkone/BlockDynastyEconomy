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

package net.blockdynasty.economy.engine.repository.ebean;


import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import net.blockdynasty.economy.core.domain.entities.account.Account;
import net.blockdynasty.economy.core.domain.entities.account.Exceptions.AccountAlreadyExist;
import net.blockdynasty.economy.core.domain.entities.account.Exceptions.AccountNotFoundException;
import net.blockdynasty.economy.core.domain.entities.account.Player;
import net.blockdynasty.economy.core.domain.entities.balance.Money;
import net.blockdynasty.economy.core.domain.entities.currency.Exceptions.CurrencyNotFoundException;
import net.blockdynasty.economy.core.domain.persistence.Exceptions.RepositoryException;
import net.blockdynasty.economy.core.domain.persistence.entities.IAccountRepository;
import net.blockdynasty.economy.engine.repository.ebean.Mappers.AccountMapper;
import net.blockdynasty.economy.engine.repository.ebean.Models.AccountDb;
import net.blockdynasty.economy.engine.repository.ebean.Models.BalanceDb;
import net.blockdynasty.economy.engine.repository.ebean.Models.CurrencyDb;
import net.blockdynasty.economy.engine.repository.ebean.Models.WalletDb;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public class AccountRepository implements IAccountRepository {
    @PersistenceContext
    private final io.ebean.Database database;

    public AccountRepository(io.ebean.Database sessionFactory) {
        this.database =sessionFactory;
    }

    @Override
    public List<Account> findAll() {
        try {
            return database.find(AccountDb.class)
                    .setDistinct(true)
                    .fetch("wallet")
                    .findList()
                    .stream()
                    .map(AccountMapper::toDomain)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RepositoryException("Repository Account Error: " + e.getMessage(), e);
        }
    }


@Override
public Account findByUuid(UUID uuid) {
    try {
        AccountDb accountDb = database.find(AccountDb.class)
                .fetch("wallet")
                .fetch("wallet.balances")
                .fetch("wallet.balances.currency")
                .where()
                .eq("uuid", uuid.toString())
                .findOne();

        if (accountDb == null) {
            throw new AccountNotFoundException("Account not found: " + uuid);
        }

        return AccountMapper.toDomain(accountDb);

    } catch (AccountNotFoundException e) {
        throw e;
    } catch (Exception e) {
        throw new RepositoryException("Repository Account Error: " + e.getMessage(), e);
    }
}

    @Override
    public Account findByPlayer(Player player) {
        try {
            AccountDb accountDb = database.find(AccountDb.class)
                    .fetch("wallet")
                    .fetch("wallet.balances")
                    .fetch("wallet.balances.currency")
                    .where()
                    .eq("id", player.getId())
                    .findOne();

            if (accountDb == null) {
                throw new AccountNotFoundException("Account not found: " + player.getNickname());
            }

            return AccountMapper.toDomain(accountDb);

        } catch (AccountNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new RepositoryException("Repository Account Error: " + e.getMessage(), e);
        }
    }

    @Override
    public Account findByNickname(String nickname) {
        try {
            AccountDb accountDb = database.find(AccountDb.class)
                    .fetch("wallet")
                    .fetch("wallet.balances")
                    .fetch("wallet.balances.currency")
                    .where()
                    .eq("nickname", nickname)
                    .findOne();

            if (accountDb == null) {
                throw new AccountNotFoundException("Account not found: " + nickname);
            }

            return AccountMapper.toDomain(accountDb);

        } catch (AccountNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new RepositoryException("Repository Account error: " + e.getMessage(), e);
        }
    }
    @Override
    public void save(Account account) {
        save(account.getPlayer(), account);
    }

    @Override
    public void save(Player account, Account newAccount) {
        System.out .println("Saving account: " + account.getNickname() + " with UUID: " + account.getUuid()+ " id "+ account.getId()); //account id no deberia ser null
        try (io.ebean.Transaction tx = database.beginTransaction()) {

            AccountDb accountDb = database.find(AccountDb.class)
                    .fetch("wallet")
                    .fetch("wallet.balances")
                    .where()
                    .eq("id", account.getId())
                    .findOne();

            if (accountDb == null) {
                throw new AccountNotFoundException("Account not found: " + account.getUuid());
            }

            accountDb.setNickname(newAccount.getNickname());
            accountDb.setUuid(newAccount.getUuid().toString());
            accountDb.setCanReceiveCurrency(newAccount.canReceiveCurrency());
            accountDb.setBlock(newAccount.isBlocked());

            updateBalancesInWallet(newAccount, accountDb.getWallet(), database);

            database.save(accountDb);

            tx.commit();
        } catch (AccountNotFoundException e) {
            System.out.println( "Account not found during save: " + account.getUuid());
            throw e;
        } catch (Exception e) {
            throw new RepositoryException("Repository accounts error: " + e.getMessage(), e);
        }
    }

    private void updateBalancesInWallet(Account account, WalletDb walletDb, io.ebean.Database database) {
        for (Money money : account.getBalances()) {
            String currencyUuid = money.getCurrency().getUuid().toString();

            Optional<BalanceDb> existingBalance = walletDb.getBalances().stream()
                    .filter(b -> b.getCurrency().getUuid().equals(currencyUuid))
                    .findFirst();

            if (existingBalance.isPresent()) {
                BalanceDb balanceToUpdate = existingBalance.get();
                balanceToUpdate.setAmount(money.getAmount());

                // --- SOLUCIÓN: Guardar explícitamente la actualización ---
                database.save(balanceToUpdate);
            } else {
                CurrencyDb currencyDb = database.find(CurrencyDb.class)
                        .where()
                        .eq("uuid", currencyUuid)
                        .findOne();

                if (currencyDb == null) {
                    throw new CurrencyNotFoundException("Currency not found: " + currencyUuid);
                }

                BalanceDb newBalance = new BalanceDb();
                newBalance.setCurrency(currencyDb);
                newBalance.setAmount(money.getAmount());
                newBalance.setWallet(walletDb);

                walletDb.getBalances().add(newBalance);
                database.save(newBalance);
            }
        }
    }

    @Override
    public void delete(Player account) {
        try (io.ebean.Transaction tx = database.beginTransaction()) {

            AccountDb accountDb = database.find(AccountDb.class)
                    .fetch("wallet")
                    .where()
                    .eq("id", account.getId())
                    .findOne();

            if (accountDb == null) {
                throw new AccountNotFoundException("Account not found: " + account.getUuid());
            }

            database.delete(accountDb);

            tx.commit();
        } catch (AccountNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new RepositoryException("Error repositorio: " + e.getMessage(), e);
        }
    }
    @Override
    public void update(Account account) {
        save(account.getPlayer(), account);
    }
    @Override
    public void update(Player account, Account newStateAccount) {
        save(account,newStateAccount);
    }

    @Override
    public void create(Account account) {
        try (io.ebean.Transaction tx = database.beginTransaction()) {

            int count = database.find(AccountDb.class)
                    .where()
                    .eq("uuid", account.getUuid().toString())
                    .eq("nickname", account.getNickname())
                    .findCount();

            if (count > 0) {
                throw new AccountAlreadyExist("Account already exist: " + account.getUuid());
            }

            AccountDb accountDb = new AccountDb();
            accountDb.setUuid(account.getUuid().toString());
            accountDb.setNickname(account.getNickname());
            accountDb.setCanReceiveCurrency(account.canReceiveCurrency());

            WalletDb walletDb = new WalletDb();
            database.save(walletDb);
            accountDb.setWallet(walletDb);

            for (Money domainMoney : account.getBalances()) {
                String currencyUuid = domainMoney.getCurrency().getUuid().toString();

                CurrencyDb currencyDb = database.find(CurrencyDb.class)
                        .where()
                        .eq("uuid", currencyUuid)
                        .findOne();

                if (currencyDb == null) {
                    throw new CurrencyNotFoundException("Currency not found: " + currencyUuid);
                }

                BalanceDb balanceDb = new BalanceDb();
                balanceDb.setCurrency(currencyDb);
                balanceDb.setAmount(domainMoney.getAmount());
                balanceDb.setWallet(walletDb);

                walletDb.getBalances().add(balanceDb);
                database.save(balanceDb);
            }

            database.save(accountDb);

            tx.commit();

            account.setId(accountDb.getId());
            System.out. println("Account created with ID: " + account.getId());
        } catch (AccountAlreadyExist | CurrencyNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new RepositoryException("Repository Accounts Error: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Account> getAccountsTopByCurrency(String currencyName, int limit, int offset) {
        try {
            List<BalanceDb> topBalances = database.find(BalanceDb.class)
                    .fetch("wallet")
                    .fetch("wallet.accounts")
                    .fetch("currency")
                    .where()
                    .or()
                    .eq("currency.singular", currencyName)
                    .eq("currency.plural", currencyName)
                    .endOr()
                    .orderBy("amount desc") // Ahora ordenamos por una propiedad directa del BalanceDb
                    .setFirstRow(offset)
                    .setMaxRows(limit)
                    .findList();

            return topBalances.stream()
                    .flatMap(balance -> balance.getWallet().getAccounts().stream())
                    .distinct() // Evita duplicados si una wallet tiene varias cuentas
                    .limit(limit)
                    .map(AccountMapper::toDomain)
                    .collect(Collectors.toList());

        } catch (Exception e) {
            throw new RepositoryException("Error retrieving accounts by currency", e);
        }
    }
}