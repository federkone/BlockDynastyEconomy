package useCaseTest.transaction;

import me.BlockDynasty.Economy.Infrastructure.repository.RepositorySql;
import mockClass.CourierTest;
import me.BlockDynasty.Economy.aplication.services.CurrencyService;
import me.BlockDynasty.Economy.domain.result.ErrorCode;
import me.BlockDynasty.Economy.domain.result.Result;
import me.BlockDynasty.Economy.aplication.useCase.account.GetAccountsUseCase;
import me.BlockDynasty.Economy.aplication.useCase.currency.GetCurrencyUseCase;
import me.BlockDynasty.Economy.aplication.useCase.transaction.DepositUseCase;
import me.BlockDynasty.Economy.domain.entities.account.Account;
import me.BlockDynasty.Economy.aplication.services.AccountService;
import me.BlockDynasty.Economy.domain.entities.currency.Currency;
import me.BlockDynasty.Economy.domain.persistence.entities.IRepository;
import mockClass.repositoryTest.ConnectionHandler.MockConnectionHibernateH2;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import mockClass.repositoryTest.RepositoryTest;
import mockClass.LoggerTest;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class DepositUseCaseTest {
    Account nullplague;
    Currency dinero;
    IRepository repository;
    CurrencyService currencyService;
    AccountService accountService;
    DepositUseCase depositUseCase;
    GetAccountsUseCase getAccountsUseCase;
    GetCurrencyUseCase getCurrencyUseCase;

    @BeforeEach
    void setUp() {
        nullplague = new Account(UUID.randomUUID(), "nullplague");
        dinero= new Currency(UUID.randomUUID(),"dinero","dinero");

        nullplague.add(dinero,BigDecimal.ZERO); // Inicializa el balance de la moneda con 0
        //nullplague.setBalance(dinero, BigDecimal.valueOf(0)); //todo:lo realiza automaticamente la cuenta, cuando no tiene el balance de la moneda a depositar, la crea en sus banalces y la setea en 0

        repository = new RepositoryTest();

        repository.saveAccount(nullplague);
        repository.saveCurrency(dinero);


        currencyService = new CurrencyService(repository);
        accountService = new AccountService(5);


        getAccountsUseCase = new GetAccountsUseCase(accountService, currencyService, repository);
        getCurrencyUseCase = new GetCurrencyUseCase(currencyService, repository);

        //accountManager.addAccountToCache(getAccountsUseCase.getAccount("nullplague"));

        depositUseCase = new DepositUseCase(getCurrencyUseCase,getAccountsUseCase, repository,new CourierTest(),new LoggerTest());
    }

    @Test
    void depositUseCaseTest() {
        Result<Void> result = depositUseCase.execute(nullplague.getUuid(), "dinero", BigDecimal.valueOf(5000));

        assertEquals(true, result.isSuccess(),
                "Expected success, but got: " + result.getErrorMessage() + " " + result.getErrorCode());

        //assertEquals(BigDecimal.valueOf(5000).setScale(2), getAccountsUseCase.getAccount("nullplague").getValue().getBalance("dinero").getBalance().setScale(2));
    }

    @Test
    void depositUseCaseTestWithoutBalance(){

        Result<Void> result = depositUseCase.execute(nullplague.getUuid(), "oro", BigDecimal.valueOf(10000));
        assertEquals(ErrorCode.CURRENCY_NOT_FOUND, result.getErrorCode() ,
                "Expected error code CURRENCY_NOT_FOUND, but got: " + result.getErrorMessage()+"  "+result.getErrorCode());;
    }


    @AfterEach
    void clearDb(){
        //repository.clearAll();
    }
}
