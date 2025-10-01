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

package BlockDynasty;

import BlockDynasty.adapters.ConsoleAdapter;
import BlockDynasty.adapters.GUI.adapters.TextInput;
import BlockDynasty.adapters.commands.CommandRegister;
import BlockDynasty.adapters.abstractions.SpongeAdapter;
import BlockDynasty.adapters.listeners.PlayerJoinListenerOffline;
import BlockDynasty.adapters.listeners.PlayerJoinListenerOnline;
import BlockDynasty.adapters.proxy.ProxyReceiverImp;
import BlockDynasty.adapters.spongeEconomyApi.EconomyServiceAdapter;
import BlockDynasty.adapters.spongeEconomyApi.MultiCurrencyService;
import BlockDynasty.utils.Console;
import Main.Economy;
import com.google.inject.Inject;
import files.Configuration;
import lib.commands.CommandsFactory;

import org.apache.logging.log4j.Logger;
import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.Server;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.Command;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.lifecycle.*;
import org.spongepowered.api.network.channel.raw.RawDataChannel;
import org.spongepowered.plugin.PluginContainer;
import org.spongepowered.plugin.builtin.jvm.Plugin;
import proxy.ProxyData;

import java.lang.invoke.MethodHandles;
import java.nio.file.Path;


@Plugin("blockdynastyeconomy")

public class SpongePlugin {
    private static PluginContainer container;
    private static Logger logger;
    private static final Economy economy= new Economy();
    private static Configuration configuration;
    public static Path configPath;
    private static RawDataChannel channel;

    @Inject
    SpongePlugin(final PluginContainer container, final Logger logger,@ConfigDir(sharedRoot = false) final Path configDir) {
        SpongePlugin.container = container;
        SpongePlugin.logger = logger;
        SpongePlugin.configPath = configDir;
    }

    @Listener
    public void onRegisterChannel(final RegisterChannelEvent event) {
        channel = event.register(ResourceKey.resolve(ProxyData.getChannelName()),RawDataChannel.class);
        ProxyReceiverImp.register().addHandler(channel);
    }

    @Listener
    public void onConstructPlugin(final ConstructPluginEvent event) {
        // Perform any one-time setup
        Console.setConsole(new ConsoleAdapter());
        economy.init(new TextInput(),new ConsoleAdapter(), new SpongeAdapter());
        configuration = economy.getConfiguration();
        Console.log("Plugin constructed...");
    }

    @Listener
    public void provideEconomy(final ProvideServiceEvent.EngineScoped<MultiCurrencyService> event) {
        //Info: Sponge Economy service only supports 1 default currency, it does not have an interface for multiple currencies....
        event.suggest(() -> new EconomyServiceAdapter( economy.getApi() ));
    }

    @Listener
    public void onServerStarting(final StartingEngineEvent<Server> event) {
        registerEvents();
        Console.log("Server is starting...");
    }

    @Listener
    public void onServerStopping(final StoppingEngineEvent<Server> event) {
        economy.shutdown();
        Console.log("Server is stopping...");
    }

    @Listener
    public void onRegisterCommands(final RegisterCommandEvent<Command.Parameterized> event) {
        CommandRegister.registerCommands(event, container, CommandsFactory.Commands.getMainCommands());
        Console.log("Registered commands...");
    }

    private void registerEvents(){
        if(configuration.getBoolean("online")){
            if(!Sponge.server().isOnlineModeEnabled()){
                Console.logError("THE SERVER IS IN OFFLINE MODE but the plugin is configured to work in ONLINE mode, please change the configuration to avoid issues.");
            }
            Sponge.eventManager().registerListeners(container, new PlayerJoinListenerOnline(economy.getPlayerJoinListener()), MethodHandles.lookup());
            Console.log("Online mode is enabled. The plugin will use UUID to identify players.");
        }

        if (!configuration.getBoolean("online")) {
            if(Sponge.server().isOnlineModeEnabled()){
                Console.logError("THE SERVER IS IN ONLINE MODE but the plugin is configured to work in OFFLINE mode, please change the configuration to avoid issues.");
            }
            Sponge.eventManager().registerListeners(container, new PlayerJoinListenerOffline(economy.getPlayerJoinListener()),MethodHandles.lookup());
            Console.log("Online mode is disabled, The plugin will use NICKNAME to identify players.");
        }
    }

    public static Logger getLogger() {
        return logger;
    }

    public static PluginContainer getPlugin() {
        return container;
    }
    public static RawDataChannel getChannel() {
        return channel;
    }
}

