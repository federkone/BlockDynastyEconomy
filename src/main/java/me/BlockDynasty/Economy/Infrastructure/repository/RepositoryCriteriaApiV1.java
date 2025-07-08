package me.BlockDynasty.Economy.Infrastructure.repository;

import jakarta.persistence.LockModeType;
import jakarta.persistence.criteria.*;
import me.BlockDynasty.Economy.domain.account.Account;
import me.BlockDynasty.Economy.domain.balance.Balance;
import me.BlockDynasty.Economy.domain.currency.Currency;
import me.BlockDynasty.Economy.Infrastructure.repository.Criteria.Criteria;
import me.BlockDynasty.Economy.Infrastructure.repository.Criteria.Filter;
import me.BlockDynasty.Economy.Infrastructure.repository.ConnectionHandler.ConnectionHibernate;
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
//TODO: PODEMOS IMPLEMENTAR UN SISTEMA DE RETRY NATIVO DE HIBERNATE, INVESTIGAR
//TODO: TAMBIEN PODEMOS AGREGAR UNA CACHE NIVEL 1 PARA CACHCEAR CONSULTAR Y AUMENTAR PERFORMANCE
public class RepositoryCriteriaApiV1 implements IRepository
{
    private final SessionFactory sessionFactory;
    private boolean topSupported = true;

    public RepositoryCriteriaApiV1(ConnectionHibernate connectionHibernate) {

        this.sessionFactory = connectionHibernate.getSession();
    }
    @Override
    public void loadCurrencies() {
    }

    @Override
    public List<Currency> loadCurrencies(Criteria criteria) {
        return createQueryWithOr(Currency.class,criteria);
    }

    @Override
    public void saveCurrency(Currency currency) {
        executeInsideTransaction(session -> session.merge(currency));
    }

    @Override
    public void deleteCurrency(Currency currency) {
        executeInsideTransaction(session -> session.remove(currency));
    }

    @Override
    public List<Account> loadAccounts(Criteria criteria)throws TransactionException {
        return createQueryWithOr(Account.class,criteria);
    }

    @Override
    public void createAccount(Account account)throws TransactionException {
        executeInsideTransaction(session -> session.persist(account));
    }

    @Override
    public void saveAccount(Account account) throws TransactionException{
        executeInsideTransaction(session -> session.merge(account));
    }

