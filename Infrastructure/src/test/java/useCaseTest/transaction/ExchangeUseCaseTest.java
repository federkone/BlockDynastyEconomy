package useCaseTest.transaction;

import BlockDynasty.Economy.aplication.events.EventManager;
import mockClass.CourierTest;
import BlockDynasty.Economy.domain.result.Result;
import BlockDynasty.Economy.aplication.useCase.account.SearchAccountUseCase;
import BlockDynasty.Economy.aplication.useCase.currency.SearchCurrencyUseCase;
import BlockDynasty.Economy.aplication.useCase.transaction.ExchangeUseCase;
import BlockDynasty.Economy.domain.entities.account.Account;
import BlockDynasty.Economy.aplication.services.AccountService;
import BlockDynasty.Economy.domain.entities.currency.Currency;
import BlockDynasty.Economy.aplication.services.CurrencyService;
import BlockDynasty.Economy.domain.persistence.entities.IRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import mockClass.LoggerTest;
import repositoryTest.FactoryRepo;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class ExchangeUseCaseTest {
    Account player;
    IRepository repository;
    CurrencyService currencyService;
    AccountService accountService;
    SearchAccountUseCase searchAccountUseCase;
    SearchCurrencyUseCase searchCurrencyUseCase;
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
        player.setMoney(coin, BigDecimal.valueOf(100));
        player.setMoney(dinero, BigDecimal.valueOf(40000)); // Increase from 10 to 40000

        // Initialize repository and save data
        repository = FactoryRepo.getDb();
        repository.saveCurrency(coin);
        repository.saveCurrency(dinero);

        try {
            repository.createAccount(player);
        } catch (Exception e) {
            fail("Error saving account: " + e.getMessage());
        }

        // Initialize caches
        currencyService = new CurrencyService(repository);
        accountService = new AccountService(5);

        // Initialize use cases
        searchAccountUseCase = new SearchAccountUseCase(accountService, currencyService, repository);
        searchCurrencyUseCase = new SearchCurrencyUseCase(currencyService, repository);

        // Initialize the exchange use case to test
        exchangeUseCase = new ExchangeUseCase(searchCurrencyUseCase, searchAccountUseCase,
                repository, new CourierTest(), new LoggerTest(),new EventManager());
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
        Account updatedAccount = searchAccountUseCase.getAccount("player").getValue();
        assertEquals(BigDecimal.valueOf(101).setScale(2), updatedAccount.getMoney(coin).getAmount().setScale(2));
        assertEquals(BigDecimal.valueOf(10000).doubleValue(), updatedAccount.getMoney(dinero).getAmount().doubleValue());
    }

    //errores a evaluar:  cuenta no encontrada,moneda no encontrada, monto negativo, saldo insuficiente.
    //pd:estos intercambios se calculan de manera automatica segun el rate de las monedas. O tambien se aceptan 2 montos de entradas para cambiar lo solicitado

    @AfterEach
    void clearDb() {
        // Optional cleanup
    }
}