package useCaseTest.transaction;

import Integrations.CourierTest;
import me.BlockDynasty.Economy.aplication.useCase.account.GetAccountsUseCase;
import me.BlockDynasty.Economy.aplication.useCase.currency.GetCurrencyUseCase;
import me.BlockDynasty.Economy.aplication.useCase.transaction.TransferFundsUseCase;
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
import me.BlockDynasty.Economy.domain.persistence.entities.IRepository;
import me.BlockDynasty.Economy.domain.result.ErrorCode;
import me.BlockDynasty.Economy.domain.result.Result;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repositoryTest.RepositoryTest;
import useCaseTest.transaction.MoksStubs.LoggerTest;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class TransferUseCaseTest {
    Account nullplague;
    Account cris;
    IRepository repository;
    CurrencyService currencyService;
    AccountService accountService;
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

        currencyService = new CurrencyService(repository);
        accountService = new AccountService(5);

        //accountManager.addAccountToCache(account1); //se conecto el player1
        //accountManager.addAccountToCache(account2); //se conecto el player2

        getAccountsUseCase = new GetAccountsUseCase(accountService, currencyService,repository);
        getCurrencyUseCase = new GetCurrencyUseCase(currencyService, repository);
        transferFundsUseCase = new TransferFundsUseCase(getCurrencyUseCase,getAccountsUseCase,repository,new CourierTest(),new LoggerTest());
    }

    @Test
    void TransferTest (){
    /*try {

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
        }*/
        Result<Void> result = transferFundsUseCase.execute("nullplague","cris","dinero", BigDecimal.valueOf(10000));

        assertEquals(BigDecimal.valueOf(0).setScale(2),getAccountsUseCase.getAccount("nullplague").getValue().getBalance("dinero").getBalance().setScale(2));
        assertEquals(BigDecimal.valueOf(10000).setScale(2),getAccountsUseCase.getAccount("cris").getValue().getBalance("dinero").getBalance().setScale(2));
    }

    @Test
    void TransferTestWithNegativeAmount (){
        Result<Void> result = transferFundsUseCase.execute("nullplague","cris","dinero", BigDecimal.valueOf(-1));
        assertEquals(result.getErrorCode(), ErrorCode.INVALID_AMOUNT);
    }

    @Test
    void TransferTestWithZeroAmount (){
        Result<Void> result = transferFundsUseCase.execute("nullplague","cris","dinero", BigDecimal.valueOf(0));
        assertEquals(result.getErrorCode(), ErrorCode.INVALID_AMOUNT);
    }
    @Test
    void TransferTestWithInsufficientFunds (){
        Result<Void> result = transferFundsUseCase.execute("nullplague","cris","dinero", BigDecimal.valueOf(100000));
        assertEquals(result.getErrorCode(),ErrorCode.INSUFFICIENT_FUNDS);
    }

    @Test
    void TransferTestWithNullAccount (){
        Result<Void> result = transferFundsUseCase.execute("nullplague","tom","dinero", BigDecimal.valueOf(10000));
        assertEquals(result.getErrorCode(),ErrorCode.ACCOUNT_NOT_FOUND);
    }

    @Test
    void TransferTestWithNullCurrency (){
        Result<Void> result = transferFundsUseCase.execute("nullplague","cris","plata", BigDecimal.valueOf(10000));
        assertEquals(result.getErrorCode(),ErrorCode.CURRENCY_NOT_FOUND);
    }

    @AfterEach
    void clearDb(){
        //repository.clearAll();
    }
}
