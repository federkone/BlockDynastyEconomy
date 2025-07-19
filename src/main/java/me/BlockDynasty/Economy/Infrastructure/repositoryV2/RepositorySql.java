package me.BlockDynasty.Economy.Infrastructure.repositoryV2;

import me.BlockDynasty.Economy.Infrastructure.repository.ConnectionHandler.Hibernate.Connection;
import me.BlockDynasty.Economy.Infrastructure.repository.Criteria.Criteria;
import me.BlockDynasty.Economy.Infrastructure.repository.Exceptions.RepositoryException;
import me.BlockDynasty.Economy.Infrastructure.repositoryV2.Models.Hibernate.AccountDb;
import me.BlockDynasty.Economy.Infrastructure.repositoryV2.Models.Hibernate.AccountMapper;
import me.BlockDynasty.Economy.domain.entities.account.Account;
import me.BlockDynasty.Economy.domain.entities.account.Exceptions.AccountAlreadyExist;
import me.BlockDynasty.Economy.domain.entities.account.Exceptions.AccountNotFoundException;
import me.BlockDynasty.Economy.domain.entities.currency.Currency;
import me.BlockDynasty.Economy.domain.entities.currency.Exceptions.CurrencyNotFoundException;
import me.BlockDynasty.Economy.domain.persistence.entities.IAccountRepository;
import me.BlockDynasty.Economy.domain.persistence.entities.ICurrencyRepository;
import me.BlockDynasty.Economy.domain.persistence.entities.IRepository;
import me.BlockDynasty.Economy.domain.persistence.transaction.ITransactions;
import me.BlockDynasty.Economy.domain.result.ErrorCode;
import me.BlockDynasty.Economy.domain.result.Result;
import me.BlockDynasty.Economy.domain.result.TransferResult;
import org.hibernate.SessionFactory;


import java.math.BigDecimal;
import java.util.List;


public class RepositorySql implements IRepository {
    private final SessionFactory sessionFactory;
    IAccountRepository accountRepository;
    ICurrencyRepository currencyRepository;
    ITransactions transactionsRepository;

    public RepositorySql(Connection connection) {
        this.sessionFactory = connection.getSession();
        this.accountRepository = new AccountRepository(sessionFactory);
        this.currencyRepository = new CurrencyRepository(sessionFactory);
        this.transactionsRepository = new TransactionRepository(sessionFactory);
    }

    @Override
    public List<Currency> loadCurrencies(Criteria criteria) {
        return currencyRepository.findAll();
    }

    public Result<Currency> loadCurrencyByName(String name) {
        try {
            Currency currency = currencyRepository.findByName(name);
            return Result.success(currency);
        } catch (CurrencyNotFoundException e) {
            return Result.failure("Currency with name " + name + " not found.", ErrorCode.CURRENCY_NOT_FOUND);
        } catch (Exception e) {
            return Result.failure("Error loading currency: " + e.getMessage(), ErrorCode.DATA_BASE_ERROR);
        }
    }

    public Result<Currency> loadCurrencyByUuid(String uuid) {
        try {
            Currency currency = currencyRepository.findByUuid(uuid);
            return Result.success(currency);
        } catch (CurrencyNotFoundException e) {
            return Result.failure("Currency with UUID " + uuid + " not found.", ErrorCode.CURRENCY_NOT_FOUND);
        } catch (Exception e) {
            return Result.failure("Error loading currency: " + e.getMessage(), ErrorCode.DATA_BASE_ERROR);
        }
    }

    public Result<Currency> loadDefaultCurrency() {
        try {
            Currency currency = currencyRepository.findDefaultCurrency();
            return Result.success(currency);
        } catch (CurrencyNotFoundException e) {
            return Result.failure("Default currency not found.", ErrorCode.CURRENCY_NOT_FOUND);
        } catch (Exception e) {
            return Result.failure("Error loading default currency: " + e.getMessage(), ErrorCode.DATA_BASE_ERROR);
        }
    }

    @Override
    public void saveCurrency(Currency currency) {
        try {
            currencyRepository.save(currency);
        }catch (CurrencyNotFoundException e) {
            currencyRepository.create(currency);
        } catch (Exception e) {
            throw new RuntimeException("Error saving currency: " + e.getMessage(), e);
        }
    }

