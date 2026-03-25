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

package lib.gui.templates.users.Offers;

import BlockDynasty.Economy.aplication.useCase.account.getAccountUseCase.GetAccountByNameUseCase;
import BlockDynasty.Economy.aplication.useCase.account.getAccountUseCase.GetOfflineAccountsUseCase;
import BlockDynasty.Economy.domain.entities.account.Account;
import BlockDynasty.Economy.domain.entities.account.Player;
import BlockDynasty.Economy.domain.result.Result;
import lib.gui.GUIFactory;
import lib.gui.components.IEntityGUI;
import lib.gui.components.IGUI;
import lib.gui.components.ITextInput;
import lib.gui.components.PlatformGUI;
import lib.gui.components.factory.Item;
import lib.gui.components.generics.Button;
import abstractions.platform.recipes.RecipeItem;
import abstractions.platform.materials.Materials;
import lib.gui.templates.users.ListPlayersFromDb;
import util.colors.ChatColor;
import util.colors.Colors;
import services.messages.Message;
import java.util.Map;

public class ListPlayersOfflineToOffer extends ListPlayersFromDb {
    private IEntityGUI sender;
    private final GetAccountByNameUseCase getAccountByNameUseCase;

    public ListPlayersOfflineToOffer(IEntityGUI sender, IGUI parent, GetAccountByNameUseCase getAccountByNameUseCase,
                                     GetOfflineAccountsUseCase getOfflineAccountsUseCase , ITextInput textInput, PlatformGUI platform) {
        super( sender, parent, getAccountByNameUseCase, getOfflineAccountsUseCase,textInput, platform);
        this.sender = sender;
        this.getAccountByNameUseCase = getAccountByNameUseCase;
    }

    @Override
    public BlockDynasty.Economy.domain.entities.account.Player findPlayerByName(String playerName) {
        Result<Account> result = getAccountByNameUseCase.execute(playerName);
        if(result.isSuccess()){
            return result.getValue().getPlayer();
        }else {return null;}
    }

    @Override
    public void openNextSection(Player target) {
        GUIFactory.createOfferFirstPanel(sender, target, this.getParent()).open();
    }

    @Override
    public void addCustomButtons() {
        super.addCustomButtons();
        setButton(4, Button.builder()
                .setItemStack(Item.of(RecipeItem.builder()
                        .setMaterial(Materials.PAPER)
                        .setName(Message.process(Map.of("color",ChatColor.stringValueOf(Colors.GREEN)),"OfferListPlayer.button1.nameItem"))
                        .setLore(Message.processLines(Map.of("color",ChatColor.stringValueOf(Colors.WHITE)),"OfferListPlayer.button1.lore"))
                        .build()))
                .build());
    }
}
