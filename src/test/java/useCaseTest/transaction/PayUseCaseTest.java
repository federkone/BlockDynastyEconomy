package useCaseTest.transaction;

import static org.junit.jupiter.api.Assertions.*;

import me.BlockDynasty.Economy.Infrastructure.repositoryV2.RepositorySql;
import me.BlockDynasty.Economy.aplication.useCase.currency.EditCurrencyUseCase;
import mockClass.CourierTest;
import me.BlockDynasty.Economy.aplication.services.CurrencyService;
import me.BlockDynasty.Economy.domain.result.ErrorCode;
import me.BlockDynasty.Economy.domain.result.Result;
import me.BlockDynasty.Economy.aplication.useCase.account.GetAccountsUseCase;
import me.BlockDynasty.Economy.aplication.useCase.currency.GetCurrencyUseCase;
import me.BlockDynasty.Economy.aplication.useCase.transaction.PayUseCase;
import me.BlockDynasty.Economy.domain.entities.account.Account;
import me.BlockDynasty.Economy.aplication.services.AccountService;
import me.BlockDynasty.Economy.domain.entities.currency.Currency;
import me.BlockDynasty.Economy.domain.persistence.entities.IRepository;
import repositoryTest.ConnectionHandler.MockConnectionHibernateH2;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import mockClass.LoggerTest;
import repositoryTest.FactoryrRepo;

import java.math.BigDecimal;
import java.util.UUID;


public class PayUseCaseTest {
    Account nullplague;
    Account cris;
    IRepository repository;
    CurrencyService currencyService;
    AccountService accountService;
    GetAccountsUseCase getAccountsUseCase;
    GetCurrencyUseCase getCurrencyUseCase;
    EditCurrencyUseCase editCurrencyUseCase;
    PayUseCase payUseCase;
    Currency coin;
    Currency dinero;
    Currency plata;

    @BeforeEach
    void setUp() {
        coin = new Currency(UUID.randomUUID(),"Coin","Coins");
        dinero = new Currency(UUID.randomUUID(),"dinero","dinero");
        plata = new Currency(UUID.randomUUID(),"plata","plata");

        nullplague = new Account(UUID.randomUUID(), "nullplague");
        cris = new Account(UUID.randomUUID(), "cris");

        nullplague.setBalance(plata, BigDecimal.valueOf(1000));
        nullplague.setBalance(coin, BigDecimal.valueOf(0));
        nullplague.setBalance(dinero, BigDecimal.valueOf(10000));
        cris.setBalance(plata, BigDecimal.valueOf(1000));
        cris.setBalance(coin, BigDecimal.valueOf(0));
        cris.setBalance(dinero, BigDecimal.valueOf(0));

        repository = FactoryrRepo.getDb();

        repository.saveCurrency(plata);
        repository.saveCurrency(coin);
        repository.saveCurrency(dinero);
        repository.saveAccount(nullplague);
        repository.saveAccount(cris);


        currencyService = new CurrencyService(repository);
        accountService = new AccountService(5);

        accountService.addAccountToCache(nullplague); //se conecto el player1
        accountService.addAccountToCache(cris); //se conecto el player2

        getAccountsUseCase = new GetAccountsUseCase(accountService, currencyService,repository);
        getCurrencyUseCase = new GetCurrencyUseCase(currencyService, repository);
        payUseCase = new PayUseCase(getCurrencyUseCase,getAccountsUseCase,repository,new CourierTest(),new LoggerTest());
        editCurrencyUseCase= new EditCurrencyUseCase(currencyService ,new CourierTest(),repository);
    }

    @Test
    void payUseCseTest (){
        Result<Void> result = payUseCase.execute("nullplague","cris","dinero", BigDecimal.valueOf(10000));
        assertTrue(result.isSuccess());

        assertEquals(BigDecimal.valueOf(0).setScale(2),getAccountsUseCase.getAccount("nullplague").getValue().getBalance(dinero).getAmount());
        assertEquals(BigDecimal.valueOf(10000).setScale(2),getAccountsUseCase.getAccount("cris").getValue().getBalance(dinero).getAmount());
    }

    @Test
    void payUseCaseTestWithNullAccount(){
        Result<Void> result = payUseCase.execute("nullplague", "robert", "dinero", BigDecimal.valueOf(10000));
        assertEquals(ErrorCode.ACCOUNT_NOT_FOUND, result.getErrorCode()); //ejemplo con patron result en lugar de excepciones
    }

    @Test
    void payUseCaseTestWithNullCurrency(){
        Result<Void> result = payUseCase.execute("nullplague", "cris", "oro", BigDecimal.valueOf(10000));
        assertEquals(ErrorCode.CURRENCY_NOT_FOUND, result.getErrorCode()); //ejemplo con patron result en lugar de excepciones
    }

    @Test
    void payUseCseTestWithoutFounds(){
        Result<Void> result = payUseCase.execute("nullplague", "cris", "dinero", BigDecimal.valueOf(10001));
        assertEquals(ErrorCode.INSUFFICIENT_FUNDS, result.getErrorCode()); //ejemplo con patron result en lugar de excepciones
    }

    @Test
    void payUseCaseTestWithCurrencyNotPayable(){
        editCurrencyUseCase.togglePayable("plata");
        Result<Void> result = payUseCase.execute("nullplague", "cris", "plata", BigDecimal.valueOf(10000));
        assertEquals(ErrorCode.CURRENCY_NOT_PAYABLE, result.getErrorCode()); //ejemplo con patron result en lugar de excepciones
    }

    @Test
    void setPayUseCaseTestAccountCanNotRecibe(){
        cris.setCanReceiveCurrency(false);
        Result<Void> result = payUseCase.execute("nullplague", "cris", "dinero", BigDecimal.valueOf(10000));
        assertEquals(ErrorCode.ACCOUNT_CAN_NOT_RECEIVE, result.getErrorCode()); //ejemplo con patron result en lugar de excepciones

        assertEquals(BigDecimal.valueOf(10000),getAccountsUseCase.getAccount("nullplague").getValue().getBalance(dinero).getAmount());
        assertEquals(BigDecimal.valueOf(0),getAccountsUseCase.getAccount("cris").getValue().getBalance(dinero).getAmount());
    }


    //@AfterEach
    //void clearDb(){
      //  repository.clearAll();
    //}
}
