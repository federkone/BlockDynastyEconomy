package net.blockdynasty.economy.engine.platform.listeners;

import net.blockdynasty.economy.core.aplication.useCase.account.CreateAccountUseCase;
import net.blockdynasty.economy.core.aplication.useCase.account.getAccountUseCase.GetAccountByNameUseCase;
import net.blockdynasty.economy.core.domain.entities.account.Account;
import net.blockdynasty.economy.core.domain.result.ErrorCode;
import net.blockdynasty.economy.core.domain.result.Result;
import net.blockdynasty.economy.core.domain.services.IAccountService;
import net.blockdynasty.economy.gui.commands.abstractions.IEntityCommands;
import net.blockdynasty.economy.libs.util.colors.ChatColor;
import net.blockdynasty.economy.libs.util.colors.Colors;
import net.blockdynasty.economy.libs.services.Console;

public class PlayerJoinListenerOffline implements LoadAccount{
    private final GetAccountByNameUseCase getAccountByNameUseCase;
    private final CreateAccountUseCase createAccountUseCase;
    private final IAccountService accountService;

    public PlayerJoinListenerOffline( GetAccountByNameUseCase getAccountByNameUseCase,
                                     CreateAccountUseCase createAccountUseCase,
                                     IAccountService accountService) {
        this.getAccountByNameUseCase = getAccountByNameUseCase;
        this.createAccountUseCase = createAccountUseCase;
        this.accountService = accountService;
    }

    @Override
    public void loadAccount(IEntityCommands player) {
        Result<Account> result = getAccountByNameUseCase.execute(player.getName()); //esto falla y procede automaticamente a crear cuenta
        if (result.isSuccess()) {
            Result<Void> resultChangeUuid = accountService.checkUuidChange(result.getValue(), player.getUniqueId());
            if(!resultChangeUuid.isSuccess()){
                Console.logError(resultChangeUuid.getErrorMessage());
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
