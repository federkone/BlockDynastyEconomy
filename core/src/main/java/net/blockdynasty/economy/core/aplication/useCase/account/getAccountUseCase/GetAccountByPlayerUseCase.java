package net.blockdynasty.economy.core.aplication.useCase.account.getAccountUseCase;

import net.blockdynasty.economy.core.domain.entities.account.Account;
import net.blockdynasty.economy.core.domain.entities.account.Player;
import net.blockdynasty.economy.core.domain.result.Result;
import net.blockdynasty.economy.core.domain.services.IAccountService;


public class GetAccountByPlayerUseCase {
    private IAccountService accountService;

    public GetAccountByPlayerUseCase(IAccountService accountService) {
        this.accountService = accountService;
    }

    public Result<Account> execute(Player player) {
        return this.accountService.getAccount(player);
    }
}
