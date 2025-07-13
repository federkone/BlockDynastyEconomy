package useCaseTest.account;

import me.BlockDynasty.Economy.aplication.useCase.account.GetAccountsUseCase;
import me.BlockDynasty.Economy.aplication.services.AccountService;
import me.BlockDynasty.Economy.aplication.services.CurrencyService;
import me.BlockDynasty.Economy.domain.entities.account.Account;
import me.BlockDynasty.Economy.domain.persistence.entities.IRepository;
import me.BlockDynasty.Economy.aplication.useCase.account.CreateAccountUseCase;
import me.BlockDynasty.Economy.domain.result.ErrorCode;
import me.BlockDynasty.Economy.domain.result.Result;
import org.junit.jupiter.api.Test;
import mockClass.repositoryTest.RepositoryTest;
import org.junit.jupiter.api.BeforeEach;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

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
        Result<Account> result= createAccountUseCase.execute(UUID.randomUUID() , "nullplague");
        assertTrue(result.isSuccess());
        assertEquals("nullplague", result.getValue().getNickname());
    }

    @Test
    void createAccountAlreadyExists() {
        UUID userUuid = UUID.randomUUID();
        String userName = "testUser";
        // Create the account for the first time
        Result<Account> firstResult = createAccountUseCase.execute(userUuid, userName);
        assertTrue(firstResult.isSuccess());
        assertEquals(userName, firstResult.getValue().getNickname());

        // Attempt to create the same account again
        Result<Account> secondResult = createAccountUseCase.execute(userUuid, userName);
        assertFalse(secondResult.isSuccess());
        assertEquals(ErrorCode.ACCOUNT_ALREADY_EXISTS, secondResult.getErrorCode());
    }
}
