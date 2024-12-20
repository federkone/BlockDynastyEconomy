package useCaseTest.transaction;

import static org.junit.jupiter.api.Assertions.*;

import me.BlockDynasty.Economy.aplication.result.ErrorCode;
import me.BlockDynasty.Economy.aplication.result.Result;
import me.BlockDynasty.Economy.aplication.useCase.account.GetAccountsUseCase;
import me.BlockDynasty.Economy.aplication.useCase.currency.GetCurrencyUseCase;
import me.BlockDynasty.Economy.aplication.useCase.transaction.PayUseCase;
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


public class PayUseCaseTest {
    Account nullplague;
    Account cris;
    IRepository repository;
    CurrencyCache currencyCache;
    AccountCache accountCache;
    GetAccountsUseCase getAccountsUseCase;
    GetCurrencyUseCase getCurrencyUseCase;
    PayUseCase payUseCase;
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
        accountCache = new AccountCache(5);

        //accountManager.addAccountToCache(account1); //se conecto el player1
        //accountManager.addAccountToCache(account2); //se conecto el player2

        getAccountsUseCase = new GetAccountsUseCase(accountCache, currencyCache,repository);
        getCurrencyUseCase = new GetCurrencyUseCase(currencyCache, repository);
        payUseCase = new PayUseCase(getCurrencyUseCase,getAccountsUseCase,repository,null,null);
    }

    @Test
    void payUseCseTest (){
        try {
            payUseCase.execute("nullplague","cris","dinero", BigDecimal.valueOf(10000));
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

        assertEquals(BigDecimal.valueOf(0),getAccountsUseCase.getAccount("nullplague").getValue().getBalance(dinero).getBalance());
        assertEquals(BigDecimal.valueOf(10000),getAccountsUseCase.getAccount("cris").getValue().getBalance(dinero).getBalance());
    }

    @Test
    void payUseCaseTestWithNullAccount(){
        /*assertThrows(AccountNotFoundException.class, () -> {
            payUseCase.execute("nullplague", "robert", "dinero", BigDecimal.valueOf(10000));
        });*/
        Result<Void> result = payUseCase.execute("nullplague", "robert", "dinero", BigDecimal.valueOf(10000));
        assertEquals(ErrorCode.ACCOUNT_NOT_FOUND, result.getErrorCode()); //ejemplo con patron result en lugar de excepciones
    }

    @Test
    void payUseCaseTestWithNullCurrency(){
        /*assertThrows(CurrencyNotFoundException.class, () -> {
            payUseCase.execute("nullplague", "cris", "oro", BigDecimal.valueOf(10000));
        });*/
        Result<Void> result = payUseCase.execute("nullplague", "cris", "oro", BigDecimal.valueOf(10000));
        assertEquals(ErrorCode.CURRENCY_NOT_FOUND, result.getErrorCode()); //ejemplo con patron result en lugar de excepciones
    }

    @Test
    void payUseCseTestWithoutFounds(){
       /* assertThrows(InsufficientFundsException.class, () -> {
            payUseCase.execute("nullplague", "cris", "dinero", BigDecimal.valueOf(10001));
        });*/
        Result<Void> result = payUseCase.execute("nullplague", "cris", "dinero", BigDecimal.valueOf(10001));
        assertEquals(ErrorCode.INSUFFICIENT_FUNDS, result.getErrorCode()); //ejemplo con patron result en lugar de excepciones
    }

    @Test
    void payUseCaseTestWithCurrencyNotPayable(){
        dinero.setPayable(false);
        /*assertThrows(CurrencyNotPayableException.class, () -> {
            payUseCase.execute("nullplague", "cris", "dinero", BigDecimal.valueOf(10000));
        });*/
        Result<Void> result = payUseCase.execute("nullplague", "cris", "dinero", BigDecimal.valueOf(10000));
        assertEquals(ErrorCode.CURRENCY_NOT_PAYABLE, result.getErrorCode()); //ejemplo con patron result en lugar de excepciones
    }

    @Test
    void setPayUseCaseTestAccountCanNotRecibe(){
        cris.setCanReceiveCurrency(false);
        /*assertThrows(AccountCanNotReciveException.class, () -> {
            payUseCase.execute("nullplague", "cris", "dinero", BigDecimal.valueOf(10000));
        });*/
        Result<Void> result = payUseCase.execute("nullplague", "cris", "dinero", BigDecimal.valueOf(10000));
        assertEquals(ErrorCode.ACCOUNT_CAN_NOT_RECEIVE, result.getErrorCode()); //ejemplo con patron result en lugar de excepciones

        assertEquals(BigDecimal.valueOf(10000),getAccountsUseCase.getAccount("nullplague").getValue().getBalance(dinero).getBalance());
        assertEquals(BigDecimal.valueOf(0),getAccountsUseCase.getAccount("cris").getValue().getBalance(dinero).getBalance());


    }



    @AfterEach
    void clearDb(){
        repository.clearAll();
    }
}
