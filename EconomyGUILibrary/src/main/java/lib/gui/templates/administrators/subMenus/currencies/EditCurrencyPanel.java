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

package lib.gui.templates.administrators.subMenus.currencies;

import BlockDynasty.Economy.aplication.useCase.currency.EditCurrencyUseCase;
import BlockDynasty.Economy.domain.entities.currency.Currency;
import lib.gui.GUIFactory;
import lib.gui.components.IGUI;
import lib.gui.components.IEntityGUI;
import lib.gui.components.ITextInput;
import lib.gui.components.Materials;
import lib.gui.components.abstractions.AbstractPanel;
import lib.util.colors.ChatColor;
import lib.util.colors.Colors;
import lib.util.colors.Message;

import java.util.Map;

public class EditCurrencyPanel extends AbstractPanel {
    private final IEntityGUI player;
    private final Currency currency;
    private final EditCurrencyUseCase editCurrencyUseCase;
    private final ITextInput textInput;

    public EditCurrencyPanel(IEntityGUI player, Currency currency, EditCurrencyUseCase editCurrencyUseCase, IGUI parentGUI, ITextInput textInput) {
        super(Message.process(Map.of("currency",currency.getSingular()),"EditCurrencyPanel.title"), 5,player, parentGUI);
        this.player = player;
        this.currency = currency;
        this.textInput = textInput;
        this.editCurrencyUseCase =editCurrencyUseCase;

        setupGUI();
    }

