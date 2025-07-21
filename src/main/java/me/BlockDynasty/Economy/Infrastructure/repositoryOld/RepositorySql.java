package me.BlockDynasty.Economy.Infrastructure.repositoryOld;

import jakarta.persistence.LockModeType;
import jakarta.persistence.criteria.*;
import me.BlockDynasty.Economy.Infrastructure.repository.ConnectionHandler.Hibernate.Connection;

import me.BlockDynasty.Economy.Infrastructure.repositoryOld.Models.Hibernate.AccountDb;
import me.BlockDynasty.Economy.Infrastructure.repositoryOld.Models.Hibernate.CurrencyDb;

import me.BlockDynasty.Economy.domain.entities.account.Account;
import me.BlockDynasty.Economy.domain.entities.currency.Currency;
import me.BlockDynasty.Economy.Infrastructure.repositoryOld.Criteria.Criteria;
import me.BlockDynasty.Economy.Infrastructure.repositoryOld.Criteria.Filter;
import me.BlockDynasty.Economy.domain.persistence.entities.IRepository;
import me.BlockDynasty.Economy.domain.result.ErrorCode;
import me.BlockDynasty.Economy.domain.result.Result;
import me.BlockDynasty.Economy.domain.result.TransferResult;
import org.hibernate.*;
import org.hibernate.query.Query;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class RepositorySql implements IRepository
{
    private final SessionFactory sessionFactory;
    private boolean topSupported = true;

    public RepositorySql(Connection connection) {
        this.sessionFactory = connection.getSession();
    }

    @Override
    public List<Currency> loadCurrencies(Criteria criteria) {
        List<CurrencyDb> currencyDbs = createQuery(CurrencyDb.class, criteria);
        List<Currency> currencies = new ArrayList<>();
        for (CurrencyDb currencyDb : currencyDbs) {
            currencies.add(currencyDb.toEntity());
        }
        return currencies;
    }

    @Override
    public Result<Currency> loadCurrencyByName(String name) {
        return null;
    }

    @Override
    public Result<Currency> loadCurrencyByUuid(String uuid) {
        return null;
    }

    @Override
    public Result<Currency> loadDefaultCurrency() {
        return null;
    }

    @Override
    public void saveCurrency(Currency currency) {
        //sessionFactory.openSession().persist( new CurrencyDb(currency));
        executeInsideTransaction(session -> session.merge(new CurrencyDb( currency)));
    }

    @Override
    public void deleteCurrency(Currency currency) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            try {
                // First delete all associated balances
                session.createQuery("DELETE FROM BalanceDb WHERE currency.uuid = :uuid")
                        .setParameter("uuid", currency.getUuid().toString())
                        .executeUpdate();

                // Then find and delete the currency by its string ID
                CurrencyDb currencyDb = session.get(CurrencyDb.class, currency.getUuid().toString());
                if (currencyDb != null) {
                    session.remove(currencyDb);
                }

                tx.commit();
            } catch (Exception e) {
                if (tx != null) {
                    tx.rollback();
                }
                throw new TransactionException("Error deleting currency: " + e.getMessage(), e);
            }
        } catch (Exception e) {
            throw new TransactionException("Session error: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Account> loadAccounts(Criteria criteria)throws TransactionException {
        List<AccountDb> accountDbDbs = createQueryWithOr(AccountDb.class, criteria);
        List<me.BlockDynasty.Economy.domain.entities.account.Account> accounts = new ArrayList<>();
        for (AccountDb accountDb : accountDbDbs) {
            accounts.add(accountDb.toEntity());
        }
        return accounts;
    }

    @Override
    public Result<Account> loadAccountByUuid(String uuid) throws TransactionException {
        try (Session session = sessionFactory.openSession()) {
            AccountDb accountDb = session.createQuery("SELECT a FROM AccountDb a WHERE a.uuid = :uuid", AccountDb.class)
                    .setParameter("uuid", uuid)
                    .uniqueResult();
            if (accountDb == null) {
                System.out.println( "Cuenta no encontrada: " + uuid);
                return Result.failure("Cuenta no encontrada", ErrorCode.ACCOUNT_NOT_FOUND);
            }
            return Result.success(accountDb.toEntity());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return Result.failure("error al cargar la cuenta: " + e.getMessage(), ErrorCode.DATA_BASE_ERROR);
        }
    }

    @Override
    public Result<Account> loadAccountByName(String name) throws TransactionException {
        try (Session session = sessionFactory.openSession()) {
            AccountDb accountDb = session.createQuery("SELECT a FROM AccountDb a WHERE a.nickname = :nickname", AccountDb.class)
                    .setParameter("nickname", name)
                    .uniqueResult();
            if (accountDb == null) {
                System.out.println( "Cuenta no encontrada: " + name);
                return Result.failure("Cuenta no encontrada", ErrorCode.ACCOUNT_NOT_FOUND);
            }
            return Result.success(accountDb.toEntity());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return Result.failure("error al cargar la cuenta: " + e.getMessage(), ErrorCode.DATA_BASE_ERROR);
        }
    }

    @Override
    public void createAccount(Account account)throws TransactionException {
        executeInsideTransaction(session -> session.persist(new AccountDb(account)));
    }

    @Override
    public void saveAccount(Account account) throws TransactionException{
        executeInsideTransaction(session -> session.merge(new AccountDb(account)));
    }

    @Override
    public Result<Void> deleteAccount(Account account) {
        return null;
    }

    //-----------transactions-------------------
    @Override
    public Result<TransferResult> transfer(String fromUuid, String toUuid, Currency currency, BigDecimal amount) {
        CurrencyDb currencyDb = new CurrencyDb(currency);
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();

            AccountDb fromDb = session.createQuery(
                            "SELECT a FROM AccountDb a JOIN FETCH a.balances b " +
                                    " WHERE a.uuid = :uuid AND b.currency = :currency", AccountDb.class)
                    .setParameter("uuid", fromUuid)
                    .setParameter("currency", currencyDb)
                    .setLockMode(LockModeType.PESSIMISTIC_WRITE)
                    .uniqueResult();

            AccountDb toDb = session.createQuery(
                            "SELECT a FROM AccountDb a JOIN FETCH a.balances b " +
                                    " WHERE a.uuid = :uuid AND b.currency = :currency", AccountDb.class)
                    .setParameter("uuid", toUuid)
                    .setParameter("currency", currencyDb)
                    .setLockMode(LockModeType.PESSIMISTIC_WRITE)
                    .uniqueResult();

            if (fromDb == null || toDb == null) {
                tx.rollback();
                return Result.failure("Cuenta no encontrada", ErrorCode.ACCOUNT_NOT_FOUND);
            }

            me.BlockDynasty.Economy.domain.entities.account.Account from = fromDb.toEntity();
            me.BlockDynasty.Economy.domain.entities.account.Account to = toDb.toEntity();
            //---logica de negocio de la cuenta
            Result<Void> result  = from.subtract(currency, amount);
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
            return Result.success(new TransferResult(from,to)); //todo: retornar las cuentas resultantes actualizadas para la cache
        } catch (Exception e) {
            return Result.failure("Error en la base de datos: " + e.getMessage(), ErrorCode.DATA_BASE_ERROR);
        }
    }
    @Override
    public Result<Account> withdraw(String accountUuid, Currency currency, BigDecimal amount) {
        CurrencyDb currencyDb = new CurrencyDb(currency);
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();

            AccountDb accountDb = session.createQuery(
                            "SELECT a FROM AccountDb a JOIN FETCH a.balances b " +
                                    " WHERE a.uuid = :uuid AND b.currency = :currency", AccountDb.class)
                    .setParameter("uuid", accountUuid)
                    .setParameter("currency", currencyDb)
                    .setLockMode(LockModeType.PESSIMISTIC_WRITE)
                    .uniqueResult();

            if (accountDb == null) {
                tx.rollback();
                return Result.failure("Cuenta no encontrada", ErrorCode.ACCOUNT_NOT_FOUND);
            }

            Account account = accountDb.toEntity();
            //---logica de negocio de la cuenta
            Result<Void> result = account.subtract(currency,amount);
            if (!result.isSuccess()) {
                tx.rollback();
                return Result.failure(result.getErrorMessage(), result.getErrorCode());
            }
           //--------------
            // Actualizar la cuenta en la base de datos
            accountDb.updateFromEntity(account);
            tx.commit();
            return Result.success(account);
        } catch (Exception e) {
            return Result.failure("Error en la base de datos: " + e.getMessage(), ErrorCode.DATA_BASE_ERROR);
        }
    }
    @Override
    public Result<Account> deposit(String accountUuid, Currency currency, BigDecimal amount) {
        CurrencyDb currencyDb = new CurrencyDb(currency);
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            AccountDb accountDb = session.createQuery(
                            "SELECT a FROM AccountDb a JOIN FETCH a.balances b " +   //la cuenta no tiene balances ni currency al momento de inicializar el server
                                    " WHERE a.uuid = :uuid AND b.currency = :currency", AccountDb.class)
                    .setParameter("uuid", accountUuid)
                    .setParameter("currency", currencyDb)
                    .setLockMode(LockModeType.PESSIMISTIC_WRITE)
                    .uniqueResult();

            if (accountDb == null) {
                tx.rollback();
                return Result.failure("Cuenta no encontrada", ErrorCode.ACCOUNT_NOT_FOUND);
            }

            Account account = accountDb.toEntity();
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
    public Result<Account> setBalance(String accountUuid, Currency currency, BigDecimal amount) {
        CurrencyDb currencyDb = new CurrencyDb(currency);
    try (Session session = sessionFactory.openSession()) {
        Transaction tx = session.beginTransaction();

        AccountDb accountDb = session.createQuery(
                        "SELECT a FROM AccountDb a JOIN FETCH a.balances b " +
                                " WHERE a.uuid = :uuid AND b.currency = :currency", AccountDb.class)
                .setParameter("uuid", accountUuid)
                .setParameter("currency", currencyDb)
                .setLockMode(LockModeType.PESSIMISTIC_WRITE)
                .uniqueResult();

        if (accountDb == null) {
            tx.rollback();
            return Result.failure("Cuenta no encontrada", ErrorCode.ACCOUNT_NOT_FOUND);
        }

        Account account = accountDb.toEntity();
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
    @Override
    public Result<Account> exchange(String playerUUID, Currency fromCurrency, BigDecimal amountFrom, Currency toCurrency, BigDecimal amountTo) {
        //CurrencyDb fromCurrencyDb = new CurrencyDb(fromCurrency);
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();

            AccountDb playerDb = session.createQuery(
                            "SELECT a FROM AccountDb a " +
                                    "JOIN FETCH a.balances " +  // Fetch all balances
                                    "WHERE a.uuid = :uuid", AccountDb.class)
                    .setParameter("uuid", playerUUID)
                    .setLockMode(LockModeType.PESSIMISTIC_WRITE)
                    .uniqueResult();


            if ( playerDb == null) {
                tx.rollback();
                return Result.failure("Cuenta no encontrada", ErrorCode.ACCOUNT_NOT_FOUND);
            }

            Account player = playerDb.toEntity();
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
                                    "JOIN FETCH a.balances " +  // Fetch all balances
                                    "WHERE a.uuid = :uuid", AccountDb.class)
                    .setParameter("uuid", fromUuid)
                    .setLockMode(LockModeType.PESSIMISTIC_WRITE)
                    .uniqueResult();


            AccountDb toDb = session.createQuery(
                            "SELECT a FROM AccountDb a " +
                                    "JOIN FETCH a.balances " +  // Fetch all balances
                                    "WHERE a.uuid = :uuid", AccountDb.class)
                    .setParameter("uuid", toUuid)
                    .setLockMode(LockModeType.PESSIMISTIC_WRITE)
                    .uniqueResult();


            if (fromDb == null || toDb == null) {
                tx.rollback();
                return Result.failure("Cuenta no encontrada", ErrorCode.ACCOUNT_NOT_FOUND);
            }
            Account from = fromDb.toEntity();
            Account to = toDb.toEntity();
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
    //-----------------------------------------------------------
    @Override
    public boolean isTopSupported() {
        return topSupported;
    }

    @Override
    public String getName() {
        return "MySql";
    }

    @Override
    public void close() {
        sessionFactory.close();
    }

    protected void executeInsideTransaction(Consumer<Session> action) throws TransactionException {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            action.accept(session);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new TransactionException(e.getMessage()); // Rethrow exception for logging or external handling
        }
    }
    //TODO: TEST LIMITS AND ORDER FROM CRITERIA
    private <T> List<T> createQuery(Class<T> entityClass, Criteria criteria)throws TransactionException  {
        List<T> results = new ArrayList<>();
        try (Session session = sessionFactory.openSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<T> criteriaQuery = cb.createQuery(entityClass);
            Root<T> root = criteriaQuery.from(entityClass);

            if (criteria != null) {
                Predicate predicate = cb.conjunction(); // Empieza con 'true'
                for (Filter filter : criteria.getFilters()) {
                    String field = filter.getField();
                    Object value = filter.getValue();
                    predicate = cb.and(predicate, cb.equal(root.get(field), value));
                }
                criteriaQuery.where(predicate);

                if (criteria.getOrder() != null) {
                    criteriaQuery.orderBy(serializeOrder(criteria.getOrder(), cb, root));
                }
            }
            Query<T> query = session.createQuery(criteriaQuery);

            // Aplica el límite si está presente
            if (criteria != null && criteria.getLimit() != null) {
                query.setMaxResults(criteria.getLimit());
            }

            results = query.getResultList();
        } catch (Exception e) {
            throw new TransactionException(e.getMessage());
        }
        return results;
    }
    private <T> List<T> createQueryWithOr(Class<T> entityClass, Criteria criteria) throws TransactionException {
        List<T> results = new ArrayList<>();
        try (Session session = sessionFactory.openSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<T> criteriaQuery = cb.createQuery(entityClass);
            Root<T> root = criteriaQuery.from(entityClass);

            if (criteria != null) {
                List<Predicate> predicates = new ArrayList<>(); // Lista de predicados para 'OR'

                for (Filter filter : criteria.getFilters()) {
                    String field = filter.getField();
                    Object value = filter.getValue();
                    predicates.add(cb.equal(root.get(field), value));
                }

                if (!predicates.isEmpty()) {
                    if (predicates.size() == 1) {
                        criteriaQuery.where(predicates.get(0)); // Aplica el único predicado directamente
                    } else {
                        criteriaQuery.where(cb.or(predicates.toArray(new Predicate[0])));
                    }
                }

                if (criteria.getOrder() != null) {
                    criteriaQuery.orderBy(serializeOrder(criteria.getOrder(), cb, root));
                }
            }

            Query<T> query = session.createQuery(criteriaQuery);

            if (criteria != null && criteria.getLimit() != null) {
                query.setMaxResults(criteria.getLimit());
            }

            results = query.getResultList();
        } catch (Exception e) {
            throw new TransactionException(e.getMessage(), e);
        }
        return results;
    }

    private <T> Order serializeOrder(String order, CriteriaBuilder criteriaBuilder, Root<T> root) {
        if (order == null || order.isEmpty()) {
            throw new IllegalArgumentException("Order string cannot be null or empty");
        }

        // Dividir la cadena del orden
        String[] orderParts = order.split(" ");
        if (orderParts.length != 2) {
            throw new IllegalArgumentException("Invalid order format. Expected format: 'field ASC' or 'field DESC'");
        }

        String field = orderParts[0]; // Campo del modelo
        String direction = orderParts[1].toUpperCase(); // Dirección (ASC o DESC)

        // Validar y construir el orden
        switch (direction) {
            case "ASC":
                return criteriaBuilder.asc(root.get(field));
            case "DESC":
                return criteriaBuilder.desc(root.get(field));
            default:
                throw new IllegalArgumentException("Invalid order direction: " + direction);
        }
    }

    @Override
    public List<Account> getAccountsTopByCurrency(String currencyName, int limit, int offset) throws TransactionException {
        try (Session session = sessionFactory.openSession()) {
            // HQL para obtener las cuentas ordenadas por balance según el nombre de la moneda
            String hql = "SELECT a FROM AccountDb a " +
                    "JOIN a.balances b " +
                    "JOIN b.currency c " +
                    "WHERE c.singular = :currencyName OR c.plural = :currencyName " +
                    "ORDER BY b.amount DESC";

            List<AccountDb> accountDbDbs =session.createQuery(hql, AccountDb.class)
                    .setParameter("currencyName", currencyName) // Vincula el nombre de la moneda al parámetro
                    .setFirstResult(offset) // Establece el desplazamiento
                    .setMaxResults(limit) // Establece el límite
                    .getResultList(); // Ejecuta la consulta y devuelve la lista de resultados

            List<Account> accounts = new ArrayList<>();
            for (AccountDb accountDb : accountDbDbs) {
                accounts.add(accountDb.toEntity());
            }
            return accounts;
        } catch (Exception e) {
            throw new TransactionException("Error retrieving accounts by currency", e);
        }
    }

    @Override
    public void clearAll() throws TransactionException{
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();

            session.createQuery("DELETE FROM BalanceDb").executeUpdate();

            // Eliminar todas las entidades de la tabla Account
            session.createQuery("DELETE FROM AccountDb").executeUpdate();

            // Eliminar todas las entidades de la tabla Currency
            session.createQuery("DELETE FROM CurrencyDb").executeUpdate();

            // Añadir más tablas si es necesario
            // session.createQuery("DELETE FROM OtraTabla").executeUpdate();

            transaction.commit();
        } catch (Exception e) {
            throw new TransactionException(e.getMessage());
        }
    }
}