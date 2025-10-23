package EngineTest.mocks;

import lib.abstractions.IConsole;
import lib.abstractions.IPlayer;
import lib.abstractions.PlatformAdapter;
import lib.gui.components.IInventory;
import lib.gui.components.IItemStack;
import lib.gui.components.Materials;
import lib.scheduler.IScheduler;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Platform implements PlatformAdapter {


    @Override
    public IPlayer getPlayer(String name) {
        return MinecraftServer.getOnlinePlayers().stream().filter(p -> p.getName().equals(name)).findFirst().orElse(null);
    }

    @Override
    public IPlayer getPlayerByUUID(UUID uuid) {
        return MinecraftServer.getOnlinePlayers().stream().filter(p -> p.getUniqueId().equals(uuid)).findFirst().orElse(null);
    }

    @Override
    public List<IPlayer> getOnlinePlayers() {
        return new ArrayList<>(MinecraftServer.getOnlinePlayers());
    }

    @Override
    public IItemStack createItemStack(Materials material) {
        return new ItemStack(material);
    }

    @Override
    public IInventory createInventory(String title, int rows) {
        return new Inventory(title, rows);
    }

    @Override
    public void dispatchCommand(String command) throws Exception {
        System.out.println("[BlockDynastyEconomyEngine] "+command);
    }

    @Override
    public void sendPluginMessage(String channel, byte[] message) {
        System.out.println("Plugin message sent on channel: "+channel);
    }

    @Override
    public IScheduler getScheduler() {
        return new Scheduler();
    }

    @Override
    public IConsole getConsole() {
        return new Console();
    }

    @Override
    public File getDataFolder() {
        return new File("./src/test/java/EngineTest/data");
    }

    @Override
    public boolean isLegacy() {
        return true;
    }

    @Override
    public boolean hasSupportAdventureText() {
        return false;
    }
}
