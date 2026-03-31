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

package EngineTest.mocks;

import EngineTest.mocks.utils.Color;
import net.blockdynasty.economy.hardcash.domain.entity.currency.ItemStackCurrency;
import net.blockdynasty.economy.hardcash.domain.entity.player.IEntityHardCash;
import net.blockdynasty.economy.gui.commands.abstractions.IEntityCommands;
import net.blockdynasty.economy.gui.gui.components.IEntityGUI;
import net.blockdynasty.economy.gui.gui.components.IInventory;
import net.blockdynasty.economy.engine.platform.IPlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Player implements IPlayer {
    private List<String> permissions;
    private UUID uuid;
    private String name;
    private boolean op = false;

    public Player(UUID uuid, String name) {
        this.permissions = new ArrayList<>();
        this.uuid = uuid;
        this.name = name;
    }

    @Override
    public void playNotificationSound() {

    }

    @Override
    public boolean isOnline() {
       return MinecraftServer.isOnline(this);
    }

    @Override
    public boolean hasPermission(String permission) {
        if(op){
            return true;
        }else {
            return this.permissions.contains(permission);
        }
    }

    @Override
    public void kickPlayer(String message) {

    }

    @Override
    public IEntityGUI asEntityGUI() {
        return this;
    }

    @Override
    public UUID getUniqueId() {
        return this.uuid;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void sendMessage(String message) {
        System.out.println(this.name+" "+"received message: "+ Color.parse(message));
    }

    @Override
    public Object getRoot() {
        return this;
    }

    @Override
    public void playSuccessSound() {

    }

    @Override
    public void playFailureSound() {

    }

    @Override
    public void closeInventory() {

    }

    @Override
    public void openInventory(IInventory inventory) {
        //System.out.println("Inventory "+inventory.getTitle()+" opened for player "+this.getName());
        System.out.println(inventory);
    }

    @Override
    public IEntityCommands asEntityCommands() {
        return this;
    }

    @Override
    public IEntityHardCash asEntityHardCash() {
        return null;
    }

    public void addPermission(String permission){
        this.permissions.add(permission);
    }
    public void removePermission(String permission){this.permissions.remove(permission);}

    public void setOp(boolean op){
        this.op = op;
    }

    @Override
    public void giveItem(ItemStackCurrency item) {

    }

    @Override
    public ItemStackCurrency takeHandItem() {
        return null;
    }

    @Override
    public boolean hasItem(ItemStackCurrency itemCurrency) {
        return false;
    }

    @Override
    public boolean hasEmptySlot() {
        return false;
    }

    @Override
    public int emptySlots() {
        return 0;
    }

    @Override
    public void removeItem(ItemStackCurrency itemCurrency) {

    }

    @Override
    public int takeAllItems(ItemStackCurrency itemCurrency) {
        return 0;
    }

    @Override
    public boolean takeItems(ItemStackCurrency itemCurrency, int amount) {
        return false;
    }

    @Override
    public int countItems(ItemStackCurrency itemCurrency) {
        return 0;
    }
}
