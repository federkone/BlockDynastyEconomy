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

package net.blockdynasty.economy.gui.gui.templates.administrators.mainMenus;

import net.blockdynasty.economy.core.aplication.useCase.account.getAccountUseCase.GetAccountByNameUseCase;
import net.blockdynasty.economy.core.aplication.useCase.account.getAccountUseCase.GetOfflineAccountsUseCase;
import net.blockdynasty.economy.core.domain.entities.account.Account;
import net.blockdynasty.economy.core.domain.entities.account.Player;
import net.blockdynasty.economy.core.domain.result.Result;
import net.blockdynasty.economy.libs.abstractions.platform.scheduler.ContextualTask;
import net.blockdynasty.economy.gui.gui.GUIFactory;
import net.blockdynasty.economy.gui.gui.components.*;
import net.blockdynasty.economy.gui.gui.components.factory.Item;
import net.blockdynasty.economy.gui.gui.components.generics.Button;
import net.blockdynasty.economy.libs.abstractions.platform.recipes.RecipeItem;
import net.blockdynasty.economy.libs.abstractions.platform.materials.Materials;
import net.blockdynasty.economy.gui.gui.components.generics.AccountsList;
import net.blockdynasty.economy.libs.services.messages.Message;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AccountSelectorToEdit extends AccountsList {
    private final IEntityGUI sender;
    private final GetAccountByNameUseCase getAccountByNameUseCase;

    public AccountSelectorToEdit(IEntityGUI sender, GetAccountByNameUseCase getAccountByNameUseCase, GetOfflineAccountsUseCase getOfflineAccountsUseCase,
                                 IGUI parent, ITextInput textInput, PlatformGUI platform) {
        super(Message.process("AccountSelectorToEdit.title"), 5,sender,parent,textInput);
        this.getAccountByNameUseCase = getAccountByNameUseCase;
        this.sender = sender;

        this.showPlayers(new ArrayList<>());
        this.createLoadingMessage();
        Runnable r= () -> {
            Result<List<Account>> result = getOfflineAccountsUseCase.execute();
            if(result.isSuccess()) {
                List<Player> players = new ArrayList<>(result.getValue().stream()
                        .map(Account::getPlayer)
                        .sorted((a, b) -> a.getNickname().compareToIgnoreCase(b.getNickname()))
                        .collect(Collectors.toList()));
                this.clearStatusMessage();
                this.showPlayers(players);
            }
        };
        platform.getScheduler().runAsync(ContextualTask.build(r,sender));
    }

    @Override
    protected IItemStack createItemFor(Player player) {
        RecipeItem recipe = RecipeItem.builder()
                .setMaterial(Materials.PLAYER_HEAD)
                .setName(player.getNickname())
                .setLore("databaseId: " + player.getId(), "uuid: " + player.getUuid().toString())
                .build();
        return Item.of(recipe);
    }

    @Override
    protected void addCustomButtons(){
        super.addCustomButtons();
        RecipeItem recipe = RecipeItem.builder()
                .setMaterial(Materials.PAPER)
                .setName( Message.process("AccountSelectorToEdit.button1.nameItem"))
                .setLore( Message.processLines("AccountSelectorToEdit.button1.lore"))
                .build();
        setButton(4, Button.builder().setItemStack(Item.of(recipe)).build());
    }
    @Override
    public Player findPlayerByName(String playerName) {
        Result<Account> result = getAccountByNameUseCase.execute(playerName);
        if(result.isSuccess()){
            return result.getValue().getPlayer();
        }else {return null;}
    }

    @Override
    public void openNextSection(Player target) {
        GUIFactory.editAccountPanel( sender, target, this).open();
    }
}