    @Override
    public void deleteCurrency(Currency currency) {
        try {
            currencyRepository.delete(currency);
        }catch (CurrencyNotFoundException e) {
            //
        }catch (Exception e) {
            //
        }

    }

    @Override
    public List<Account> loadAccounts(Criteria criteria) {
        return accountRepository.findAll();
    }

    @Override
    public Result<Account> loadAccountByUuid(String uuid) {
        try{
            Account account =accountRepository.findByUuid(uuid);
            return Result.success(account);
        }catch (AccountNotFoundException e) {
            return Result.failure("Account with UUID " + uuid + " not found.",ErrorCode.ACCOUNT_NOT_FOUND);
        } catch (Exception e) {
            return Result.failure("Error loading account: " + e.getMessage(),ErrorCode.DATA_BASE_ERROR);
        }
    }

    @Override
    public Result<Account> loadAccountByName(String name) {
        try{
            Account account = accountRepository.findByNickname(name);
            return Result.success(account);
        }catch (AccountNotFoundException e) {
            return Result.failure("Account with name " + name + " not found.", ErrorCode.ACCOUNT_NOT_FOUND);
        } catch (Exception e) {
            return Result.failure("Error loading account: " + e.getMessage(), ErrorCode.DATA_BASE_ERROR);
        }
    }

    @Override
    public void createAccount(Account account) {
        try {
            accountRepository.create(account);
        }catch (AccountAlreadyExist e){//
             //
        }
        catch (Exception e) {
           //
        }
    }

    @Override
    public void saveAccount(Account account) {
        try {
            accountRepository.save(account);
        }catch (AccountNotFoundException e) {
            accountRepository.create(account);
        }catch (Exception e) {
            throw new RepositoryException ( "Error saving account: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Account> getAccountsTopByCurrency(String currencyName, int limit, int offset) {
           return accountRepository.getAccountsTopByCurrency( currencyName, limit, offset );
    }

    @Override
    public boolean isTopSupported() {
        return true;
    }

    @Override
    public String getName() {
        return "mysql";
    }

    @Override
    public void close() {
        sessionFactory.close();
    }

    @Override
    public void clearAll() {
        try (org.hibernate.Session session = sessionFactory.openSession()) {
            org.hibernate.Transaction tx = session.beginTransaction();
            try {
                // Delete in correct order to respect foreign key constraints
                session.createQuery("DELETE FROM BalanceDb").executeUpdate();
                session.createQuery("DELETE FROM WalletDb").executeUpdate();
                session.createQuery("DELETE FROM AccountDb").executeUpdate();
                session.createQuery("DELETE FROM CurrencyDb").executeUpdate();
                tx.commit();
            } catch (Exception e) {
                tx.rollback();
                throw new RepositoryException("Error clearing database: " + e.getMessage(), e);
            }
        }
    }

    @Override
    public Result<TransferResult> transfer(String fromUuid, String toUuid, Currency currency, BigDecimal amount) {
        return transactionsRepository.transfer(fromUuid, toUuid, currency, amount);
    }

    @Override
    public Result<Account> withdraw(String accountUuid, Currency currency, BigDecimal amount) {
        return transactionsRepository.withdraw( accountUuid, currency, amount);
    }

    @Override
    public Result<Account> deposit(String accountUuid, Currency currency, BigDecimal amount) {
        return transactionsRepository.deposit( accountUuid, currency, amount);
    }

    @Override
    public Result<Account> exchange(String fromUuid, Currency fromCurrency, BigDecimal amountFrom, Currency toCurrency, BigDecimal amountTo) {
        return transactionsRepository.exchange(fromUuid, fromCurrency, amountFrom, toCurrency, amountTo);
    }

    @Override
    public Result<TransferResult> trade(String fromUuid, String toUuid, Currency fromCurrency, Currency toCurrency, BigDecimal amountFrom, BigDecimal amountTo) {
        return transactionsRepository.trade(fromUuid, toUuid, fromCurrency, toCurrency, amountFrom, amountTo);
    }

    @Override
    public Result<Account> setBalance(String accountUuid, Currency currency, BigDecimal amount) {
        return transactionsRepository.setBalance(accountUuid, currency, amount);
    }
}
