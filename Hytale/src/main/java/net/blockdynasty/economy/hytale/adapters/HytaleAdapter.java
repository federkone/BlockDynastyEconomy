package net.blockdynasty.economy.hytale.adapters;


import net.blockdynasty.economy.gui.gui.components.IInventory;
import net.blockdynasty.economy.gui.gui.components.IItemStack;
import net.blockdynasty.economy.gui.gui.components.ITextInput;
import net.blockdynasty.economy.hardcash.domain.entity.currency.ItemStackCurrency;
import net.blockdynasty.economy.hardcash.domain.entity.currency.RecipeItemCurrency;
import net.blockdynasty.economy.hardcash.domain.entity.platform.HardCashCreator;
import net.blockdynasty.economy.hytale.adapters.Materials.MaterialAdapter;
import net.blockdynasty.economy.hytale.adapters.sheduler.SchedulerAdapter;
import net.blockdynasty.economy.hytale.adapters.textInput.ChatInput;
import com.hypixel.hytale.server.core.command.system.CommandManager;
import com.hypixel.hytale.server.core.console.ConsoleSender;
import com.hypixel.hytale.server.core.universe.PlayerRef;

import net.blockdynasty.economy.engine.platform.IPlatform;
import net.blockdynasty.economy.engine.platform.IPlayer;
import net.blockdynasty.economy.libs.abstractions.platform.IConsole;
import net.blockdynasty.economy.libs.abstractions.platform.IProxySubscriber;
import net.blockdynasty.economy.libs.abstractions.platform.recipes.RecipeInventory;
import net.blockdynasty.economy.libs.abstractions.platform.recipes.RecipeItem;
import net.blockdynasty.economy.libs.abstractions.platform.scheduler.IScheduler;
import net.blockdynasty.economy.libs.services.configuration.IConfiguration;


import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;


public class HytaleAdapter implements IPlatform {
    private static Map<UUID, PlayerRef> playerUUIDCache = new HashMap<>();
    private static Map<String, PlayerRef> playerNameCache = new HashMap<>();

    public static void connectPlayer(PlayerRef playerRef){
        playerUUIDCache.put(playerRef.getUuid(), playerRef);
        playerNameCache.put(playerRef.getUsername(), playerRef);
    }

    public static void disconnectPlayer(PlayerRef playerRef){
        playerUUIDCache.remove(playerRef.getUuid());
        playerNameCache.remove(playerRef.getUsername());
    }

    @Override
    public void sendPluginMessage(String s, byte[] bytes) {

    }

    @Override
    public void registerMessageChannel(IProxySubscriber iProxySubscriber) {

    }

    @Override
    public IScheduler getScheduler() {
        return new SchedulerAdapter();
    }

    @Override
    public IConsole getConsole() {
        return new ConsoleAdapter();
    }

    @Override
    public File getDataFolder() {
        return new File("mods/DynastyEconomy");
    }

    @Override
    public boolean isLegacy() {
        return true;
    }

    @Override
    public boolean isOnlineMode() {
        return true;
    }

    @Override
    public boolean hasSupportAdventureText() {
        return false;
    }

    @Override
    public IItemStack createItemStack(RecipeItem recipeItem) {
        return MaterialAdapter.createItemStack(recipeItem);
    }

    @Override
    public IInventory createInventory(RecipeInventory recipeInventory) {
        return new InventoryAdapter(recipeInventory);
    }

    @Override
    public ITextInput getTextInput() {
        return new ChatInput();
    }

    @Override
    public IPlayer getPlayer(String s) {
        PlayerRef p = playerNameCache.get(s);
        return p != null ? new PlayerAdapter(p) : null;
    }

    @Override
    public ItemStackCurrency createItemStackNBT(RecipeItemCurrency recipeItemCurrency) {
        return MaterialAdapter.createItemStack(recipeItemCurrency);
    }

    @Override
    public ItemStackCurrency createItemBase64(RecipeItemCurrency recipeItemCurrency) {
        return MaterialAdapter.createItemStack(recipeItemCurrency);
    }

    @Override
    public void dispatchCommand(String s) throws Exception {
        CommandManager.get().handleCommand(ConsoleSender.INSTANCE,s);
    }

    @Override
    public HardCashCreator asPlatformHardCash() {
        return this;
    }

    @Override
    public IPlayer getPlayerByUUID(UUID uuid) {
        PlayerRef p= playerUUIDCache.get(uuid);
        return p != null ? new PlayerAdapter(p) : null;
    }

    @Override
    public List<net.blockdynasty.economy.libs.abstractions.platform.entity.IPlayer> getOnlinePlayers() {
        List<PlayerRef> playerRefs = playerNameCache.values().stream().collect(Collectors.toList());
        return playerRefs.stream().map(PlayerAdapter::new).collect(Collectors.toList());
    }


    @Override
    public boolean hasSupportHardCash() {
        return false;
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
