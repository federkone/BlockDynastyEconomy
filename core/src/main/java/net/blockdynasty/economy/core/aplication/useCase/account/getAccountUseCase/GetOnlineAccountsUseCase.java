package net.blockdynasty.economy.core.aplication.useCase.account.getAccountUseCase;

import net.blockdynasty.economy.core.domain.entities.account.Account;
import net.blockdynasty.economy.core.domain.result.ErrorCode;
import net.blockdynasty.economy.core.domain.result.Result;
import net.blockdynasty.economy.core.domain.services.IAccountService;

import java.util.List;

public class GetOnlineAccountsUseCase {
    private final IAccountService accountService;

    public GetOnlineAccountsUseCase(IAccountService accountService) {
        this.accountService = accountService;
    }

    public Result<List<Account>> execute() {
        List<Account> accounts = accountService.getAccountsOnline();
        if (accounts.isEmpty()) {
            return Result.failure("No accounts found", ErrorCode.ACCOUNT_NOT_FOUND);
        }
        return Result.success(accounts);
    }
}
