package useCaseTest.transaction;

import me.BlockDynasty.Economy.domain.result.ErrorCode;
import me.BlockDynasty.Economy.domain.result.Result;
import me.BlockDynasty.Economy.aplication.useCase.account.GetAccountsUseCase;
import me.BlockDynasty.Economy.aplication.useCase.currency.GetCurrencyUseCase;
import me.BlockDynasty.Economy.aplication.useCase.transaction.SetBalanceUseCase;
import me.BlockDynasty.Economy.domain.account.Account;
import me.BlockDynasty.Economy.domain.account.AccountCache;
import me.BlockDynasty.Economy.domain.currency.CurrencyCache;
import me.BlockDynasty.Economy.domain.repository.IRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import me.BlockDynasty.Economy.domain.currency.Currency;

import repositoryTest.RepositoryTest;
import useCaseTest.transaction.MoksStubs.LoggerTest;
import useCaseTest.transaction.MoksStubs.UpdateForwarderTest;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class SetBalanceUseCaseTest {
    Account nullplague;
    Currency dinero;
    IRepository repository;
    CurrencyCache currencyCache;
    AccountCache accountCache;
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

        currencyCache = new CurrencyCache(repository);
        accountCache = new AccountCache(5);

        getAccountsUseCase = new GetAccountsUseCase(accountCache, currencyCache, repository);
        getCurrencyUseCase = new GetCurrencyUseCase(currencyCache, repository);
        setBalanceUseCase = new SetBalanceUseCase(getCurrencyUseCase,getAccountsUseCase, repository,new UpdateForwarderTest(),new LoggerTest());
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
