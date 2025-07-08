package useCaseTest.transaction;

import Integrations.CourierTest;
import me.BlockDynasty.Economy.Infrastructure.services.CurrencyService;
import me.BlockDynasty.Economy.domain.result.ErrorCode;
import me.BlockDynasty.Economy.domain.result.Result;
import me.BlockDynasty.Economy.aplication.useCase.account.GetAccountsUseCase;
import me.BlockDynasty.Economy.aplication.useCase.currency.GetCurrencyUseCase;
import me.BlockDynasty.Economy.aplication.useCase.transaction.DepositUseCase;
import me.BlockDynasty.Economy.domain.account.Account;
import me.BlockDynasty.Economy.Infrastructure.services.AccountService;
import me.BlockDynasty.Economy.domain.currency.Currency;
import me.BlockDynasty.Economy.domain.persistence.entities.IRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repositoryTest.RepositoryTest;
import useCaseTest.transaction.MoksStubs.LoggerTest;

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

        //nullplague.setBalance(dinero, BigDecimal.valueOf(0)); //todo:lo realiza automaticamente la cuenta, cuando no tiene el balance de la moneda a depositar, la crea en sus banalces y la setea en 0


        repository = new RepositoryTest();

        repository.saveCurrency(dinero);
        repository.saveAccount(nullplague);

        currencyService = new CurrencyService(repository);
        accountService = new AccountService(5);


        getAccountsUseCase = new GetAccountsUseCase(accountService, currencyService, repository);
        getCurrencyUseCase = new GetCurrencyUseCase(currencyService, repository);

        //accountManager.addAccountToCache(getAccountsUseCase.getAccount("nullplague"));

        depositUseCase = new DepositUseCase(getCurrencyUseCase,getAccountsUseCase, repository,new CourierTest(),new LoggerTest());
    }

    @Test
    void depositUseCaseTest() {
        depositUseCase.execute("nullplague", "dinero", BigDecimal.valueOf(5000));
        //Currency dinero = currencyManager.getCurrency("dinero");
        assertEquals(BigDecimal.valueOf(5000).setScale(2), getAccountsUseCase.getAccount("nullplague").getValue().getBalance("dinero").getBalance().setScale(2));
    }

    @Test
    void depositUseCaseTestWithoutBalance(){
        /*assertThrows(CurrencyNotFoundException.class, () -> {
            depositUseCase.execute("nullplague", "oro", BigDecimal.valueOf(10000));
        });*/
        Result<Void> result = depositUseCase.execute("nullplague", "oro", BigDecimal.valueOf(10000));
        assertEquals(ErrorCode.CURRENCY_NOT_FOUND, result.getErrorCode());
    }

    @AfterEach
    void clearDb(){
        //repository.clearAll();
    }
}
