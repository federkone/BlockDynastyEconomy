package useCaseTest.transaction;

import mockClass.CourierTest;
import BlockDynasty.Economy.domain.result.ErrorCode;
import BlockDynasty.Economy.domain.result.Result;
import BlockDynasty.Economy.aplication.useCase.account.GetAccountsUseCase;
import BlockDynasty.Economy.aplication.useCase.currency.GetCurrencyUseCase;
import BlockDynasty.Economy.aplication.useCase.transaction.TradeCurrenciesUseCase;
import BlockDynasty.Economy.domain.entities.account.Account;
import BlockDynasty.Economy.aplication.services.AccountService;
import BlockDynasty.Economy.domain.entities.account.Exceptions.AccountCanNotReciveException;
import BlockDynasty.Economy.domain.entities.account.Exceptions.AccountNotFoundException;
import BlockDynasty.Economy.domain.entities.account.Exceptions.InsufficientFundsException;
import BlockDynasty.Economy.domain.entities.currency.Currency;
import BlockDynasty.Economy.aplication.services.CurrencyService;
import BlockDynasty.Economy.domain.entities.currency.Exceptions.CurrencyNotFoundException;
import BlockDynasty.Economy.domain.entities.currency.Exceptions.CurrencyNotPayableException;
import BlockDynasty.Economy.domain.entities.currency.Exceptions.DecimalNotSupportedException;
import BlockDynasty.Economy.domain.persistence.Exceptions.TransactionException;
import BlockDynasty.Economy.domain.persistence.entities.IRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import mockClass.LoggerTest;
import repositoryTest.FactoryRepo;

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

        nullplague.setMoney(coin, BigDecimal.valueOf(1));
        nullplague.setMoney(dinero, BigDecimal.valueOf(0));
        cris.setMoney(coin, BigDecimal.valueOf(0));
        cris.setMoney(dinero, BigDecimal.valueOf(30000));

        repository = FactoryRepo.getDb();


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

        assertEquals(BigDecimal.valueOf(30000).setScale(2),getAccountsUseCase.getAccount("nullplague").getValue().getMoney(dinero).getAmount());
        assertEquals(BigDecimal.valueOf(1).setScale(2),getAccountsUseCase.getAccount("cris").getValue().getMoney(coin).getAmount());
    }

    @Test
    void TradeCurrencyUseCseTestInsufficientFounds(){
        Result<Void> result = tradeCurrenciesUseCase.execute("nullplague","cris","Coin","dinero",BigDecimal.valueOf(2),BigDecimal.valueOf(30000));
        assertEquals(ErrorCode.INSUFFICIENT_FUNDS, result.getErrorCode());
        assertEquals(BigDecimal.valueOf(1).setScale(2),getAccountsUseCase.getAccount("nullplague").getValue().getMoney(coin).getAmount());
        assertEquals(BigDecimal.valueOf(30000).setScale(2),getAccountsUseCase.getAccount("cris").getValue().getMoney(dinero).getAmount());
    }


    @AfterEach
    void clearDb(){
        //repository.clearAll();
    }
}
