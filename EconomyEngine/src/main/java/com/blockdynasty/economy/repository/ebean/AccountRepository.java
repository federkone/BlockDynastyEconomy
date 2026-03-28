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

/*package com.blockdynasty.economy.repository.ebean;

import BlockDynasty.Economy.domain.entities.account.Account;
import BlockDynasty.Economy.domain.entities.account.Exceptions.AccountAlreadyExist;
import BlockDynasty.Economy.domain.entities.account.Exceptions.AccountNotFoundException;
import BlockDynasty.Economy.domain.entities.account.Player;
import BlockDynasty.Economy.domain.entities.balance.Money;
import BlockDynasty.Economy.domain.entities.currency.Exceptions.CurrencyNotFoundException;
import BlockDynasty.Economy.domain.persistence.Exceptions.RepositoryException;
import BlockDynasty.Economy.domain.persistence.entities.IAccountRepository;
import com.blockdynasty.economy.repository.ebean.Mappers.AccountMapper;
import com.blockdynasty.economy.repository.ebean.Models.AccountDb;
import com.blockdynasty.economy.repository.ebean.Models.BalanceDb;
import com.blockdynasty.economy.repository.ebean.Models.CurrencyDb;
import com.blockdynasty.economy.repository.ebean.Models.WalletDb;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;

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
            // Ebean usa una API fluida que reemplaza al HQL
            return database.find(AccountDb.class)
                    .setDistinct(true) // Equivalente al SELECT DISTINCT
                    .fetch("wallet")   // Join optimizado (Eager loading) si lo necesitas
                    .findList()        // Ejecuta la consulta y devuelve List<AccountDb>
                    .stream()
                    .map(AccountMapper::toDomain)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            // En Ebean, si no inicias una transacción explícita para leer,
            // no hay nada que hacer rollback. Solo capturamos el error.
            throw new RepositoryException("Repository Account Error: " + e.getMessage(), e);
        }
    }


@Override
public Account findByUuid(UUID uuid) {
    try {
        // La API fluida de Ebean reemplaza al HQL complejo
        AccountDb accountDb = database.find(AccountDb.class)
                .fetch("wallet")              // LEFT JOIN FETCH a.wallet
                .fetch("wallet.balances")     // LEFT JOIN FETCH w.balances
                .fetch("wallet.balances.currency") // LEFT JOIN FETCH b.currency
                .where()
                .eq("uuid", uuid.toString())
                .findOne(); // Devuelve null si no hay resultados, en lugar de lanzar excepción

        if (accountDb == null) {
            throw new AccountNotFoundException("Account not found: " + uuid);
        }

        return AccountMapper.toDomain(accountDb);

    } catch (AccountNotFoundException e) {
        throw e; // Relanzamos nuestra excepción personalizada
    } catch (Exception e) {
        // Ebean no requiere rollback manual en consultas de lectura (SELECT)
        throw new RepositoryException("Repository Account Error: " + e.getMessage(), e);
    }
}

    @Override
    public Account findByPlayer(Player player) {
        try {
            // Usamos la API fluida para cargar la cuenta y toda su jerarquía
            AccountDb accountDb = database.find(AccountDb.class)
                    .fetch("wallet")
                    .fetch("wallet.balances")
                    .fetch("wallet.balances.currency")
                    .where()
                    .eq("id", player.getId()) // Buscamos por el ID del player
                    .findOne(); // Retorna null si no existe, evitando excepciones costosas

            if (accountDb == null) {
                throw new AccountNotFoundException("Account not found: " + player.getNickname());
            }

            return AccountMapper.toDomain(accountDb);

        } catch (AccountNotFoundException e) {
            throw e;
        } catch (Exception e) {
            // No hace falta rollback en SELECTs simples con Ebean
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
        // Ebean maneja la transacción automáticamente con un bloque try-with-resources
        try (io.ebean.Transaction tx = database.beginTransaction()) {

            AccountDb accountDb = database.find(AccountDb.class)
                    .fetch("wallet")
                    .fetch("wallet.balances")
                    .fetch("wallet.balances.currency")
                    .where()
                    .eq("id", account.getId())
                    .findOne();

            if (accountDb == null) {
                throw new AccountNotFoundException("Account not found: " + account.getUuid());
            }

            // Update basic properties
            accountDb.setNickname(newAccount.getNickname());
            accountDb.setUuid(newAccount.getUuid().toString());
            accountDb.setCanReceiveCurrency(newAccount.canReceiveCurrency());
            accountDb.setBlock(newAccount.isBlocked());

            // Actualización de balances (Asegúrate de que updateBalancesInWallet ahora reciba el objeto Database o Transaction de Ebean)
            updateBalancesInWallet(newAccount, accountDb.getWallet(), database);

            // Guardamos los cambios en la entidad principal (y cascada si está configurada)
            database.save(accountDb);

            tx.commit();
        } catch (AccountNotFoundException e) {
            throw e;
        } catch (Exception e) {
            // El rollback es automático al cerrar el try-with-resources si no se llamó a tx.commit()
            throw new RepositoryException("Repository accounts error: " + e.getMessage(), e);
        }
    }
    // Helper method similar to what's in TransactionRepository
    private void updateBalancesInWallet(Account account, WalletDb walletDb, io.ebean.Database database) {
        for (Money money : account.getBalances()) {
            String currencyUuid = money.getCurrency().getUuid().toString();

            // Buscamos si ya existe el balance en la colección cargada
            Optional<BalanceDb> existingBalance = walletDb.getBalances().stream()
                    .filter(b -> b.getCurrency().getUuid().equals(currencyUuid))
                    .findFirst();

            if (existingBalance.isPresent()) {
                // Actualizamos el monto existente
                existingBalance.get().setAmount(money.getAmount());
            } else {
                // Buscamos la moneda en la base de datos para crear el nuevo balance
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

                // Añadimos a la lista (Ebean persistirá esto por cascada si está configurado,
                // o puedes llamar a database.save(newBalance) explícitamente)
                walletDb.getBalances().add(newBalance);
            }
        }
    }

    @Override
    public void delete(Player account) {
        // Ebean simplifica el borrado; puedes borrar por ID directamente o por entidad
        try (io.ebean.Transaction tx = database.beginTransaction()) {

            // Buscamos la entidad para asegurar que existe (y cargar cascadas si es necesario)
            AccountDb accountDb = database.find(AccountDb.class)
                    .fetch("wallet")
                    .where()
                    .eq("id", account.getId())
                    .findOne();

            if (accountDb == null) {
                throw new AccountNotFoundException("Account not found: " + account.getUuid());
            }

            // Borrado físico de la entidad
            database.delete(accountDb);

            tx.commit();
        } catch (AccountNotFoundException e) {
            throw e;
        } catch (Exception e) {
            // Rollback automático al cerrar el try-with-resources sin commit
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

            // 1. Verificar existencia (Sintaxis fluida de Ebean)
            int count = database.find(AccountDb.class)
                    .where()
                    .eq("uuid", account.getUuid().toString())
                    .eq("nickname", account.getNickname())
                    .findCount();

            if (count > 0) {
                throw new AccountAlreadyExist("Account already exist: " + account.getUuid());
            }

            // 2. Crear nueva cuenta
            AccountDb accountDb = new AccountDb();
            accountDb.setUuid(account.getUuid().toString());
            accountDb.setNickname(account.getNickname());
            accountDb.setCanReceiveCurrency(account.canReceiveCurrency());

            // 3. Crear nueva Wallet
            WalletDb walletDb = new WalletDb();
            // En Ebean, si tienes CascadeType.ALL, no necesitas persistir la wallet antes.
            // Pero para mantener tu lógica exacta:
            database.save(walletDb);

            accountDb.setWallet(walletDb);

            // 4. Procesar balances
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

                // Añadir a la lista de la wallet persistida
                walletDb.getBalances().add(balanceDb);
            }

            // 5. Guardar la cuenta (Persistencia final)
            database.save(accountDb);

            tx.commit();

            // Sincronizar ID generado de vuelta al dominio
            account.setId(accountDb.getId());

        } catch (AccountAlreadyExist | CurrencyNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new RepositoryException("Repository Accounts Error: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Account> getAccountsTopByCurrency(String currencyName, int limit, int offset) {
        try {
            // Ebean traduce automáticamente los joins en la API fluida
            List<AccountDb> accountDbs = database.find(AccountDb.class)
                    .fetch("wallet")
                    .fetch("wallet.balances")
                    .fetch("wallet.balances.currency")
                    .where()
                    .or()
                    .eq("wallet.balances.currency.singular", currencyName)
                    .eq("wallet.balances.currency.plural", currencyName)
                    .endOr()
                    .orderBy("wallet.balances.amount desc")
                    .setFirstRow(offset) // Equivalente a setFirstResult
                    .setMaxRows(limit)   // Equivalente a setMaxResults
                    .findList();

            return accountDbs.stream()
                    .map(AccountMapper::toDomain)
                    .collect(Collectors.toList());

        } catch (Exception e) {
            // En Ebean, las consultas de lectura no requieren rollback manual
            throw new RepositoryException("Error retrieving accounts by currency", e);
        }
    }
}*/