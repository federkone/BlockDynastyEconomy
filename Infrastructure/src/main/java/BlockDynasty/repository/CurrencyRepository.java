package BlockDynasty.repository;

import jakarta.persistence.NoResultException;
import BlockDynasty.Economy.domain.persistence.Exceptions.RepositoryException;
import BlockDynasty.repository.Models.Hibernate.CurrencyDb;
import BlockDynasty.repository.Mappers.CurrencyMapper;
import BlockDynasty.Economy.domain.entities.currency.Currency;
import BlockDynasty.Economy.domain.entities.currency.Exceptions.CurrencyAlreadyExist;
import BlockDynasty.Economy.domain.entities.currency.Exceptions.CurrencyNotFoundException;
import BlockDynasty.Economy.domain.persistence.entities.ICurrencyRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.List;
import java.util.stream.Collectors;

public class CurrencyRepository implements ICurrencyRepository {
    private SessionFactory sessionFactory;

    public CurrencyRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<Currency> findAll() {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            try {
                List<Currency> currencies = session.createQuery("SELECT c FROM CurrencyDb c", CurrencyDb.class)
                        .getResultList()
                        .stream()
                        .map(CurrencyMapper::toDomain)
                        .collect(Collectors.toList());
                tx.commit();
                return currencies;
            } catch (Exception e) {
                tx.rollback();
                throw new RepositoryException("Error repositorio: " + e.getMessage(), e);
            }
        }
    }

    @Override
    public Currency findByUuid(String uuid) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            try {
                CurrencyDb entity = session.createQuery(
                                "SELECT c FROM CurrencyDb c WHERE c.uuid = :uuid", CurrencyDb.class)
                        .setParameter("uuid", uuid)
                        .getSingleResult();
                tx.commit();
                return CurrencyMapper.toDomain(entity);
            } catch (NoResultException e) {
                tx.rollback();
                throw new CurrencyNotFoundException("Currency no encontrado: " + uuid);
            } catch (Exception e) {
                tx.rollback();
                throw new RepositoryException("Error repositorio: "+e.getMessage(), e);
            }
        }
    }

    @Override
    public Currency findDefaultCurrency() {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            try {
                CurrencyDb entity = session.createQuery(
                                "SELECT c FROM CurrencyDb c WHERE c.defaultCurrency = :isDefault", CurrencyDb.class)
                        .setParameter("isDefault", true)
                        .getSingleResult();
                tx.commit();
                return CurrencyMapper.toDomain(entity);
            } catch (NoResultException e) {
                tx.rollback();
                throw new CurrencyNotFoundException("Currency default no encontrado: ");
            } catch (Exception e) {
                tx.rollback();
                throw new RepositoryException("Error repositorio: "+e.getMessage(), e);
            }
        }
    }

    @Override
    public Currency findByName(String name) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            try {
                CurrencyDb entity = session.createQuery(
                                "SELECT c FROM CurrencyDb c WHERE c.singular = :name OR c.plural = :name", CurrencyDb.class)
                        .setParameter("name", name)
                        .getSingleResult();
                tx.commit();
                return CurrencyMapper.toDomain(entity);
            } catch (NoResultException e) {
                tx.rollback();
                throw new CurrencyNotFoundException(e.getMessage());
            } catch (Exception e) {
                tx.rollback();
                throw new RepositoryException("Error repositorio: "+e.getMessage(),e);
            }
        }
    }

    @Override
    public void save(Currency currency) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            try {
                CurrencyDb currencyDb = session.createQuery(
                                "SELECT c FROM CurrencyDb c WHERE c.uuid = :uuid OR c.plural = :plural OR c.singular = :singular", CurrencyDb.class)
                        .setParameter("uuid", currency.getUuid().toString())
                        .setParameter("plural", currency.getPlural())
                        .setParameter("singular", currency.getSingular())
                        .getSingleResult();

                currencyDb.update(currency);
                session.merge(currencyDb);
                tx.commit();
            } catch (NoResultException e) {
                throw new CurrencyNotFoundException("Currency no encontrado: " + currency.getUuid());
            } catch (Exception e) {
                tx.rollback();
                throw new RepositoryException("Error repositorio: "+e.getMessage(),e);
            }
        }
    }

    @Override
    public void delete(Currency currency) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            try {
                CurrencyDb currencyDb = session.createQuery(
                                "SELECT c FROM CurrencyDb c WHERE c.uuid = :uuid OR c.plural = :plural OR c.singular = :singular", CurrencyDb.class)
                        .setParameter("uuid", currency.getUuid().toString())
                        .setParameter("plural", currency.getPlural())
                        .setParameter("singular", currency.getSingular())
                        .getSingleResult();

                session.remove(currencyDb);
                tx.commit();
            }catch (NoResultException e) {
                tx.rollback();
                throw new CurrencyNotFoundException("Currency no encontrado: " + currency.getUuid());
            } catch (Exception e) {
                tx.rollback();
                throw new RepositoryException("Error repositorio: "+e.getMessage(),e);
            }
        }
    }

    @Override
    public void update(Currency currency) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            try {
                CurrencyDb currencyDb = session.createQuery(
                                "SELECT c FROM CurrencyDb c WHERE c.uuid = :uuid OR c.plural = :plural OR c.singular = :singular", CurrencyDb.class)
                        .setParameter("uuid", currency.getUuid().toString())
                        .setParameter("plural", currency.getPlural())
                        .setParameter("singular", currency.getSingular())
                        .getSingleResult();

                currencyDb.update(currency);
                session.merge(currencyDb);
                tx.commit();
            } catch (NoResultException e) {
                throw new CurrencyNotFoundException("Currency no encontrado: " + currency.getUuid());
            } catch (Exception e) {
                tx.rollback();
                throw new  RepositoryException("Error repositorio: "+e.getMessage(),e);
            }
        }
    }

    @Override
    public void create(Currency currency) {
        if (currency == null) {
            throw new IllegalArgumentException("Currency no puede ser nulo");
        }

        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            try {
                // Check for existing currency with same UUID, singular or plural name
                Long count = session.createQuery(
                                "SELECT COUNT(c) FROM CurrencyDb c WHERE c.uuid = :uuid OR c.singular = :singular OR c.plural = :plural", Long.class)
                        .setParameter("uuid", currency.getUuid().toString())
                        .setParameter("singular", currency.getSingular())
                        .setParameter("plural", currency.getPlural())
                        .getSingleResult();

                if (count > 0) {
                    throw new CurrencyAlreadyExist("Ya existe una moneda con el mismo UUID, nombre singular o plural");
                }

                CurrencyDb entity = CurrencyMapper.toEntity(currency);
                session.persist(entity);
                tx.commit();
            } catch (Exception e) {
                tx.rollback();
                throw new RepositoryException("Error al crear la moneda: " + e.getMessage(), e);
            }
        }
    }
}
