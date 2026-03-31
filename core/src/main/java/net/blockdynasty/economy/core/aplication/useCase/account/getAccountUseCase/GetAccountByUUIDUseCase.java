package net.blockdynasty.economy.core.aplication.useCase.account.getAccountUseCase;

import net.blockdynasty.economy.core.domain.entities.account.Account;
import net.blockdynasty.economy.core.domain.result.Result;
import net.blockdynasty.economy.core.domain.services.IAccountService;

import java.util.UUID;

public class GetAccountByUUIDUseCase {
    private final IAccountService accountService;

    public GetAccountByUUIDUseCase(IAccountService accountService) {
        this.accountService = accountService;
    }

    public Result<Account> execute(UUID uuid) {
        return this.accountService.getAccount(uuid);
        //podemos hacer un new Account(account); como programación defensiva, o elaborar un value Object para mostrar valores hacia el exterior
    }
}
