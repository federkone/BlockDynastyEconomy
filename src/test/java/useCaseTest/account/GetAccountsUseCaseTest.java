package useCaseTest.account;

import me.BlockDynasty.Economy.aplication.result.ErrorCode;
import me.BlockDynasty.Economy.aplication.result.Result;
import me.BlockDynasty.Economy.aplication.useCase.account.CreateAccountUseCase;
import me.BlockDynasty.Economy.aplication.useCase.account.GetAccountsUseCase;
import me.BlockDynasty.Economy.aplication.useCase.currency.CreateCurrencyUseCase;
import me.BlockDynasty.Economy.aplication.useCase.currency.GetCurrencyUseCase;
import me.BlockDynasty.Economy.aplication.useCase.transaction.DepositUseCase;
import me.BlockDynasty.Economy.config.file.MessageService;
import me.BlockDynasty.Economy.domain.account.Account;
import me.BlockDynasty.Economy.domain.account.AccountCache;
import me.BlockDynasty.Economy.domain.account.Exceptions.AccountNotFoundException;
import me.BlockDynasty.Economy.domain.currency.Currency;
import me.BlockDynasty.Economy.domain.currency.CurrencyCache;
import me.BlockDynasty.Economy.domain.repository.ConnectionHandler.ConnectionHibernate;
import me.BlockDynasty.Economy.domain.repository.IRepository;
import me.BlockDynasty.Economy.domain.repository.RepositoryCriteriaApi;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repositoryTest.RepositoryTest;
import me.BlockDynasty.Economy.config.file.F;


