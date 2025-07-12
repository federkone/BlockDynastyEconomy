package useCaseTest.account;

import me.BlockDynasty.Economy.aplication.useCase.account.GetAccountsUseCase;
import me.BlockDynasty.Economy.aplication.services.AccountService;
import me.BlockDynasty.Economy.aplication.services.CurrencyService;
import me.BlockDynasty.Economy.domain.persistence.entities.IRepository;
import me.BlockDynasty.Economy.aplication.useCase.account.CreateAccountUseCase;
import org.junit.jupiter.api.Test;
import mockClass.repositoryTest.RepositoryTest;
import org.junit.jupiter.api.BeforeEach;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class CreateAccountUseCaseTest {
    IRepository repository;
    GetAccountsUseCase getAccountsUseCase;
    CreateAccountUseCase createAccountUseCase;
    AccountService accountService;
    CurrencyService currencyService;

    @BeforeEach
    void setUp() {
        repository = new RepositoryTest();
        accountService = new AccountService(5);
        currencyService = new CurrencyService(repository);
        getAccountsUseCase = new GetAccountsUseCase(accountService, currencyService,repository);
        createAccountUseCase = new CreateAccountUseCase(accountService, currencyService,getAccountsUseCase,repository);
    }

    @Test
    void createAccount(){
        createAccountUseCase.execute(UUID.randomUUID() , "nullplague");

        assertNotEquals(null, getAccountsUseCase.getAccount("nullplague"));
    }
}
