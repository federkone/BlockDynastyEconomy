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
import BlockDynasty.Economy.aplication.useCase.account.SearchAccountUseCase;
import BlockDynasty.Economy.domain.entities.account.Player;
import BlockDynasty.Economy.domain.result.Result;
import lib.commands.abstractions.IEntityCommands;
import lib.gui.GUIFactory;
import lib.gui.abstractions.IGUI;
import lib.gui.abstractions.IEntityGUI;
import lib.gui.abstractions.ITextInput;
import lib.gui.abstractions.Materials;
import lib.gui.templates.abstractions.AbstractGUI;
import lib.util.colors.ChatColor;
import lib.util.colors.Colors;

public class EditAccountGUI extends AbstractGUI {
    private final DeleteAccountUseCase deleteAccountUseCase;
    private final EditAccountUseCase editAccountUseCase;
    private final SearchAccountUseCase searchAccountUseCase;
    private final ITextInput textInput;

    public EditAccountGUI(
            DeleteAccountUseCase deleteAccountUseCase,
            EditAccountUseCase editAccountUseCase,
            SearchAccountUseCase searchAccountUseCase,
            IEntityGUI sender, Player target, IGUI parent, ITextInput textInput) {
        super("Edit Account: "+target.getNickname(), 5,sender,parent);
        this.deleteAccountUseCase = deleteAccountUseCase;
        this.editAccountUseCase = editAccountUseCase;
        this.searchAccountUseCase = searchAccountUseCase;
        this.textInput = textInput;

        buttons(sender,target);
    }

    private void buttons(IEntityGUI sender, Player target) {
        setItem(15,createItem(Materials.REDSTONE,"Delete account","The player's account is deleted,before a confirmation"),
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

        setItem(40,createItem(Materials.BARRIER, "Back","Click to go back"),f->{
            GUIFactory.accountPanel(sender,this.getParent().getParent()).open();});

        setItem(29,createItem(Materials.PAPER,"Deposit Currency","Deposit currency into the player's account"),
                f -> {
                    GUIFactory.depositPanel(sender , target, this).open();
                });

        setItem(31,createItem(Materials.PAPER,"Set balance currency","Set the balance of a currency in the player's account"),
                f -> {
                    GUIFactory.setPanel(sender , target, this).open();
                });

        setItem(33,createItem(Materials.PAPER,"Withdraw Currency","Withdraw currency from the player's account"),
                f -> {
                    GUIFactory.withdrawPanel(sender , target, this).open();
                });

        setItem(11,createItem(Materials.BOOK, "See Balance","See the balance of the player's account"),
                f -> {
                    GUIFactory.balancePanel( sender, target.getUuid(), this).open();
                });

        boolean isBlocked = searchAccountUseCase.getAccount(target.getUuid()).getValue().isBlocked();
        if (isBlocked) {
            setItem(13, createItem(Materials.RED_CONCRETE, ChatColor.stringValueOf(Colors.RED)+"Account is blocked", "Click to unblock transactions","This affects:","Withdraw","Deposit","Transfer", "Pay","Trade","Exchange","All economy op"),
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
            setItem(13, createItem(Materials.LIME_CONCRETE, ChatColor.stringValueOf(Colors.GREEN)+"Account is enabled", "Click to block transactions Account","This affects:","Withdraw","Deposit","Transfer", "Pay","Trade","Exchange","All economy op"),
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