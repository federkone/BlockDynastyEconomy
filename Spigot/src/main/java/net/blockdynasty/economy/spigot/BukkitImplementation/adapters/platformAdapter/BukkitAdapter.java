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

package net.blockdynasty.economy.spigot.BukkitImplementation.adapters.platformAdapter;

import net.blockdynasty.economy.gui.gui.components.IInventory;
import net.blockdynasty.economy.gui.gui.components.IItemStack;
import net.blockdynasty.economy.gui.gui.components.ITextInput;
import net.blockdynasty.economy.hardcash.domain.entity.currency.ItemStackCurrency;
import net.blockdynasty.economy.hardcash.domain.entity.currency.RecipeItemCurrency;
import net.blockdynasty.economy.hardcash.domain.entity.platform.HardCashCreator;
import net.blockdynasty.economy.libs.abstractions.platform.IConsole;
import net.blockdynasty.economy.libs.abstractions.platform.IProxySubscriber;
import net.blockdynasty.economy.libs.abstractions.platform.recipes.RecipeInventory;
import net.blockdynasty.economy.libs.abstractions.platform.recipes.RecipeItem;
import net.blockdynasty.economy.libs.abstractions.platform.scheduler.ContextualTask;
import net.blockdynasty.economy.libs.abstractions.platform.scheduler.IScheduler;
import net.blockdynasty.economy.libs.services.configuration.IConfiguration;
import net.blockdynasty.economy.spigot.BukkitImplementation.BlockDynastyEconomy;
import net.blockdynasty.economy.spigot.BukkitImplementation.adapters.GUI.adapters.InventoryAdapter;
import net.blockdynasty.economy.spigot.BukkitImplementation.adapters.GUI.adapters.ItemStackAdapter;
import net.blockdynasty.economy.spigot.BukkitImplementation.adapters.GUI.adapters.ItemStackProvider;
import net.blockdynasty.economy.spigot.BukkitImplementation.adapters.GUI.adapters.textInput.TextInputFactory;
import net.blockdynasty.economy.spigot.BukkitImplementation.adapters.proxy.ProxySubscriberImp;
import net.blockdynasty.economy.spigot.BukkitImplementation.scheduler.Scheduler;
import net.blockdynasty.economy.spigot.BukkitImplementation.scheduler.SchedulerFactory;
import net.blockdynasty.economy.spigot.BukkitImplementation.utils.Version;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import net.blockdynasty.economy.engine.MessageChannel.proxy.ProxyData;
import net.blockdynasty.economy.engine.platform.IPlatform;
import net.blockdynasty.economy.engine.platform.IPlayer;

import java.io.File;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class BukkitAdapter implements IPlatform {
    private final BlockDynastyEconomy plugin;
    private final Server server;

    public BukkitAdapter(BlockDynastyEconomy plugin) {
        this.plugin = plugin;
        this.server = plugin.getServer();
        SchedulerFactory.init(plugin);
    }

    @Override
    public IPlayer getPlayer(String name) {
        Player player = server.getPlayer(name);
        if (player == null) {
            return null;
        }
        return EntityPlayerAdapter.of(player);
    }

    @Override
    public void dispatchCommand(String command) {
        //Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
        Scheduler.run(ContextualTask.build(()->this.server.dispatchCommand(Bukkit.getConsoleSender(), command)));
    }

    @Override
    public void sendPluginMessage(String channel, byte[] message) {
        server.sendPluginMessage(plugin, channel, message);
    }

    @Override
    public void registerMessageChannel(IProxySubscriber proxySubscriber) {
        server.getMessenger().registerOutgoingPluginChannel(plugin,ProxyData.getChannelName());
        server.getMessenger().registerIncomingPluginChannel(plugin,ProxyData.getChannelName(), new ProxySubscriberImp(proxySubscriber));
    }

    @Override
    public IScheduler getScheduler() {
        return SchedulerFactory.getScheduler();
    }

    @Override
    public IConsole getConsole() {
        return new ConsoleAdapter();
    }

    @Override
    public File getDataFolder() {
        return plugin.getDataFolder();
    }

    @Override
    public boolean isLegacy() {
        return Version.isLegacy();
    }

    @Override
    public boolean isOnlineMode() {
        return server.getOnlineMode();
    }

    @Override
    public boolean hasSupportAdventureText() {
        return Version.hasSupportAdventureText();
    }

    @Override
    public IItemStack createItemStack(RecipeItem recipeItem) {
        ItemStack itemStack = ItemStackProvider.createItemStack(recipeItem);
        return new ItemStackAdapter(itemStack);
    }

    @Override
    public ItemStackCurrency createItemStackNBT(RecipeItemCurrency recipe) {
        ItemStack itemStack = ItemStackProvider.createItemStackNBT(recipe);
        return new ItemStackAdapter(itemStack);
    }

    @Override
    public ItemStackCurrency createItemBase64(RecipeItemCurrency recipeItemCurrency) {
        ItemStack itemStack = ItemStackProvider.creteItemStackBase64(recipeItemCurrency);
        return new ItemStackAdapter(itemStack);
    }


    @Override
    public boolean hasSupportHardCash() {
        return Version.hasSupportHardCash();
    }

    @Override
    public IInventory createInventory(RecipeInventory recipeInventory) {
        Inventory inventory = server.createInventory(null, recipeInventory.getRows() * 9, recipeInventory.getTitle());
        return new InventoryAdapter(inventory);
    }

    @Override
    public IPlayer getPlayerByUUID(UUID uuid) {
        Player player = server.getPlayer(uuid);
        if (player == null) {
            return null;
        }
        return EntityPlayerAdapter.of(player);
    }

    @Override
    public List<net.blockdynasty.economy.libs.abstractions.platform.entity.IPlayer> getOnlinePlayers() {
        return server.getOnlinePlayers().stream().map(EntityPlayerAdapter::of).collect(Collectors.toList());
    }

    @Override
    public ITextInput getTextInput() {
        return TextInputFactory.getTextInput(plugin);
    }

    @Override
    public HardCashCreator asPlatformHardCash() {
        return this;
    }

    @Override
    public boolean hasSupportGui() {
        return true;
    }

    @Override
    public void startServer(IConfiguration configuration) {
        plugin.on(configuration);
    }

    @Override
    public void reloadServer() {
        plugin.reload();
    }
}
