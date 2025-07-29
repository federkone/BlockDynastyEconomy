package useCaseTest.transaction;

import BlockDynasty.Economy.aplication.events.EventManager;
import BlockDynasty.Economy.aplication.useCase.account.CreateAccountUseCase;
import BlockDynasty.Economy.aplication.useCase.currency.CreateCurrencyUseCase;
import mockClass.CourierTest;
import BlockDynasty.Economy.aplication.services.CurrencyService;
import BlockDynasty.Economy.domain.result.Result;
import BlockDynasty.Economy.aplication.useCase.account.SearchAccountUseCase;
import BlockDynasty.Economy.aplication.useCase.currency.SearchCurrencyUseCase;
import BlockDynasty.Economy.aplication.useCase.transaction.DepositUseCase;
import BlockDynasty.Economy.domain.entities.account.Account;
import BlockDynasty.Economy.aplication.services.AccountService;
import BlockDynasty.Economy.domain.entities.currency.Currency;
import BlockDynasty.Economy.domain.persistence.entities.IRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import mockClass.LoggerTest;
import repositoryTest.FactoryRepo;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class DepositUseCaseTest {
    Account nullplague;
    Currency dinero;
    IRepository repository;
    CurrencyService currencyService;
    AccountService accountService;
    DepositUseCase depositUseCase;
    SearchAccountUseCase searchAccountUseCase;
    SearchCurrencyUseCase searchCurrencyUseCase;
    CreateAccountUseCase createAccountUseCase;
    CreateCurrencyUseCase createCurrenyUseCase;

    @BeforeEach
    void setUp() {
        nullplague = new Account(UUID.randomUUID(), "nullplague");
        dinero= new Currency(UUID.randomUUID(),"dinero","dinero");

        repository = FactoryRepo.getDb();
        currencyService = new CurrencyService(repository);
        accountService = new AccountService(5);

        searchAccountUseCase = new SearchAccountUseCase(accountService, currencyService, repository);
        searchCurrencyUseCase = new SearchCurrencyUseCase(currencyService, repository);
        createAccountUseCase = new CreateAccountUseCase(accountService, currencyService, searchAccountUseCase,repository);
        createCurrenyUseCase = new CreateCurrencyUseCase(currencyService, searchAccountUseCase, new CourierTest(), repository);
        depositUseCase = new DepositUseCase(searchCurrencyUseCase, searchAccountUseCase, repository,new CourierTest(),new LoggerTest(),new EventManager());

        createCurrenyUseCase.createCurrency(dinero.getSingular(), dinero.getPlural());
        createAccountUseCase.execute(nullplague.getUuid(), nullplague.getNickname());
    }

    @Test
    void depositUseCaseTest() {
        Result<Void> result = depositUseCase.execute(nullplague.getUuid(), "dinero", BigDecimal.valueOf(5000));

        assertTrue( result.isSuccess(),"Expected success, but got: " + result.getErrorMessage() + " " + result.getErrorCode());
        assertEquals(BigDecimal.valueOf(5000).setScale(2), searchAccountUseCase.getAccount("nullplague").getValue().getMoney("dinero").getAmount().setScale(2));
    }

    /*@Test
    void depositUseCaseTestWithoutBalance(){
        Result<Void> result = depositUseCase.execute(nullplague.getUuid(), "oro", BigDecimal.valueOf(10000));
        assertEquals(ErrorCode.CURRENCY_NOT_FOUND, result.getErrorCode(),  result.getErrorMessage()+"  "+result.getErrorCode());;
    }*/


    @AfterEach
    void clearDb(){
        //repository.clearAll();
    }
}
