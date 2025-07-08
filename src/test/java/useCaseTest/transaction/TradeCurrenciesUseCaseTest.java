package useCaseTest.transaction;

import Integrations.CourierTest;
import me.BlockDynasty.Economy.domain.result.ErrorCode;
import me.BlockDynasty.Economy.domain.result.Result;
import me.BlockDynasty.Economy.aplication.useCase.account.GetAccountsUseCase;
import me.BlockDynasty.Economy.aplication.useCase.currency.GetCurrencyUseCase;
import me.BlockDynasty.Economy.aplication.useCase.transaction.TradeCurrenciesUseCase;
import me.BlockDynasty.Economy.domain.account.Account;
import me.BlockDynasty.Economy.Infrastructure.services.AccountService;
import me.BlockDynasty.Economy.domain.account.Exceptions.AccountCanNotReciveException;
import me.BlockDynasty.Economy.domain.account.Exceptions.AccountNotFoundException;
import me.BlockDynasty.Economy.domain.account.Exceptions.InsufficientFundsException;
import me.BlockDynasty.Economy.domain.currency.Currency;
import me.BlockDynasty.Economy.Infrastructure.services.CurrencyService;
import me.BlockDynasty.Economy.domain.currency.Exceptions.CurrencyNotFoundException;
import me.BlockDynasty.Economy.domain.currency.Exceptions.CurrencyNotPayableException;
import me.BlockDynasty.Economy.domain.currency.Exceptions.DecimalNotSupportedException;
import me.BlockDynasty.Economy.Infrastructure.repository.Exceptions.TransactionException;
import me.BlockDynasty.Economy.domain.persistence.entities.IRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repositoryTest.RepositoryTest;
import useCaseTest.transaction.MoksStubs.LoggerTest;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class TradeCurrenciesUseCaseTest {
    Account nullplague;
    Account cris;
    IRepository repository;
    CurrencyService currencyService;
    AccountService accountService;
    GetAccountsUseCase getAccountsUseCase;
    GetCurrencyUseCase getCurrencyUseCase;
    TradeCurrenciesUseCase tradeCurrenciesUseCase;
    Currency coin;
    Currency dinero;

    @BeforeEach
    void setUp() {
        coin = new Currency(UUID.randomUUID(),"Coin","Coins");
        dinero = new Currency(UUID.randomUUID(),"dinero","dinero");

        nullplague = new Account(UUID.randomUUID(), "nullplague");
        cris = new Account(UUID.randomUUID(), "cris");

        nullplague.setBalance(coin, BigDecimal.valueOf(1));
        nullplague.setBalance(dinero, BigDecimal.valueOf(0));
        cris.setBalance(coin, BigDecimal.valueOf(0));
        cris.setBalance(dinero, BigDecimal.valueOf(30000));

        repository = new RepositoryTest();


        repository.saveCurrency(coin);
        repository.saveCurrency(dinero);
        try{
            repository.createAccount(nullplague);
            repository.createAccount(cris);
        }catch (Exception e){
            fail("Error saving accounts " + e.getMessage());
        }


        currencyService = new CurrencyService(repository);
        accountService = new AccountService(5);

        //accountManager.addAccountToCache(account1); //se conecto el player1
        //accountManager.addAccountToCache(account2); //se conecto el player2

        getAccountsUseCase = new GetAccountsUseCase(accountService, currencyService,repository);
        getCurrencyUseCase = new GetCurrencyUseCase(currencyService, repository);

        tradeCurrenciesUseCase = new TradeCurrenciesUseCase(getCurrencyUseCase,getAccountsUseCase,repository,new CourierTest(),new LoggerTest());
    }

    @Test
    void TradeCurrencyUseCaseTest (){
        try {
            tradeCurrenciesUseCase.execute( "nullplague","cris","Coin","dinero",BigDecimal.valueOf(1),BigDecimal.valueOf(30000));
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
        }catch (TransactionException e){
            fail("Transaction exception"+ e);
        }catch (Exception e){
            fail("Exception"+ e);
        }

        assertEquals(BigDecimal.valueOf(30000),getAccountsUseCase.getAccount("nullplague").getValue().getBalance(dinero).getBalance());
        assertEquals(BigDecimal.valueOf(1),getAccountsUseCase.getAccount("cris").getValue().getBalance(coin).getBalance());
    }

    @Test
    void TradeCurrencyUseCseTestInsufficientFounds(){
        /*assertThrows(InsufficientFundsException.class, () -> {
            tradeCurrenciesUseCase.execute("nullplague","cris","Coin","dinero",BigDecimal.valueOf(2),BigDecimal.valueOf(30000));
        });*/
        Result<Void> result = tradeCurrenciesUseCase.execute("nullplague","cris","Coin","dinero",BigDecimal.valueOf(2),BigDecimal.valueOf(30000));
        assertEquals(ErrorCode.INSUFFICIENT_FUNDS, result.getErrorCode());
        assertEquals(BigDecimal.valueOf(1),getAccountsUseCase.getAccount("nullplague").getValue().getBalance(coin).getBalance());
        assertEquals(BigDecimal.valueOf(30000),getAccountsUseCase.getAccount("cris").getValue().getBalance(dinero).getBalance());
    }


    @AfterEach
    void clearDb(){
        //repository.clearAll();
    }
}
