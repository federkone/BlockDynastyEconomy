package useCaseTest.account;

import me.BlockDynasty.Economy.aplication.useCase.account.GetAccountsUseCase;
import me.BlockDynasty.Economy.domain.account.AccountCache;
import me.BlockDynasty.Economy.domain.currency.CurrencyCache;
import me.BlockDynasty.Economy.domain.repository.IRepository;
import me.BlockDynasty.Economy.aplication.useCase.account.CreateAccountUseCase;
import org.junit.jupiter.api.Test;
import repositoryTest.RepositoryTest;
import org.junit.jupiter.api.BeforeEach;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class CreateAccountUseCaseTest {
    IRepository repository;
    GetAccountsUseCase getAccountsUseCase;
    CreateAccountUseCase createAccountUseCase;
    AccountCache accountCache;
    CurrencyCache currencyCache;

    @BeforeEach
    void setUp() {
        repository = new RepositoryTest();
        accountCache = new AccountCache(5);
        currencyCache = new CurrencyCache(repository);
        getAccountsUseCase = new GetAccountsUseCase(accountCache, currencyCache,repository);
        createAccountUseCase = new CreateAccountUseCase(accountCache, currencyCache,getAccountsUseCase,repository);
    }

    @Test
    void createAccount(){
        createAccountUseCase.execute(UUID.randomUUID() , "nullplague");

        assertNotEquals(null, getAccountsUseCase.getAccount("nullplague"));
    }
}
