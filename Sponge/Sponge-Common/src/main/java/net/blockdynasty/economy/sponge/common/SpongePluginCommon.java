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

package net.blockdynasty.economy.sponge.common;

import net.blockdynasty.economy.api.DynastyEconomy;
import net.blockdynasty.economy.gui.commands.CommandService;
import net.blockdynasty.economy.hardcash.aplication.listener.ItemNoteValidator;
import net.blockdynasty.providers.services.ServiceProvider;
import org.spongepowered.api.data.DataRegistration;
import org.spongepowered.api.data.type.HandTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.block.InteractBlockEvent;
import org.spongepowered.api.item.inventory.ItemStack;
import net.blockdynasty.economy.sponge.common.adapters.commands.CommandRegister;
import net.blockdynasty.economy.sponge.common.adapters.platformAdapter.ItemStackCurrencyAdapter;
import net.blockdynasty.economy.sponge.common.adapters.platformAdapter.NBTApi.CustomKeys;
import net.blockdynasty.economy.sponge.common.adapters.platformAdapter.SpongeAdapter;
import net.blockdynasty.economy.sponge.common.adapters.listeners.PlayerJoinListener;
import net.blockdynasty.economy.sponge.common.adapters.integrations.spongeEconomyApi.EconomyServiceAdapter;
import net.blockdynasty.economy.sponge.common.utils.Console;
import net.blockdynasty.economy.engine.Economy;

import org.apache.logging.log4j.Logger;

import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.Server;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.event.lifecycle.*;
import org.spongepowered.api.network.channel.raw.RawDataChannel;
import org.spongepowered.api.service.economy.EconomyService;
import org.spongepowered.plugin.PluginContainer;
import net.blockdynasty.economy.engine.MessageChannel.proxy.ProxyData;
import net.blockdynasty.economy.sponge.common.utils.TestEconomyCommand;

import java.lang.invoke.MethodHandles;
import java.nio.file.Path;
import java.util.Optional;


public class SpongePluginCommon {
    private static PluginContainer container;
    private static Logger logger;
    private static Economy economy;
    private static Path configPath;
    private static RawDataChannel channel;
    //private final Metrics metrics;


    public SpongePluginCommon(final PluginContainer container, final Logger logger, @ConfigDir(sharedRoot = false) final Path configDir) {
        SpongePluginCommon.container = container;
        SpongePluginCommon.logger = logger;
        SpongePluginCommon.configPath = configDir;

        //metrics = factory.make(27472);
    }

    public void onEngineStarting(StartingEngineEvent<Server> event) {
        Console.log("Registering StartingEngineEvent...");
        try {
            economy = Economy.init(new SpongeAdapter());
            Console.log("Plugin constructed...");
        }catch (Exception e){
            Console.logError("Error during plugin construction: " + e.getMessage());
            throw new RuntimeException();
        }
        Sponge.eventManager().registerListeners(container, new PlayerJoinListener(economy.getPlayerJoinListener()), MethodHandles.lookup());
        CommandRegister.registerCommands(container, CommandService.getMainCommands());

        Optional<DynastyEconomy> api = ServiceProvider.get(DynastyEconomy.class);
        api.ifPresent(EconomyServiceAdapter::init);
    }

    protected void registerTestEconomyCommand(){
        TestEconomyCommand.registerTestCommand(container); //<--- tester EconomyServiceAdapter
    }

    public void onRegisterChannel(final RegisterChannelEvent event) { //segundo
        channel = event.register(ResourceKey.resolve(ProxyData.getChannelName()),RawDataChannel.class);
    }

    public void registerEconomyService(ProvideServiceEvent.EngineScoped<EconomyService> event) {
        event.suggest(EconomyServiceAdapter::new);
    }

    public void onRegisterData(RegisterDataEvent event) {
        event.register(DataRegistration.of(CustomKeys.NAME_CURRENCY, ItemStack.class));
        event.register(DataRegistration.of(CustomKeys.VALUE, ItemStack.class));
        event.register(DataRegistration.of(CustomKeys.UUID_CURRENCY, ItemStack.class));
    }

    /*Prevent place currency*/
    public void onBlockPlace(InteractBlockEvent.Secondary event){
        event.cause().first(Player.class).ifPresent(player -> {
            ItemStack item = player.itemInHand(HandTypes.MAIN_HAND);
            if(item == null) return;
            //if(item.type() != ItemTypes.PLAYER_HEAD.get()) return;
            if(!ItemNoteValidator.isANoteCurrency(new ItemStackCurrencyAdapter(item))) return;
            event.setCancelled(true);
        });
    }

    public void onServerStopping(final StoppingEngineEvent<Server> event) {
        Economy.shutdown();
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
    public static Path getConfigPath() {
        return configPath;
    }
}

