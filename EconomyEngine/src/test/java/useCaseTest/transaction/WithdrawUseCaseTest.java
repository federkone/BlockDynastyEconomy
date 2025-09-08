package useCaseTest.transaction;

import BlockDynasty.Economy.Core;
import BlockDynasty.Economy.domain.entities.balance.Money;
import BlockDynasty.Economy.domain.services.ICurrencyService;
import mockClass.CourierTest;
import BlockDynasty.Economy.domain.result.ErrorCode;
import BlockDynasty.Economy.domain.result.Result;
import BlockDynasty.Economy.aplication.useCase.account.SearchAccountUseCase;
import BlockDynasty.Economy.domain.entities.account.Account;
import BlockDynasty.Economy.aplication.useCase.transaction.WithdrawUseCase;
import BlockDynasty.Economy.aplication.services.AccountService;
import BlockDynasty.Economy.domain.entities.currency.Currency;
import BlockDynasty.Economy.domain.persistence.entities.IRepository;
import mockClass.MockListener;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import mockClass.LoggerTest;
import repositoryTest.FactoryRepo;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class WithdrawUseCaseTest {
    Account nullplague;
    Currency dinero;
    IRepository repository;
    ICurrencyService currencyService;
    AccountService accountService;
    WithdrawUseCase withdrawUseCase;
    SearchAccountUseCase searchAccountUseCase;
    Core core;

    @BeforeEach
    void setUp() {
        nullplague = new Account(UUID.randomUUID(), "nullplague");
        dinero= new Currency(UUID.randomUUID(),"dinero","dinero");

        repository = FactoryRepo.getDb();

       this.core = new Core(repository, 5, new MockListener(), new CourierTest(),new LoggerTest());
       currencyService = core.getServicesManager().getCurrencyService();

        core.getCurrencyUseCase().getCreateCurrencyUseCase().createCurrency(dinero.getSingular(), dinero.getPlural());
        core.getAccountsUseCase().getCreateAccountUseCase().execute(nullplague.getUuid(), nullplague.getNickname());
        core.getTransactionsUseCase().getDepositUseCase().execute(nullplague.getUuid(), "dinero", BigDecimal.valueOf(5000));

        withdrawUseCase = core.getTransactionsUseCase().getWithdrawUseCase();
        searchAccountUseCase = core.getAccountsUseCase().getGetAccountsUseCase();

    }

    @Test
    void withdrawUseCaseTestWithFounds() {
        Result<Void> result =withdrawUseCase.execute(nullplague.getUuid(), "dinero", BigDecimal.valueOf(5000));
        assertEquals(true, result.isSuccess());
        Result<Money> accountResult = core.getAccountsUseCase().getGetBalanceUseCase().getBalance(nullplague.getUuid(),"dinero");
        assertEquals(0,accountResult.getValue().getAmount().compareTo(BigDecimal.valueOf(0 )));
    }

    @Test
    void withdrawUseCaseTestWithoutFounds(){
        Result<Void> result = withdrawUseCase.execute(nullplague.getUuid(), "dinero", BigDecimal.valueOf(10000));
        assertEquals(ErrorCode.INSUFFICIENT_FUNDS, result.getErrorCode() );
    }

    @Test
    void withdrawUseCaseTestWithNegativeAmount(){
        Result<Void> result = withdrawUseCase.execute(nullplague.getNickname(), "dinero", BigDecimal.valueOf(-10000));
        assertEquals(ErrorCode.INVALID_AMOUNT, result.getErrorCode());
    }

    @Test
    void withdrawUseCaseTestWithNullAccount(){
        Result<Void> result = withdrawUseCase.execute("cris", "dinero", BigDecimal.valueOf(10000));
        assertEquals(ErrorCode.ACCOUNT_NOT_FOUND, result.getErrorCode());
    }

    @Test
    void withdrawUseCaseTestWithNullCurrency(){
        Result<Void> result = withdrawUseCase.execute("nullplague", "oro", BigDecimal.valueOf(10000));
        assertEquals(ErrorCode.CURRENCY_NOT_FOUND, result.getErrorCode());
    }

    @Test
    void withdrawUseCaseTestWithNullBalance(){
        currencyService.add(new Currency(UUID.randomUUID(),"oro","oro"));
        Result<Void> result = withdrawUseCase.execute(nullplague.getUuid(), "oro", BigDecimal.valueOf(10000));
        assertEquals(ErrorCode.ACCOUNT_NOT_HAVE_BALANCE, result.getErrorCode(), result.getErrorMessage() );
    }

    @Test
    void withdrawUseCaseTestWithCurrencyNoSupportDecimals(){
        core.getCurrencyUseCase().getEditCurrencyUseCase().toggleDecimals("dinero");

        Result<Void> result = withdrawUseCase.execute("nullplague", "dinero", BigDecimal.valueOf(1000.50));
        assertEquals(ErrorCode.DECIMAL_NOT_SUPPORTED, result.getErrorCode());
    }
    @AfterEach
    void clearDb(){
        //repository.clearAll();
    }
}