    private void setupGUI() {
        // Current currency info
        String color = ChatColor.stringValueOf(currency.getColor());
        setItem(4, createItem(Materials.GOLD_INGOT,
                        Message.process(Map.of("currency",color + currency.getSingular() + " / " + currency.getPlural()), "EditCurrencyPanel.button1.nameItem"),
                        Message.processLines(Map.of(
                                "symbol",color + currency.getSymbol(),
                                "color",color + currency.getColor(),
                                "balance",color + currency.getDefaultBalance(),
                                "rate",color + currency.getExchangeRate(),
                                "transferable",(currency.isTransferable() ? ChatColor.stringValueOf(Colors.GREEN)+"Yes" : ChatColor.stringValueOf(Colors.RED)+"No"),
                                "default",(currency.isDefaultCurrency() ? ChatColor.stringValueOf(Colors.GREEN)+"Yes" : ChatColor.stringValueOf(Colors.RED)+"No"),
                                "decimals",(currency.isDecimalSupported() ? ChatColor.stringValueOf(Colors.GREEN)+"Yes" : ChatColor.stringValueOf(Colors.RED)+"No")),"EditCurrencyPanel.button1.lore")),
                null);

        // Edit Start Balance button
        setItem(11, createItem(Materials.EMERALD_BLOCK, Message.process(Map.of("color",ChatColor.stringValueOf(Colors.GREEN)), "EditCurrencyPanel.button2.nameItem"),
                Message.process(Map.of("color",ChatColor.stringValueOf(Colors.WHITE)), "EditCurrencyPanel.button2.lore")), f -> {openStartBalanceInput();});

        // Set Currency Rate button
        setItem(13, createItem(Materials.GOLD_NUGGET, Message.process(Map.of("color",ChatColor.stringValueOf(Colors.GREEN)), "EditCurrencyPanel.button3.nameItem"),
                Message.process(Map.of("color",ChatColor.stringValueOf(Colors.WHITE)), "EditCurrencyPanel.button3.lore")), f -> {openExchangeRateInput();});

        // Edit Color button
        setItem(15, createItem(Materials.LIME_DYE, Message.process(Map.of("color",ChatColor.stringValueOf(Colors.GREEN)), "EditCurrencyPanel.button4.nameItem"),
                Message.process(Map.of("color",ChatColor.stringValueOf(Colors.WHITE)), "EditCurrencyPanel.button4.lore")), f -> {openColorSelectionGUI();});

        // Edit Symbol button
        setItem(20, createItem(Materials.NAME_TAG, Message.process(Map.of("color",ChatColor.stringValueOf(Colors.GREEN)), "EditCurrencyPanel.button5.nameItem"),
                Message.process(Map.of("color",ChatColor.stringValueOf(Colors.WHITE)), "EditCurrencyPanel.button5.lore")), f -> {openSymbolInput();});

        // Set Default Currency button
        setItem(22, createItem(Materials.NETHER_STAR, Message.process(Map.of("color",ChatColor.stringValueOf(Colors.GREEN)), "EditCurrencyPanel.button6.nameItem"),
                Message.process(Map.of("color",ChatColor.stringValueOf(Colors.WHITE)), "EditCurrencyPanel.button6.lore")), f -> {
            try {
                editCurrencyUseCase.setDefaultCurrency(currency.getSingular());
                player.sendMessage(ChatColor.stringValueOf(Colors.GREEN)+"[Bank] "+ChatColor.stringValueOf(Colors.GRAY) + currency.getSingular() + " is now the default currency.");
                openEditCurrencyGUI();
            } catch (Exception e) {
                player.sendMessage(ChatColor.stringValueOf(Colors.GREEN)+"[Bank] "+ChatColor.stringValueOf(Colors.RED)+"Error: " + e.getMessage());
                openEditCurrencyGUI();
            }
        });

        // Toggle Payable button
        setItem(24, createItem(
                        currency.isTransferable() ? Materials.LIME_CONCRETE: Materials.RED_CONCRETE,
                        currency.isTransferable() ? "Transferable: "+ ChatColor.stringValueOf(Colors.GREEN)+"Activated" : "Transferable: "+ChatColor.stringValueOf(Colors.RED)+"Disabled",
                        ChatColor.stringValueOf(Colors.GRAY)+"Click to " + (currency.isTransferable() ? "Disable" : "Enable"),"This option affects:","Transfer","Pay","Trade/Offers"),
                f -> {
                    try {
                        editCurrencyUseCase.togglePayable(currency.getSingular());
                        player.sendMessage(ChatColor.stringValueOf(Colors.GREEN)+"[Bank] "+ChatColor.stringValueOf(Colors.GRAY)+"Transfer option changed");
                        openEditCurrencyGUI();
                    } catch (Exception e) {
                        player.sendMessage(ChatColor.stringValueOf(Colors.GREEN)+"[Bank] Error: " + e.getMessage());
                        openEditCurrencyGUI();
                    }
                });

        // Edit Singular Name button
        setItem(29, createItem(Materials.PAPER, Message.process(Map.of("color",ChatColor.stringValueOf(Colors.GREEN)), "EditCurrencyPanel.button7.nameItem"),
                Message.process(Map.of("color",ChatColor.stringValueOf(Colors.WHITE)), "EditCurrencyPanel.button7.lore")), f -> {openSingularNameInput();});

        // Edit Plural Name button
        setItem(31, createItem(Materials.BOOK, Message.process(Map.of("color",ChatColor.stringValueOf(Colors.GREEN)), "EditCurrencyPanel.button8.nameItem"),
                Message.process(Map.of("color",ChatColor.stringValueOf(Colors.WHITE)), "EditCurrencyPanel.button8.lore")), f -> {openPluralNameInput();});

        // Toggle Decimals button
        setItem(33, createItem(
                        currency.isDecimalSupported() ?Materials.LIME_CONCRETE : Materials.RED_CONCRETE,
                        currency.isDecimalSupported() ? "Decimals support: "+ ChatColor.stringValueOf(Colors.GREEN)+"Activated" : "Decimals support: "+ChatColor.stringValueOf(Colors.RED)+"Disabled",
                        "Click to " + (currency.isDecimalSupported() ? "Disable" : "Enable") + " decimal support"),
                f -> {
                    try {
                        editCurrencyUseCase.toggleDecimals(currency.getSingular());
                        player.sendMessage(ChatColor.stringValueOf(Colors.GREEN)+"[Bank] Decimal support changed.");
                        openEditCurrencyGUI();
                    } catch (Exception e) {
                        player.sendMessage(ChatColor.stringValueOf(Colors.GREEN)+"[Bank] Error: " + e.getMessage());
                        openEditCurrencyGUI();
                    }
                });

        // Back button
        setItem(40, createItem(Materials.BARRIER, ChatColor.stringValueOf(Colors.RED)+"Back",
                ChatColor.stringValueOf(Colors.GRAY)+"Click to go back"), f -> {
            GUIFactory.currencyListToEditPanel(player, this.getParent().getParent()).open();
        });
    }

