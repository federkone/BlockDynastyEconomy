package listeners;

import BlockDynasty.Economy.aplication.useCase.account.CreateAccountUseCase;
import BlockDynasty.Economy.aplication.useCase.account.SearchAccountUseCase;
import BlockDynasty.Economy.domain.entities.account.Account;
import BlockDynasty.Economy.domain.result.Result;
import BlockDynasty.Economy.domain.services.IAccountService;
import BlockDynasty.Economy.domain.services.ICurrencyService;
import lib.commands.abstractions.Source;

public class PlayerJoinListener implements IPlayerJoin {
    protected final CreateAccountUseCase createAccountUseCase;
    protected final SearchAccountUseCase searchAccountUseCase;
    protected final IAccountService accountService;

    public PlayerJoinListener(CreateAccountUseCase createAccountUseCase, SearchAccountUseCase searchAccountUseCase, IAccountService accountService) {

        this.createAccountUseCase = createAccountUseCase;
        this.searchAccountUseCase = searchAccountUseCase;
        this.accountService = accountService;
    }

    public void loadOnlinePlayerAccount(Source player) {
        Result<Account> result = searchAccountUseCase.getAccount(player.getUniqueId());
        if (result.isSuccess()) {
            Result<Void> resultChangeName = accountService.checkNameChange(result.getValue(), player.getName());
            if(!resultChangeName.isSuccess()){
                //player.kick(Component.text("Error al cargar tu cuenta de economía. Por favor, vuelve a ingresar o contacta a un administrador.")); //paper
                player.kickPlayer("Error al cargar tu cuenta de economía. Por favor, vuelve a ingresar o contacta a un administrador.");
                return;
            }
            accountService.addAccountToOnline(result.getValue());
            return;
        }

        Result<Account> creationResult = createAccountUseCase.execute(player.getUniqueId(), player.getName());
        if (!creationResult.isSuccess()) {
            //player.kick(Component.text("Error al crear o cargar tu cuenta de economía. Por favor, vuelve a ingresar o contacta a un administrador.")); //paper
            player.kickPlayer("Error al crear o cargar tu cuenta de economía. Por favor, vuelve a ingresar o contacta a un administrador.");
        }
    }

    @Override
    public void loadOfflinePlayerAccount(Source player) {
        Result<Account> result = searchAccountUseCase.getAccount(player.getName());
        if (result.isSuccess()) {
            Result<Void> resultChangeUuid = accountService.checkUuidChange(result.getValue(), player.getUniqueId());
            if(!resultChangeUuid.isSuccess()){
                //player.kick(Component.text("Error al cargar tu cuenta de economía. Por favor, vuelve a ingresar o contacta a un administrador."));
                player.kickPlayer("Error al cargar tu cuenta de economía. Por favor, vuelve a ingresar o contacta a un administrador.");
                return;
            }
            accountService.addAccountToOnline(result.getValue());
            return;
        }

        Result<Account> creationResult = createAccountUseCase.execute(player.getUniqueId(), player.getName());
        if (!creationResult.isSuccess()) {
            //player.kick(Component.text("Error al crear o cargar tu cuenta de economía. Por favor, vuelve a ingresar o contacta a un administrador."));
            player.kickPlayer("Error al crear o cargar tu cuenta de economía. Por favor, vuelve a ingresar o contacta a un administrador.");
        }
    }

    public void offLoadPlayerAccount(Source player) {
        accountService.removeAccountOnline(player.getUniqueId());
    }
}
