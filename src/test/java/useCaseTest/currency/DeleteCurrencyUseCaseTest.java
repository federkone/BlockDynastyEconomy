package useCaseTest.currency;

import me.BlockDynasty.Economy.domain.result.Result;
import me.BlockDynasty.Economy.aplication.useCase.account.CreateAccountUseCase;
import me.BlockDynasty.Economy.aplication.useCase.account.GetAccountsUseCase;
import me.BlockDynasty.Economy.aplication.useCase.currency.CreateCurrencyUseCase;
import me.BlockDynasty.Economy.domain.entities.account.Account;
import me.BlockDynasty.Economy.aplication.services.AccountService;
import me.BlockDynasty.Economy.domain.entities.balance.Balance;
import me.BlockDynasty.Economy.aplication.services.CurrencyService;
import me.BlockDynasty.Economy.domain.persistence.entities.IRepository;
import me.BlockDynasty.Economy.aplication.useCase.currency.DeleteCurrencyUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import mockClass.repositoryTest.RepositoryTest;

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
        repository = new RepositoryTest();
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

        assertEquals(1, getAccountsUseCase.getAccount("Nullplague").getValue().getBalances().size()); //todo se borra en db pero no en local cache, el caso de uso deleteCurrency deberia llamar a actualizar toda la cache de alguna manera


    }
}
