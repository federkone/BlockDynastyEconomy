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

package lib.gui.templates.administrators.subMenus.accounts;

import BlockDynasty.Economy.aplication.useCase.account.DeleteAccountUseCase;
import BlockDynasty.Economy.aplication.useCase.account.EditAccountUseCase;
import BlockDynasty.Economy.aplication.useCase.account.getAccountUseCase.GetAccountByUUIDUseCase;
import BlockDynasty.Economy.domain.entities.account.Player;
import BlockDynasty.Economy.domain.result.Result;
import lib.abstractions.PlatformAdapter;
import lib.commands.abstractions.IEntityCommands;
import lib.gui.GUIFactory;
import lib.gui.components.IGUI;
import lib.gui.components.IEntityGUI;
import lib.gui.components.ITextInput;
import lib.gui.components.factory.Item;
import lib.gui.components.recipes.RecipeItem;
import lib.util.materials.Materials;
import lib.gui.components.generics.AbstractPanel;
import lib.util.colors.ChatColor;
import lib.util.colors.Colors;

public class EditAccountPanel extends AbstractPanel {
    private final DeleteAccountUseCase deleteAccountUseCase;
    private final EditAccountUseCase editAccountUseCase;
    private final GetAccountByUUIDUseCase getAccountByUUIDUseCase;
    private final PlatformAdapter platformAdapter;
    private final ITextInput textInput;

    public EditAccountPanel(
            DeleteAccountUseCase deleteAccountUseCase,
            EditAccountUseCase editAccountUseCase,
            GetAccountByUUIDUseCase getAccountByUUIDUseCase,
            PlatformAdapter platformAdapter,
            IEntityGUI sender, Player target, IGUI parent, ITextInput textInput) {
        super("Edit Account: "+target.getNickname(), 5,sender,parent);
        this.platformAdapter = platformAdapter;
        this.deleteAccountUseCase = deleteAccountUseCase;
        this.editAccountUseCase = editAccountUseCase;
        this.getAccountByUUIDUseCase = getAccountByUUIDUseCase;
        this.textInput = textInput;

        buttons(sender,target);
    }

    private void buttons(IEntityGUI sender, Player target) {
        setItem(15, Item.of(RecipeItem.builder()
                        .setMaterial(Materials.REDSTONE)
                        .setName(ChatColor.stringValueOf(Colors.RED)+"Delete account")
                        .setLore("The player's account is deleted,before a confirmation")
                        .build()),
                f -> {
                    textInput.open(this,sender, "Delete: "+target.getNickname(), "Confirm yes/no", s ->{
                        if(s.equals("yes")){
                            Result<Void> result = deleteAccountUseCase.execute(target.getNickname());
                            if(result.isSuccess()){
                                sender.sendMessage("The player has been eliminated "+ target.getNickname());
                                IEntityCommands p = platformAdapter.getPlayer(target.getNickname());
                                if(p != null){
                                    p.kickPlayer("Your economy account has been deleted.");
                                }
                                return null;
                            }else{
                                sender.sendMessage(result.getErrorMessage()+" "+ result.getErrorCode());
                                return null;
                            }
                        }else {
                            this.open();
                            return null;
                        }
                    });
                });

        setItem(22,Item.of(RecipeItem.builder()
                        .setMaterial(Materials.PAPER)
                        .setName(ChatColor.stringValueOf(Colors.AQUA)+"Sell Command")
                        .setLore("Sell a command to a player","Example: give <playername> <itemname> [amount]",
                                ChatColor.stringValueOf(Colors.RED)+"IMPORTANT: Enter the full command",ChatColor.stringValueOf(Colors.RED)+"including any necessary parameters, such as player name.")
                        .build()),
                f -> {
                    GUIFactory.sellCommandPanel(sender , target, this).open();
                });

        setItem(40,Item.of(RecipeItem.builder()
                .setMaterial(Materials.BARRIER)
                .setName(ChatColor.stringValueOf(Colors.RED)+"Back")
                .setLore("Click to go back")
                .build()
        ),f->{
            GUIFactory.accountSelectorToEdit(sender,this.getParent().getParent()).open();
        });

        setItem(29,Item.of(RecipeItem.builder()
                        .setMaterial(Materials.PAPER)
                        .setName(ChatColor.stringValueOf(Colors.AQUA)+"Deposit Currency")
                        .setLore("Deposit currency into the player's account")
                        .build()),
                f -> {
                    GUIFactory.depositPanel(sender , target, this).open();
                });

        setItem(31,Item.of(RecipeItem.builder()
                        .setMaterial(Materials.PAPER)
                        .setName(ChatColor.stringValueOf(Colors.AQUA)+"Set balance currency")
                        .setLore("Set the balance of a currency in the player's account")
                        .build()),
                f -> {
                    GUIFactory.setPanel(sender , target, this).open();
                });

        setItem(33,Item.of(RecipeItem.builder()
                        .setMaterial(Materials.PAPER)
                        .setName(ChatColor.stringValueOf(Colors.AQUA)+"Withdraw Currency")
                        .setLore("Withdraw currency from the player's account")
                        .build()),
                f -> {
                    GUIFactory.withdrawPanel(sender , target, this).open();
                });

        setItem(11,Item.of(RecipeItem.builder()
                        .setMaterial(Materials.BOOK)
                        .setName(ChatColor.stringValueOf(Colors.YELLOW)+"See Balance")
                        .setLore("See the balance of the player's account")
                        .build()),
                f -> {
                    GUIFactory.balancePanel( sender, target.getUuid(), this).open();
                });

        boolean isBlocked = getAccountByUUIDUseCase.execute(target.getUuid()).getValue().isBlocked();
        if (isBlocked) {
            setItem(13, Item.of(RecipeItem.builder()
                            .setMaterial(Materials.RED_CONCRETE)
                            .setName(ChatColor.stringValueOf(Colors.RED)+"Account is blocked")
                            .setLore("Click to unblock transactions","This affects:","Withdraw","Deposit","Transfer", "Pay","Trade","Exchange","All economy op")
                            .build()),
                    f -> {
                        Result<Void>result= editAccountUseCase.unblockAccount(target.getUuid());
                        if (result.isSuccess()){
                            GUIFactory.editAccountPanel( sender, target, this.getParent()).open();
                        }
                        else {
                            sender.sendMessage(result.getErrorMessage()+" "+ result.getErrorCode());
                        }
                    });
        } else {
            setItem(13, Item.of(RecipeItem.builder()
                            .setMaterial(Materials.LIME_CONCRETE)
                            .setName(ChatColor.stringValueOf(Colors.GREEN)+"Account is enabled")
                            .setLore("Click to block transactions Account","This affects:","Withdraw","Deposit","Transfer", "Pay","Trade","Exchange","All economy op")
                            .build()),
                    f -> {
                        Result<Void>result= editAccountUseCase.blockAccount(target.getUuid());
                        if (result.isSuccess()){
                            GUIFactory.editAccountPanel( sender, target, this.getParent()).open();
                        }
                        else {
                            sender.sendMessage(result.getErrorMessage()+" "+ result.getErrorCode());
                        }
                    });
        }


    }
}