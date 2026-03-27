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

package BlockDynasty.BukkitImplementation;

import BlockDynasty.BukkitImplementation.Integrations.treasuryEconomy.TreasuryHook;
import BlockDynasty.BukkitImplementation.adapters.GUI.adapters.ItemStackProvider;
import BlockDynasty.BukkitImplementation.adapters.GUI.listener.ClickListener;
import BlockDynasty.BukkitImplementation.adapters.GUI.listener.CloseListener;
import BlockDynasty.BukkitImplementation.Integrations.Placeholder.PlaceHolder;
import BlockDynasty.BukkitImplementation.adapters.listeners.BlockPlaceListener;
import BlockDynasty.BukkitImplementation.adapters.platformAdapter.EntityPlayerAdapter;
import BlockDynasty.BukkitImplementation.adapters.platformAdapter.messages.MessageSenderFactory;
import BlockDynasty.BukkitImplementation.adapters.proxy.ChannelRegister;
import BlockDynasty.BukkitImplementation.Integrations.vault.Vault;

import BlockDynasty.BukkitImplementation.adapters.platformAdapter.BukkitAdapter;
import BlockDynasty.BukkitImplementation.adapters.commands.CommandRegister;
import BlockDynasty.BukkitImplementation.adapters.listeners.PlayerListener;

import BlockDynasty.BukkitImplementation.utils.Console;
import BlockDynasty.BukkitImplementation.utils.Updater;
import BlockDynasty.BukkitImplementation.utils.Version;
import com.blockdynasty.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bstats.bukkit.Metrics;
import org.bukkit.plugin.java.JavaPlugin;
import services.configuration.IConfiguration;

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