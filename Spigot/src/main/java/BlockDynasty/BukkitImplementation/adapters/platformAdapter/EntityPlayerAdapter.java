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
import BlockDynasty.BukkitImplementation.adapters.GUI.adapters.Materials.MaterialProvider;
import BlockDynasty.BukkitImplementation.utils.Version;
import domain.entity.currency.ItemStackCurrency;
import domain.entity.player.IEntityHardCash;
import lib.commands.abstractions.IEntityCommands;
import lib.gui.components.IEntityGUI;
import lib.gui.components.IInventory;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.block.ShulkerBox;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import com.blockdynasty.economy.platform.IPlayer;
import org.bukkit.inventory.meta.BlockStateMeta;

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
        player.playSound(player.getLocation(), MaterialProvider.getPickupSound(), 1.0f, 1.0f);
    }

    @Override
    public void playSuccessSound() {
        player.playSound(player.getLocation(),  MaterialProvider.getClickSound(), 0.3f, 1.0f);
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

    @Override
    public IEntityHardCash asEntityHardCash() {
        return this;
    }

    @Override
    public void giveItem(ItemStackCurrency item) {
        this.player.getInventory().addItem((ItemStack) item.getRoot());
    }

    @Override
    public ItemStackCurrency takeHandItem() {
        ItemStack itemStack = player.getItemInHand();
        return new ItemStackCurrencyAdapter(itemStack);
    }

    @Override
    public boolean hasItem(ItemStackCurrency itemCurrency) {
        for (ItemStack itemStack : player.getInventory().getContents()) {
            if (itemStack != null && itemStack.isSimilar((ItemStack) itemCurrency.getRoot())) {
                if (itemStack.getAmount() < 64) {
                    return true;
                }
            }
        }
        return false;
    }

    public int emptySlots() {
        int emptySlots = 0;
        ItemStack[] contents = player.getInventory().getContents();

        for (int i = 0; i < 36; i++) {
            ItemStack itemStack = contents[i];

            if (itemStack == null || itemStack.getType() == Material.AIR) {
                emptySlots++;
            }
        }
        return emptySlots;
    }

    @Override
    public int takeAllItems(ItemStackCurrency targetItem) {
        int totalExtracted = 0;
        ItemStack itemStack = (ItemStack) targetItem.getRoot();
        Inventory playerInventory = player.getInventory();
        for (int i = 0; i < playerInventory.getSize(); i++) {
            ItemStack currentItem = playerInventory.getItem(i);

            if (currentItem == null || currentItem.getType() == Material.AIR) {
                continue;
            }

            if (currentItem.isSimilar(itemStack)) {
                totalExtracted += currentItem.getAmount();
                playerInventory.setItem(i, null);
                continue;
            }

            if (currentItem.getItemMeta() instanceof BlockStateMeta) {
                BlockStateMeta meta = (BlockStateMeta) currentItem.getItemMeta();

                if (meta.getBlockState() instanceof ShulkerBox) {
                    ShulkerBox shulker = (ShulkerBox) meta.getBlockState();
                    Inventory shulkerInventory = shulker.getInventory();
                    boolean modified = false;
                    for (int j = 0; j < shulkerInventory.getSize(); j++) {
                        ItemStack shulkerSlotItem = shulkerInventory.getItem(j);

                        if (shulkerSlotItem != null && shulkerSlotItem.isSimilar(itemStack)) {
                            totalExtracted += shulkerSlotItem.getAmount();
                            shulkerInventory.setItem(j, null); // Vaciamos el slot interno
                            modified = true;
                        }
                    }
                    if (modified) {
                        meta.setBlockState(shulker);
                        currentItem.setItemMeta(meta);
                    }
                }
            }
        }
        return totalExtracted;
    }

    @Override
    public int countItems(ItemStackCurrency targetItem) {
        int total = 0;
        Inventory playerInventory = player.getInventory();
        ItemStack targetItemStack = (ItemStack) targetItem.getRoot();

        for (ItemStack currentItem : playerInventory.getContents()) {
            if (currentItem == null || currentItem.getType() == Material.AIR) {
                continue;
            }

            if (currentItem.isSimilar(targetItemStack)) {
                total += currentItem.getAmount();
            }
            else if (currentItem.getItemMeta() instanceof BlockStateMeta) {
                BlockStateMeta meta = (BlockStateMeta) currentItem.getItemMeta();

                if (meta.getBlockState() instanceof ShulkerBox) {
                    ShulkerBox shulker = (ShulkerBox) meta.getBlockState();
                    for (ItemStack shulkerItem : shulker.getInventory().getContents()) {
                        if (shulkerItem != null && shulkerItem.isSimilar(targetItemStack)) {
                            total += shulkerItem.getAmount();
                        }
                    }
                }
            }
        }

        return total;
    }

    @Override
    public boolean takeItems(ItemStackCurrency itemCurrency, int amount){
        if (countItems(itemCurrency) < amount) {
            return false;
        }

        int remainingToExtract = amount;
        Inventory playerInventory = player.getInventory();
        ItemStack targetItem = (ItemStack) itemCurrency.getRoot();

        for (int i = 0; i < playerInventory.getSize(); i++) {
            if (remainingToExtract <= 0) break;

            ItemStack currentItem = playerInventory.getItem(i);
            if (currentItem == null || currentItem.getType() == Material.AIR) continue;

            if (currentItem.isSimilar(targetItem)) {
                int stackAmount = currentItem.getAmount();

                if (stackAmount <= remainingToExtract) {
                    remainingToExtract -= stackAmount;
                    playerInventory.setItem(i, null);
                } else {
                    currentItem.setAmount(stackAmount - remainingToExtract);
                    remainingToExtract = 0;
                }
                continue;
            }

            if (currentItem.getItemMeta() instanceof BlockStateMeta) {
                BlockStateMeta meta = (BlockStateMeta) currentItem.getItemMeta();

                if (meta.getBlockState() instanceof ShulkerBox) {
                    ShulkerBox shulker = (ShulkerBox) meta.getBlockState();
                    Inventory shulkerInventory = shulker.getInventory();
                    boolean modified = false;

                    for (int j = 0; j < shulkerInventory.getSize(); j++) {
                        if (remainingToExtract <= 0) break;

                        ItemStack shulkerSlotItem = shulkerInventory.getItem(j);
                        if (shulkerSlotItem != null && shulkerSlotItem.isSimilar(targetItem)) {
                            int shulkerStackAmount = shulkerSlotItem.getAmount();

                            if (shulkerStackAmount <= remainingToExtract) {
                                remainingToExtract -= shulkerStackAmount;
                                shulkerInventory.setItem(j, null);
                            } else {
                                shulkerSlotItem.setAmount(shulkerStackAmount - remainingToExtract);
                                remainingToExtract = 0;
                            }
                            modified = true;
                        }
                    }

                    if (modified) {
                        meta.setBlockState(shulker);
                        currentItem.setItemMeta(meta);
                    }
                }
            }
        }

        return true;
    }

    @Override
    public boolean hasEmptySlot() {
        return player.getInventory().firstEmpty() != -1;
    }

    @Override
    public void removeItem(ItemStackCurrency itemCurrency) {
        player.getInventory().removeItem((ItemStack) itemCurrency.getRoot());
    }
}
