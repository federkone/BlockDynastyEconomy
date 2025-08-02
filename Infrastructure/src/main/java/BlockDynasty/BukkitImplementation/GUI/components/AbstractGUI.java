package BlockDynasty.BukkitImplementation.GUI.components;

import BlockDynasty.BukkitImplementation.GUI.MaterialAdapter;
import BlockDynasty.BukkitImplementation.GUI.RegisterGuiModule;
import BlockDynasty.BukkitImplementation.GUI.services.GUIService;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public abstract class AbstractGUI implements IGUI {
    private final GUIService guiService= RegisterGuiModule.getGuiService();
    private final Inventory inventory;
    private final String title;
    private final Map<Integer, Consumer<Player>> actions = new HashMap<>();

    public AbstractGUI(String title, int rows) {
        this.title = title;
        this.inventory = Bukkit.createInventory(null, rows * 9, title);
        clearGui();
    }
    protected void fill() {
        ItemStack filler = MaterialAdapter.getPanelGlass();

        for (int slot = 0; slot < inventory.getSize(); slot++) {
            setItem(slot, filler, null);   //filler can be null for empty slots
        }
    }

    @Override
    public void open(Player player) {
        player.openInventory(inventory);
        guiService.registerGUI(player, this); //esto reemplaza automaticamente el GUI anterior ya que esta basado en un mapa con key del jugador
    }

    @Override
    public void handleClick(int slot, Player player) {
        Consumer<Player> action = actions.get(slot);
        if (action != null) {
            action.accept(player);
        }
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }

    @Override
    public String getTitle() {
        return title;
    }

    protected void setItem(int slot, ItemStack item, Consumer<Player> action) {
        inventory.setItem(slot, item);
        if (item == null || action == null) {
            actions.remove(slot);
        } else {
            actions.put(slot, action);
        }
    }

    protected ItemStack createItem(Material material, String name, String... lore) {
        ItemStack item = new ItemStack(material);
        return createItem(item, name, lore);
    }

    public ItemStack createItem(ItemStack base, String name, String... lore) {
        ItemMeta meta = base.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(name);
            if (lore.length > 0) {
                meta.setLore(Arrays.asList(lore));
            }
            base.setItemMeta(meta);
        }
        return base;
    }

    protected void clearGui(){
        fill();
    }
}