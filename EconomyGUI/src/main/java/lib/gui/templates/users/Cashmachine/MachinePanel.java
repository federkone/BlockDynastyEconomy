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
package lib.gui.templates.users.Cashmachine;

import abstractions.platform.materials.Materials;
import abstractions.platform.recipes.RecipeItem;
import aplication.useCase.HardCashUseCaseFactory;
import aplication.useCase.items.deposit.IDepositItemUseCase;
import aplication.useCase.nbtItems.IDepositItemNBTUseCase;
import lib.gui.GUIFactory;
import lib.gui.components.IEntityGUI;
import lib.gui.components.IGUI;
import lib.gui.components.factory.Item;
import lib.gui.components.generics.AbstractPanel;
import lib.gui.components.generics.Button;
import services.messages.Message;
import util.colors.ChatColor;
import util.colors.Colors;

import java.util.Map;

public class MachinePanel extends AbstractPanel {
    private IDepositItemNBTUseCase depositItemNBTUseCase;
    private IDepositItemUseCase depositItemUseCase;
    private IDepositItemUseCase depositAllItemUseCase;

    public MachinePanel(IEntityGUI owner, IGUI parent) {
        super(Message.process("MachinePanel.title"), 4, owner, parent);
        this.depositItemNBTUseCase = HardCashUseCaseFactory.getDepositItemNBTUseCase();
        this.depositItemUseCase = HardCashUseCaseFactory.getDepositItemUseCase();
        this.depositAllItemUseCase = HardCashUseCaseFactory.getDepositAllItemUseCase();
        renderView();
    }

    public void renderView() {
        setButton(11,
                Button.builder()
                        .setItemStack(Item.of(RecipeItem.builder()
                                .setMaterial(Materials.BOOK)
                                .setName("Deposit Note")
                                .setLore("Deposit a note to convert it into balance.")
                                //.setName(Message.process(Map.of("color", ChatColor.stringValueOf(Colors.GOLD)), "MachinePanel.button1.nameItem"))
                                //.setLore(Message.process(Map.of("color", ChatColor.stringValueOf(Colors.WHITE)), "MachinePanel.button1.lore"))
                                .build()))
                        .setLeftClickAction(event -> {
                            if (event.hasPermission("BlockDynastyEconomy.players.depositCash")) {
                                this.depositItemNBTUseCase.execute(owner.asEntityHardCash());
                            } else {
                                event.sendMessage(ChatColor.stringValueOf(Colors.RED) + "You don't have permission");
                            }
                        })
                        .build()
        );

        setButton(15,
                Button.builder()
                        .setItemStack(Item.of(RecipeItem.builder()
                                .setMaterial(Materials.EMERALD)
                                .setName("Deposit Currency Item")
                                .setLore("Deposit your currency items to convert them into wallet balance.")
                                .build()))
                        .setLeftClickAction(event -> {
                            if (event.hasPermission("BlockDynastyEconomy.players.depositCash")) {
                                this.depositItemUseCase.execute(owner.asEntityHardCash());
                            } else {
                                event.sendMessage(ChatColor.stringValueOf(Colors.RED) + "You don't have permission");
                            }
                        })
                        .build()
        );

        setButton(16,
                Button.builder()
                        .setItemStack(Item.of(RecipeItem.builder()
                                .setMaterial(Materials.EMERALD)
                                .setName("Deposit All Currency Item in your inventory")
                                .setLore("Deposit your currency items to convert them into wallet balance.")
                                .build()))
                        .setLeftClickAction(event -> {
                            if (event.hasPermission("BlockDynastyEconomy.players.depositCash")) {
                                this.depositAllItemUseCase.execute(owner.asEntityHardCash());
                            } else {
                                event.sendMessage(ChatColor.stringValueOf(Colors.RED) + "You don't have permission");
                            }
                        })
                        .build()
        );

        setButton(24, Button.builder()
                .setItemStack(Item.of(RecipeItem.builder()
                        .setName("Extract Currency Item")
                        .setLore("Extract your balance into currency items.")
                        .setMaterial(Materials.DIAMOND)
                        .build()))
                .setLeftClickAction(event -> {
                    if (event.hasPermission("BlockDynastyEconomy.players.extractCash")) {
                        GUIFactory.extractorItemPanel(event, this).open();
                    } else {
                        event.sendMessage(ChatColor.stringValueOf(Colors.RED) + "You don't have permission");
                    }
                })
                .build());

        setButton(20, Button.builder()
                .setItemStack(Item.of(RecipeItem.builder()
                        .setName("Write Note")
                        .setLore("Write a note to convert your balance in note.")
                        //.setName(Message.process(Map.of("color", ChatColor.stringValueOf(Colors.GOLD)), "MachinePanel.button2.nameItem"))
                        //.setLore(Message.process(Map.of("color", ChatColor.stringValueOf(Colors.WHITE)), "MachinePanel.button2.lore"))
                        .setMaterial(Materials.WRITABLE_BOOK)
                        .build()))
                .setLeftClickAction(event -> {
                    if (event.hasPermission("BlockDynastyEconomy.players.extractCash")) {
                        GUIFactory.extractorNBTPanel(event, this).open();
                    } else {
                        event.sendMessage(ChatColor.stringValueOf(Colors.RED) + "You don't have permission");
                    }
                })
                .build());

        setButton(31, Button.builder()
                .setItemStack(Item.of(RecipeItem.builder()
                        .setMaterial(Materials.BARRIER)
                        .setName(Message.process(Map.of("color", ChatColor.stringValueOf(Colors.RED)), "Paginated.button4.nameItem"))
                        .setLore(Message.process(Map.of("color", ChatColor.stringValueOf(Colors.WHITE)), "Paginated.button4.lore"))
                        .build())
                )
                .setLeftClickAction(unused -> this.openParent())
                .build());

    }
}