    public void transfer(Account userFrom, Account userTo)throws TransactionException {
        executeInsideTransaction(session -> {
            session.merge(userFrom);
            session.merge(userTo);
        });
    }
@Override
public Result<TransferResult> transfer(String fromUuid, String toUuid, Currency currency, BigDecimal amount) {
    try (Session session = sessionFactory.openSession()) {
        Transaction tx = session.beginTransaction();

        Account from = session.createQuery(
                        "SELECT a FROM Account a JOIN FETCH a.balances b " +
                                " WHERE a.uuid = :uuid AND b.currency = :currency", Account.class)
                .setParameter("uuid", fromUuid)
                .setParameter("currency", currency)
                .setLockMode(LockModeType.PESSIMISTIC_WRITE)
                .uniqueResult();

        Account to = session.createQuery(
                        "SELECT a FROM Account a JOIN FETCH a.balances b " +
                                " WHERE a.uuid = :uuid AND b.currency = :currency", Account.class)
                .setParameter("uuid", toUuid)
                .setParameter("currency", currency)
                .setLockMode(LockModeType.PESSIMISTIC_WRITE)
                .uniqueResult();

        // Validación y mutación
        Balance fromBal = from.getBalance(currency);
        if (!from.hasEnough(currency,amount)) {
            tx.rollback(); //para liberar el bloqueo pesimista
            return Result.failure("Saldo insuficiente", ErrorCode.INSUFFICIENT_FUNDS);
        }
        fromBal.setBalance(fromBal.getBalance().subtract(amount));

        Balance toBal = to.getBalance(currency);
        toBal.setBalance(toBal.getBalance().add(amount));

        tx.commit();
        return Result.success(new TransferResult(from,to)); //todo: retornar las cuentas resultantes actualizadas para la cache
    } catch (Exception e) {
        return Result.failure("Error en la base de datos: " + e.getMessage(), ErrorCode.DATA_BASE_ERROR);
    }
}
@Override
public Result<Account> withdraw(String accountUuid, Currency currency, BigDecimal amount) {
    try (Session session = sessionFactory.openSession()) {
        Transaction tx = session.beginTransaction();

        Account account = session.createQuery(
                        "SELECT a FROM Account a JOIN FETCH a.balances b " +
                                " WHERE a.uuid = :uuid AND b.currency = :currency", Account.class)
                .setParameter("uuid", accountUuid)
                .setParameter("currency", currency)
                .setLockMode(LockModeType.PESSIMISTIC_WRITE)
                .uniqueResult();

        if (account == null) {
            tx.rollback();
            return Result.failure("Cuenta no encontrada", ErrorCode.ACCOUNT_NOT_FOUND);
        }

        Balance balance = account.getBalance(currency);
        if (balance.getBalance().compareTo(amount) < 0) {
            tx.rollback();
            return Result.failure("Saldo insuficiente", ErrorCode.INSUFFICIENT_FUNDS);
        }

        balance.setBalance(balance.getBalance().subtract(amount));

        tx.commit();
        return Result.success(account);
    } catch (Exception e) {
        return Result.failure("Error en la base de datos: " + e.getMessage(), ErrorCode.DATA_BASE_ERROR);
    }
}
@Override
public Result<Account> deposit(String accountUuid, Currency currency, BigDecimal amount) {
    try (Session session = sessionFactory.openSession()) {
        Transaction tx = session.beginTransaction();

        Account account = session.createQuery(
                        "SELECT a FROM Account a JOIN FETCH a.balances b " +
                                " WHERE a.uuid = :uuid AND b.currency = :currency", Account.class)
                .setParameter("uuid", accountUuid)
                .setParameter("currency", currency)
                .setLockMode(LockModeType.PESSIMISTIC_WRITE)
                .uniqueResult();

        if (account == null) {
            tx.rollback();
            return Result.failure("Cuenta no encontrada", ErrorCode.ACCOUNT_NOT_FOUND);
        }

        Balance balance = account.getBalance(currency);
        balance.setBalance(balance.getBalance().add(amount));

        tx.commit();
        return Result.success(account);
    } catch (Exception e) {
        return Result.failure("Error en la base de datos: " + e.getMessage(), ErrorCode.DATA_BASE_ERROR);
    }
}
//set balance va a reemplazar su balance por el recibido por parametro
    @Override
public Result<Account> setBalance(String accountUuid, Currency currency, BigDecimal amount) {
    try (Session session = sessionFactory.openSession()) {
        Transaction tx = session.beginTransaction();

        Account account = session.createQuery(
                        "SELECT a FROM Account a JOIN FETCH a.balances b " +
                                " WHERE a.uuid = :uuid AND b.currency = :currency", Account.class)
                .setParameter("uuid", accountUuid)
                .setParameter("currency", currency)
                .setLockMode(LockModeType.PESSIMISTIC_WRITE)
                .uniqueResult();

        if (account == null) {
            tx.rollback();
            return Result.failure("Cuenta no encontrada", ErrorCode.ACCOUNT_NOT_FOUND);
        }

        Balance balance = account.getBalance(currency);
        balance.setBalance(amount);

        tx.commit();
        return Result.success(account);
    } catch (Exception e) {
        return Result.failure("Error en la base de datos: " + e.getMessage(), ErrorCode.DATA_BASE_ERROR);
    }
    }
@Override
public Result<Account> exchange(String fromUuid, Currency fromCurrency, BigDecimal amountFrom,  Currency toCurrency,BigDecimal amountTo) {
    try (Session session = sessionFactory.openSession()) {
        Transaction tx = session.beginTransaction();

        Account from = session.createQuery(
                        "SELECT a FROM Account a " +
                                "JOIN FETCH a.balances " +  // Fetch all balances
                                "WHERE a.uuid = :uuid", Account.class)
                .setParameter("uuid", fromUuid)
                .setLockMode(LockModeType.PESSIMISTIC_WRITE)
                .uniqueResult();


        if ( from == null) {
            tx.rollback();
            return Result.failure("Cuenta no encontrada", ErrorCode.ACCOUNT_NOT_FOUND);
        }

        Balance fromBal = from.getBalance(fromCurrency);
        if (fromBal == null){
            tx.rollback();
            return Result.failure("Cuenta no tiene balance para la moneda de origen", ErrorCode.CURRENCY_NOT_FOUND);
        }
        if (fromBal.getBalance().compareTo(amountFrom) < 0) {
            tx.rollback();

            return Result.failure("Saldo insuficiente", ErrorCode.INSUFFICIENT_FUNDS);
        }

        fromBal.setBalance(fromBal.getBalance().subtract(amountFrom));

        Balance toBal = from.getBalance(toCurrency);
        if (toBal == null){
            tx.rollback();
            return Result.failure("Cuenta no tiene balance para la moneda a entregar", ErrorCode.CURRENCY_NOT_FOUND);
        }
        toBal.setBalance(toBal.getBalance().add(amountTo));

        tx.commit();
        return Result.success(from);
    } catch (Exception e) {
        return Result.failure("Error en la base de datos: " + e.getMessage(), ErrorCode.DATA_BASE_ERROR);
    }
}
    @Override
    public Result<TransferResult> trade(String fromUuid, String toUuid, Currency fromCurrency, Currency toCurrency, BigDecimal amountFrom, BigDecimal amountTo) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();

