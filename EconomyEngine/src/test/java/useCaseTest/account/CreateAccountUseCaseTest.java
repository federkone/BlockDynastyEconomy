package useCaseTest.account;

import BlockDynasty.Economy.aplication.useCase.account.SearchAccountUseCase;
import BlockDynasty.Economy.aplication.services.AccountService;
import BlockDynasty.Economy.aplication.services.CurrencyService;
import BlockDynasty.Economy.domain.entities.account.Account;
import BlockDynasty.Economy.domain.persistence.entities.IRepository;
import BlockDynasty.Economy.aplication.useCase.account.CreateAccountUseCase;
import BlockDynasty.Economy.domain.result.ErrorCode;
import BlockDynasty.Economy.domain.result.Result;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import repositoryTest.FactoryRepo;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class CreateAccountUseCaseTest {
    IRepository repository;
    SearchAccountUseCase searchAccountUseCase;
    CreateAccountUseCase createAccountUseCase;
    AccountService accountService;
    CurrencyService currencyService;

    @BeforeEach
    void setUp() {
        repository = FactoryRepo.getDb();
        currencyService = new CurrencyService(repository);
        accountService = new AccountService(5 ,repository, currencyService);
        searchAccountUseCase = new SearchAccountUseCase(accountService,repository);
        createAccountUseCase = new CreateAccountUseCase(accountService, currencyService, searchAccountUseCase,repository);
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
