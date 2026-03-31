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

package net.blockdynasty.economy.spigot.BukkitImplementation;

import net.blockdynasty.economy.libs.services.configuration.IConfiguration;
import net.blockdynasty.economy.spigot.BukkitImplementation.Integrations.treasuryEconomy.TreasuryHook;
import net.blockdynasty.economy.spigot.BukkitImplementation.adapters.GUI.adapters.ItemStackProvider;
import net.blockdynasty.economy.spigot.BukkitImplementation.adapters.GUI.listener.ClickListener;
import net.blockdynasty.economy.spigot.BukkitImplementation.adapters.GUI.listener.CloseListener;
import net.blockdynasty.economy.spigot.BukkitImplementation.Integrations.Placeholder.PlaceHolder;
import net.blockdynasty.economy.spigot.BukkitImplementation.adapters.listeners.BlockPlaceListener;
import net.blockdynasty.economy.spigot.BukkitImplementation.adapters.platformAdapter.EntityPlayerAdapter;
import net.blockdynasty.economy.spigot.BukkitImplementation.adapters.platformAdapter.messages.MessageSenderFactory;
import net.blockdynasty.economy.spigot.BukkitImplementation.adapters.proxy.ChannelRegister;
import net.blockdynasty.economy.spigot.BukkitImplementation.Integrations.vault.Vault;

import net.blockdynasty.economy.spigot.BukkitImplementation.adapters.platformAdapter.BukkitAdapter;
import net.blockdynasty.economy.spigot.BukkitImplementation.adapters.commands.CommandRegister;
import net.blockdynasty.economy.spigot.BukkitImplementation.adapters.listeners.PlayerListener;

import net.blockdynasty.economy.spigot.BukkitImplementation.utils.Console;
import net.blockdynasty.economy.spigot.BukkitImplementation.utils.Updater;
import net.blockdynasty.economy.spigot.BukkitImplementation.utils.Version;
import net.blockdynasty.economy.engine.Economy;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bstats.bukkit.Metrics;
import org.bukkit.plugin.java.JavaPlugin;

public class BlockDynastyEconomy extends JavaPlugin {
    private Metrics metrics;
    private PlayerListener playerJoinListener;

    @Override
    public void onEnable() {
        if (Version.isUnsupportedVersion()){
            getLogger().severe("Unsupported Minecraft version detected: " + org.bukkit.Bukkit.getBukkitVersion());
            getLogger().severe("Disabling plugin to prevent issues.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        try {
            Economy.init(new BukkitAdapter(this));
            MessageSenderFactory.createMessageSender(Economy.getConfiguration());
            this.playerJoinListener = new PlayerListener(Economy.getPlayerJoinListener());
            getServer().getPluginManager().registerEvents(playerJoinListener, this);

            Vault.init(this,Economy.getConfiguration().getBoolean("vault"));
            TreasuryHook.register();
            PlaceHolder.register(this);
        } catch (Exception e) {
            getLogger().severe("An error occurred during plugin initialization: " + e.getMessage());
            getServer().getPluginManager().disablePlugin(this);
        }
        try {
            metrics= new Metrics(this, 27470);
            Updater.check(this,129308);
        }catch (Exception e) {
            Console.logError("Failed to initialize metrics or check for updates: " + e.getMessage());
        }
    }
    public void on(IConfiguration configuration){
        MessageSenderFactory.createMessageSender(configuration);
        HandlerList.unregisterAll(this.playerJoinListener);
        ItemStackProvider.init(configuration,this);
        registerCommands();
        registerEvents();
        Bukkit.getOnlinePlayers().forEach(player -> {Economy.getPlayerJoinListener().loadPlayerAccount(EntityPlayerAdapter.of(player));});
        Console.log("§aPlugin enabled successfully!");
    }

    private void registerCommands(){
        CommandRegister.registerAllEconomySystem(this);
    }
    private void registerEvents() {
        getServer().getPluginManager().registerEvents(new PlayerListener(Economy.getPlayerJoinListener()), this);
        getServer().getPluginManager().registerEvents(new ClickListener(),this);
        getServer().getPluginManager().registerEvents(new CloseListener(),this);
        getServer().getPluginManager().registerEvents(new BlockPlaceListener(),this);
    }

    public void reload() {
        Console.log("Reloading plugin...");
        HandlerList.unregisterAll(this);
        ChannelRegister.unhook(this);
        Economy.shutdown();
        try {
            Economy.init(new BukkitAdapter(this));
            MessageSenderFactory.createMessageSender(Economy.getConfiguration());
            this.playerJoinListener = new PlayerListener(Economy.getPlayerJoinListener());
            getServer().getPluginManager().registerEvents(playerJoinListener, this);
        } catch (Exception e) {
            Console.logError("during plugin initialization: " + e.getMessage());
        }
    }

    @Override
    public void onDisable() {
        Economy.shutdown();
        Vault.unhook();
        PlaceHolder.unregister();
        ChannelRegister.unhook(this);
        if (metrics != null) {
            metrics.shutdown();
        }
    }
}