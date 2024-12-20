package me.BlockDynasty.Economy.domain.repository;

import jakarta.persistence.criteria.*;
import me.BlockDynasty.Economy.domain.account.Account;
import me.BlockDynasty.Economy.domain.currency.Currency;
import me.BlockDynasty.Economy.domain.repository.Criteria.Criteria;
import me.BlockDynasty.Economy.domain.repository.Criteria.Filter;
import me.BlockDynasty.Economy.domain.repository.ConnectionHandler.ConnectionHibernate;
import org.hibernate.*;
import org.hibernate.query.Query;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;
//TODO: PODEMOS IMPLEMENTAR UN SISTEMA DE RETRY NATIVO DE HIBERNATE, INVESTIGAR
//TODO: TAMBIEN PODEMOS AGREGAR UNA CACHE NIVEL 1 PARA CACHCEAR CONSULTAR Y AUMENTAR PERFORMANCE
public class RepositoryCriteriaApi implements IRepository
{
    private final SessionFactory sessionFactory;
    private boolean topSupported = true;

    public RepositoryCriteriaApi(ConnectionHibernate connectionHibernate) {

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
        executeInsideTransaction(session -> {
            // Eliminar los balances asociados primero
            Query deleteQuery = session.createQuery(
                    "DELETE FROM Balance WHERE currency = :currency"
            );
            deleteQuery.setParameter("currency", currency);
            deleteQuery.executeUpdate();

            // Luego eliminar la moneda
            session.remove(currency);
        });
    }

    @Override
    public List<Account> loadAccounts(Criteria criteria)throws TransactionException {
        return createQuery(Account.class,criteria);
    }

    @Override
    public void createAccount(Account account)throws TransactionException {
        executeInsideTransaction(session -> session.persist(account));
    }

    @Override
    public void saveAccount(Account account) throws TransactionException{
        executeInsideTransaction(session -> session.merge(account));
    }

    @Override
    public void transfer(Account userFrom, Account userTo)throws TransactionException {
        executeInsideTransaction(session -> {
            session.merge(userFrom);
            session.merge(userTo);
        });
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

    /*public List<Account> getAccountsTopByCurrency(String currencyName, int limit, int offset) throws TransactionException {
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
    }*/
    public List<Account> getAccountsTopByCurrency(String currencyName, int limit, int offset) throws TransactionException {
        try (Session session = sessionFactory.openSession()) {
            // HQL con fetch join para evitar el problema N+1
            String hql = "SELECT DISTINCT a FROM Account a " +
                    "JOIN FETCH a.balances b " +
                    "JOIN FETCH b.currency c " +
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
