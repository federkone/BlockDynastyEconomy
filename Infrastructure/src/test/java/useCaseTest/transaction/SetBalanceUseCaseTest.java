package useCaseTest.transaction;

import BlockDynasty.Economy.aplication.services.OfferService;
import BlockDynasty.Economy.aplication.useCase.UsesCaseFactory;
import BlockDynasty.Economy.domain.entities.balance.Balance;
import mockClass.CourierTest;
import BlockDynasty.Economy.aplication.services.CurrencyService;
import BlockDynasty.Economy.domain.result.ErrorCode;
import BlockDynasty.Economy.domain.result.Result;
import BlockDynasty.Economy.aplication.useCase.transaction.SetBalanceUseCase;
import BlockDynasty.Economy.domain.entities.account.Account;
import BlockDynasty.Economy.aplication.services.AccountService;
import BlockDynasty.Economy.domain.persistence.entities.IRepository;
import mockClass.MockListener;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import BlockDynasty.Economy.domain.entities.currency.Currency;

import mockClass.LoggerTest;
import repositoryTest.FactoryRepo;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class SetBalanceUseCaseTest {
    Account nullplague;
    Currency dinero;
    IRepository repository;
    CurrencyService currencyService;
    AccountService accountService;
    SetBalanceUseCase setBalanceUseCase;
    UsesCaseFactory useCaseFactory;

    @BeforeEach
    void setUp() {
        nullplague = new Account(UUID.randomUUID(), "nullplague");
        dinero= new Currency(UUID.randomUUID(),"dinero","dinero");

        repository = FactoryRepo.getDb();
        currencyService = new CurrencyService(repository);
        accountService = new AccountService(5);

        useCaseFactory = new UsesCaseFactory(accountService , currencyService, new LoggerTest(), new OfferService(new MockListener()) ,repository,new CourierTest());

        setBalanceUseCase = useCaseFactory.getSetBalanceUseCase();
        useCaseFactory.getCreateCurrencyUseCase().createCurrency(dinero.getSingular(), dinero.getPlural());
        useCaseFactory.getCreateAccountUseCase().execute(nullplague.getUuid(), nullplague.getNickname());
    }

    @Test
    void setBalanceUseCaseTest() {
        Result<Void> result = setBalanceUseCase.execute(nullplague.getNickname(), "dinero", BigDecimal.valueOf(1));
        assertTrue(result.isSuccess());

        Result<Balance> accountResult = useCaseFactory.getGetBalanceUseCase().getBalance( nullplague.getNickname(), "dinero");
        assertEquals(BigDecimal.valueOf(1), accountResult.getValue( ).getAmount());
    }

    @Test
    void  setBalanceUseCaseInvalidAmountTest(){
        Result<Void> result = setBalanceUseCase.execute(nullplague.getNickname(), "dinero", BigDecimal.valueOf(-1));
        assertFalse(result.isSuccess());
        assertEquals(ErrorCode.INVALID_AMOUNT, result.getErrorCode());

    }

    @AfterEach
    void clearDb(){
        //repository.clearAll();
    }
}
