package useCaseTest.transaction;

import BlockDynasty.Economy.Core;
import BlockDynasty.Economy.domain.entities.balance.Money;
import mockClass.CourierTest;
import BlockDynasty.Economy.domain.result.ErrorCode;
import BlockDynasty.Economy.domain.result.Result;
import BlockDynasty.Economy.aplication.useCase.transaction.SetBalanceUseCase;
import BlockDynasty.Economy.domain.entities.account.Account;
import BlockDynasty.Economy.domain.persistence.entities.IRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import BlockDynasty.Economy.domain.entities.currency.Currency;

import mockClass.LoggerTest;
import repositoryTest.FactoryRepo;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class SetMoneyUseCaseTest {
    Account nullplague;
    Currency dinero;
    IRepository repository;
    SetBalanceUseCase setBalanceUseCase;
    Core core;

    @BeforeEach
    void setUp() {
        nullplague = new Account(UUID.randomUUID(), "nullplague");
        dinero= new Currency(UUID.randomUUID(),"dinero","dinero");

        repository = FactoryRepo.getDb();

        this.core = new Core(repository, 5, new CourierTest(),new LoggerTest());


        setBalanceUseCase = core.getTransactionsUseCase().getSetBalanceUseCase();
        core.getCurrencyUseCase().getCreateCurrencyUseCase().createCurrency(dinero.getSingular(), dinero.getPlural());
        core.getAccountsUseCase().getCreateAccountUseCase().execute(nullplague.getUuid(), nullplague.getNickname());
    }

    @Test
    void setBalanceUseCaseTest() {
        Result<Void> result = setBalanceUseCase.execute(nullplague.getNickname(), "dinero", BigDecimal.valueOf(1));
        assertTrue(result.isSuccess());

        Result<Money> accountResult = core.getAccountsUseCase().getGetBalanceUseCase().getBalance( nullplague.getNickname(), "dinero");
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
