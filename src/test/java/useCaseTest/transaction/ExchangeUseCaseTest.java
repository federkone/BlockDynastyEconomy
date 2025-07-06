package useCaseTest.transaction;


import me.BlockDynasty.Economy.domain.result.ErrorCode;
import me.BlockDynasty.Economy.domain.result.Result;
import me.BlockDynasty.Economy.aplication.useCase.account.GetAccountsUseCase;
import me.BlockDynasty.Economy.aplication.useCase.currency.GetCurrencyUseCase;
import me.BlockDynasty.Economy.aplication.useCase.transaction.ExchangeUseCase;
import me.BlockDynasty.Economy.domain.account.Account;
import me.BlockDynasty.Economy.domain.account.AccountCache;
import me.BlockDynasty.Economy.domain.currency.Currency;
import me.BlockDynasty.Economy.domain.currency.CurrencyCache;
import me.BlockDynasty.Economy.domain.repository.IRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repositoryTest.RepositoryTest;
import useCaseTest.transaction.MoksStubs.LoggerTest;
import useCaseTest.transaction.MoksStubs.UpdateForwarderTest;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class ExchangeUseCaseTest {
    Account player;
    IRepository repository;
    CurrencyCache currencyCache;
    AccountCache accountCache;
    GetAccountsUseCase getAccountsUseCase;
    GetCurrencyUseCase getCurrencyUseCase;
    ExchangeUseCase exchangeUseCase;
    Currency coin;
    Currency dinero;

    @BeforeEach
    void setUp() {
        // Keep the same currency setup
        coin = new Currency(UUID.randomUUID(), "Coin", "Coins");
        coin.setExchangeRate(1.0);
        coin.setDecimalSupported(false); // Coins don't support decimals

        dinero = new Currency(UUID.randomUUID(), "dinero", "dinero");
        dinero.setExchangeRate(30000.0);

        // Increase the initial dinero balance to have enough for the test
        player = new Account(UUID.randomUUID(), "player");
        player.setBalance(coin, BigDecimal.valueOf(100));
        player.setBalance(dinero, BigDecimal.valueOf(40000)); // Increase from 10 to 40000

        // Initialize repository and save data
        repository = new RepositoryTest();
        repository.saveCurrency(coin);
        repository.saveCurrency(dinero);

        try {
            repository.createAccount(player);
        } catch (Exception e) {
            fail("Error saving account: " + e.getMessage());
        }

        // Initialize caches
        currencyCache = new CurrencyCache(repository);
        accountCache = new AccountCache(5);

        // Initialize use cases
        getAccountsUseCase = new GetAccountsUseCase(accountCache, currencyCache, repository);
        getCurrencyUseCase = new GetCurrencyUseCase(currencyCache, repository);

        // Initialize the exchange use case to test
        exchangeUseCase = new ExchangeUseCase(getCurrencyUseCase, getAccountsUseCase,
                repository, new UpdateForwarderTest(), new LoggerTest());
    }

    @Test
    void exchangeCurrencySuccessTest() {
        //quiero 1 coin a cambio de dinero
        Result<BigDecimal> result = exchangeUseCase.execute("player", "Dinero", "coin", null, BigDecimal.valueOf(1));

        System.out.println(result.getErrorCode());
        assertTrue(result.isSuccess());

        // The result should be the amount of dinero received
        assertEquals(BigDecimal.valueOf(30000).doubleValue(), result.getValue().doubleValue());

        // Verify balances were updated correctly
        Account updatedAccount = getAccountsUseCase.getAccount("player").getValue();
        assertEquals(BigDecimal.valueOf(101), updatedAccount.getBalance(coin).getBalance());
        assertEquals(BigDecimal.valueOf(10000).doubleValue(), updatedAccount.getBalance(dinero).getBalance().doubleValue());
    }
    @AfterEach
    void clearDb() {
        // Optional cleanup
    }
}