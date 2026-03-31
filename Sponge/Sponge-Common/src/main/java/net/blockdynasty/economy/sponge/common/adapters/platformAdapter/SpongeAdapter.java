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

package net.blockdynasty.economy.sponge.common.adapters.platformAdapter;

import net.blockdynasty.economy.engine.MessageChannel.proxy.ProxyData;
import net.blockdynasty.economy.engine.platform.IPlatform;
import net.blockdynasty.economy.engine.platform.IPlayer;
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
import net.blockdynasty.economy.libs.abstractions.platform.scheduler.IScheduler;
import net.blockdynasty.economy.libs.services.configuration.IConfiguration;
import net.blockdynasty.economy.sponge.common.adapters.GUI.adapters.InventoryAdapter;
import net.blockdynasty.economy.sponge.common.adapters.GUI.adapters.ItemStackAdapter;
import net.blockdynasty.economy.sponge.common.adapters.GUI.adapters.MaterialAdapter;
import net.blockdynasty.economy.sponge.common.SpongePluginCommon;
import net.blockdynasty.economy.sponge.common.adapters.GUI.adapters.TextInput;
import net.blockdynasty.economy.sponge.common.adapters.scheduler.Scheduler;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.item.inventory.ContainerType;
import org.spongepowered.api.item.inventory.ContainerTypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.type.ViewableInventory;
import org.spongepowered.api.network.channel.ChannelBuf;
import org.spongepowered.api.network.channel.raw.RawDataChannel;
import org.spongepowered.api.network.channel.raw.play.RawPlayDataChannel;
import org.spongepowered.plugin.PluginContainer;
import net.blockdynasty.economy.sponge.common.utils.Version;


import java.io.File;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class SpongeAdapter implements IPlatform {
    private PluginContainer pluginContainer = SpongePluginCommon.getPlugin();
    private RawDataChannel channel = SpongePluginCommon.getChannel();

    @Override
    public IPlayer getPlayer(String name) {
        return Sponge.server().player(name).map(EntityPlayerAdapter::of).orElse(null);
    }

    @Override
    public ItemStackCurrency createItemStackNBT(RecipeItemCurrency recipeItemCurrency) {
        return new ItemStackCurrencyAdapter(MaterialAdapter.createItemStackCurrency(recipeItemCurrency));
    }

    @Override
    public ItemStackCurrency createItemBase64(RecipeItemCurrency recipeItemCurrency) {
        return new ItemStackCurrencyAdapter(MaterialAdapter.createItemStackBase64(recipeItemCurrency));
    }

    @Override
    public IPlayer getPlayerByUUID(UUID uuid) {
        return Sponge.server().player(uuid).map(EntityPlayerAdapter::of).orElse(null);
    }

    @Override
    public List<net.blockdynasty.economy.libs.abstractions.platform.entity.IPlayer> getOnlinePlayers() {
        return Sponge.server().onlinePlayers().stream()
                .map(EntityPlayerAdapter::of)
                .collect(Collectors.toList());
    }

    @Override
    public void registerMessageChannel(IProxySubscriber proxySubscriber) {
        SpongePluginCommon.getChannel().play().addHandler((buf, connection) -> {
            proxySubscriber.onPluginMessageReceived(ProxyData.getChannelName(), buf.readBytes(buf.available()));
        });}

    @Override
    public void dispatchCommand(String command) throws Exception {
        Sponge.server().commandManager().process(command);
    }

    @Override
    public void sendPluginMessage(String channel, byte[] message) {
        if(Version.isNeoForge()) return;
        Collection<ServerPlayer> players = Sponge.server().onlinePlayers();
        ServerPlayer player = players.stream().findFirst().orElse(null);

        if (player != null) {
            RawPlayDataChannel playChannel = SpongePluginCommon.getChannel().play();
            playChannel.sendTo(player, (ChannelBuf buf) -> {
                buf.writeBytes(message);
            });
        }
    }

    @Override
    public IScheduler getScheduler() {
        return new Scheduler();
    }

    @Override
    public IConsole getConsole() {
        return new ConsoleAdapter();
    }

    @Override
    public File getDataFolder() {
        return SpongePluginCommon.getConfigPath().toFile();
    }

    @Override
    public boolean isLegacy() {
        return false;
    }

    @Override
    public boolean isOnlineMode() {
        return Sponge.server().isOnlineModeEnabled();
    }

    @Override
    public boolean hasSupportAdventureText() {
        return true;
    }

    @Override
    public ITextInput getTextInput() {
        return new TextInput();
    }

    @Override
    public HardCashCreator asPlatformHardCash() {
        return this;
    }

    @Override
    public IItemStack createItemStack(RecipeItem recipeItem){
        ItemStack itemStack = MaterialAdapter.createItemStack(recipeItem);
        return new ItemStackAdapter(itemStack);
    }

    @Override
    public IInventory createInventory(RecipeInventory recipeInventory) {
        ViewableInventory viewableInventory= ViewableInventory.builder()
                .type(getTypeFromRows(recipeInventory.getRows()))
                .completeStructure()
                .plugin(this.pluginContainer)
                .build();
        return new InventoryAdapter(viewableInventory,recipeInventory);
    }


    private ContainerType getTypeFromRows(int rows) {
        switch (rows) {
            case 1:
                return ContainerTypes.GENERIC_9X1.get();
            case 2:
                return ContainerTypes.GENERIC_9X2.get();
            case 3:
                return ContainerTypes.GENERIC_9X3.get();
            case 4:
                return ContainerTypes.GENERIC_9X4.get();
            case 5:
                return ContainerTypes.GENERIC_9X5.get();
            case 6:
                return ContainerTypes.GENERIC_9X6.get();
            default:
                throw new IllegalArgumentException("Invalid number of rows: " + rows);
        }
    }

    @Override
    public boolean hasSupportHardCash() {
        return true;
    }

    @Override
    public boolean hasSupportGui() {
        return true;
    }

    @Override
    public void startServer(IConfiguration configuration) {

    }

    @Override
    public void reloadServer() {

    }
}
