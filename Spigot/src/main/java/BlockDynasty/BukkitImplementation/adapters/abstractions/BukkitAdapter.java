package BlockDynasty.BukkitImplementation.adapters.abstractions;

import BlockDynasty.BukkitImplementation.BlockDynastyEconomy;
import BlockDynasty.BukkitImplementation.adapters.GUI.adapters.InventoryAdapter;
import BlockDynasty.BukkitImplementation.adapters.GUI.adapters.ItemStackAdapter;
import BlockDynasty.BukkitImplementation.adapters.GUI.adapters.MaterialAdapter;
import BlockDynasty.BukkitImplementation.scheduler.Scheduler;
import BlockDynasty.BukkitImplementation.scheduler.SchedulerFactory;
import lib.abstractions.IPlayer;
import lib.abstractions.PlatformAdapter;
import lib.gui.abstractions.IInventory;
import lib.gui.abstractions.IItemStack;
import lib.gui.abstractions.Materials;
import lib.scheduler.ContextualTask;
import lib.scheduler.IScheduler;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


//permite explicarle a la libreria como obtener un jugador y como ejecutar un comando
public class BukkitAdapter implements PlatformAdapter {

    @Override
    public IPlayer getPlayer(String name) {
        Player player = Bukkit.getPlayer(name);
        if (player == null) {
            return null;
        }
        return EntityPlayerAdapter.of(player);
    }

    @Override
    public void dispatchCommand(String command) {
        //Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
        Scheduler.run(ContextualTask.build(()->Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command)));
    }

    @Override
    public void sendPluginMessage(String channel, byte[] message) {
        Bukkit.getServer().sendPluginMessage(BlockDynastyEconomy.getInstance(), channel, message);
    }

    @Override
    public IScheduler getScheduler() {
        return SchedulerFactory.getScheduler();
    }

    @Override
    public File getDataFolder() {
        return BlockDynastyEconomy.getInstance().getDataFolder();
    }


    @Override
    public IItemStack createItemStack(Materials material) {
        ItemStack itemStack = MaterialAdapter.toBukkitItemStack(material);
        return new ItemStackAdapter(itemStack);
    }

    @Override
    public IInventory createInventory(String title, int rows) {
        Inventory inventory = Bukkit.createInventory(null, rows * 9, title);
        return new InventoryAdapter(inventory);
    }

    @Override
    public IPlayer getPlayerByUUID(UUID uuid) {
        Player player = Bukkit.getPlayer(uuid);
        if (player == null) {
            return null;
        }
        return EntityPlayerAdapter.of(player);
    }

    @Override
    public List<IPlayer> getOnlinePlayers() {
        return Bukkit.getOnlinePlayers().stream().map(EntityPlayerAdapter::of).collect(Collectors.toList());
    }


}
