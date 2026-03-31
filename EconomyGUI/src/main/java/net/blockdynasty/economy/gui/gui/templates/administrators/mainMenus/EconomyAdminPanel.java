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

public class EconomyAdminPanel extends AbstractPanel {
    private final IEntityGUI sender;

    public EconomyAdminPanel(IEntityGUI sender) {
        super(Message.process("EconomyAdminPanel.title"), 5,sender);
        this.sender = sender;

        initializeButtons();
    }

    private void initializeButtons() {
        setButton(20, Button.builder()
                .setItemStack(Item.of(RecipeItem.builder()
                        .setMaterial(Materials.EMERALD)
                        .setName(Message.process(Map.of("color", ChatColor.stringValueOf(Colors.GREEN)),"EconomyAdminPanel.button1.nameItem"))
                        .setLore(Message.process(Map.of("color", ChatColor.stringValueOf(Colors.WHITE)),"EconomyAdminPanel.button1.lore"))
                        .build()))
                .setLeftClickAction(event -> {GUIFactory.currencyPanel( sender, this).open();})
                .build());

        setButton(22,Button.builder()
                .setItemStack(Item.of(RecipeItem.builder().setName(ChatColor.stringValueOf(Colors.GREEN)+"Edit Bank GUI")
                                .setLore(ChatColor.stringValueOf(Colors.WHITE)+"Features can be enabled or disabled")
                                .setMaterial(Materials.BOOK)
                        .build()))
                .setLeftClickAction(f->{GUIFactory.bankPanelEditor(sender,this).open();})
                .build());

        setButton(24, Button.builder()
                .setItemStack(Item.of(RecipeItem.builder()
                        .setMaterial(Materials.PLAYER_HEAD)
                        .setName(Message.process(Map.of("color", ChatColor.stringValueOf(Colors.GREEN)),"EconomyAdminPanel.button2.nameItem"))
                        .setLore(Message.process(Map.of("color", ChatColor.stringValueOf(Colors.WHITE)),"EconomyAdminPanel.button2.lore"))
                        .build()))
                .setLeftClickAction(event -> {GUIFactory.accountSelectorToEdit( sender, this).open();})
                .build());

        setButton(40, Button.builder()
                .setItemStack(Item.of(RecipeItem.builder()
                        .setMaterial(Materials.BARRIER)
                        .setName(Message.process(Map.of("color", ChatColor.stringValueOf(Colors.RED)),"EconomyAdminPanel.button3.nameItem"))
                        .setLore(Message.process(Map.of("color", ChatColor.stringValueOf(Colors.WHITE)),"EconomyAdminPanel.button3.lore"))
                        .build()))
                .setLeftClickAction(event -> {this.close();})
                .build());
    }
}