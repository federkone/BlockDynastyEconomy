package useCaseTest.currency;

import BlockDynasty.Economy.domain.entities.balance.Money;
import BlockDynasty.Economy.domain.result.Result;
import BlockDynasty.Economy.aplication.useCase.account.CreateAccountUseCase;
import BlockDynasty.Economy.aplication.useCase.account.SearchAccountUseCase;
import BlockDynasty.Economy.aplication.useCase.currency.CreateCurrencyUseCase;
import BlockDynasty.Economy.domain.entities.account.Account;
import BlockDynasty.Economy.aplication.services.AccountService;
import BlockDynasty.Economy.aplication.services.CurrencyService;
import BlockDynasty.Economy.domain.persistence.entities.IRepository;
import BlockDynasty.Economy.aplication.useCase.currency.DeleteCurrencyUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repositoryTest.FactoryRepo;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DeleteCurrencyUseCaseTest {
    IRepository repository;
    CurrencyService currencyService;
    AccountService accountService;
    DeleteCurrencyUseCase deleteCurrencyUseCase;
    CreateCurrencyUseCase createCurrencyUseCase;
    CreateAccountUseCase createAccountUseCase;
    SearchAccountUseCase searchAccountUseCase;

    @BeforeEach
    public void setUp() {
        repository = FactoryRepo.getDb();
        currencyService = new CurrencyService(repository);
        accountService = new AccountService(5 ,repository, currencyService);
        searchAccountUseCase = new SearchAccountUseCase(accountService, repository);
        deleteCurrencyUseCase = new DeleteCurrencyUseCase(currencyService, accountService,repository,null);
        createCurrencyUseCase = new CreateCurrencyUseCase(currencyService, accountService, null,repository);
        createAccountUseCase = new CreateAccountUseCase(accountService, currencyService, searchAccountUseCase,repository);

    }

    @Test
    public void deleteCurrencyTest() {

        createCurrencyUseCase.createCurrency("dinero", "dinero");
        createAccountUseCase.execute(UUID.randomUUID(), "Nullplague");

        deleteCurrencyUseCase.deleteCurrency("dinero");

        Result<Account> account = searchAccountUseCase.getAccount("Nullplague");

        for (Money money : account.getValue().getBalances()) {
            System.out.println(money.getCurrency().getSingular());
        }

        assertEquals(1, searchAccountUseCase.getAccount("Nullplague").getValue().getBalances().size());


    }
}
