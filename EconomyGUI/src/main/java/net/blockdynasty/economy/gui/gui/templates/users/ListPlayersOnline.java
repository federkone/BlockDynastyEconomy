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

package net.blockdynasty.economy.gui.gui.templates.users;

import net.blockdynasty.economy.core.domain.entities.account.Player;
import net.blockdynasty.economy.libs.abstractions.platform.entity.IPlayer;
import net.blockdynasty.economy.gui.gui.components.PlatformGUI;
import net.blockdynasty.economy.gui.gui.GUIFactory;
import net.blockdynasty.economy.gui.gui.components.IGUI;
import net.blockdynasty.economy.gui.gui.components.IEntityGUI;
import net.blockdynasty.economy.gui.gui.components.ITextInput;
import net.blockdynasty.economy.gui.gui.components.factory.Item;
import net.blockdynasty.economy.gui.gui.components.generics.Button;
import net.blockdynasty.economy.libs.abstractions.platform.recipes.RecipeItem;
import net.blockdynasty.economy.libs.abstractions.platform.materials.Materials;
import net.blockdynasty.economy.gui.gui.components.generics.AccountsList;
import net.blockdynasty.economy.libs.util.colors.ChatColor;
import net.blockdynasty.economy.libs.util.colors.Colors;
import net.blockdynasty.economy.libs.services.messages.Message;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ListPlayersOnline extends AccountsList {
    private final IEntityGUI sender;
    private final PlatformGUI platformAdapter;

    public ListPlayersOnline(IEntityGUI sender, IGUI parent , ITextInput textInput, PlatformGUI platformAdapter) {
        super(Message.process("listPlayersOnline.title"), 5,sender,parent, textInput );
        this.sender = sender;
        this.platformAdapter = platformAdapter;
        List<Player> players = platformAdapter.getOnlinePlayers().stream()
                .map(p -> new Player(p.getUniqueId(), p.getName()))
                .sorted((a, b) -> a.getNickname().compareToIgnoreCase(b.getNickname()))
                .filter(player -> !player.getUuid().equals(sender.getUniqueId()))
                .collect(Collectors.toList());

        showPlayers(players);
    }

    @Override
    public Player findPlayerByName(String playerName) {
        IEntityGUI player;
        IPlayer target = platformAdapter.getOnlinePlayers().stream()
                .filter(p -> p.getName().equalsIgnoreCase(playerName))
                .findFirst().orElse(null); //online players only
        if (target != null) {
            return new Player(target.getUniqueId(), target.getName());
        } else {
            return null;
        }
    }

    @Override
    public void openNextSection(Player target) {
        GUIFactory.walletSelector(sender,target,this.getParent()).open();
    }
    @Override
    public void addCustomButtons(){
        super.addCustomButtons(); // Call the parent method to add the default buttons accountList
        setButton(4, Button.builder()
                .setItemStack(Item.of( RecipeItem.builder()
                        .setMaterial(Materials.PAPER)
                        .setName(Message.process(Map.of("color",ChatColor.stringValueOf(Colors.GREEN)), "listPlayersOnline.button1.nameItem"))
                        .setLore(Message.processLines(Map.of("color",ChatColor.stringValueOf(Colors.WHITE)), "listPlayersOnline.button1.lore"))
                        .build()))
                .build());
    }
}