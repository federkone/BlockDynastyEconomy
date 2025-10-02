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

import BlockDynasty.Economy.aplication.useCase.account.SearchAccountUseCase;
import BlockDynasty.Economy.domain.entities.account.Account;
import BlockDynasty.Economy.domain.result.Result;
import lib.gui.GUIFactory;
import lib.gui.components.IEntityGUI;
import lib.gui.components.ITextInput;
import lib.gui.components.Materials;
import lib.gui.components.abstractions.AbstractPanel;
import lib.gui.templates.users.Offers.ListPlayerOnlineToOffer;
import lib.gui.templates.users.Offers.ListPlayersOfflineToOffer;
import lib.util.colors.ChatColor;
import lib.util.colors.Colors;


public class BankPanel extends AbstractPanel {
    private final IEntityGUI player;
    private final SearchAccountUseCase searchAccountUseCase;
    private final ITextInput textInput;

    public BankPanel(IEntityGUI player , SearchAccountUseCase SearchAccountUseCase, ITextInput textInput) {
        super("Bank "+"["+player.getName()+"]", 4, player);
        this.player = player;
        this.searchAccountUseCase = SearchAccountUseCase;
        this.textInput = textInput;

        setupGUI();
    }

    private void setupGUI() {
        Result<Account> account = searchAccountUseCase.getAccount(player.getUniqueId());
        boolean isBlocked = false;
        if (account.isSuccess()){
            isBlocked = account.getValue().isBlocked();
        }
        setItem(4, isBlocked
                        ? createItem(Materials.RED_CONCRETE, ChatColor.stringValueOf(Colors.RED)+"Account is blocked", "Your account is temporary blocked", "This affects:", "Withdraw", "Deposit", "Transfer", "Pay", "Trade", "Exchange", "All economy op")
                        : createItem(Materials.LIME_CONCRETE, ChatColor.stringValueOf(Colors.GREEN)+"Account is enabled", "Your account is allowed to do transactions", "This affects:", "Withdraw", "Deposit", "Transfer", "Pay", "Trade", "Exchange", "All economy op"),
                null);
        setItem(11,createItem(Materials.DIAMOND, ChatColor.stringValueOf(Colors.GOLD)+"Exchange currencies",ChatColor.stringValueOf(Colors.WHITE)+"Click to exchange currencies"),
                unused -> {
                    GUIFactory.exchangeFirstPanel(player,this).open();
                });
        setItem(24, createItem(Materials.WRITABLE_BOOK,ChatColor.stringValueOf(Colors.GOLD)+"Create Offer to Player online",ChatColor.stringValueOf(Colors.WHITE)+"Click to create an offer for trade currencies",ChatColor.stringValueOf(Colors.WHITE)+"with other players Online"),
                f -> {
                    new ListPlayerOnlineToOffer(player,this,textInput).open();
                });
        setItem(25, createItem(Materials.WRITABLE_BOOK,ChatColor.stringValueOf(Colors.GOLD)+"Create Offer To Network Player",ChatColor.stringValueOf(Colors.WHITE)+"Click to create an offer for trade currencies",ChatColor.stringValueOf(Colors.WHITE)+"with other players from the network"),
                f -> {
                    new ListPlayersOfflineToOffer(player,this,searchAccountUseCase,textInput).open();
                });
        setItem(22,createItem(Materials.ENDER_CHEST,ChatColor.stringValueOf(Colors.GOLD)+"My Active Offers",ChatColor.stringValueOf(Colors.WHITE)+"These are the offers you have created",ChatColor.stringValueOf(Colors.WHITE)+"and are currently active."),
                f -> {
                    GUIFactory.myActiveOffers(player,this).open();
                });;
        setItem(13, createItem(Materials.BOOK, ChatColor.stringValueOf(Colors.GOLD)+"See Balance", ChatColor.stringValueOf(Colors.WHITE)+"Click to see your balance"),
                unused -> {
                    GUIFactory.balancePanel(player,this).open();
                });
        setItem(20,createItem(Materials.CHEST,ChatColor.stringValueOf(Colors.GOLD)+"Received Offers",ChatColor.stringValueOf(Colors.WHITE)+"Here you can view all the offers sent",ChatColor.stringValueOf(Colors.WHITE)+"to you by other players."),
                f -> {
                    GUIFactory.receivedOffers(player,this).open();
                });
        setItem(15, createItem(Materials.PLAYER_HEAD, ChatColor.stringValueOf(Colors.GOLD)+"Pay a Player Online", ChatColor.stringValueOf(Colors.WHITE)+"Click to pay another player Online in this server"),
                unused -> {
                    GUIFactory.listPlayersOnline(player,this).open();
                });
        setItem(16,createItem(Materials.PLAYER_HEAD,ChatColor.stringValueOf(Colors.GOLD)+"Transfer a Player from the Network", ChatColor.stringValueOf(Colors.WHITE)+"Click to Transfer founds to another",ChatColor.stringValueOf(Colors.WHITE)+"Player from the network"),
                f->{
                    GUIFactory.listPlayersFromDb(player,this).open();
                });
        setItem(31, createItem(Materials.BARRIER, ChatColor.stringValueOf(Colors.RED)+"Exit", ChatColor.stringValueOf(Colors.WHITE)+"Click to exit"), unused -> this.close());
    }
}