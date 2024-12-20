package useCaseTest.transaction;

import me.BlockDynasty.Economy.aplication.result.ErrorCode;
import me.BlockDynasty.Economy.aplication.result.Result;
import me.BlockDynasty.Economy.aplication.useCase.account.GetAccountsUseCase;
import me.BlockDynasty.Economy.aplication.useCase.currency.GetCurrencyUseCase;
import me.BlockDynasty.Economy.aplication.useCase.transaction.DepositUseCase;
import me.BlockDynasty.Economy.domain.account.Account;
import me.BlockDynasty.Economy.domain.account.AccountCache;
import me.BlockDynasty.Economy.domain.currency.Currency;
import me.BlockDynasty.Economy.domain.currency.CurrencyCache;
import me.BlockDynasty.Economy.domain.currency.Exceptions.CurrencyNotFoundException;
import me.BlockDynasty.Economy.domain.repository.IRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repositoryTest.RepositoryTest;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class DepositUseCaseTest {
    Account nullplague;
    Currency dinero;
    IRepository repository;
    CurrencyCache currencyCache;
    AccountCache accountCache;
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

        currencyCache = new CurrencyCache(repository);
        accountCache = new AccountCache(5);


        getAccountsUseCase = new GetAccountsUseCase(accountCache, currencyCache, repository);
        getCurrencyUseCase = new GetCurrencyUseCase(currencyCache, repository);

        //accountManager.addAccountToCache(getAccountsUseCase.getAccount("nullplague"));

        depositUseCase = new DepositUseCase(getCurrencyUseCase,getAccountsUseCase, repository,null,null);
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
