/**
 * Copyright 2025 Federico Barrionuevo "@federkone"
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package platform.listeners;

import BlockDynasty.Economy.aplication.useCase.UseCaseFactory;
import BlockDynasty.Economy.aplication.useCase.account.CreateAccountUseCase;
import BlockDynasty.Economy.aplication.useCase.account.getAccountUseCase.GetAccountByNameUseCase;
import BlockDynasty.Economy.aplication.useCase.account.getAccountUseCase.GetAccountByUUIDUseCase;
import BlockDynasty.Economy.domain.entities.account.Account;
import BlockDynasty.Economy.domain.result.Result;
import BlockDynasty.Economy.domain.services.IAccountService;
import lib.commands.abstractions.IEntityCommands;

public class PlayerJoinListener implements IPlayerJoin {
    protected final CreateAccountUseCase createAccountUseCase;
    protected final GetAccountByNameUseCase getAccountByNameUseCase;
    protected final GetAccountByUUIDUseCase getAccountByUUIDUseCase;
    protected final IAccountService accountService;

    public PlayerJoinListener(UseCaseFactory useCaseFactory, IAccountService accountService) {
        this.createAccountUseCase = useCaseFactory.createAccount();
        this.getAccountByNameUseCase = useCaseFactory.searchAccountByName();
        this.getAccountByUUIDUseCase = useCaseFactory.searchAccountByUUID();
        this.accountService = accountService;
    }

    public void loadOnlinePlayerAccount(IEntityCommands player) {
        Result<Account> result = getAccountByUUIDUseCase.execute(player.getUniqueId());
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
    public void loadOfflinePlayerAccount(IEntityCommands player) {
        Result<Account> result = getAccountByNameUseCase.execute(player.getName());
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

    public void offLoadPlayerAccount(IEntityCommands player) {
        accountService.removeAccountOnline(player.getUniqueId());
    }
}
