package useCaseTest.account;

import BlockDynasty.Economy.aplication.services.AccountService;
import BlockDynasty.Economy.aplication.services.CurrencyService;
import BlockDynasty.Economy.aplication.useCase.account.CreateAccountUseCase;
import BlockDynasty.Economy.aplication.useCase.account.SearchAccountUseCase;
import BlockDynasty.Economy.aplication.useCase.account.DeleteAccountUseCase;
import BlockDynasty.Economy.domain.entities.account.Account;
import BlockDynasty.Economy.domain.persistence.entities.IRepository;
import BlockDynasty.Economy.domain.result.ErrorCode;
import BlockDynasty.Economy.domain.result.Result;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repositoryTest.FactoryRepo;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class DeleteAccountUseCaseTest {
    IRepository repository;
    SearchAccountUseCase searchAccountUseCase;
    CreateAccountUseCase createAccountUseCase;
    DeleteAccountUseCase deleteAccountUseCase;
    AccountService accountService;
    CurrencyService currencyService;

    @BeforeEach
    void setUp() {
        repository = FactoryRepo.getDb();
        currencyService = new CurrencyService(repository);
        accountService = new AccountService(5 ,repository, currencyService);
        searchAccountUseCase = new SearchAccountUseCase(accountService,repository);
        createAccountUseCase = new CreateAccountUseCase(accountService, currencyService, searchAccountUseCase,repository);
        deleteAccountUseCase = new DeleteAccountUseCase(repository, accountService, searchAccountUseCase);

    }

    @Test
    void deleteAccountByName() {
        createAccountUseCase.execute(UUID.randomUUID() , "nullplague");

        Result<Void> result = deleteAccountUseCase.execute("nullplague");
        assertTrue(result.isSuccess(), "Expected deletion to be successful");

        Result<Account> accountResult = searchAccountUseCase.getAccount("nullplague");
        System.out.println(accountResult.getErrorCode());
        assertEquals(ErrorCode.ACCOUNT_NOT_FOUND , accountResult.getErrorCode(), "Expected account to be not found after deletion");
    }


}
