package useCaseTest.currency;

import me.BlockDynasty.Economy.aplication.result.Result;
import me.BlockDynasty.Economy.aplication.useCase.account.CreateAccountUseCase;
import me.BlockDynasty.Economy.aplication.useCase.account.GetAccountsUseCase;
import me.BlockDynasty.Economy.aplication.useCase.currency.CreateCurrencyUseCase;
import me.BlockDynasty.Economy.domain.account.Account;
import me.BlockDynasty.Economy.domain.account.AccountCache;
import me.BlockDynasty.Economy.domain.balance.Balance;
import me.BlockDynasty.Economy.domain.currency.CurrencyCache;
import me.BlockDynasty.Economy.domain.repository.ConnectionHandler.ConnectionHibernate;
import me.BlockDynasty.Economy.domain.repository.IRepository;
import me.BlockDynasty.Economy.aplication.useCase.currency.DeleteCurrencyUseCase;
import me.BlockDynasty.Economy.domain.repository.RepositoryCriteriaApi;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repositoryTest.RepositoryTest;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DeleteCurrencyUseCaseTest {
    IRepository repository;
    CurrencyCache currencyCache;
    AccountCache accountCache;
    DeleteCurrencyUseCase deleteCurrencyUseCase;
    CreateCurrencyUseCase createCurrencyUseCase;
    CreateAccountUseCase createAccountUseCase;
    GetAccountsUseCase getAccountsUseCase;

    @BeforeEach
    public void setUp() {
        repository = new RepositoryTest();
        currencyCache = new CurrencyCache(repository);
        accountCache = new AccountCache(5);
        getAccountsUseCase = new GetAccountsUseCase(accountCache, currencyCache, repository);
        deleteCurrencyUseCase = new DeleteCurrencyUseCase( currencyCache,getAccountsUseCase,repository,null);
        createCurrencyUseCase = new CreateCurrencyUseCase(currencyCache,getAccountsUseCase, null,repository);
        createAccountUseCase = new CreateAccountUseCase(accountCache, currencyCache, getAccountsUseCase ,repository);

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
