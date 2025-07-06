package useCaseTest.transaction;

import me.BlockDynasty.Economy.domain.result.ErrorCode;
import me.BlockDynasty.Economy.domain.result.Result;
import me.BlockDynasty.Economy.aplication.useCase.account.GetAccountsUseCase;
import me.BlockDynasty.Economy.aplication.useCase.currency.GetCurrencyUseCase;
import me.BlockDynasty.Economy.domain.account.Account;
import me.BlockDynasty.Economy.aplication.useCase.transaction.WithdrawUseCase;
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
        accountCache = new AccountCache(5);
        getAccountsUseCase = new GetAccountsUseCase(accountCache, currencyCache, repository);
        getCurrencyUseCase = new GetCurrencyUseCase(currencyCache, repository);


        withdrawUseCase = new WithdrawUseCase(getCurrencyUseCase,getAccountsUseCase, repository,new UpdateForwarderTest(),new LoggerTest());


    }

    @Test
    void withdrawUseCaseTestWithFounds() {
        withdrawUseCase.execute("nullplague", "dinero", BigDecimal.valueOf(5000));
        assertEquals(BigDecimal.valueOf(0), getAccountsUseCase.getAccount("nullplague").getValue().getBalance(dinero).getBalance());
    }

    @Test
    void withdrawUseCaseTestWithoutFounds(){
       /* assertThrows(InsufficientFundsException.class, () -> {
            withdrawUseCase.execute("nullplague", "dinero", BigDecimal.valueOf(10000));
        });*/
        Result<Void> result = withdrawUseCase.execute("nullplague", "dinero", BigDecimal.valueOf(10000));
        assertEquals(ErrorCode.INSUFFICIENT_FUNDS, result.getErrorCode());
    }

    @Test
    void withdrawUseCaseTestWithNegativeAmount(){
        /*assertThrows(CurrencyAmountNotValidException.class, () -> {
            withdrawUseCase.execute("nullplague", "dinero", BigDecimal.valueOf(-10000));
        });*/
        Result<Void> result = withdrawUseCase.execute("nullplague", "dinero", BigDecimal.valueOf(-10000));
        assertEquals(ErrorCode.INVALID_AMOUNT, result.getErrorCode());
    }

    @Test
    void withdrawUseCaseTestWithNullAccount(){
        /*assertThrows(AccountNotFoundException.class, () -> {
            withdrawUseCase.execute("cris", "dinero", BigDecimal.valueOf(10000));
        });*/
        Result<Void> result = withdrawUseCase.execute("cris", "dinero", BigDecimal.valueOf(10000));
        assertEquals(ErrorCode.ACCOUNT_NOT_FOUND, result.getErrorCode());
    }

    @Test
    void withdrawUseCaseTestWithNullCurrency(){
       /* assertThrows(CurrencyNotFoundException.class, () -> {
            withdrawUseCase.execute("nullplague", "oro", BigDecimal.valueOf(10000));
        });*/
        Result<Void> result = withdrawUseCase.execute("nullplague", "oro", BigDecimal.valueOf(10000));
        assertEquals(ErrorCode.CURRENCY_NOT_FOUND, result.getErrorCode());
    }

    @Test
    void withdrawUseCaseTestWithNullBalance(){
        //todo: EL GETACCOUNTUSECASE ESTA AGREGANDO/CHEQUEADO AUTOMATICAMENTE LAS MONEDAS DEL SISTEMA AL USUARIO EN CADA CONSULTA, POR LO TANTO SIEMPRE VA A TENER LOS TIPOS DE MONEDAS QUE EXISTAN EN EL SISTEMA
        currencyCache.add(new Currency(UUID.randomUUID(),"oro","oro"));
        Result<Void> result = withdrawUseCase.execute("nullplague", "oro", BigDecimal.valueOf(10000));
        //assertEquals(ErrorCode.ACCOUNT_NOT_HAVE_BALANCE, result.getErrorCode());
        assertEquals(ErrorCode.INSUFFICIENT_FUNDS, result.getErrorCode()); //en el core agrega el balance inexistente a la cuenta con sus balances por defecto de la currency
    }

    @AfterEach
    void clearDb(){
        //repository.clearAll();
    }
}
