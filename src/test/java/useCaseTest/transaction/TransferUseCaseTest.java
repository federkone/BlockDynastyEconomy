package useCaseTest.transaction;

import me.BlockDynasty.Economy.aplication.useCase.account.GetAccountsUseCase;
import me.BlockDynasty.Economy.aplication.useCase.currency.GetCurrencyUseCase;
import me.BlockDynasty.Economy.aplication.useCase.transaction.TransferFundsUseCase;
import me.BlockDynasty.Economy.domain.account.Account;
import me.BlockDynasty.Economy.domain.account.AccountCache;
import me.BlockDynasty.Economy.domain.account.Exceptions.AccountCanNotReciveException;
import me.BlockDynasty.Economy.domain.account.Exceptions.AccountNotFoundException;
import me.BlockDynasty.Economy.domain.account.Exceptions.InsufficientFundsException;
import me.BlockDynasty.Economy.domain.currency.Currency;
import me.BlockDynasty.Economy.domain.currency.CurrencyCache;
import me.BlockDynasty.Economy.domain.currency.Exceptions.CurrencyNotFoundException;
import me.BlockDynasty.Economy.domain.currency.Exceptions.CurrencyNotPayableException;
import me.BlockDynasty.Economy.domain.currency.Exceptions.DecimalNotSupportedException;
import me.BlockDynasty.Economy.domain.repository.IRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repositoryTest.RepositoryTest;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class TransferUseCaseTest {
    Account nullplague;
    Account cris;
    IRepository repository;
    CurrencyCache currencyCache;
    AccountCache accountCache;
    GetAccountsUseCase getAccountsUseCase;
    GetCurrencyUseCase getCurrencyUseCase;
    TransferFundsUseCase transferFundsUseCase;
    Currency coin;
    Currency dinero;

    @BeforeEach
    void setUp() {
        coin = new Currency(UUID.randomUUID(),"Coin","Coins");
        dinero = new Currency(UUID.randomUUID(),"dinero","dinero");

        nullplague = new Account(UUID.randomUUID(), "nullplague");
        cris = new Account(UUID.randomUUID(), "cris");

        nullplague.setBalance(coin, BigDecimal.valueOf(0));
        nullplague.setBalance(dinero, BigDecimal.valueOf(10000));
        cris.setBalance(coin, BigDecimal.valueOf(0));
        cris.setBalance(dinero, BigDecimal.valueOf(0));

        repository = new RepositoryTest();

        repository.saveCurrency(coin);
        repository.saveCurrency(dinero);
        repository.saveAccount(nullplague);
        repository.saveAccount(cris);

        currencyCache = new CurrencyCache(repository);
        accountCache = new AccountCache();

        //accountManager.addAccountToCache(account1); //se conecto el player1
        //accountManager.addAccountToCache(account2); //se conecto el player2

        getAccountsUseCase = new GetAccountsUseCase(accountCache, currencyCache,repository);
        getCurrencyUseCase = new GetCurrencyUseCase(currencyCache, repository);
        transferFundsUseCase = new TransferFundsUseCase(getCurrencyUseCase,getAccountsUseCase,repository,null,null);
    }

    @Test
    void TransferTest (){
        try {
            transferFundsUseCase.execute("nullplague","cris","dinero", BigDecimal.valueOf(10000));
        }catch (AccountNotFoundException e){
            fail("Account not found");
        }catch (InsufficientFundsException e){
            fail("Insufficient funds");
        }catch (CurrencyNotFoundException e){
            fail("Currency not found");
        }catch (AccountCanNotReciveException e){
            fail("Account can not recive");
        }catch (CurrencyNotPayableException e){
            fail("Currency not payable");
        }catch (DecimalNotSupportedException e){
            fail("Decimal not supported");
        }

        assertEquals(BigDecimal.valueOf(0).setScale(2),getAccountsUseCase.getAccount("nullplague").getBalance("dinero").getBalance().setScale(2));
        assertEquals(BigDecimal.valueOf(10000).setScale(2),getAccountsUseCase.getAccount("cris").getBalance("dinero").getBalance().setScale(2));

    }

    @AfterEach
    void clearDb(){
        //repository.clearAll();
    }
}
