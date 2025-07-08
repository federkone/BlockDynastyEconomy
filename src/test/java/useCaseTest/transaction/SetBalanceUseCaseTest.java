package useCaseTest.transaction;

import Integrations.CourierTest;
import me.BlockDynasty.Economy.Infrastructure.services.CurrencyService;
import me.BlockDynasty.Economy.domain.result.ErrorCode;
import me.BlockDynasty.Economy.domain.result.Result;
import me.BlockDynasty.Economy.aplication.useCase.account.GetAccountsUseCase;
import me.BlockDynasty.Economy.aplication.useCase.currency.GetCurrencyUseCase;
import me.BlockDynasty.Economy.aplication.useCase.transaction.SetBalanceUseCase;
import me.BlockDynasty.Economy.domain.account.Account;
import me.BlockDynasty.Economy.Infrastructure.services.AccountService;
import me.BlockDynasty.Economy.domain.persistence.entities.IRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import me.BlockDynasty.Economy.domain.currency.Currency;

import repositoryTest.RepositoryTest;
import useCaseTest.transaction.MoksStubs.LoggerTest;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class SetBalanceUseCaseTest {
    Account nullplague;
    Currency dinero;
    IRepository repository;
    CurrencyService currencyService;
    AccountService accountService;
    SetBalanceUseCase setBalanceUseCase;
    GetAccountsUseCase getAccountsUseCase;
    GetCurrencyUseCase getCurrencyUseCase;

    @BeforeEach
    void setUp() {
        nullplague = new Account(UUID.randomUUID(), "nullplague");
        dinero= new Currency(UUID.randomUUID(),"dinero","dinero");

        nullplague.setBalance(dinero, BigDecimal.valueOf(10000));

        repository = new RepositoryTest();

        repository.saveCurrency(dinero);
        repository.saveAccount(nullplague);

        currencyService = new CurrencyService(repository);
        accountService = new AccountService(5);

        getAccountsUseCase = new GetAccountsUseCase(accountService, currencyService, repository);
        getCurrencyUseCase = new GetCurrencyUseCase(currencyService, repository);
        setBalanceUseCase = new SetBalanceUseCase(getCurrencyUseCase,getAccountsUseCase, repository,new CourierTest(),new LoggerTest());
    }

    @Test
    void setBalanceUseCaseTest() {
        setBalanceUseCase.execute(nullplague.getNickname(), "dinero", BigDecimal.valueOf(1));
        assertEquals(BigDecimal.valueOf(1), getAccountsUseCase.getAccount("nullplague").getValue().getBalance(dinero).getBalance());
    }

    @Test
    void  setBalanceUseCaseUnvalidAmountTest(){
        /*assertThrows(CurrencyAmountNotValidException.class, () -> {
            setBalanceUseCase.execute(nullplague.getNickname(), "dinero", BigDecimal.valueOf(-1));
        });*/

        Result<Void> result = setBalanceUseCase.execute(nullplague.getNickname(), "dinero", BigDecimal.valueOf(-1));
        assertEquals(ErrorCode.INVALID_AMOUNT, result.getErrorCode());
        assertEquals(BigDecimal.valueOf(10000), getAccountsUseCase.getAccount("nullplague").getValue().getBalance(dinero).getBalance());
    }

    @AfterEach
    void clearDb(){
        //repository.clearAll();
    }
}
