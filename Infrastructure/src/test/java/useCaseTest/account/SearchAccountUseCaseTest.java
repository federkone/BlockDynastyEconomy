package useCaseTest.account;

import BlockDynasty.Economy.aplication.events.EventManager;
import BlockDynasty.Economy.aplication.services.CurrencyService;
import BlockDynasty.Economy.domain.result.ErrorCode;
import BlockDynasty.Economy.domain.result.Result;
import BlockDynasty.Economy.aplication.useCase.account.CreateAccountUseCase;
import BlockDynasty.Economy.aplication.useCase.account.SearchAccountUseCase;
import BlockDynasty.Economy.aplication.useCase.currency.CreateCurrencyUseCase;
import BlockDynasty.Economy.aplication.useCase.currency.SearchCurrencyUseCase;
import BlockDynasty.Economy.aplication.useCase.transaction.DepositUseCase;
import BlockDynasty.BukkitImplementation.services.MessageService;
import BlockDynasty.Economy.domain.entities.account.Account;
import BlockDynasty.Economy.aplication.services.AccountService;
import BlockDynasty.Economy.domain.entities.currency.Currency;
import BlockDynasty.Economy.domain.persistence.entities.IRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repositoryTest.FactoryRepo;


import java.math.BigDecimal;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class SearchAccountUseCaseTest {
    IRepository repository;
    SearchAccountUseCase searchAccountUseCase;
    CreateAccountUseCase createAccountUseCase;
    AccountService accountService;
    CurrencyService currencyService;
    DepositUseCase depositUseCase;
    SearchCurrencyUseCase searchCurrencyUseCase;
    CreateCurrencyUseCase createCurrencyUseCase;
    MessageService messageService;

    @BeforeEach
    void setUp() {
        repository = FactoryRepo.getDb();
        accountService = new AccountService(5);
        currencyService = new CurrencyService(repository);
        searchAccountUseCase = new SearchAccountUseCase(accountService, currencyService,repository);
        createAccountUseCase = new CreateAccountUseCase(accountService, currencyService, searchAccountUseCase,repository);
        createCurrencyUseCase = new CreateCurrencyUseCase(currencyService, searchAccountUseCase,null, repository);
        searchCurrencyUseCase = new SearchCurrencyUseCase(currencyService, repository);
        depositUseCase = new DepositUseCase(searchCurrencyUseCase, searchAccountUseCase, repository, null, null,new EventManager());
        messageService = new MessageService(currencyService);

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
        robert.setMoney(defaultCurrency,BigDecimal.valueOf(1000));
        nullplague.setMoney(defaultCurrency,BigDecimal.valueOf(2000));
        Cris.setMoney(defaultCurrency,BigDecimal.valueOf(3000));
        Javi.setMoney(defaultCurrency,BigDecimal.valueOf(4000));
        fedrakon.setMoney(defaultCurrency,BigDecimal.valueOf(5000));
        xabier.setMoney(defaultCurrency,BigDecimal.valueOf(6000));
        jose.setMoney(defaultCurrency,BigDecimal.valueOf(7000));
        luca.setMoney(defaultCurrency,BigDecimal.valueOf(8000));
        pri.setMoney(defaultCurrency,BigDecimal.valueOf(9000));
        facu.setMoney(defaultCurrency,BigDecimal.valueOf(10000));
        repository.saveCurrency(defaultCurrency);
        repository.createAccount(robert);
        repository.createAccount(nullplague);
        repository.createAccount(Cris);
        repository.createAccount(Javi);
        repository.createAccount(fedrakon);
        repository.createAccount(xabier);
        repository.createAccount(jose);
        repository.createAccount(luca);
        repository.createAccount(pri);
        repository.createAccount(facu);


    }

    @Test
    void getTopAccountsUseCaseTest() {
        Result<List<Account>> resultTopAccounts = searchAccountUseCase.getTopAccounts("default",10,0);
        List<Account> accounts = resultTopAccounts.getValue();

        List<Account> accounts2= repository.loadAccounts( );
        //System.out.println("Accounts in repository: " + accounts2.size());
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
        Result<List<Account>> resultTopaccounts = searchAccountUseCase.getTopAccounts("default",10,0);
        assertEquals(ErrorCode.ACCOUNT_NOT_FOUND, resultTopaccounts.getErrorCode());
    }

    @Test
    void getTopAccountsUseCaseTestWithNegativeLimit(){
        /*assertThrows(IllegalArgumentException.class, () -> {
            getAccountsUseCase.getTopAccounts("default",-10,0);
        });*/
        Result<List<Account>> resultTopaccounts = searchAccountUseCase.getTopAccounts("default",-10,0);
        assertEquals(ErrorCode.INVALID_ARGUMENT, resultTopaccounts.getErrorCode());
    }

    @Test
    void getOfflineAccounts(){
        Result<List<Account>> result = searchAccountUseCase.getOfflineAccounts();
        assertTrue(result.isSuccess());
        result.getValue().forEach(System.out::println);
        List<Account> accounts = result.getValue();
        assertFalse(accounts.isEmpty());
        assertEquals(10, accounts.size());
        assertTrue(accounts.stream().anyMatch(account -> account.getNickname().equals("robert")));
        assertTrue(accounts.stream().anyMatch(account -> account.getNickname().equals("nullplague")));
    }

}
