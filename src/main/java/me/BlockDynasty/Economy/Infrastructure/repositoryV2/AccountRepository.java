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
                List<Account>  accounts = session.createQuery("SELECT a FROM AccountDb a", AccountDb.class)
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
                AccountDb accountDb = session.createQuery("SELECT a FROM AccountDb a WHERE a.uuid = :uuid", AccountDb.class)
                        .setParameter("uuid", uuid)
                        .getSingleResult();
                tx.commit();
                return AccountMapper.toDomain(accountDb);
            }catch (NoResultException e) {
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
                AccountDb entity = session.createQuery(
                                "SELECT a FROM AccountDb a WHERE a.nickname = :nickname", AccountDb.class)
                        .setParameter("nickname", nickname)
                        .getSingleResult();
                tx.commit();
                return AccountMapper.toDomain(entity);
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

    }

    @Override
    public void delete(Account account) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            try {
                AccountDb accountDb = session.createQuery("SELECT a FROM AccountDb a WHERE a.uuid = :uuid", AccountDb.class)
                        .setParameter("uuid", account.getUuid().toString())
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
}
