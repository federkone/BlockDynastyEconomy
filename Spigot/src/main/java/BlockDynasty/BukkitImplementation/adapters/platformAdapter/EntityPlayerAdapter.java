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

package BlockDynasty.BukkitImplementation.adapters.platformAdapter;

import BlockDynasty.BukkitImplementation.BlockDynastyEconomy;
import BlockDynasty.BukkitImplementation.adapters.GUI.adapters.MaterialAdapter;
import BlockDynasty.BukkitImplementation.utils.Version;
import lib.commands.abstractions.IEntityCommands;
import lib.gui.components.IEntityGUI;
import lib.abstractions.IPlayer;
import lib.gui.components.IInventory;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.UUID;

public class EntityPlayerAdapter implements IPlayer {
    private Player player;

    private EntityPlayerAdapter(Player player) {
        this.player = player;
    }

    public static EntityPlayerAdapter of(Player player) {
        return new EntityPlayerAdapter(player);
    }

    @Override
    public String getName() {
        return player.getName();
    }

    @Override
    public boolean isOnline() {
        return player.isOnline();
    }

    @Override
    public UUID getUniqueId() {
        return player.getUniqueId();
    }

    @Override
    public void sendMessage(String message) {
        if (!Version.hasSupportAdventureText() || BlockDynastyEconomy.getConfiguration().getBoolean("forceVanillaColorsSystem")){
            message = translateColorCodes(message);
            player.sendMessage(message);
        }else {
            Component textonuevo = MiniMessage.miniMessage().deserialize(message);
            player.sendMessage(textonuevo);
        }
    }

    private String translateColorCodes(String message) {
        return message.replaceAll("&([0-9a-fk-or])", "§$1");
    }

    @Override
    public boolean hasPermission(String permission) {
        return player.hasPermission(permission);
    }

    @Override
    public void kickPlayer(String message) {
        player.kickPlayer(message);
    }

    @Override
    public void closeInventory() {
        player.closeInventory();
    }

    @Override
    public void openInventory(IInventory inventory) {
        Inventory inventoryBukkit= (Inventory) inventory.getHandle();
        player.openInventory(inventoryBukkit);
    }

    @Override
    public void playNotificationSound() {
        player.playSound(player.getLocation(), MaterialAdapter.getPickupSound(), 1.0f, 1.0f);
    }

    @Override
    public void playSuccessSound() {
        player.playSound(player.getLocation(),  MaterialAdapter.getClickSound(), 0.3f, 1.0f);
    }

    @Override
    public void playFailureSound() {
        player.playSound(player.getLocation(), "block.note_block.pling", 0.3f, 0.5f);
    }

    @Override
    public Object getRoot() {
        return player;
    }

    @Override
    public IEntityGUI asEntityGUI() {
        return this;
    }

    @Override
    public IEntityCommands asEntityCommands() {
        return this;
    }

}
