package BlockDynasty.Economy.aplication.useCase.account;

import BlockDynasty.Economy.domain.entities.account.Account;
import BlockDynasty.Economy.domain.persistence.entities.IRepository;
import BlockDynasty.Economy.domain.result.Result;
import BlockDynasty.Economy.domain.services.IAccountService;
import BlockDynasty.Economy.aplication.useCase.account.GetAccountsUseCase;

import java.util.UUID;

//todo: dado un nombre o uuid de un jugador, elimina su cuenta de la cache  y la base de datos
//el comando que utiliza este caso de uso en infraestructura podria expulsar inmediatamente al jugador del servidor para evitar problemas
public class DeleteAccountUseCase {
    IRepository repository;
    IAccountService accountService;
    GetAccountsUseCase getAccountsUseCase;

    public DeleteAccountUseCase(IRepository repository, IAccountService accountService,GetAccountsUseCase getAccountsUseCase) {
           this.repository= repository;
           this.accountService = accountService;
           this.getAccountsUseCase = getAccountsUseCase;
    }

    public Result<Void> execute(String name){
        // Primero, obtenemos la cuenta del jugador por su nombre
        Result<Account> accountResult = getAccountsUseCase.getAccount(name);
        if (!accountResult.isSuccess()) {
            return Result.failure("Account not found for player: " + name, accountResult.getErrorCode());
        }
        Result<Void> result =repository.deleteAccount(accountResult.getValue());
        accountService.removeAccountFromCache(accountResult.getValue().getUuid());
        if (!result.isSuccess()) {
            return Result.failure("Failed to delete account for player: " + name, result.getErrorCode());
        }
        return Result.success(null);
    }

    public Result<Void> execute(UUID uuid){
        // Primero, obtenemos la cuenta del jugador por su UUID
        Result<Account> accountResult = getAccountsUseCase.getAccount(uuid);
        if (!accountResult.isSuccess()) {
            return Result.failure("Account not found for UUID: " + uuid, accountResult.getErrorCode());
        }
        Result<Void> result = repository.deleteAccount(accountResult.getValue());
        accountService.removeAccountFromCache(accountResult.getValue().getUuid());
        if (!result.isSuccess()) {
            return Result.failure("Failed to delete account for UUID: " + uuid, result.getErrorCode());
        }
        return Result.success(null);
    }
}
