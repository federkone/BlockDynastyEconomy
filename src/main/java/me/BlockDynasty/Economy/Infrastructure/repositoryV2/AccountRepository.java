package me.BlockDynasty.Economy.Infrastructure.repositoryV2;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import me.BlockDynasty.Economy.Infrastructure.repository.Exceptions.RepositoryException;
import me.BlockDynasty.Economy.Infrastructure.repositoryV2.Models.Hibernate.*;

import me.BlockDynasty.Economy.domain.entities.account.Account;
import me.BlockDynasty.Economy.domain.entities.account.Exceptions.AccountAlreadyExist;
import me.BlockDynasty.Economy.domain.entities.account.Exceptions.AccountNotFoundException;
import me.BlockDynasty.Economy.domain.entities.balance.Balance;
import me.BlockDynasty.Economy.domain.entities.currency.Exceptions.CurrencyNotFoundException;
import me.BlockDynasty.Economy.domain.persistence.entities.IAccountRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;


public class AccountRepository implements IAccountRepository {
    @PersistenceContext
    private SessionFactory sessionFactory;

    public AccountRepository( SessionFactory sessionFactory) {
        this.sessionFactory =sessionFactory;
    }

    @Override
    public List<Account> findAll() {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            try {
                List<Account> accounts = session.createQuery(
                                "SELECT DISTINCT a FROM AccountDb a" , AccountDb.class)
                        .getResultList()
                        .stream()
                        .map(AccountMapper::toDomain)
                        .collect(Collectors.toList());
                tx.commit();
                return accounts;
            }catch (Exception e) {
                tx.rollback();
                throw new RepositoryException( "Error repositorio: " + e.getMessage(), e);
            }
        }
    }

    @Override
    public Account findByUuid(String uuid) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            try {
                AccountDb accountDb = session.createQuery(
                                "SELECT a FROM AccountDb a " +
                                        "WHERE a.uuid = :uuid",
                                AccountDb.class)
                        .setParameter("uuid", uuid)
                        .getSingleResult();
                tx.commit();
                System.out.println(accountDb.getWallet().getBalances().size());
                return AccountMapper.toDomain(accountDb);
            } catch (NoResultException e) {
                tx.rollback();
                throw new AccountNotFoundException("Account no encontrado: " + uuid);
            } catch (Exception e) {
                tx.rollback();
                throw new RepositoryException("Error repositorio: " + e.getMessage(), e);
            }
        }
    }

    @Override
    public Account findByNickname(String nickname) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            try {
                AccountDb accountDb = session.createQuery(
                                "SELECT a FROM AccountDb a " +
                                        "WHERE a.nickname = :name",
                                AccountDb.class)
                        .setParameter("name", nickname)
                        .getSingleResult();
                tx.commit();
                return AccountMapper.toDomain(accountDb);
            } catch (NoResultException e) {
                tx.rollback();
                throw new AccountNotFoundException(e.getMessage());
            } catch (Exception e) {
                tx.rollback();
                throw new RepositoryException("Error repositorio: "+e.getMessage(),e);
            }
        }
    }

    @Override
    public void save(Account account) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            try {
                AccountDb accountDb = session.createQuery(
                                "SELECT a FROM AccountDb a JOIN FETCH a.wallet w LEFT JOIN FETCH w.balances b LEFT JOIN FETCH b.currency WHERE a.uuid = :uuid",
                                AccountDb.class)
                        .setParameter("uuid", account.getUuid().toString())
                        .getSingleResult();

                // Update basic properties
                accountDb.setNickname(account.getNickname());
                accountDb.setCanReceiveCurrency(account.canReceiveCurrency());

                // Create a map of existing balances by currency UUID for quick lookup
                Map<String, BalanceDb> existingBalances = accountDb.getWallet().getBalances().stream()
                        .collect(Collectors.toMap(b -> b.getCurrency().getUuid(), Function.identity()));

                // Process each balance from the domain object
                for (Balance domainBalance : account.getBalances()) {
                    String currencyUuid = domainBalance.getCurrency().getUuid().toString();

                    if (existingBalances.containsKey(currencyUuid)) {
                        // Update existing balance
                        existingBalances.get(currencyUuid).setAmount(domainBalance.getAmount());
                    } else {
                        // Create new balance if it doesn't exist
                        CurrencyDb currencyDb = session.createQuery(
                                        "FROM CurrencyDb WHERE uuid = :uuid", CurrencyDb.class)
                                .setParameter("uuid", currencyUuid)
                                .getSingleResult();

                        BalanceDb newBalance = new BalanceDb();
                        newBalance.setCurrency(currencyDb);
                        newBalance.setAmount(domainBalance.getAmount());
                        accountDb.getWallet().addBalance(newBalance);
                    }
                }

                tx.commit();
            } catch (NoResultException e) {
                tx.rollback();
                throw new AccountNotFoundException("Account no encontrado: " + account.getUuid());
            } catch (Exception e) {
                tx.rollback();
                throw new RepositoryException("Error repositorio: " + e.getMessage(), e);
            }
        }
    }

    @Override
    public void delete(Account account) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            try {
                AccountDb accountDb = session.createQuery("SELECT a FROM AccountDb a WHERE a.uuid = :uuid OR a.nickname=:name", AccountDb.class)
                        .setParameter("uuid", account.getUuid().toString())
                        .setParameter("name", account.getNickname())
                        .getSingleResult();

                session.remove(accountDb);
                tx.commit();
            } catch (NoResultException e) {
                throw new AccountNotFoundException("Account no encontrado: " + account.getUuid());
            }catch (Exception e) {
                tx.rollback();
                throw new RepositoryException("Error repositorio: " + e.getMessage(), e);
            }
        }

    }

    @Override
    //actualizar primero implica saber que quiero actualizar, ya que si traigo una wallet con balances, lo mas probable es que primero tenga que traer la cuenta con su wallet, su wallet con el balance de la currency detectada a actualizar y recien ahi actualizar el monto
    //ya que esta consulta este trayendo la cuenta sin su wallet.
    public void update(Account account) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            try {
                AccountDb accountDb = session.createQuery(
                                "SELECT a FROM AccountDb a JOIN FETCH a.wallet w LEFT JOIN FETCH w.balances b LEFT JOIN FETCH b.currency WHERE a.uuid = :uuid",
                                AccountDb.class)
                        .setParameter("uuid", account.getUuid().toString())
                        .getSingleResult();

                // Update basic properties
                accountDb.setNickname(account.getNickname());
                accountDb.setCanReceiveCurrency(account.canReceiveCurrency());

                // Create a map of existing balances by currency UUID for quick lookup
                Map<String, BalanceDb> existingBalances = accountDb.getWallet().getBalances().stream()
                        .collect(Collectors.toMap(b -> b.getCurrency().getUuid(), Function.identity()));

                // Process each balance from the domain object
                for (Balance domainBalance : account.getBalances()) {
                    String currencyUuid = domainBalance.getCurrency().getUuid().toString();

                    if (existingBalances.containsKey(currencyUuid)) {
                        // Update existing balance
                        existingBalances.get(currencyUuid).setAmount(domainBalance.getAmount());
                    } else {
                        // Create new balance if it doesn't exist
                        CurrencyDb currencyDb = session.createQuery(
                                        "FROM CurrencyDb WHERE uuid = :uuid", CurrencyDb.class)
                                .setParameter("uuid", currencyUuid)
                                .getSingleResult();

                        BalanceDb newBalance = new BalanceDb();
                        newBalance.setCurrency(currencyDb);
                        newBalance.setAmount(domainBalance.getAmount());
                        accountDb.getWallet().addBalance(newBalance);
                    }
                }

                tx.commit();
            } catch (NoResultException e) {
               // tx.rollback();
                throw new AccountNotFoundException("Account no encontrado: " + account.getUuid());
            } catch (Exception e) {
                tx.rollback();
                throw new RepositoryException("Error repositorio: " + e.getMessage(), e);
            }
        }
    }

    @Override
    public void create(Account account) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            try {
                    Long count  = session.createQuery("SELECT COUNT(a) FROM AccountDb a WHERE a.uuid = :uuid", Long.class)
                        .setParameter("uuid", account.getUuid().toString())
                        .getSingleResult();

                if (count > 0) {
                    throw new AccountAlreadyExist("Account Ya existe: " + account.getUuid());
                }

                AccountDb entity= AccountMapper.toEntity(account);
                session.persist(entity);
                tx.commit();
            }catch (Exception e) {
                tx.rollback();
                throw new RepositoryException("Error repositorio: " + e.getMessage(), e);
            }
        }
    }

    @Override
    public List<Account> getAccountsTopByCurrency(String currencyName, int limit, int offset) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            try {
                // HQL query to navigate through wallet to balances
                String hql = "SELECT DISTINCT a FROM AccountDb a " +
                        "JOIN FETCH a.wallet w " +
                        "JOIN FETCH w.balances b " +
                        "JOIN FETCH b.currency c " +
                        "WHERE c.singular = :currencyName OR c.plural = :currencyName " +
                        "ORDER BY b.amount DESC";

                List<AccountDb> accountDbs = session.createQuery(hql, AccountDb.class)
                        .setParameter("currencyName", currencyName)
                        .setFirstResult(offset)
                        .setMaxResults(limit)
                        .getResultList();

                tx.commit();
                // Convert to domain entities
                return accountDbs.stream()
                        .map(AccountMapper::toDomain)
                        .collect(Collectors.toList());
            } catch (Exception e) {
                tx.rollback();
                throw new RepositoryException("Error retrieving accounts by currency", e);
            }
        }
    }
}
