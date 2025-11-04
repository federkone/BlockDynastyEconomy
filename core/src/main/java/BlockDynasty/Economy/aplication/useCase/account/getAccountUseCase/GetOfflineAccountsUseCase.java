package BlockDynasty.Economy.aplication.useCase.account.getAccountUseCase;

import BlockDynasty.Economy.domain.entities.account.Account;
import BlockDynasty.Economy.domain.result.ErrorCode;
import BlockDynasty.Economy.domain.result.Result;
import BlockDynasty.Economy.domain.services.IAccountService;

import java.util.List;

public class GetOfflineAccountsUseCase {
    private final IAccountService accountService;

    public GetOfflineAccountsUseCase(IAccountService accountService) {
        this.accountService = accountService;
    }

    public Result<List<Account>> execute() {
        List<Account> accounts = accountService.getAccountsOffline();
        if (accounts.isEmpty()) {
            return Result.failure("No accounts found", ErrorCode.ACCOUNT_NOT_FOUND);
        }
        return Result.success(accounts);
    }
}
