package net.blockdynasty.economy.engine.platform.listeners;

import net.blockdynasty.economy.core.aplication.useCase.account.CreateAccountUseCase;
import net.blockdynasty.economy.core.aplication.useCase.account.getAccountUseCase.GetAccountByUUIDUseCase;
import net.blockdynasty.economy.core.domain.entities.account.Account;
import net.blockdynasty.economy.core.domain.result.ErrorCode;
import net.blockdynasty.economy.core.domain.result.Result;
import net.blockdynasty.economy.core.domain.services.IAccountService;
import net.blockdynasty.economy.gui.commands.abstractions.IEntityCommands;
import net.blockdynasty.economy.libs.util.colors.ChatColor;
import net.blockdynasty.economy.libs.util.colors.Colors;
import net.blockdynasty.economy.libs.services.Console;

public class PlayerJoinListenerOnline implements LoadAccount{
    private final GetAccountByUUIDUseCase getAccountByUUIDUseCase;
    private final CreateAccountUseCase createAccountUseCase;
    private final IAccountService accountService;

    public PlayerJoinListenerOnline( GetAccountByUUIDUseCase getAccountByUUIDUseCase,
                                    CreateAccountUseCase createAccountUseCase,
                                    IAccountService accountService) {
        this.getAccountByUUIDUseCase = getAccountByUUIDUseCase;
        this.createAccountUseCase = createAccountUseCase;
        this.accountService = accountService;
    }

    public void loadAccount(IEntityCommands player) {
        Result<Account> result = getAccountByUUIDUseCase.execute(player.getUniqueId());//esto falla y procede automaticamente a crear cuenta
        if (result.isSuccess()) {
            Result<Void> resultChangeName = accountService.checkNameChange(result.getValue(), player.getName());
            if(!resultChangeName.isSuccess()){
                Console.logError(resultChangeName.getErrorMessage());
                player.sendMessage(ChatColor.stringValueOf(Colors.RED)+"Error loading your finance account. Please log in again or contact an administrator.");
                return;
            }
            accountService.addAccountToOnline(result.getValue());
        }else if(result.getErrorCode() == ErrorCode.ACCOUNT_NOT_FOUND){
            Result<Account> creationResult = createAccountUseCase.execute(player.getUniqueId(), player.getName());
            if (!creationResult.isSuccess()) {
                Console.logError(creationResult.getErrorMessage());
                player.sendMessage(ChatColor.stringValueOf(Colors.RED)+"Error creating your finance account. Please log in again or contact an administrator.");
            }
        }else{
            Console.logError(result.getErrorMessage());
            player.sendMessage(ChatColor.stringValueOf(Colors.RED)+"Error loading your finance account. Please log in again or contact an administrator.");
        }
    }
}
