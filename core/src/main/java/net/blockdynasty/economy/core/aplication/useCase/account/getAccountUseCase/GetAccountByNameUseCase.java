package net.blockdynasty.economy.core.aplication.useCase.account.getAccountUseCase;

import net.blockdynasty.economy.core.domain.entities.account.Account;
import net.blockdynasty.economy.core.domain.result.Result;
import net.blockdynasty.economy.core.domain.services.IAccountService;

public class GetAccountByNameUseCase {
    private final IAccountService accountService;

    public GetAccountByNameUseCase(IAccountService accountService) {
        this.accountService = accountService;
    }

    public Result<Account> execute(String name) {
        return this.accountService.getAccount(name);
        //podemos hacer un new Account(account); como programación defensiva, o elaborar un value Object para mostrar valores hacia el exterior
    }
}
