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

package lib.gui.templates.users;

import BlockDynasty.Economy.aplication.useCase.account.getAccountUseCase.GetAccountByUUIDUseCase;
import BlockDynasty.Economy.domain.entities.account.Account;
import BlockDynasty.Economy.domain.result.Result;
import lib.gui.GUIFactory;
import lib.gui.components.IEntityGUI;
import lib.gui.components.ITextInput;
import lib.gui.components.factory.Item;
import lib.gui.components.recipes.RecipeItem;
import lib.util.materials.Materials;
import lib.gui.components.generics.AbstractPanel;
import lib.util.colors.ChatColor;
import lib.util.colors.Colors;
import lib.util.colors.Message;

import java.util.Map;


public class BankPanel extends AbstractPanel {
    private final IEntityGUI player;
    private GetAccountByUUIDUseCase getAccount;
    private ITextInput textInput;

    public BankPanel(IEntityGUI player, GetAccountByUUIDUseCase getAccount, ITextInput textInput) {
        super(Message.process("BankPanel.title") +" ["+player.getName()+"]", 4, player);
        this.player = player;
        this.getAccount = getAccount;
        this.textInput = textInput;
        setupGUI();
    }

    private void setupGUI() {
        Result<Account> account = getAccount.execute(player.getUniqueId());
        boolean isBlocked = false;
        if (account.isSuccess()){
            isBlocked = account.getValue().isBlocked();
        }

        setItem(4, isBlocked
                        ? Item.of(RecipeItem.builder()
                        .setMaterial(Materials.RED_CONCRETE)
                        .setName(Message.process(Map.of("color", ChatColor.stringValueOf(Colors.RED)),"BankPanel.button1.nameItem1"))
                        .setLore( Message.processLines(Map.of("color", ChatColor.stringValueOf(Colors.WHITE)),"BankPanel.button1.lore"))
                        .build())
                        : Item.of(RecipeItem.builder()
                        .setMaterial(Materials.LIME_CONCRETE)
                        .setName(Message.process(Map.of("color", ChatColor.stringValueOf(Colors.GREEN)),"BankPanel.button1.nameItem2"))
                        .setLore(Message.processLines(Map.of("color", ChatColor.stringValueOf(Colors.WHITE)),"BankPanel.button1.lore"))
                        .build()),
                null);

        setItem(11,Item.of(RecipeItem.builder()
                        .setMaterial(Materials.DIAMOND)
                        .setName(Message.process(Map.of("color", ChatColor.stringValueOf(Colors.GOLD)),"BankPanel.button2.nameItem"))
                        .setLore(Message.process(Map.of("color", ChatColor.stringValueOf(Colors.WHITE)),"BankPanel.button2.lore"))
                        .build()),
                unused -> {
                    GUIFactory.exchangeFirstPanel(player,this).open();
                });

        setItem(24, Item.of(RecipeItem.builder()
                        .setMaterial(Materials.WRITABLE_BOOK)
                        .setName(Message.process(Map.of("color", ChatColor.stringValueOf(Colors.GOLD)),"BankPanel.button3.nameItem"))
                        .setLore(Message.processLines(Map.of("color", ChatColor.stringValueOf(Colors.WHITE)),"BankPanel.button3.lore"))
                        .build()),
                f -> {
                    GUIFactory.listPlayerOnlineToOffer(player,this).open();
                });

        setItem(25, Item.of(RecipeItem.builder()
                        .setMaterial(Materials.WRITABLE_BOOK)
                        .setName(Message.process(Map.of("color", ChatColor.stringValueOf(Colors.GOLD)),"BankPanel.button4.nameItem"))
                        .setLore(Message.processLines(Map.of("color", ChatColor.stringValueOf(Colors.WHITE)),"BankPanel.button4.lore"))
                        .build()),
                f -> {
                    GUIFactory.listPlayersOfflineToOffer(player,this).open();
                });

        setItem(22,Item.of(RecipeItem.builder()
                        .setMaterial(Materials.ENDER_CHEST)
                        .setName(Message.process(Map.of("color", ChatColor.stringValueOf(Colors.GOLD)),"BankPanel.button5.nameItem"))
                        .setLore(Message.processLines(Map.of("color", ChatColor.stringValueOf(Colors.WHITE)),"BankPanel.button5.lore"))
                        .build()),
                f -> {
                    GUIFactory.myActiveOffers(player,this).open();
                });;

        setItem(13, Item.of(RecipeItem.builder()
                        .setMaterial(Materials.BOOK)
                        .setName(Message.process(Map.of("color", ChatColor.stringValueOf(Colors.GOLD)),"BankPanel.button6.nameItem"))
                        .setLore(Message.process(Map.of("color", ChatColor.stringValueOf(Colors.WHITE)),"BankPanel.button6.lore"))
                        .build()),
                unused -> {
                    GUIFactory.balancePanel(player,this).open();
                });

        setItem(20,Item.of(RecipeItem.builder()
                        .setMaterial(Materials.CHEST)
                        .setName(Message.process(Map.of("color", ChatColor.stringValueOf(Colors.GOLD)),"BankPanel.button7.nameItem"))
                        .setLore(Message.processLines(Map.of("color", ChatColor.stringValueOf(Colors.WHITE)),"BankPanel.button7.lore"))
                        .build()),
                f -> {
                    GUIFactory.receivedOffers(player,this).open();
                });

        setItem(15, Item.of(RecipeItem.builder()
                        .setMaterial(Materials.PLAYER_HEAD)
                        .setName(Message.process(Map.of("color", ChatColor.stringValueOf(Colors.GOLD)),"BankPanel.button8.nameItem"))
                        .setLore(Message.processLines(Map.of("color", ChatColor.stringValueOf(Colors.WHITE)),"BankPanel.button8.lore"))
                        .build()),
                unused -> {
                    GUIFactory.listPlayersOnline(player,this).open();
                });

        setItem(16,Item.of(        RecipeItem.builder()
                        .setMaterial(Materials.PLAYER_HEAD)
                        .setName(Message.process(Map.of("color", ChatColor.stringValueOf(Colors.GOLD)),"BankPanel.button9.nameItem"))
                        .setLore(Message.processLines(Map.of("color", ChatColor.stringValueOf(Colors.WHITE)),"BankPanel.button9.lore"))
                        .build()),
                f->{
                    GUIFactory.listPlayersFromDb(player,this).open();
                });

        setItem(31, Item.of(RecipeItem.builder()
                        .setMaterial(Materials.BARRIER)
                        .setName(Message.process(Map.of("color", ChatColor.stringValueOf(Colors.RED)),"BankPanel.button10.nameItem"))
                        .setLore(Message.process(Map.of("color", ChatColor.stringValueOf(Colors.WHITE)),"BankPanel.button10.lore"))
                        .build()),
                unused -> this.close());
    }
}