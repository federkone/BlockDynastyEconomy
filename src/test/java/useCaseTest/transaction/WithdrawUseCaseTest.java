package useCaseTest.transaction;

import me.BlockDynasty.Economy.aplication.useCase.account.GetAccountsUseCase;
import me.BlockDynasty.Economy.aplication.useCase.currency.GetCurrencyUseCase;
import me.BlockDynasty.Economy.domain.account.Account;
import me.BlockDynasty.Economy.domain.account.Exceptions.AccountNotFoundException;
import me.BlockDynasty.Economy.domain.account.Exceptions.InsufficientFundsException;
import me.BlockDynasty.Economy.aplication.useCase.transaction.WithdrawUseCase;
import me.BlockDynasty.Economy.domain.account.AccountCache;
import me.BlockDynasty.Economy.domain.currency.Currency;
import me.BlockDynasty.Economy.domain.currency.CurrencyCache;
import me.BlockDynasty.Economy.domain.currency.Exceptions.CurrencyAmountNotValidException;
import me.BlockDynasty.Economy.domain.currency.Exceptions.CurrencyNotFoundException;
import me.BlockDynasty.Economy.domain.repository.Criteria.Criteria;
import me.BlockDynasty.Economy.domain.repository.IRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repositoryTest.RepositoryTest;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class WithdrawUseCaseTest {
    Account nullplague;
    Currency dinero;
    IRepository repository;
    CurrencyCache currencyCache;
    AccountCache accountCache;
    WithdrawUseCase withdrawUseCase;
    GetAccountsUseCase getAccountsUseCase;
    GetCurrencyUseCase getCurrencyUseCase;

    @BeforeEach
    void setUp() {
        repository = new RepositoryTest();

        nullplague = new Account(UUID.randomUUID(), "nullplague");
        dinero= new Currency(UUID.randomUUID(),"dinero","dinero");
        dinero.setDefaultCurrency(true);
        nullplague.setBalance(dinero, BigDecimal.valueOf(5000));


        repository.saveCurrency(dinero);
        repository.saveAccount(nullplague);

        currencyCache = new CurrencyCache(repository);
        accountCache = new AccountCache();
        getAccountsUseCase = new GetAccountsUseCase(accountCache, currencyCache, repository);
        getCurrencyUseCase = new GetCurrencyUseCase(currencyCache, repository);




        withdrawUseCase = new WithdrawUseCase(getCurrencyUseCase,getAccountsUseCase, repository,null,null);
        System.out.println("default currency: " +getCurrencyUseCase.getDefaultCurrency().getSingular());
        System.out.println("cantidad de monedas en cache: " +currencyCache.getCurrencies().size());

    }

    @Test
    void withdrawUseCaseTestWithFounds() {
        withdrawUseCase.execute("nullplague", "dinero", BigDecimal.valueOf(5000));
        assertEquals(BigDecimal.valueOf(0), getAccountsUseCase.getAccount("nullplague").getBalance(dinero).getBalance());
    }

    @Test
    void withdrawUseCaseTestWithoutFounds(){
        assertThrows(InsufficientFundsException.class, () -> {
            withdrawUseCase.execute("nullplague", "dinero", BigDecimal.valueOf(10000));
        });
    }

    @Test
    void withdrawUseCaseTestWithNegativeAmount(){
        assertThrows(CurrencyAmountNotValidException.class, () -> {
            withdrawUseCase.execute("nullplague", "dinero", BigDecimal.valueOf(-10000));
        });
    }

    @Test
    void withdrawUseCaseTestWithNullAccount(){
        assertThrows(AccountNotFoundException.class, () -> {
            withdrawUseCase.execute("cris", "dinero", BigDecimal.valueOf(10000));
        });
    }

    @Test
    void withdrawUseCaseTestWithNullCurrency(){
        assertThrows(CurrencyNotFoundException.class, () -> {
            withdrawUseCase.execute("nullplague", "oro", BigDecimal.valueOf(10000));
        });
    }

    @AfterEach
    void clearDb(){
        //repository.clearAll();
    }
}
