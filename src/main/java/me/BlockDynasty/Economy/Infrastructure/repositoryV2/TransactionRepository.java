package me.BlockDynasty.Economy.Infrastructure.repositoryV2;

import jakarta.persistence.LockModeType;
import jakarta.persistence.NoResultException;

import me.BlockDynasty.Economy.Infrastructure.repositoryV2.Models.Hibernate.AccountDb;
import me.BlockDynasty.Economy.Infrastructure.repositoryV2.Models.Hibernate.AccountMapper;
import me.BlockDynasty.Economy.Infrastructure.repositoryV2.Models.Hibernate.CurrencyDb;
import me.BlockDynasty.Economy.Infrastructure.repositoryV2.Models.Hibernate.CurrencyMapper;

import me.BlockDynasty.Economy.domain.entities.account.Account;
import me.BlockDynasty.Economy.domain.entities.currency.Currency;
import me.BlockDynasty.Economy.domain.persistence.transaction.ITransactions;
import me.BlockDynasty.Economy.domain.result.ErrorCode;
import me.BlockDynasty.Economy.domain.result.Result;
import me.BlockDynasty.Economy.domain.result.TransferResult;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.math.BigDecimal;

public class TransactionRepository  implements ITransactions {

    private final SessionFactory sessionFactory;
    public TransactionRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
    @Override
    public Result<TransferResult> transfer(String fromUuid, String toUuid, Currency currency, BigDecimal amount) {
        CurrencyDb currencyDb = CurrencyMapper.toEntity(currency);
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();

            AccountDb fromDb = session.createQuery(
                            "SELECT a FROM AccountDb a JOIN FETCH a.wallet w JOIN FETCH w.balances b JOIN FETCH b.currency " +
                                    "WHERE a.uuid = :uuid AND b.currency = :currency", AccountDb.class)
                    .setParameter("uuid", fromUuid)
                    .setParameter("currency", currencyDb)
                    .setLockMode(LockModeType.PESSIMISTIC_WRITE)
                    .uniqueResult();

            AccountDb toDb = session.createQuery(
                            "SELECT a FROM AccountDb a JOIN FETCH a.wallet w JOIN FETCH w.balances b JOIN FETCH b.currency " +
                                    "WHERE a.uuid = :uuid AND b.currency = :currency", AccountDb.class)
                    .setParameter("uuid", toUuid)
                    .setParameter("currency", currencyDb)
                    .setLockMode(LockModeType.PESSIMISTIC_WRITE)
                    .uniqueResult();

            if (fromDb == null || toDb == null) {
                tx.rollback();
                return Result.failure("Cuenta no encontrada", ErrorCode.ACCOUNT_NOT_FOUND);
            }

            Account from = AccountMapper.toDomain(fromDb);
            Account to = AccountMapper.toDomain(toDb);
            //---logica de negocio de la cuenta
            Result<Void> result = from.subtract(currency, amount);
            if (!result.isSuccess()) {
                tx.rollback(); //para liberar el bloqueo pesimista
                return Result.failure(result.getErrorMessage(), result.getErrorCode());
            }
            to.add(currency, amount);
            //---------------------------------------
            // Guardar las cuentas actualizadas
            fromDb.updateFromEntity(from);
            toDb.updateFromEntity(to);

            tx.commit();
            return Result.success(new TransferResult(from, to)); //todo: retornar las cuentas resultantes actualizadas para la cache
        }catch (NoResultException e) {
            return Result.failure("Cuenta no encontrada", ErrorCode.ACCOUNT_NOT_FOUND);
        } catch (Exception e) {
            return Result.failure("Error en la base de datos: " + e.getMessage(), ErrorCode.DATA_BASE_ERROR);
        }
    }

    @Override
    public Result<Account> withdraw(String accountUuid, Currency currency, BigDecimal amount) {
        CurrencyDb currencyDb = CurrencyMapper.toEntity(currency);
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();

            AccountDb accountDb = session.createQuery(
                            "SELECT a FROM AccountDb a JOIN FETCH a.wallet w JOIN FETCH w.balances b " +
                                    " WHERE a.uuid = :uuid AND b.currency = :currency", AccountDb.class)
                    .setParameter("uuid", accountUuid)
                    .setParameter("currency", currencyDb)
                    .setLockMode(LockModeType.PESSIMISTIC_WRITE)
                    .uniqueResult();

            if (accountDb == null) {
                tx.rollback();
                return Result.failure("Cuenta no encontrada", ErrorCode.ACCOUNT_NOT_FOUND);
            }

            Account account = AccountMapper.toDomain(accountDb);
            //---logica de negocio de la cuenta
            Result<Void> result = account.subtract(currency, amount);
            if (!result.isSuccess()) {
                tx.rollback();
                return Result.failure(result.getErrorMessage(), result.getErrorCode());
            }
            //--------------
            // Actualizar la cuenta en la base de datos
            accountDb.updateFromEntity(account);
            tx.commit();
            return Result.success(account);
        }catch (NoResultException e) {
            return Result.failure("Cuenta no encontrada", ErrorCode.ACCOUNT_NOT_FOUND);
        } catch (Exception e) {
            return Result.failure("Error en la base de datos: " + e.getMessage(), ErrorCode.DATA_BASE_ERROR);
        }
    }
    @Override
    public Result<Account> deposit(String accountUuid, Currency currency, BigDecimal amount) {
        CurrencyDb currencyDb = CurrencyMapper.toEntity(currency);
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            AccountDb accountDb = session.createQuery(
                            "SELECT a FROM AccountDb a JOIN FETCH a.wallet w JOIN FETCH w.balances b " +   //la cuenta no tiene balances ni currency al momento de inicializar el server
                                    " WHERE a.uuid = :uuid AND b.currency = :currency", AccountDb.class)
                    .setParameter("uuid", accountUuid)
                    .setParameter("currency", currencyDb)
                    .setLockMode(LockModeType.PESSIMISTIC_WRITE)
                    .uniqueResult();

            if (accountDb == null) {
                tx.rollback();
                return Result.failure("Cuenta no encontrada", ErrorCode.ACCOUNT_NOT_FOUND);
            }

            Account account = AccountMapper.toDomain(accountDb);
            //---logica de negocio de la cuenta
            Result<Void> result = account.add(currency, amount);
            if (!result.isSuccess()){
                tx.rollback();
                return Result.failure(result.getErrorMessage(), result.getErrorCode());
            }
            //-------------------------------
            // Actualizar la cuenta en la base de datos
            accountDb.updateFromEntity(account);
            tx.commit();
            return Result.success(account);
        } catch (Exception e) {
            return Result.failure("Error en la base de datos: " + e.getMessage(), ErrorCode.DATA_BASE_ERROR);
        }
    }
    @Override
    public Result<Account> exchange(String playerUUID, Currency fromCurrency, BigDecimal amountFrom, Currency toCurrency, BigDecimal amountTo) {
        //CurrencyDb fromCurrencyDb = new CurrencyDb(fromCurrency);
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();

            AccountDb playerDb = session.createQuery(
                            "SELECT a FROM AccountDb a " +
                                    "JOIN FETCH a.wallet w " +  // Fetch the wallet
                                    "JOIN FETCH w.balances " +  // Fetch all balances
                                    "WHERE a.uuid = :uuid", AccountDb.class)
                    .setParameter("uuid", playerUUID)
                    .setLockMode(LockModeType.PESSIMISTIC_WRITE)
                    .uniqueResult();


            if ( playerDb == null) {
                tx.rollback();
                return Result.failure("Cuenta no encontrada", ErrorCode.ACCOUNT_NOT_FOUND);
            }

            Account player = AccountMapper.toDomain(playerDb);
            //---logica de negocio de la cuenta
            Result<Void> resultSubtract = player.subtract(fromCurrency, amountFrom);
            if (!resultSubtract.isSuccess()) {
                tx.rollback();
                return Result.failure(resultSubtract.getErrorMessage(), resultSubtract.getErrorCode());
            }

            Result<Void> resultAdd = player.add(toCurrency, amountTo);
            if (!resultAdd.isSuccess()) {
                tx.rollback();
                return Result.failure(resultAdd.getErrorMessage(), resultAdd.getErrorCode());
            }
            //--------------------------------------------
            // Actualizar la cuenta en la base de datos
            playerDb.updateFromEntity(player);
            tx.commit();
            return Result.success(player);
        } catch (Exception e) {
            return Result.failure("Error en la base de datos: " + e.getMessage(), ErrorCode.DATA_BASE_ERROR);
        }
    }

    @Override
    public Result<TransferResult> trade(String fromUuid, String toUuid, Currency fromCurrency, Currency toCurrency, BigDecimal amountFrom, BigDecimal amountTo) {
        //CurrencyDb fromCurrencyDb = new CurrencyDb(fromCurrency);
        //CurrencyDb toCurrencyDb = new CurrencyDb(toCurrency);
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();

            AccountDb fromDb = session.createQuery(
                            "SELECT a FROM AccountDb a " +
                                    "JOIN FETCH a.wallet w " +  // Fetch the wallet
                                    "JOIN FETCH w.balances " +  // Fetch all balances
                                    "WHERE a.uuid = :uuid", AccountDb.class)
                    .setParameter("uuid", fromUuid)
                    .setLockMode(LockModeType.PESSIMISTIC_WRITE)
                    .uniqueResult();


            AccountDb toDb = session.createQuery(
                            "SELECT a FROM AccountDb a " +
                                    "JOIN FETCH a.wallet w " +  // Fetch the wallet
                                    "JOIN FETCH w.balances " +  // Fetch all balances
                                    "WHERE a.uuid = :uuid", AccountDb.class)
                    .setParameter("uuid", toUuid)
                    .setLockMode(LockModeType.PESSIMISTIC_WRITE)
                    .uniqueResult();


            if (fromDb == null || toDb == null) {
                tx.rollback();
                return Result.failure("Cuenta no encontrada", ErrorCode.ACCOUNT_NOT_FOUND);
            }
            Account from = AccountMapper.toDomain(fromDb);
            Account to = AccountMapper.toDomain(toDb);
            //---logica de negocio de la cuenta-------------------
            Result<Void> resultSubtractFrom = from.subtract(fromCurrency, amountFrom);
            if (!resultSubtractFrom.isSuccess()) {
                tx.rollback();
                return Result.failure(resultSubtractFrom.getErrorMessage(), resultSubtractFrom.getErrorCode());
            }
            Result<Void> resultSubtractTo = to.subtract(toCurrency, amountTo);
            if (!resultSubtractTo.isSuccess()) {
                tx.rollback();
                return Result.failure(resultSubtractTo.getErrorMessage(), resultSubtractTo.getErrorCode());
            }
            Result<Void> resultAddFrom = from.add(toCurrency, amountTo);
            if (!resultAddFrom.isSuccess()) {
                tx.rollback();
                return Result.failure(resultAddFrom.getErrorMessage(), resultAddFrom.getErrorCode());
            }
            Result<Void> resultAddTo = to.add(fromCurrency, amountFrom);
            if (!resultAddTo.isSuccess()) {
                tx.rollback();
                return Result.failure(resultAddTo.getErrorMessage(), resultAddTo.getErrorCode());
            }
            //-----------------------------------------------------------
            // Actualizar las cuentas en la base de datos
            fromDb.updateFromEntity(from);
            toDb.updateFromEntity(to);
            tx.commit();
            return Result.success(new TransferResult(from, to));
        } catch (Exception e) {
            return Result.failure("Error en la base de datos: " + e.getMessage(), ErrorCode.DATA_BASE_ERROR);
        }
    }
    @Override
    public Result<Account> setBalance(String accountUuid, Currency currency, BigDecimal amount) {
        CurrencyDb currencyDb = CurrencyMapper.toEntity(currency);
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();

            AccountDb accountDb = session.createQuery(
                            "SELECT a FROM AccountDb a JOIN FETCH a.wallet w JOIN FETCH w.balances b " +
                                    " WHERE a.uuid = :uuid AND b.currency = :currency", AccountDb.class)
                    .setParameter("uuid", accountUuid)
                    .setParameter("currency", currencyDb)
                    .setLockMode(LockModeType.PESSIMISTIC_WRITE)
                    .uniqueResult();

            if (accountDb == null) {
                tx.rollback();
                return Result.failure("Cuenta no encontrada", ErrorCode.ACCOUNT_NOT_FOUND);
            }

            Account account = AccountMapper.toDomain(accountDb);
            //---logica de negocio de la cuenta
            Result<Void> result = account.setBalance(currency, amount);
            if (!result.isSuccess()) {
                tx.rollback();
                return Result.failure(result.getErrorMessage(), result.getErrorCode());
            }
            //-------------------------------------
            // Actualizar la cuenta en la base de datos
            accountDb.updateFromEntity(account);
            tx.commit();
            return Result.success(account);
        } catch (Exception e) {
            return Result.failure("Error en la base de datos: " + e.getMessage(), ErrorCode.DATA_BASE_ERROR);
        }
    }
}
