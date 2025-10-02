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

import BlockDynasty.BukkitImplementation.adapters.ConsoleAdapter;
import BlockDynasty.BukkitImplementation.adapters.GUI.adapters.TextInput;
import BlockDynasty.BukkitImplementation.adapters.GUI.listener.ClickListener;
import BlockDynasty.BukkitImplementation.adapters.GUI.listener.CloseListener;
import BlockDynasty.BukkitImplementation.Integrations.Placeholder.PlaceHolder;
import BlockDynasty.BukkitImplementation.adapters.proxy.ChannelRegister;
import BlockDynasty.BukkitImplementation.Integrations.vault.Vault;

import BlockDynasty.BukkitImplementation.adapters.abstractions.BukkitAdapter;
import BlockDynasty.BukkitImplementation.adapters.commands.CommandRegister;
import BlockDynasty.BukkitImplementation.adapters.listeners.PlayerJoinListenerOffline;
import BlockDynasty.BukkitImplementation.adapters.listeners.PlayerJoinListenerOnline;

import BlockDynasty.BukkitImplementation.utils.Console;


import Main.Economy;
import files.Configuration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class BlockDynastyEconomy extends JavaPlugin {
    private static BlockDynastyEconomy instance;
    private final Economy economy = new Economy();
    private static Configuration configuration;

    @Override
    public void onLoad() {

    }

    @Override
    public void onEnable() {
        instance = this;
        try {
            initCoreServices();
            registerCommands();
            registerEvents();
            setupIntegrations();
            Console.log("§aPlugin enabled successfully!");
        } catch (Exception e) {
            Console.logError("An error occurred during plugin initialization: " + e.getMessage());
            getServer().getPluginManager().disablePlugin(this);
        }
    }

    @Override
    public void onDisable() {
        economy.shutdown();
        Vault.unhook();
        PlaceHolder.unregister();
        ChannelRegister.unhook(this);
    }

    private void initCoreServices() {
        //int expireCacheTopMinutes = getConfig().getInt("expireCacheTopMinutes", 60);
        Console.setConsole(new ConsoleAdapter());
        economy.init(new TextInput(),new ConsoleAdapter(),new BukkitAdapter());
        configuration = economy.getConfiguration();
    }
    private void registerCommands(){
        CommandRegister.registerAll();
    }

    private void registerEvents() {
        Listener economyListener=new PlayerJoinListenerOnline(economy.getPlayerJoinListener());
        if(configuration.getBoolean("online")) {
            if(!getServer().getOnlineMode()){
                Console.logError("THE SERVER IS IN OFFLINE MODE but the plugin is configured to work in ONLINE mode, please change the configuration to avoid issues.");
            }
            economyListener = new PlayerJoinListenerOnline(economy.getPlayerJoinListener());
            Console.log("Online mode is enabled. The plugin will use UUID to identify players.");
        }
        if (!configuration.getBoolean("online")){
            if(getServer().getOnlineMode()){
                Console.logError("THE SERVER IS IN ONLINE MODE but the plugin is configured to work in OFFLINE mode, please change the configuration to avoid issues.");
            }
            economyListener = new PlayerJoinListenerOffline(economy.getPlayerJoinListener());
            Console.log("Online mode is disabled, The plugin will use NICKNAME to identify players.");
        }

        getServer().getPluginManager().registerEvents(economyListener, this);
        getServer().getPluginManager().registerEvents(new ClickListener(),this);
        getServer().getPluginManager().registerEvents(new CloseListener(),this);

    }
    private void setupIntegrations() {
        Vault.init(economy.getApiWithLog(economy.getVaultLogger()));
        //vault.init(economy.getVaultLogger());
        PlaceHolder.register(economy.getPlaceHolder());
        ChannelRegister.init(this);
    }

    public static BlockDynastyEconomy getInstance() {
        return instance;
    }

    public static Configuration getConfiguration() {
        return configuration;
    }
}