    private void openEditCurrencyGUI() {
        GUIFactory.editCurrencyPanel(player, currency, this.getParent()).open();
    }

    private void openColorSelectionGUI() {
        GUIFactory.colorSelectorPanel(player,currency,this).open();
    }

    private void openStartBalanceInput(){
        textInput.open(this,player,"Initial balance:"+currency.getSingular(),currency.getDefaultBalance().toString(), s->{
            try {
                double startBal = Double.parseDouble(s);
                try {
                    editCurrencyUseCase.editStartBal(currency.getSingular(), startBal);
                    player.sendMessage(ChatColor.stringValueOf(Colors.GREEN)+"[Bank] "+ChatColor.stringValueOf(Colors.GRAY)+"Initial balance updated correctly.");
                    openEditCurrencyGUI();
                } catch (Exception e) {
                    player.sendMessage(ChatColor.stringValueOf(Colors.GREEN)+"[Bank] "+ChatColor.stringValueOf(Colors.RED)+"Error: " + e.getMessage());
                    openEditCurrencyGUI();
                }
            }catch (NumberFormatException e){
                return "Invalid format";
            }
            return null;
        });
    }

    private void openExchangeRateInput(){
        textInput.open(this,player,"Exchange rate:"+currency.getSingular(),String.valueOf(currency.getExchangeRate()),s->{
            try {
                double rate = Double.parseDouble(s);
                try {
                    editCurrencyUseCase.setCurrencyRate(currency.getSingular(), rate);
                    player.sendMessage(ChatColor.stringValueOf(Colors.GREEN)+"[Bank] "+ChatColor.stringValueOf(Colors.GRAY)+"Exchange rate updated correctly.");
                    openEditCurrencyGUI();
                } catch (Exception e) {
                    player.sendMessage(ChatColor.stringValueOf(Colors.GREEN)+"[Bank] "+ChatColor.stringValueOf(Colors.RED)+"Error: " + e.getMessage());
                    openEditCurrencyGUI();
                }
            } catch (NumberFormatException e) {
                return "Invalid format";
            }
            return null;
        });
    }

    private void openSymbolInput(){
        textInput.open(this,player,"Symbol:"+currency.getSingular(),currency.getSymbol(),s ->{
            try {
                editCurrencyUseCase.editSymbol(currency.getSingular(), s);
                player.sendMessage(ChatColor.stringValueOf(Colors.GREEN)+"[Bank] "+ChatColor.stringValueOf(Colors.GRAY)+"Symbol updated successfully.");
                openEditCurrencyGUI();
            } catch (Exception e) {
                player.sendMessage(ChatColor.stringValueOf(Colors.GREEN)+"[Bank] "+ChatColor.stringValueOf(Colors.RED)+"Error: " + e.getMessage());
                openEditCurrencyGUI();
            }
            return null;
        });
    }

    private void openSingularNameInput(){
        textInput.open(this,player,"Singular Name",currency.getSingular(), s ->{
            try {
                editCurrencyUseCase.setSingularName(currency.getSingular(), s);
                player.sendMessage(ChatColor.stringValueOf(Colors.GREEN)+"[Bank] "+ChatColor.stringValueOf(Colors.GRAY)+"Singular name updated correctly.");
                openEditCurrencyGUI();
            } catch (Exception e) {
                player.sendMessage(ChatColor.stringValueOf(Colors.GREEN)+"[Bank] "+ChatColor.stringValueOf(Colors.RED)+"Error: " + e.getMessage());
                openEditCurrencyGUI();
            }
            return null;
        });
    }

    private void openPluralNameInput() {
        textInput.open(this,player,"Plural Name", currency.getPlural(),s->{
            try {
                editCurrencyUseCase.setPluralName(currency.getSingular(), s);
                player.sendMessage(ChatColor.stringValueOf(Colors.GREEN)+"[Bank] "+ChatColor.stringValueOf(Colors.GRAY)+"Plural noun updated correctly.");
                openEditCurrencyGUI();
            } catch (Exception e) {
                player.sendMessage(ChatColor.stringValueOf(Colors.GREEN)+"[Bank] "+ChatColor.stringValueOf(Colors.RED)+"Error: " + e.getMessage());
                openEditCurrencyGUI();
            }
            return null;
        });
    }
}
