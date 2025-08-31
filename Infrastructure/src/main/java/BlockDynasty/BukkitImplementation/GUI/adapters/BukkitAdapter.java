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

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public class BukkitAdapter implements AbstractGUI.PlatformAdapter{
    @Override
    public IItemStack createItemStack(Materials material, String name, String... lore) {
        Material materialBukkit = MaterialAdapter.toBukkitMaterial(material);
        ItemStack itemStack = new ItemStack(materialBukkit);
        ItemStackAdapter adapter = new ItemStackAdapter(itemStack);

        adapter.setDisplayName(name);
        adapter.setLore(Arrays.asList(lore));

        return adapter;
    }

    @Override
    public IInventory createInventory(AbstractGUI gui) {
        Inventory inventory = Bukkit.createInventory(null, gui.getRows()*9, gui.getTitle());
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