import java.math.BigDecimal;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class GetAccountsUseCaseTest {
    IRepository repository;
    GetAccountsUseCase getAccountsUseCase;
    CreateAccountUseCase createAccountUseCase;
    AccountCache accountCache;
    CurrencyCache currencyCache;
    DepositUseCase depositUseCase;
    GetCurrencyUseCase getCurrencyUseCase;
    CreateCurrencyUseCase createCurrencyUseCase;
    MessageService messageService;

    @BeforeEach
    void setUp() {
        repository = new RepositoryTest();
        accountCache = new AccountCache(5);
        currencyCache = new CurrencyCache(repository);
        getAccountsUseCase = new GetAccountsUseCase(accountCache, currencyCache,repository);
        createAccountUseCase = new CreateAccountUseCase(accountCache, currencyCache,getAccountsUseCase,repository);
        createCurrencyUseCase = new CreateCurrencyUseCase(currencyCache, getAccountsUseCase,null, repository);
        getCurrencyUseCase = new GetCurrencyUseCase(currencyCache, repository);
        depositUseCase = new DepositUseCase(getCurrencyUseCase, getAccountsUseCase, repository, null, null);
        messageService = new MessageService(currencyCache);

        //createCurrencyUseCase.createCurrency("dinero", "dinero");

      /*  createAccountUseCase.execute(UUID.randomUUID() , "robert");
        //createAccountUseCase.execute(UUID.randomUUID() , "nullplague");
        createAccountUseCase.execute(UUID.randomUUID() , "Cris");
        createAccountUseCase.execute(UUID.randomUUID() , "Javi");
        createAccountUseCase.execute(UUID.randomUUID() , "fedrakon");
        createAccountUseCase.execute(UUID.randomUUID() , "xabier");
        createAccountUseCase.execute(UUID.randomUUID() , "jose");
        createAccountUseCase.execute(UUID.randomUUID() , "luca");
        createAccountUseCase.execute(UUID.randomUUID() , "pri");
        createAccountUseCase.execute(UUID.randomUUID() , "facu");

        depositUseCase.execute("Nullplague", "dinero", BigDecimal.valueOf(5000));
        depositUseCase.execute("Cris", "dinero", BigDecimal.valueOf(7000));
        depositUseCase.execute("Javi", "dinero", BigDecimal.valueOf(8000));
        depositUseCase.execute("fedrakon", "dinero", BigDecimal.valueOf(9000));
        depositUseCase.execute("xabier", "dinero", BigDecimal.valueOf(10000));
        depositUseCase.execute("jose", "dinero", BigDecimal.valueOf(11000));
        depositUseCase.execute("luca", "dinero", BigDecimal.valueOf(12000));
        depositUseCase.execute("pri", "dinero", BigDecimal.valueOf(13000));
        depositUseCase.execute("facu", "dinero", BigDecimal.valueOf(14000));

*/

        Currency defaultCurrency = new Currency(UUID.randomUUID(), "default", "default");
        Account robert = new Account(UUID.randomUUID(), "robert");
        Account nullplague = new Account(UUID.randomUUID(), "nullplague");
        Account Cris = new Account(UUID.randomUUID(),"Cris");
        Account Javi = new Account(UUID.randomUUID(),"Javi");
        Account fedrakon = new Account(UUID.randomUUID(),"fedrakon");
        Account xabier = new Account(UUID.randomUUID(),"xabier");
        Account jose = new Account(UUID.randomUUID(),"jose");
        Account luca = new Account(UUID.randomUUID(),"luca");
        Account pri = new Account(UUID.randomUUID(),"pri");
        Account facu = new Account(UUID.randomUUID(),"facu");
        robert.deposit(defaultCurrency,BigDecimal.valueOf(1000));
        nullplague.deposit(defaultCurrency,BigDecimal.valueOf(2000));
        Cris.deposit(defaultCurrency,BigDecimal.valueOf(3000));
        Javi.deposit(defaultCurrency,BigDecimal.valueOf(4000));
        fedrakon.deposit(defaultCurrency,BigDecimal.valueOf(5000));
        xabier.deposit(defaultCurrency,BigDecimal.valueOf(6000));
        jose.deposit(defaultCurrency,BigDecimal.valueOf(7000));
        luca.deposit(defaultCurrency,BigDecimal.valueOf(8000));
        pri.deposit(defaultCurrency,BigDecimal.valueOf(9000));
        facu.deposit(defaultCurrency,BigDecimal.valueOf(10000));
        repository.saveAccount(robert);
        repository.saveAccount(nullplague);
        repository.saveAccount(Cris);
        repository.saveAccount(Javi);
        repository.saveAccount(fedrakon);
        repository.saveAccount(xabier);
        repository.saveAccount(jose);
        repository.saveAccount(luca);
        repository.saveAccount(pri);
        repository.saveAccount(facu);


    }

    @Test
    void getTopAccountsUseCaseTest() {
        Result<List<Account>> resultTopAccounts =getAccountsUseCase.getTopAccounts("default",10,0);
        List<Account> accounts = resultTopAccounts.getValue();
        assertEquals (10, accounts.size());
        assertEquals("facu",accounts.get(0).getNickname()); //primero
        assertEquals("pri",accounts.get(1).getNickname());  //segundo
        assertEquals("robert",accounts.get(9).getNickname()); //ultimo

        //List<Account> accounts2 =  getAccountsUseCase.getTopAccounts("dinero",5,0);
        //System.out.println(messageService.getBalanceTopMessage(accounts,"default"));

    }

    @Test
    void getTopAccountsWithoutAccountsUseCaseTest(){
        repository.clearAll();
        /*assertThrows(AccountNotFoundException.class, () -> {
           getAccountsUseCase.getTopAccounts("default",10,0);
        });*/
        Result<List<Account>> resultTopaccounts = getAccountsUseCase.getTopAccounts("default",10,0);
        assertEquals(ErrorCode.ACCOUNT_NOT_FOUND, resultTopaccounts.getErrorCode());
    }

    @Test
    void getTopAccountsUseCaseTestWithNegativeLimit(){
        /*assertThrows(IllegalArgumentException.class, () -> {
            getAccountsUseCase.getTopAccounts("default",-10,0);
        });*/
        Result<List<Account>> resultTopaccounts = getAccountsUseCase.getTopAccounts("default",-10,0);
        assertEquals(ErrorCode.INVALID_ARGUMENT, resultTopaccounts.getErrorCode());
    }


}