            Account from = session.createQuery(
                            "SELECT a FROM Account a " +
                                    "JOIN FETCH a.balances " +  // Fetch all balances
                                    "WHERE a.uuid = :uuid", Account.class)
                    .setParameter("uuid", fromUuid)
                    .setLockMode(LockModeType.PESSIMISTIC_WRITE)
                    .uniqueResult();


            Account to = session.createQuery(
                            "SELECT a FROM Account a " +
                                    "JOIN FETCH a.balances " +  // Fetch all balances
                                    "WHERE a.uuid = :uuid", Account.class)
                    .setParameter("uuid", toUuid)
                    .setLockMode(LockModeType.PESSIMISTIC_WRITE)
                    .uniqueResult();


            if (from == null || to == null) {
                tx.rollback();
                return Result.failure("Cuenta no encontrada", ErrorCode.ACCOUNT_NOT_FOUND);
            }

            // Get balances for both currencies for both accounts
            Balance fromFromBal = from.getBalance(fromCurrency);//esta
            Balance fromToBal = from.getBalance(toCurrency);//no esta
            Balance toFromBal = to.getBalance(fromCurrency);//no esta
            Balance toToBal = to.getBalance(toCurrency);//esta

            // Check sufficient funds
            if (fromFromBal.getBalance().compareTo(amountFrom) < 0) {
                tx.rollback();
                return Result.failure("Saldo insuficiente en la cuenta de origen", ErrorCode.INSUFFICIENT_FUNDS);
            }
            if (toToBal.getBalance().compareTo(amountTo) < 0) {
                tx.rollback();
                return Result.failure("Saldo insuficiente en la cuenta de destino", ErrorCode.INSUFFICIENT_FUNDS);
            }

            // Perform the trade
            fromFromBal.setBalance(fromFromBal.getBalance().subtract(amountFrom)); // from loses fromCurrency
            fromToBal.setBalance(fromToBal.getBalance().add(amountTo));            // from gains toCurrency

            toToBal.setBalance(toToBal.getBalance().subtract(amountTo));           // to loses toCurrency
            toFromBal.setBalance(toFromBal.getBalance().add(amountFrom));          // to gains fromCurrency

            tx.commit();
            return Result.success(new TransferResult(from, to));
        } catch (Exception e) {
            return Result.failure("Error en la base de datos: " + e.getMessage(), ErrorCode.DATA_BASE_ERROR);
        }
    }
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
            String hql = "SELECT a FROM Account a " +
                    "JOIN a.balances b " +
                    "JOIN b.currency c " +
                    "WHERE c.singular = :currencyName OR c.plural = :currencyName " +
                    "ORDER BY b.amount DESC";

            return session.createQuery(hql, Account.class)
                    .setParameter("currencyName", currencyName) // Vincula el nombre de la moneda al parámetro
                    .setFirstResult(offset) // Establece el desplazamiento
                    .setMaxResults(limit) // Establece el límite
                    .getResultList(); // Ejecuta la consulta y devuelve la lista de resultados
        } catch (Exception e) {
            throw new TransactionException("Error retrieving accounts by currency", e);
        }
    }

    @Override
    public void clearAll() throws TransactionException{
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();

            session.createQuery("DELETE FROM Balance").executeUpdate();

            // Eliminar todas las entidades de la tabla Account
            session.createQuery("DELETE FROM Account").executeUpdate();

            // Eliminar todas las entidades de la tabla Currency
            session.createQuery("DELETE FROM Currency").executeUpdate();

            // Añadir más tablas si es necesario
            // session.createQuery("DELETE FROM OtraTabla").executeUpdate();

            transaction.commit();
        } catch (Exception e) {
            throw new TransactionException(e.getMessage());
        }
    }
}