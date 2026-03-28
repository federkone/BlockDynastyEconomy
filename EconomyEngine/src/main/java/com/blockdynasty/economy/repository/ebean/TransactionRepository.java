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
/*
package com.blockdynasty.economy.repository.ebean;

import BlockDynasty.Economy.domain.entities.account.Account;
import BlockDynasty.Economy.domain.entities.account.Player;
import BlockDynasty.Economy.domain.entities.balance.Money;
import BlockDynasty.Economy.domain.entities.currency.Exceptions.CurrencyNotFoundException;
import BlockDynasty.Economy.domain.entities.currency.ICurrency;
import BlockDynasty.Economy.domain.persistence.transaction.ITransactions;
import BlockDynasty.Economy.domain.result.ErrorCode;
import BlockDynasty.Economy.domain.result.Result;
import BlockDynasty.Economy.domain.result.TransferResult;
import com.blockdynasty.economy.repository.ebean.Mappers.AccountMapper;
import com.blockdynasty.economy.repository.ebean.Models.AccountDb;
import com.blockdynasty.economy.repository.ebean.Models.BalanceDb;
import com.blockdynasty.economy.repository.ebean.Models.CurrencyDb;
import com.blockdynasty.economy.repository.ebean.Models.WalletDb;
import jakarta.persistence.LockModeType;
import jakarta.persistence.NoResultException;

import java.math.BigDecimal;
import java.util.Optional;

public class TransactionRepository  implements ITransactions {
    private final io.ebean.Database database;
    public TransactionRepository(io.ebean.Database sessionFactory) {
        this.database = sessionFactory;
    }

    @Override
    public Result<TransferResult> transfer(String fromUuid, String toUuid, ICurrency currency, BigDecimal amount) {
        try (io.ebean.Transaction tx = database.beginTransaction()) {
            // En Ebean, forUpdate() aplica el bloqueo pesimista (PESSIMISTIC_WRITE)
            AccountDb fromDb = database.find(AccountDb.class)
                    .fetch("wallet")
                    .fetch("wallet.balances")
                    .fetch("wallet.balances.currency")
                    .where().eq("uuid", fromUuid)
                    .forUpdate()
                    .findOne();

            AccountDb toDb = database.find(AccountDb.class)
                    .fetch("wallet")
                    .fetch("wallet.balances")
                    .fetch("wallet.balances.currency")
                    .where().eq("uuid", toUuid)
                    .forUpdate()
                    .findOne();

            if (fromDb == null || toDb == null) {
                return Result.failure("Cuenta no encontrada", ErrorCode.ACCOUNT_NOT_FOUND);
            }

            Account from = AccountMapper.toDomain(fromDb);
            Account to = AccountMapper.toDomain(toDb);

            // Lógica de dominio
            Result<Void> result = from.subtract(currency, amount);
            if (!result.isSuccess()) {
                return Result.failure(result.getErrorMessage(), result.getErrorCode());
            }
            to.add(currency, amount);

            // Sincronizar balances con las entidades y persistir
            updateBalancesInDb(from, fromDb, database);
            updateBalancesInDb(to, toDb, database);

            database.save(fromDb);
            database.save(toDb);

            tx.commit();
            return Result.success(new TransferResult(from, to));

        } catch (Exception e) {
            // El rollback es automático al no hacer tx.commit() dentro del try-with-resources
            return Result.failure("Error en la transferencia: " + e.getMessage(), ErrorCode.UNKNOWN_ERROR);
        }
    }

    @Override
    public Result<TransferResult> transfer(Player playerFrom, Player playerTo, ICurrency currency, BigDecimal amount) {
        try (io.ebean.Transaction tx = database.beginTransaction()) {

            // Uso de forUpdate() para bloqueo pesimista basado en el ID del Player
            AccountDb fromDb = database.find(AccountDb.class)
                    .fetch("wallet")
                    .fetch("wallet.balances")
                    .fetch("wallet.balances.currency")
                    .where().eq("id", playerFrom.getId())
                    .forUpdate()
                    .findOne();

            AccountDb toDb = database.find(AccountDb.class)
                    .fetch("wallet")
                    .fetch("wallet.balances")
                    .fetch("wallet.balances.currency")
                    .where().eq("id", playerTo.getId())
                    .forUpdate()
                    .findOne();

            if (fromDb == null || toDb == null) {
                return Result.failure("Cuenta no encontrada", ErrorCode.ACCOUNT_NOT_FOUND);
            }

            Account from = AccountMapper.toDomain(fromDb);
            Account to = AccountMapper.toDomain(toDb);

            // Lógica de dominio
            Result<Void> result = from.subtract(currency, amount);
            if (!result.isSuccess()) {
                return Result.failure(result.getErrorMessage(), result.getErrorCode());
            }
            to.add(currency, amount);

            // Sincronización y persistencia
            updateBalancesInDb(from, fromDb, database);
            updateBalancesInDb(to, toDb, database);

            database.save(fromDb);
            database.save(toDb);

            tx.commit();
            return Result.success(new TransferResult(from, to));

        } catch (Exception e) {
            // Rollback automático al cerrar el try-with-resources sin commit()
            return Result.failure("Error en la transferencia: " + e.getMessage(), ErrorCode.UNKNOWN_ERROR);
        }
    }

    // Helper method to update balances in the database
    private void updateBalancesInDb(Account account, AccountDb accountDb, io.ebean.Database database) {
        WalletDb walletDb = accountDb.getWallet();

        for (Money money : account.getBalances()) {
            String currencyUuid = money.getCurrency().getUuid().toString();

            // Buscar el balance existente en la colección cargada de la Wallet
            Optional<BalanceDb> existingBalance = walletDb.getBalances().stream()
                    .filter(b -> b.getCurrency().getUuid().equals(currencyUuid))
                    .findFirst();

            if (existingBalance.isPresent()) {
                // Actualizar monto del balance existente
                existingBalance.get().setAmount(money.getAmount());
            } else {
                // Buscar la moneda en la DB para crear el nuevo balance
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

                // Al añadirlo a la lista, Ebean lo persistirá por cascada al guardar la cuenta/wallet
                walletDb.getBalances().add(newBalance);
            }
        }
    }

    @Override
    public Result<Account> withdraw(String accountUuid, ICurrency currency, BigDecimal amount) {
        try (io.ebean.Transaction tx = database.beginTransaction()) {

            // Bloqueo pesimista con forUpdate()
            AccountDb accountDb = database.find(AccountDb.class)
                    .fetch("wallet")
                    .fetch("wallet.balances")
                    .fetch("wallet.balances.currency")
                    .where().eq("uuid", accountUuid)
                    .forUpdate()
                    .findOne();

            if (accountDb == null) {
                return Result.failure("Cuenta no encontrada", ErrorCode.ACCOUNT_NOT_FOUND);
            }

            Account account = AccountMapper.toDomain(accountDb);
            Result<Void> result = account.subtract(currency, amount);

            if (!result.isSuccess()) {
                // El rollback es automático al salir del bloque sin commit
                return Result.failure(result.getErrorMessage(), result.getErrorCode());
            }

            // Actualización de balances persistentes
            updateBalancesInDb(account, accountDb, database);

            database.save(accountDb);
            tx.commit();

            return Result.success(account);

        } catch (Exception e) {
            return Result.failure("Error en el retiro: " + e.getMessage(), ErrorCode.UNKNOWN_ERROR);
        }
    }

    @Override
    public Result<Account> withdraw(Player player, ICurrency currency, BigDecimal amount) {
        try (io.ebean.Transaction tx = database.beginTransaction()) {

            // Búsqueda con bloqueo pesimista por ID de la entidad
            AccountDb accountDb = database.find(AccountDb.class)
                    .fetch("wallet")
                    .fetch("wallet.balances")
                    .fetch("wallet.balances.currency")
                    .where().eq("id", player.getId())
                    .forUpdate()
                    .findOne();

            if (accountDb == null) {
                return Result.failure("Cuenta no encontrada", ErrorCode.ACCOUNT_NOT_FOUND);
            }

            Account account = AccountMapper.toDomain(accountDb);
            Result<Void> result = account.subtract(currency, amount);

            if (!result.isSuccess()) {
                return Result.failure(result.getErrorMessage(), result.getErrorCode());
            }

            updateBalancesInDb(account, accountDb, database);

            database.save(accountDb);
            tx.commit();

            return Result.success(account);

        } catch (Exception e) {
            return Result.failure("Error en el retiro: " + e.getMessage(), ErrorCode.UNKNOWN_ERROR);
        }
    }

    @Override
    public Result<Account> deposit(String accountUuid, ICurrency currency, BigDecimal amount) {
        try (io.ebean.Transaction tx = database.beginTransaction()) {

            // Bloqueo pesimista con forUpdate() para asegurar la atomicidad del depósito
            AccountDb accountDb = database.find(AccountDb.class)
                    .fetch("wallet")
                    .fetch("wallet.balances")
                    .fetch("wallet.balances.currency")
                    .where().eq("uuid", accountUuid)
                    .forUpdate()
                    .findOne();

            if (accountDb == null) {
                return Result.failure("Cuenta no encontrada", ErrorCode.ACCOUNT_NOT_FOUND);
            }

            Account account = AccountMapper.toDomain(accountDb);
            Result<Void> result = account.add(currency, amount);

            if (!result.isSuccess()) {
                return Result.failure(result.getErrorMessage(), result.getErrorCode());
            }

            // Sincronización de balances y persistencia
            updateBalancesInDb(account, accountDb, database);

            database.save(accountDb);
            tx.commit();

            return Result.success(account);

        } catch (Exception e) {
            return Result.failure("Error en el depósito: " + e.getMessage(), ErrorCode.DATA_BASE_ERROR);
        }
    }

    @Override
    public Result<Account> deposit(Player player, ICurrency currency, BigDecimal amount) {
        try (io.ebean.Transaction tx = database.beginTransaction()) {

            // Bloqueo pesimista por ID del Player usando forUpdate()
            AccountDb accountDb = database.find(AccountDb.class)
                    .fetch("wallet")
                    .fetch("wallet.balances")
                    .fetch("wallet.balances.currency")
                    .where().eq("id", player.getId())
                    .forUpdate()
                    .findOne();

            if (accountDb == null) {
                return Result.failure("Cuenta no encontrada", ErrorCode.ACCOUNT_NOT_FOUND);
            }

            Account account = AccountMapper.toDomain(accountDb);
            Result<Void> result = account.add(currency, amount);

            if (!result.isSuccess()) {
                return Result.failure(result.getErrorMessage(), result.getErrorCode());
            }

            // Sincronización mediante el helper y guardado final
            updateBalancesInDb(account, accountDb, database);

            database.save(accountDb);
            tx.commit();

            return Result.success(account);

        } catch (Exception e) {
            return Result.failure("Error en el depósito: " + e.getMessage(), ErrorCode.DATA_BASE_ERROR);
        }
    }

    @Override
    public Result<Account> exchange(String playerUUID, ICurrency fromCurrency, BigDecimal amountFrom, ICurrency toCurrency, BigDecimal amountTo) {
        try (io.ebean.Transaction tx = database.beginTransaction()) {

            // Bloqueo pesimista mediante forUpdate() para evitar condiciones de carrera durante el intercambio
            AccountDb playerDb = database.find(AccountDb.class)
                    .fetch("wallet")
                    .fetch("wallet.balances")
                    .fetch("wallet.balances.currency")
                    .where().eq("uuid", playerUUID)
                    .forUpdate()
                    .findOne();

            if (playerDb == null) {
                return Result.failure("Cuenta no encontrada", ErrorCode.ACCOUNT_NOT_FOUND);
            }

            Account player = AccountMapper.toDomain(playerDb);

            // Operación 1: Retiro de la moneda origen
            Result<Void> resultSubtract = player.subtract(fromCurrency, amountFrom);
            if (!resultSubtract.isSuccess()) {
                return Result.failure(resultSubtract.getErrorMessage(), resultSubtract.getErrorCode());
            }

            // Operación 2: Depósito de la moneda destino
            Result<Void> resultAdd = player.add(toCurrency, amountTo);
            if (!resultAdd.isSuccess()) {
                return Result.failure(resultAdd.getErrorMessage(), resultAdd.getErrorCode());
            }

            // Sincronización de los balances modificados en el dominio hacia la entidad
            updateBalancesInDb(player, playerDb, database);

            database.save(playerDb);
            tx.commit();

            return Result.success(player);

        } catch (Exception e) {
            // Rollback automático al no llamar a tx.commit()
            return Result.failure("Error en el intercambio: " + e.getMessage(), ErrorCode.DATA_BASE_ERROR);
        }
    }

    @Override
    public Result<Account> exchange(Player playerFrom, ICurrency fromCurrency, BigDecimal amountFrom, ICurrency toCurrency, BigDecimal amountTo) {
        try (io.ebean.Transaction tx = database.beginTransaction()) {

            // Bloqueo pesimista por ID de la entidad para el intercambio atómico
            AccountDb playerDb = database.find(AccountDb.class)
                    .fetch("wallet")
                    .fetch("wallet.balances")
                    .fetch("wallet.balances.currency")
                    .where().eq("id", playerFrom.getId())
                    .forUpdate()
                    .findOne();

            if (playerDb == null) {
                return Result.failure("Cuenta no encontrada", ErrorCode.ACCOUNT_NOT_FOUND);
            }

            Account player = AccountMapper.toDomain(playerDb);

            // Operación 1: Retirar moneda origen
            Result<Void> resultSubtract = player.subtract(fromCurrency, amountFrom);
            if (!resultSubtract.isSuccess()) {
                return Result.failure(resultSubtract.getErrorMessage(), resultSubtract.getErrorCode());
            }

            // Operación 2: Añadir moneda destino
            Result<Void> resultAdd = player.add(toCurrency, amountTo);
            if (!resultAdd.isSuccess()) {
                return Result.failure(resultAdd.getErrorMessage(), resultAdd.getErrorCode());
            }

            // Sincronización de balances en memoria hacia la entidad
            updateBalancesInDb(player, playerDb, database);

            database.save(playerDb);
            tx.commit();

            return Result.success(player);

        } catch (Exception e) {
            // Rollback automático gestionado por Ebean al no ejecutar commit()
            return Result.failure("Error en el intercambio: " + e.getMessage(), ErrorCode.DATA_BASE_ERROR);
        }
    }

    @Override
    public Result<TransferResult> trade(String fromUuid, String toUuid, ICurrency fromCurrency, ICurrency toCurrency, BigDecimal amountFrom, BigDecimal amountTo) {
        try (io.ebean.Transaction tx = database.beginTransaction()) {

            // Bloqueo pesimista para ambas cuentas involucradas en el trade
            AccountDb fromDb = database.find(AccountDb.class)
                    .fetch("wallet")
                    .fetch("wallet.balances")
                    .fetch("wallet.balances.currency")
                    .where().eq("uuid", fromUuid)
                    .forUpdate()
                    .findOne();

            AccountDb toDb = database.find(AccountDb.class)
                    .fetch("wallet")
                    .fetch("wallet.balances")
                    .fetch("wallet.balances.currency")
                    .where().eq("uuid", toUuid)
                    .forUpdate()
                    .findOne();

            if (fromDb == null || toDb == null) {
                return Result.failure("Cuenta no encontrada", ErrorCode.ACCOUNT_NOT_FOUND);
            }

            Account from = AccountMapper.toDomain(fromDb);
            Account to = AccountMapper.toDomain(toDb);

            // Validaciones y operaciones de dominio (Atómicas en memoria)
            Result<Void> subFrom = from.subtract(fromCurrency, amountFrom);
            if (!subFrom.isSuccess()) return Result.failure(subFrom.getErrorMessage(), subFrom.getErrorCode());

            Result<Void> subTo = to.subtract(toCurrency, amountTo);
            if (!subTo.isSuccess()) return Result.failure(subTo.getErrorMessage(), subTo.getErrorCode());

            from.add(toCurrency, amountTo);
            to.add(fromCurrency, amountFrom);

            // Sincronización con la base de datos
            updateBalancesInDb(from, fromDb, database);
            updateBalancesInDb(to, toDb, database);

            database.save(fromDb);
            database.save(toDb);

            tx.commit();
            return Result.success(new TransferResult(from, to));

        } catch (Exception e) {
            return Result.failure("Error en el intercambio: " + e.getMessage(), ErrorCode.DATA_BASE_ERROR);
        }
    }

    @Override
    public Result<TransferResult> trade(Player playerFrom, Player playerTo, ICurrency fromCurrency, ICurrency toCurrency, BigDecimal amountFrom, BigDecimal amountTo) {
        try (io.ebean.Transaction tx = database.beginTransaction()) {

            // Bloqueo pesimista por ID del Player para asegurar la integridad del intercambio
            AccountDb fromDb = database.find(AccountDb.class)
                    .fetch("wallet")
                    .fetch("wallet.balances")
                    .fetch("wallet.balances.currency")
                    .where().eq("id", playerFrom.getId())
                    .forUpdate()
                    .findOne();

            AccountDb toDb = database.find(AccountDb.class)
                    .fetch("wallet")
                    .fetch("wallet.balances")
                    .fetch("wallet.balances.currency")
                    .where().eq("id", playerTo.getId())
                    .forUpdate()
                    .findOne();

            if (fromDb == null || toDb == null) {
                return Result.failure("Cuenta no encontrada", ErrorCode.ACCOUNT_NOT_FOUND);
            }

            Account from = AccountMapper.toDomain(fromDb);
            Account to = AccountMapper.toDomain(toDb);

            // Validaciones de saldo (Lógica de dominio)
            Result<Void> subFrom = from.subtract(fromCurrency, amountFrom);
            if (!subFrom.isSuccess()) return Result.failure(subFrom.getErrorMessage(), subFrom.getErrorCode());

            Result<Void> subTo = to.subtract(toCurrency, amountTo);
            if (!subTo.isSuccess()) return Result.failure(subTo.getErrorMessage(), subTo.getErrorCode());

            // Intercambio de valores en los objetos de dominio
            from.add(toCurrency, amountTo);
            to.add(fromCurrency, amountFrom);

            // Sincronizar cambios en los balances de la base de datos
            updateBalancesInDb(from, fromDb, database);
            updateBalancesInDb(to, toDb, database);

            database.save(fromDb);
            database.save(toDb);

            tx.commit();
            return Result.success(new TransferResult(from, to));

        } catch (Exception e) {
            // Fallback automático por Ebean si falla cualquier paso intermedio
            return Result.failure("Error en el intercambio: " + e.getMessage(), ErrorCode.DATA_BASE_ERROR);
        }
    }

    @Override
    public Result<Account> setBalance(String accountUuid, ICurrency currency, BigDecimal amount) {
        try (io.ebean.Transaction tx = database.beginTransaction()) {

            // Bloqueo pesimista con forUpdate para asegurar que nadie más toque los balances
            AccountDb accountDb = database.find(AccountDb.class)
                    .fetch("wallet")
                    .fetch("wallet.balances")
                    .fetch("wallet.balances.currency")
                    .where().eq("uuid", accountUuid)
                    .forUpdate()
                    .findOne();

            if (accountDb == null) {
                return Result.failure("Cuenta no encontrada", ErrorCode.ACCOUNT_NOT_FOUND);
            }

            Account account = AccountMapper.toDomain(accountDb);
            Result<Void> result = account.setMoney(currency, amount);

            if (!result.isSuccess()) {
                return Result.failure(result.getErrorMessage(), result.getErrorCode());
            }

            // Sincronizar balances con la entidad usando el helper común
            updateBalancesInDb(account, accountDb, database);

            database.save(accountDb);
            tx.commit();

            return Result.success(account);

        } catch (Exception e) {
            return Result.failure("Error al establecer balance: " + e.getMessage(), ErrorCode.DATA_BASE_ERROR);
        }
    }

    @Override
    public Result<Account> setBalance(Player player, ICurrency currency, BigDecimal amount) {
        try (io.ebean.Transaction tx = database.beginTransaction()) {

            // Búsqueda con bloqueo pesimista por el ID de la entidad (Player)
            AccountDb accountDb = database.find(AccountDb.class)
                    .fetch("wallet")
                    .fetch("wallet.balances")
                    .fetch("wallet.balances.currency")
                    .where().eq("id", player.getId())
                    .forUpdate()
                    .findOne();

            if (accountDb == null) {
                return Result.failure("Cuenta no encontrada", ErrorCode.ACCOUNT_NOT_FOUND);
            }

            Account account = AccountMapper.toDomain(accountDb);
            Result<Void> result = account.setMoney(currency, amount);

            if (!result.isSuccess()) {
                return Result.failure(result.getErrorMessage(), result.getErrorCode());
            }

            // Actualización de balances mediante el helper común
            updateBalancesInDb(account, accountDb, database);

            database.save(accountDb);
            tx.commit();

            return Result.success(account);

        } catch (Exception e) {
            return Result.failure("Error al establecer balance: " + e.getMessage(), ErrorCode.DATA_BASE_ERROR);
        }
    }
}
*/