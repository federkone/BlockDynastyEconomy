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

import net.blockdynasty.economy.gui.gui.GUIFactory;
import net.blockdynasty.economy.gui.gui.components.IGUI;
import net.blockdynasty.economy.gui.gui.components.IEntityGUI;
import net.blockdynasty.economy.gui.gui.components.factory.Item;
import net.blockdynasty.economy.gui.gui.components.generics.Button;
import net.blockdynasty.economy.libs.abstractions.platform.recipes.RecipeItem;
import net.blockdynasty.economy.libs.abstractions.platform.materials.Materials;
import net.blockdynasty.economy.gui.gui.components.generics.AbstractPanel;
import net.blockdynasty.economy.libs.util.colors.ChatColor;
import net.blockdynasty.economy.libs.util.colors.Colors;
import net.blockdynasty.economy.libs.services.messages.Message;

import java.util.Map;

public class CurrencyAdminPanel extends AbstractPanel {
    private final IEntityGUI player;

    public CurrencyAdminPanel(IEntityGUI player, IGUI parent) {
        super(Message.process("CurrencyAdminPanel.title"), 3,player,parent);
        this.player = player;
        setupGUI();
    }

    private void setupGUI() {
        // Create Currency button
        setButton(10, Button.builder()
                .setItemStack(Item.of(RecipeItem.builder()
                                .setMaterial(Materials.EMERALD)
                                .setName(Message.process(Map.of("color", ChatColor.stringValueOf(Colors.GREEN)),"CurrencyAdminPanel.button1.nameItem"))
                                .setLore(Message.process(Map.of("color", ChatColor.stringValueOf(Colors.WHITE)),"CurrencyAdminPanel.button1.lore"))
                                .build()))
                .setLeftClickAction( unused -> {GUIFactory.createCurrencyPanel(player,this);})
                .build());

        // Delete Currency button
        setButton(12, Button.builder()
                .setItemStack(Item.of(RecipeItem.builder()
                                .setMaterial(Materials.REDSTONE)
                                .setName(Message.process(Map.of("color", ChatColor.stringValueOf(Colors.RED)),"CurrencyAdminPanel.button2.nameItem"))
                                .setLore(Message.process(Map.of("color", ChatColor.stringValueOf(Colors.WHITE)),"CurrencyAdminPanel.button2.lore"))
                                .build()))
                .setLeftClickAction(unused -> {GUIFactory.currencyListToDeletePanel(player, this).open();})
                .build());

        // Edit Currency button
        setButton(14, Button.builder()
                .setItemStack(Item.of(RecipeItem.builder()
                        .setMaterial(Materials.BOOK)
                        .setName(Message.process(Map.of("color", ChatColor.stringValueOf(Colors.YELLOW)),"CurrencyAdminPanel.button3.nameItem"))
                        .setLore(Message.process(Map.of("color", ChatColor.stringValueOf(Colors.WHITE)),"CurrencyAdminPanel.button3.lore"))
                        .build()))
                .setLeftClickAction( unused -> {GUIFactory.currencyListToEditPanel(player, this).open();})
                .build());

        // Toggle Features button
        setButton(16, Button.builder()
                .setItemStack(Item.of(RecipeItem.builder()
                    .setMaterial(Materials.PAPER)
                    .setName(Message.process(Map.of("color", ChatColor.stringValueOf(Colors.GREEN)),"CurrencyAdminPanel.button4.nameItem"))
                    .setLore(Message.process(Map.of("color", ChatColor.stringValueOf(Colors.WHITE)),"CurrencyAdminPanel.button4.lore"))
                    .build()))
                .setLeftClickAction( unused -> {player.sendMessage("[Bank] This feature is not implemented yet.");})
                .build());

        // Exit button
        setButton(22, Button.builder()
                .setItemStack(Item.of(RecipeItem.builder()
                        .setMaterial(Materials.BARRIER)
                        .setName(Message.process(Map.of("color", ChatColor.stringValueOf(Colors.RED)),"CurrencyAdminPanel.button5.nameItem"))
                        .setLore(Message.process(Map.of("color", ChatColor.stringValueOf(Colors.WHITE)),"CurrencyAdminPanel.button5.lore"))
                        .build()))
                .setLeftClickAction( unused -> {this.openParent();})
                .build());
    }

}