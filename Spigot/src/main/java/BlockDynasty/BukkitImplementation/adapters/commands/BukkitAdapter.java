package BlockDynasty.BukkitImplementation.adapters.commands;

import BlockDynasty.BukkitImplementation.adapters.GUI.adapters.InventoryAdapter;
import BlockDynasty.BukkitImplementation.adapters.GUI.adapters.ItemStackAdapter;
import BlockDynasty.BukkitImplementation.adapters.GUI.adapters.MaterialAdapter;
import BlockDynasty.BukkitImplementation.adapters.GUI.adapters.PlayerAdapter;
import BlockDynasty.BukkitImplementation.scheduler.ContextualTask;
import BlockDynasty.BukkitImplementation.scheduler.Scheduler;
import lib.commands.abstractions.PlatformAdapter;
import lib.commands.abstractions.Source;
import lib.gui.abstractions.IInventory;
import lib.gui.abstractions.IItemStack;
import lib.gui.abstractions.IPlayer;
import lib.gui.abstractions.Materials;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;


//permite explicarle a la libreria como obtener un jugador y como ejecutar un comando
public class BukkitAdapter implements PlatformAdapter {
    @Override
    public Source getPlayer(String name) {
        Player player = Bukkit.getPlayer(name);
        if (player == null) {
            return null;
        }
        return new SourceAdapter(player);
    }

    @Override
    public void dispatchCommand(String command) {
        //Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
        Scheduler.run(ContextualTask.build(()->Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command)));
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
    public Source getPlayerByUUID(UUID uuid) {
        Player player = Bukkit.getPlayer(uuid);
        if (player == null) {
            return null;
        }
        return new SourceAdapter(player);
    }

    @Override
    public Optional<IPlayer> getPlayerOnlineByUUID(UUID uuid) {
        return Optional.ofNullable(Bukkit.getPlayer(uuid)).map(PlayerAdapter::new);
    }

    @Override
    public List<IPlayer> getOnlinePlayers() {
        return Bukkit.getOnlinePlayers().stream().map(PlayerAdapter::new).collect(Collectors.toList());
    }
}
