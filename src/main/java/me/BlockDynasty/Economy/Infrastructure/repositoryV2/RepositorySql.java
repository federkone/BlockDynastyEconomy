package me.BlockDynasty.Economy.Infrastructure.repositoryV2;

import me.BlockDynasty.Economy.Infrastructure.repository.ConnectionHandler.Hibernate.Connection;
import me.BlockDynasty.Economy.Infrastructure.repository.Criteria.Criteria;
import me.BlockDynasty.Economy.domain.entities.account.Account;
import me.BlockDynasty.Economy.domain.entities.currency.Currency;
import me.BlockDynasty.Economy.domain.persistence.entities.IAccountRepository;
import me.BlockDynasty.Economy.domain.persistence.entities.ICurrencyRepository;
import me.BlockDynasty.Economy.domain.persistence.entities.IRepository;
import me.BlockDynasty.Economy.domain.result.ErrorCode;
import me.BlockDynasty.Economy.domain.result.Result;
import me.BlockDynasty.Economy.domain.result.TransferResult;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.math.BigDecimal;
import java.util.List;

/*public class RepositorySql implements IRepository {
    private final SessionFactory sessionFactory;
    private boolean topSupported = true;

    public RepositorySql(Connection connection) {
        this.sessionFactory = connection.getSession();
    }

    @Override
    public List<Currency> loadCurrencies(Criteria criteria) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            try {
                CurrencyRepository repo = new CurrencyRepository(session);
                List<Currency> currencies = repo.findAll();
                tx.commit();
                return currencies;
            }catch (Exception e) {
                return  null;
            }
        }
    }

    @Override
    public void saveCurrency(Currency currency) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            CurrencyRepository repo = new CurrencyRepository(session);
            repo.save(currency);
            tx.commit();
        }
    }

    @Override
    public void deleteCurrency(Currency currency) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            CurrencyRepository repo = new CurrencyRepository(session);
            repo.delete(currency);
            tx.commit();
        }
    }

    @Override
    public List<Account> loadAccounts(Criteria criteria) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            AccountRepository repo = new AccountRepository(session);
            List<Account> accounts = repo.findAll();
            tx.commit();
            return accounts;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public Result<Account> loadAccountByUuid(String uuid) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            AccountRepository repo = new AccountRepository(session);
            Account account = repo.findByUuid(uuid);
            tx.commit();
            return Result.success(account);
        } catch (Exception e) {
            return Result.failure(e.getMessage(), ErrorCode.ACCOUNT_NOT_FOUND);
        }
    }

    @Override
    public Result<Account> loadAccountByName(String name) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            AccountRepository repo = new AccountRepository(session);
            Account account = repo.findByNickname(name);
            tx.commit();
            return Result.success(account);
        } catch (Exception e) {
            return Result.failure(e.getMessage(), ErrorCode.ACCOUNT_NOT_FOUND);
        }
    }

    @Override
    public void createAccount(Account account) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            AccountRepository repo = new AccountRepository(session);
            repo.create(account);
            tx.commit();
        }
    }

    @Override
    public void saveAccount(Account account) {
            try (Session session = sessionFactory.openSession()) {
                Transaction tx = session.beginTransaction();
                AccountRepository repo = new AccountRepository(session);
                repo.save(account);
                tx.commit();
        }
    }

    @Override
    public List<Account> getAccountsTopByCurrency(String currencyName, int limit, int offset) {
        return List.of();
    }

    @Override
    public boolean isTopSupported() {
        return false;
    }

    @Override
    public String getName() {
        return "";
    }

    @Override
    public void close() {

    }

    @Override
    public void clearAll() {

    }

    @Override
    public Result<TransferResult> transfer(String fromUuid, String toUuid, Currency currency, BigDecimal amount) {
        return null;
    }

    @Override
    public Result<Account> withdraw(String accountUuid, Currency currency, BigDecimal amount) {
        return null;
    }

    @Override
    public Result<Account> deposit(String accountUuid, Currency currency, BigDecimal amount) {
        return null;
    }

    @Override
    public Result<Account> exchange(String fromUuid, Currency fromCurrency, BigDecimal amountFrom, Currency toCurrency, BigDecimal amountTo) {
        return null;
    }

    @Override
    public Result<TransferResult> trade(String fromUuid, String toUuid, Currency fromCurrency, Currency toCurrency, BigDecimal amountFrom, BigDecimal amountTo) {
        return null;
    }

    @Override
    public Result<Account> setBalance(String accountUuid, Currency currency, BigDecimal amount) {
        return null;
    }
}*/
