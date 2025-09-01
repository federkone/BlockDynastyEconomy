package BlockDynasty.BukkitImplementation.GUI.adapters;

import lib.components.IInventory;
import lib.components.IItemStack;
import lib.components.IPlayer;
import lib.components.Materials;
import lib.templates.abstractions.AbstractGUI;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public class PlatformAdapter implements AbstractGUI.PlatformAdapter{
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
    public Optional<IPlayer> getPlayerOnlineByUUID(UUID uuid) {
        return Optional.ofNullable(Bukkit.getPlayer(uuid)).map(PlayerAdapter::new);
    }

    @Override
    public List<IPlayer> getOnlinePlayers() {
        return Bukkit.getOnlinePlayers().stream().map(PlayerAdapter::new).collect(Collectors.toList());
    }
}
