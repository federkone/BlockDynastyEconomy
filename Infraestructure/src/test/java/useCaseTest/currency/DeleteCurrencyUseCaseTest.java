package useCaseTest.currency;

import BlockDynasty.Economy.domain.result.Result;
import BlockDynasty.Economy.aplication.useCase.account.CreateAccountUseCase;
import BlockDynasty.Economy.aplication.useCase.account.GetAccountsUseCase;
import BlockDynasty.Economy.aplication.useCase.currency.CreateCurrencyUseCase;
import BlockDynasty.Economy.domain.entities.account.Account;
import BlockDynasty.Economy.aplication.services.AccountService;
import BlockDynasty.Economy.domain.entities.balance.Balance;
import BlockDynasty.Economy.aplication.services.CurrencyService;
import BlockDynasty.Economy.domain.persistence.entities.IRepository;
import BlockDynasty.Economy.aplication.useCase.currency.DeleteCurrencyUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repositoryTest.FactoryrRepo;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DeleteCurrencyUseCaseTest {
    IRepository repository;
    CurrencyService currencyService;
    AccountService accountService;
    DeleteCurrencyUseCase deleteCurrencyUseCase;
    CreateCurrencyUseCase createCurrencyUseCase;
    CreateAccountUseCase createAccountUseCase;
    GetAccountsUseCase getAccountsUseCase;

    @BeforeEach
    public void setUp() {
        repository = FactoryrRepo.getDb();
        currencyService = new CurrencyService(repository);
        accountService = new AccountService(5);
        getAccountsUseCase = new GetAccountsUseCase(accountService, currencyService, repository);
        deleteCurrencyUseCase = new DeleteCurrencyUseCase(currencyService,getAccountsUseCase,repository,null);
        createCurrencyUseCase = new CreateCurrencyUseCase(currencyService,getAccountsUseCase, null,repository);
        createAccountUseCase = new CreateAccountUseCase(accountService, currencyService, getAccountsUseCase ,repository);

    }

    @Test
    public void deleteCurrencyTest() {

        createCurrencyUseCase.createCurrency("dinero", "dinero");
        createAccountUseCase.execute(UUID.randomUUID(), "Nullplague");

        deleteCurrencyUseCase.deleteCurrency("dinero");

        Result<Account> account = getAccountsUseCase.getAccount("Nullplague");

        for (Balance balance : account.getValue().getBalances()) {
            System.out.println(balance.getCurrency().getSingular());
        }

        assertEquals(1, getAccountsUseCase.getAccount("Nullplague").getValue().getBalances().size());


    }
}
