package BlockDynasty.Economy.aplication.useCase.account.getAccountUseCase;

import BlockDynasty.Economy.domain.entities.account.Account;
import BlockDynasty.Economy.domain.result.ErrorCode;
import BlockDynasty.Economy.domain.result.Result;
import BlockDynasty.Economy.domain.services.IAccountService;

public class GetAccountByNameUseCase {
    private final IAccountService accountService;

    public GetAccountByNameUseCase(IAccountService accountService) {
        this.accountService = accountService;
    }

    public Result<Account> execute(String name) {
        Account account = this.accountService.getAccount(name);
        if (account != null) {
            return Result.success(account);
        }
        return Result.failure("Account not found", ErrorCode.ACCOUNT_NOT_FOUND);
        //podemos hacer un new Account(account); como programación defensiva, o elaborar un value Object para mostrar valores hacia el exterior
    }
}
